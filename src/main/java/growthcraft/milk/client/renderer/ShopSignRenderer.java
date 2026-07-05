package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class ShopSignRenderer implements BlockEntityRenderer<ShopSignBlockEntity> {
    private static final Quaternionf ROTATE_Y_180 = Axis.YP.rotationDegrees(180);

    private final BlockEntityRendererProvider.Context context;
    private final Map<WoodType, HangingSignRenderer.HangingSignModel> hangingSignModelsCache = new HashMap<>();
    private final Map<Float, Quaternionf> rotationCache = new HashMap<>();

    public ShopSignRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public void render(ShopSignBlockEntity sign, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = sign.getBlockState();
        SignBlock signBlock = (SignBlock) state.getBlock();
        WoodType woodType = SignBlock.getWoodType(signBlock);
        HangingSignRenderer.HangingSignModel model = getModel(woodType);
        model.evaluateVisibleParts(state);

        poseStack.pushPose();
        translateSign(poseStack, state);
        renderSign(poseStack, buffer, packedLight, packedOverlay, woodType, model);
        renderItem(sign, poseStack, buffer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    private void renderItem(ShopSignBlockEntity sign, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack item = sign.getItem();
        if (item.isEmpty()) {
            return;
        }

        int renderId = (int) sign.getBlockPos().asLong();
        var itemRenderer = Minecraft.getInstance().getItemRenderer();
        boolean isGui3d = itemRenderer.getModel(item, sign.getLevel(), null, renderId).isGui3d();

        if (item.is(Items.ANVIL) || item.is(Items.GRINDSTONE)) {
            poseStack.scale(0.75F, 0.75F, 0.15F);
            poseStack.translate(0.0F, -0.45F, 0.375F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
            poseStack.translate(0.0F, 0.0F, -0.745F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
        } else if (item.getItem() instanceof BlockItem && isGui3d) {
            poseStack.scale(0.75F, 0.75F, 0.15F);
            poseStack.translate(0.0F, -0.45F, -0.325F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
            poseStack.translate(0.0F, 0.0F, 0.605F);
            poseStack.mulPose(ROTATE_Y_180);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
        } else if (isGui3d) {
            poseStack.scale(0.55F, 0.55F, 2.7F);
            poseStack.translate(0.0F, -0.5F, -0.001F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
        } else {
            poseStack.scale(0.45F, 0.45F, 0.3F);
            poseStack.translate(0.0F, -0.70F, -0.205F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
            poseStack.mulPose(ROTATE_Y_180);
            poseStack.translate(0.0F, 0.0F, -0.405F);
            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, sign.getLevel(), renderId);
        }
    }

    private void translateSign(PoseStack poseStack, BlockState state) {
        poseStack.translate(0.5D, 0.9375D, 0.5D);
        poseStack.mulPose(getRotation(state));
        poseStack.translate(0.0F, -0.3125F, 0.0F);
    }

    private void renderSign(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, WoodType woodType, Model model) {
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = Sheets.getHangingSignMaterial(woodType);
        VertexConsumer vertexConsumer = material.buffer(buffer, model::renderType);
        ((HangingSignRenderer.HangingSignModel) model).root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    private HangingSignRenderer.HangingSignModel getModel(WoodType woodType) {
        return hangingSignModelsCache.computeIfAbsent(woodType,
                type -> new HangingSignRenderer.HangingSignModel(context.bakeLayer(ModelLayers.createHangingSignModelName(type))));
    }

    private Quaternionf getRotation(BlockState state) {
        float angle = ((SignBlock) state.getBlock()).getYRotationDegrees(state);
        return rotationCache.computeIfAbsent(angle, ignored -> Axis.YP.rotationDegrees(-angle));
    }
}
