package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class MixingVatBlockEntityRenderer implements BlockEntityRenderer<MixingVatBlockEntity> {
    private static final MachineFluidRenderer.Bounds MAIN_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 4.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 12.0F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds SIDE_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 12.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 14.0F / 16.0F, 15.0F / 16.0F);

    public MixingVatBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MixingVatBlockEntity vat, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        MachineFluidRenderer.renderSurface(poseStack, buffer, vat.getMainTank().getFluid(), vat.getMainTank().getCapacity(), MAIN_BOUNDS, packedLight);
        MachineFluidRenderer.renderSurface(poseStack, buffer, vat.getSideTank().getFluid(), vat.getSideTank().getCapacity(), SIDE_BOUNDS, packedLight);
    }
}
