package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class BrewKettleScreen extends AbstractContainerScreen<BrewKettleMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/brew_kettle_screen.png");

    private static final int INPUT_TANK_X = 46;
    private static final int OUTPUT_TANK_X = 114;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 16;
    private static final int TANK_H = 52;
    private static final int LID_BUTTON_X = 10;
    private static final int LID_BUTTON_Y = 27;
    private static final int LID_BUTTON_W = 13;
    private static final int LID_BUTTON_H = 16;

    private final FluidTankRenderer tankRenderer;

    public BrewKettleScreen(BrewKettleMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int lidV = this.menu.hasLid() ? 57 : 112;
        int lidU = isMouseAbove(mouseX, mouseY, this.leftPos + LID_BUTTON_X, this.topPos + LID_BUTTON_Y, LID_BUTTON_W, LID_BUTTON_H) ? 221 : 186;
        graphics.blit(TEXTURE, this.leftPos + 7, this.topPos + 16, lidU, lidV, 34, 54);

        int progress = this.menu.getProgressionScaled(28);
        if (progress > 0) {
            graphics.blit(TEXTURE, this.leftPos + 98, this.topPos + 30, 176, 0, 9, progress);
        }

        if (this.menu.isHeated()) {
            graphics.blit(TEXTURE, this.leftPos + 68, this.topPos + 53, 176, 28, 13, 13);
        }

        this.tankRenderer.render(graphics, this.leftPos + INPUT_TANK_X, this.topPos + TANK_Y, this.menu.getInputFluidStack());
        this.tankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + TANK_Y, this.menu.getOutputFluidStack());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTankTooltip(graphics, mouseX, mouseY, this.leftPos + INPUT_TANK_X, this.topPos + TANK_Y, this.menu.getInputFluidStack());
        renderTankTooltip(graphics, mouseX, mouseY, this.leftPos + OUTPUT_TANK_X, this.topPos + TANK_Y, this.menu.getOutputFluidStack());
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.minecraft != null && this.minecraft.gameMode != null
                && isMouseAbove((int) mouseX, (int) mouseY, this.leftPos + LID_BUTTON_X, this.topPos + LID_BUTTON_Y, LID_BUTTON_W, LID_BUTTON_H)) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderTankTooltip(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, FluidStack stack) {
        if (!isMouseAbove(mouseX, mouseY, x, y, TANK_W, TANK_H)) return;
        Component name = stack.isEmpty() ? Component.translatable("gui.growthcraft_cellar.empty") : stack.getHoverName();
        Component amount = Component.literal(stack.getAmount() + " mB / " + this.menu.getTankCapacity() + " mB");
        graphics.renderTooltip(this.font, Component.empty().append(name).append(Component.literal(" ")).append(amount), mouseX, mouseY);
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
