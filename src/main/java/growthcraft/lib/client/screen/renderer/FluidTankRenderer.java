package growthcraft.lib.client.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * Simple reusable renderer for drawing a fluid stack inside a rectangular GUI region.
 * <p>
 * Features:
 * - Uses the fluid's still texture (from the block atlas)
 * - Applies the fluid's tint color
 * - Supports configurable alpha scaling for additional transparency control
 * - Optionally draws a colored overlay rectangle on top (e.g., to simulate a glass tint)
 * <p>
 * This class is UI-agnostic and can be reused by any screen.
 *
 * @param alphaScale 1.0 = original alpha; <1 for more transparency
 */
public record FluidTankRenderer(int width, int height, int capacityMb, float alphaScale) {
    /**
     * @param width      tank draw width in pixels
     * @param height     tank draw height in pixels
     * @param capacityMb tank capacity in millibuckets
     * @param alphaScale scales the fluid's tint alpha (1.0 keeps original, 0.1 makes it very transparent)
     */
    public FluidTankRenderer(int width, int height, int capacityMb, float alphaScale) {
        this.width = width;
        this.height = height;
        this.capacityMb = Math.max(1, capacityMb);
        this.alphaScale = Math.max(0f, Math.min(1f, alphaScale));
    }

    /**
     * Render the given fluid into the specified screen-space rectangle.
     *
     * @param graphics gui graphics
     * @param x        left x in screen coords
     * @param y        top y in screen coords
     * @param stack    fluid stack to render (amount determines fill height)
     */
    public void render(GuiGraphics graphics, int x, int y, FluidStack stack) {
        if (stack == null || stack.isEmpty()) return;
        int amount = stack.getAmount();
        if (amount <= 0) return;

        int filled = Math.max(1, (int) Math.floor((amount / (double) capacityMb) * height));
        int yTop = y + (height - filled);

        IClientFluidTypeExtensions ext = IClientFluidTypeExtensions.of(stack.getFluid());
        ResourceLocation stillTex = ext.getStillTexture();
        if (stillTex == null) return;

        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        var sprite = atlas.apply(stillTex);

        int tint = ext.getTintColor(stack);
        float a = ((tint >>> 24) & 0xFF) / 255.0f;
        float r = ((tint >>> 16) & 0xFF) / 255.0f;
        float g = ((tint >>> 8) & 0xFF) / 255.0f;
        float b = (tint & 0xFF) / 255.0f;
        a = a * this.alphaScale;

        // Ensure alpha blending is enabled so the fluid can render translucently over the GUI background.
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Apply color and draw the sprite stretched to the filled area.
        graphics.setColor(r, g, b, a);
        graphics.blit(x, yTop, 0, this.width, filled, sprite);

        // Reset color and blending state to avoid leaking into other GUI draws.
        graphics.setColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    /**
     * Optionally draw a simple colored overlay rectangle (e.g., glass tint).
     * Provide ARGB packed color.
     */
    public void renderOverlayTint(GuiGraphics graphics, int x, int y, int argbColor) {
        int a = (argbColor >>> 24) & 0xFF;
        int r = (argbColor >>> 16) & 0xFF;
        int g = (argbColor >>> 8) & 0xFF;
        int b = argbColor & 0xFF;
        graphics.setColor(r / 255f, g / 255f, b / 255f, a / 255f);
        // draw as a 1x1 white pixel stretched: use fill with color via GuiGraphics
        graphics.fill(x, y, x + this.width, y + this.height, argbColor);
        graphics.setColor(1f, 1f, 1f, 1f);
    }
}
