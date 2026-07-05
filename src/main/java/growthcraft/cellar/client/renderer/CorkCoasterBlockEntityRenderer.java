package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import growthcraft.cellar.block.CorkCoasterBlock;
import growthcraft.cellar.block.entity.CorkCoasterBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CorkCoasterBlockEntityRenderer implements BlockEntityRenderer<CorkCoasterBlockEntity> {
    public CorkCoasterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CorkCoasterBlockEntity coaster, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack item = coaster.getItem(0);
        if (item.isEmpty()) {
            return;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BlockState state = coaster.getBlockState();

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.8F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(switch (state.getValue(CorkCoasterBlock.FACING)) {
            case SOUTH -> 0.0F;
            case EAST -> 90.0F;
            case WEST -> 270.0F;
            default -> 180.0F;
        }));
        poseStack.translate(0.0F, -0.1F, 0.0F);
        itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, coaster.getLevel(), 0);
        poseStack.popPose();
    }
}
