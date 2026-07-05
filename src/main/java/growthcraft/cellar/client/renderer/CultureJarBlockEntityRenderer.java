package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.lib.client.renderer.MachineFluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class CultureJarBlockEntityRenderer implements BlockEntityRenderer<CultureJarBlockEntity> {
    private static final MachineFluidRenderer.Bounds JAR_BOUNDS = new MachineFluidRenderer.Bounds(
            6.125F / 16.0F, 0.1F / 16.0F, 6.125F / 16.0F,
            9.875F / 16.0F, 5.5F / 16.0F, 9.875F / 16.0F);

    public CultureJarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CultureJarBlockEntity jar, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        MachineFluidRenderer.renderSurface(poseStack, buffer, jar.getTank().getFluid(), jar.getTank().getCapacity(), JAR_BOUNDS, packedLight);
    }
}
