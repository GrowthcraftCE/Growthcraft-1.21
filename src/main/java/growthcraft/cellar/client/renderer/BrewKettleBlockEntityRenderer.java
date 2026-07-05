package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.neoforged.neoforge.fluids.FluidStack;

public class BrewKettleBlockEntityRenderer implements BlockEntityRenderer<BrewKettleBlockEntity> {
    private static final MachineFluidRenderer.Bounds KETTLE_BOUNDS = new MachineFluidRenderer.Bounds(
            1.0F / 16.0F, 2.5F / 16.0F, 1.0F / 16.0F,
            15.0F / 16.0F, 13.5F / 16.0F, 15.0F / 16.0F);

    public BrewKettleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BrewKettleBlockEntity kettle, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        FluidStack input = kettle.getInputTank().getFluid();
        if (!input.isEmpty()) {
            MachineFluidRenderer.renderSurface(poseStack, buffer, input, kettle.getInputTank().getCapacity(), KETTLE_BOUNDS, packedLight);
            return;
        }

        FluidStack output = kettle.getOutputTank().getFluid();
        MachineFluidRenderer.renderSurface(poseStack, buffer, output, kettle.getOutputTank().getCapacity(), KETTLE_BOUNDS, packedLight);
    }
}
