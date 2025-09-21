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
    private static final int TANK_X = 80; // adjust as needed to match texture
    private static final int TANK_Y = 18;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;

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
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Draw fluid tank contents via reusable renderer with shimmer effect
        FluidStack stack = this.menu.getClientFluidStack();
        this.tankRenderer.renderWithShimmer(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, stack);
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

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
