package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.RoasterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RoasterScreen extends AbstractContainerScreen<RoasterMenu> {
    public RoasterScreen(RoasterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        graphics.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xFFC6C6C6);
        graphics.fill(x + 7, y + 17, x + 169, y + 75, 0xFF8B8B8B);
        graphics.fill(x + 8, y + 18, x + 168, y + 74, 0xFFEFEFEF);
        graphics.fill(x + 7, y + 83, x + 169, y + 161, 0xFF8B8B8B);
        graphics.fill(x + 8, y + 84, x + 168, y + 160, 0xFFEFEFEF);

        graphics.fill(x + 78, y + 38, x + 102, y + 43, 0xFF5F5F5F);
        int progress = this.menu.getProgressionScaled(24);
        graphics.fill(x + 78, y + 38, x + 78 + progress, y + 43, 0xFFFFB347);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, Component.translatable("label.growthcraft_cellar.roaster_level", this.menu.getRoastingLevel()), 68, 20, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }
}
