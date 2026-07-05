package growthcraft.lib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public final class MachineFluidRenderer {
    private static final float DEFAULT_ALPHA = 0.85F;

    private MachineFluidRenderer() {
    }

    public static void renderSurface(PoseStack poseStack, MultiBufferSource buffer, FluidStack fluidStack, int capacity, Bounds bounds, int light) {
        if (fluidStack.isEmpty() || fluidStack.getAmount() <= 0 || capacity <= 0) {
            return;
        }

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        ResourceLocation texture = extensions.getStillTexture(fluidStack);
        if (texture == null) {
            texture = extensions.getFlowingTexture(fluidStack);
        }
        if (texture == null) {
            return;
        }

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(texture);
        int tint = extensions.getTintColor(fluidStack);
        float alpha = alpha(tint);
        float red = ((tint >> 16) & 0xFF) / 255.0F;
        float green = ((tint >> 8) & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;

        double fill = Math.clamp(fluidStack.getAmount() / (double) capacity, 0.0D, 1.0D);
        float y = (float) (bounds.minY() + (bounds.maxY() - bounds.minY()) * fill);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));
        putTopQuad(poseStack, consumer, sprite, bounds.minX(), y, bounds.minZ(), bounds.maxX(), bounds.maxZ(),
                red, green, blue, alpha, light);
    }

    private static float alpha(int tint) {
        float alpha = ((tint >>> 24) & 0xFF) / 255.0F;
        return alpha == 0.0F ? DEFAULT_ALPHA : alpha;
    }

    private static void putTopQuad(PoseStack poseStack, VertexConsumer consumer, TextureAtlasSprite sprite,
                                   float minX, float y, float minZ, float maxX, float maxZ,
                                   float red, float green, float blue, float alpha, int light) {
        vertex(poseStack, consumer, minX, y, minZ, red, green, blue, alpha, sprite.getU0(), sprite.getV0(), light);
        vertex(poseStack, consumer, maxX, y, minZ, red, green, blue, alpha, sprite.getU1(), sprite.getV0(), light);
        vertex(poseStack, consumer, maxX, y, maxZ, red, green, blue, alpha, sprite.getU1(), sprite.getV1(), light);
        vertex(poseStack, consumer, minX, y, maxZ, red, green, blue, alpha, sprite.getU0(), sprite.getV1(), light);
    }

    private static void vertex(PoseStack poseStack, VertexConsumer consumer,
                               float x, float y, float z,
                               float red, float green, float blue, float alpha,
                               float u, float v, int light) {
        consumer.addVertex(poseStack.last().pose(), x, y, z)
                .setColor(red, green, blue, alpha)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(Direction.UP.getStepX(), Direction.UP.getStepY(), Direction.UP.getStepZ());
    }

    public record Bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        public Bounds {
            if (maxX <= minX || maxY <= minY || maxZ <= minZ) {
                throw new IllegalArgumentException("Fluid render bounds must have positive volume");
            }
        }
    }
}
