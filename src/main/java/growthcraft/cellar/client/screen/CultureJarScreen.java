package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.CultureJarMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class CultureJarScreen extends AbstractContainerScreen<CultureJarMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/culture_jar_screen.png");

    // Tank render area inside the GUI (relative to top-left of the GUI)
    private static final int TANK_X = 80; // adjust as needed to match texture
    private static final int TANK_Y = 18;
    private static final int TANK_W = 15;
    private static final int TANK_H = 52;

    public CultureJarScreen(CultureJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Draw fluid tank contents as a simple tinted bar
        int amount = this.menu.getFluidAmount();
        int capacity = this.menu.getTankCapacity();
        if (amount > 0 && capacity > 0) {
            FluidStack stack = this.menu.getClientFluidStack();
            int color = 0xAAFFFFFF; // default with alpha
            if (!stack.isEmpty()) {
                color = (0xAA << 24) | (IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor() & 0xFFFFFF);
            }
            int filled = Math.max(1, (int)Math.floor((amount / (double)capacity) * TANK_H));
            int x0 = this.leftPos + TANK_X;
            int y1 = this.topPos + TANK_Y + TANK_H; // bottom
            int y0 = y1 - filled; // top of filled area
            graphics.fill(x0, y0, x0 + TANK_W, y1, color);
        }
        // optional: draw a thin dark border for clarity
        int bx = this.leftPos + TANK_X;
        int by = this.topPos + TANK_Y;
        int bcolor = 0xFF2F2F2F;
        graphics.fill(bx - 1, by - 1, bx + TANK_W + 1, by, bcolor);
        graphics.fill(bx - 1, by + TANK_H, bx + TANK_W + 1, by + TANK_H + 1, bcolor);
        graphics.fill(bx - 1, by, bx, by + TANK_H, bcolor);
        graphics.fill(bx + TANK_W, by, bx + TANK_W + 1, by + TANK_H, bcolor);
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
