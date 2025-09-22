package growthcraft.cellar.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Renders the contained fluid inside the Culture Jar in the world.
 * The jar itself should be a cutout/translucent model; this renderer only draws the inner fluid cuboid.
 */
public class CultureJarBlockEntityRenderer implements BlockEntityRenderer<CultureJarBlockEntity> {
    // Inner bounds of the fluid within the 1x1x1 block space. Tweaked to sit inside the glass.
    // Vanilla units are in block coords (0..1). We'll keep a small inset to avoid Z-fighting.
    // Fit snugly inside the jar’s inner walls (block shape is 5..11 -> 0.3125..0.6875)
    private static final float MIN_X = 5.5f / 16.0f; // 0.34375
    private static final float MAX_X = 10.5f / 16.0f; // 0.65625
    private static final float MIN_Z = MIN_X;
    private static final float MAX_Z = MAX_X;
    private static final float MIN_Y = 2.0f / 16.0f; // start slightly above the base
    private static final float MAX_Y = 12.0f / 16.0f; // below the lid

    public CultureJarBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(CultureJarBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be.getLevel() == null) return;

        FluidStack stack = be.getTank().getFluid();
        if (stack.isEmpty() || stack.getAmount() <= 0) return;

        // Determine fluid texture and tint
        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(stack.getFluid());
        ResourceLocation tex = ext.getStillTexture();
        if (tex == null) tex = ext.getFlowingTexture();
        if (tex == null) return;

        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite sprite = atlas.apply(tex);

        int argb = ext.getTintColor();
        float a = ((argb >>> 24) & 0xFF) / 255f;
        float r = ((argb >>> 16) & 0xFF) / 255f;
        float g = ((argb >>> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        if (a == 0f) a = 0.9f; // default to mostly-opaque for visibility during bring-up

        // Compute fill height
        int capacity = be.getTank().getCapacity();
        double fillFrac = Math.max(0.01, Math.min(1.0, stack.getAmount() / (double) capacity));
        float topY = (float) (MIN_Y + (MAX_Y - MIN_Y) * fillFrac);

        // Lighting from the fluid position (use top face for better brightness)
        int light = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos());
        int overlay = OverlayTexture.NO_OVERLAY;

        // Build quads
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(InventoryMenu.BLOCK_ATLAS));

        poseStack.pushPose();
        // Centered as block-local already; no additional transforms required

        // Sides (NORTH, SOUTH, WEST, EAST) + TOP. We skip bottom face.
        // NORTH (-Z)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MIN_X, MIN_Y, MIN_Z,
                MAX_X, topY, MIN_Z,
                Direction.NORTH, light, overlay);
        // SOUTH (+Z)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MIN_X, MIN_Y, MAX_Z,
                MAX_X, topY, MAX_Z,
                Direction.SOUTH, light, overlay);
        // WEST (-X)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MIN_X, MIN_Y, MAX_Z,
                MIN_X, topY, MIN_Z,
                Direction.WEST, light, overlay);
        // EAST (+X)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MAX_X, MIN_Y, MIN_Z,
                MAX_X, topY, MAX_Z,
                Direction.EAST, light, overlay);
        // TOP (+Y)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MIN_X, topY, MIN_Z,
                MAX_X, topY, MAX_Z,
                Direction.UP, light, overlay);
        // BOTTOM (-Y)
        putQuad(poseStack, vc, r, g, b, a, sprite,
                MIN_X, MIN_Y, MIN_Z,
                MAX_X, MIN_Y, MAX_Z,
                Direction.DOWN, light, overlay);

        poseStack.popPose();
    }

    private static void putQuad(PoseStack poseStack,
                                VertexConsumer vc,
                                float r, float g, float b, float a,
                                TextureAtlasSprite sprite,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                Direction face,
                                int light, int overlay) {
        // Map texture over the face with basic planar projection
        float u1 = sprite.getU0();
        float v1 = sprite.getV0();
        float u2 = sprite.getU1();
        float v2 = sprite.getV1();

        float nx = 0, ny = 0, nz = 0;
        switch (face) {
            case NORTH -> { nx = 0; ny = 0; nz = -1; }
            case SOUTH -> { nx = 0; ny = 0; nz = 1; }
            case WEST  -> { nx = -1; ny = 0; nz = 0; }
            case EAST  -> { nx = 1; ny = 0; nz = 0; }
            case UP    -> { nx = 0; ny = 1; nz = 0; }
            case DOWN  -> { nx = 0; ny = -1; nz = 0; }
        }

        // Arrange the four corners depending on face
        // We'll emit vertices in the correct winding for the face normal.
        switch (face) {
            case NORTH -> { // -Z (x increases to the right, y up)
                vertex(vc, poseStack, x1, y1, z1, r, g, b, a, u1, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y2, z1, r, g, b, a, u1, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y2, z1, r, g, b, a, u2, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y1, z1, r, g, b, a, u2, v2, overlay, light, nx, ny, nz);
            }
            case SOUTH -> { // +Z
                vertex(vc, poseStack, x1, y1, z2, r, g, b, a, u1, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y2, z2, r, g, b, a, u1, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y2, z2, r, g, b, a, u2, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y1, z2, r, g, b, a, u2, v2, overlay, light, nx, ny, nz);
            }
            case WEST -> { // -X
                vertex(vc, poseStack, x1, y1, z1, r, g, b, a, u2, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y1, z2, r, g, b, a, u1, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y2, z2, r, g, b, a, u1, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y2, z1, r, g, b, a, u2, v1, overlay, light, nx, ny, nz);
            }
            case EAST -> { // +X
                vertex(vc, poseStack, x2, y1, z1, r, g, b, a, u1, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y2, z1, r, g, b, a, u1, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y2, z2, r, g, b, a, u2, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y1, z2, r, g, b, a, u2, v2, overlay, light, nx, ny, nz);
            }
            case UP -> { // +Y (top)
                vertex(vc, poseStack, x1, y1, z1, r, g, b, a, u1, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y1, z1, r, g, b, a, u2, v1, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x2, y1, z2, r, g, b, a, u2, v2, overlay, light, nx, ny, nz);
                vertex(vc, poseStack, x1, y1, z2, r, g, b, a, u1, v2, overlay, light, nx, ny, nz);
            }
            case DOWN -> {
                // not used
            }
        }
    }

    private static void vertex(VertexConsumer vc, PoseStack poseStack,
                               float x, float y, float z,
                               float r, float g, float b, float a,
                               float u, float v,
                               int overlay, int light,
                               float nx, float ny, float nz) {
        vc.addVertex(poseStack.last().pose(), x, y, z)
          .setColor(r, g, b, a)
          .setUv(u, v)
          .setOverlay(overlay)
          .setLight(light)
          .setNormal(nx, ny, nz);
    }
}
