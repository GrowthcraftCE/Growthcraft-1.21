package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.CultureJarMenu;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class CultureJarScreen extends AbstractContainerScreen<CultureJarMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/culture_jar_screen.png");

    // Tank render area inside the GUI (relative to top-left of the GUI)
    private static final int TANK_X = 80; // aligned with legacy texture layout
    private static final int TANK_Y = 18;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;
    private static final int PROGRESS_X = 106;
    private static final int PROGRESS_Y = 43;
    private static final int PROGRESS_W = 9;
    private static final int PROGRESS_H = 27;
    private static final int[][] BUBBLE_PIXELS = new int[][] {
            { 108, 43 },
            { 107, 44 },
            { 108, 44 },
            { 113, 47 },
            { 112, 48 },
            { 113, 48 },
            { 109, 51 },
            { 108, 52 },
            { 109, 52 },
            { 113, 55 },
            { 114, 55 },
            { 108, 56 },
            { 112, 56 },
            { 113, 56 },
            { 114, 56 },
            { 107, 57 },
            { 108, 57 },
            { 112, 57 },
            { 113, 57 },
            { 114, 57 },
            { 107, 60 },
            { 108, 60 },
            { 106, 61 },
            { 107, 61 },
            { 108, 61 },
            { 114, 61 },
            { 106, 62 },
            { 107, 62 },
            { 108, 62 },
            { 113, 62 },
            { 114, 62 },
            { 110, 65 },
            { 111, 65 },
            { 112, 65 },
            { 109, 66 },
            { 110, 66 },
            { 111, 66 },
            { 112, 66 },
            { 109, 67 },
            { 110, 67 },
            { 111, 67 },
            { 112, 67 },
            { 109, 68 },
            { 110, 68 },
            { 111, 68 },
            { 112, 68 },
            { 110, 69 },
            { 111, 69 },
            { 112, 69 }
    };

    // Scale factor for fluid alpha (1.0 = original alpha). 0.7 keeps it visible with gentle transparency.
    private static final float FLUID_ALPHA_SCALE = 0.7f;

    private FluidTankRenderer tankRenderer;

    public CultureJarScreen(CultureJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), FLUID_ALPHA_SCALE);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Background
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Heat/fire indicator (legacy UVs: u=176,v=28, w=13,h=13)
        if (this.menu.isHeated()) {
            graphics.blit(TEXTURE, this.leftPos + 59, this.topPos + 57, 176, 28, 13, 13);
        }

        // Draw fluid tank contents via reusable renderer (flowing texture animates via atlas)
        FluidStack stack = this.menu.getClientFluidStack();
        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, stack);

        drawProgress(graphics);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        // Tooltip over tank
        int x0 = this.leftPos + TANK_X;
        int y0 = this.topPos + TANK_Y;
        if (mouseX >= x0 && mouseX < x0 + TANK_W && mouseY >= y0 && mouseY < y0 + TANK_H) {
            FluidStack stack = this.menu.getClientFluidStack();
            int amt = this.menu.getFluidAmount();
            int cap = this.menu.getTankCapacity();
            Component name = stack.isEmpty() ? Component.translatable("gui.growthcraft_cellar.empty") : stack.getHoverName();
            Component amountText = Component.literal(amt + " mB / " + cap + " mB");
            Component tooltip = Component.empty().append(name).append(Component.literal(" ")).append(amountText);
            graphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }

        if (this.menu.getProcessTotal() > 0 && isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.renderTooltip(this.font, Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    private void drawProgress(GuiGraphics graphics) {
        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress <= 0) {
            return;
        }

        int minY = 43 + PROGRESS_H - progress;
        for (int[] pixel : BUBBLE_PIXELS) {
            int x = pixel[0];
            int y = pixel[1];
            if (y >= minY) {
                graphics.fill(this.leftPos + x, this.topPos + y, this.leftPos + x + 1, this.topPos + y + 1, 0xFFEDEDED);
            }
        }
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}

