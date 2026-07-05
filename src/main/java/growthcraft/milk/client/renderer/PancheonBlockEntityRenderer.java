package growthcraft.milk.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import growthcraft.milk.block.entity.PancheonBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.fluids.FluidStack;

public class PancheonBlockEntityRenderer implements BlockEntityRenderer<PancheonBlockEntity> {
    private static final MachineFluidRenderer.Bounds FULL_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 3.5F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds LOWER_OUTPUT_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 1.0F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 2.25F / 16.0F, 15.0F / 16.0F);
    private static final MachineFluidRenderer.Bounds UPPER_OUTPUT_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 2.25F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 3.5F / 16.0F, 15.0F / 16.0F);

    public PancheonBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PancheonBlockEntity pancheon, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        FluidStack input = pancheon.getInputTank().getFluid();
        if (!input.isEmpty()) {
            MachineFluidRenderer.renderSurface(poseStack, buffer, input, pancheon.getInputTank().getCapacity(), FULL_BOUNDS, packedLight);
            return;
        }

        MachineFluidRenderer.renderSurface(poseStack, buffer, pancheon.getOutputTank0().getFluid(), pancheon.getOutputTank0().getCapacity(), UPPER_OUTPUT_BOUNDS, packedLight);
        MachineFluidRenderer.renderSurface(poseStack, buffer, pancheon.getOutputTank1().getFluid(), pancheon.getOutputTank1().getCapacity(), LOWER_OUTPUT_BOUNDS, packedLight);
    }
}
