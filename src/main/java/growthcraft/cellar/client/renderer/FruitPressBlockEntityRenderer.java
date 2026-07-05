package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class FruitPressBlockEntityRenderer implements BlockEntityRenderer<FruitPressBlockEntity> {
    private static final MachineFluidRenderer.Bounds TRAY_BOUNDS = new MachineFluidRenderer.Bounds(
            3.0F / 16.0F, 2.0F / 16.0F, 3.0F / 16.0F,
            13.0F / 16.0F, 5.0F / 16.0F, 13.0F / 16.0F);

    public FruitPressBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(FruitPressBlockEntity press, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        MachineFluidRenderer.renderSurface(poseStack, buffer, press.getTank().getFluid(), press.getTank().getCapacity(), TRAY_BOUNDS, packedLight);
    }
}
