package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.RoasterMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RoasterScreen extends AbstractContainerScreen<RoasterMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/roaster_screen.png");

    private static final int PROGRESS_X = 76;
    private static final int PROGRESS_Y = 44;
    private static final int PROGRESS_U = 176;
    private static final int PROGRESS_V = 0;
    private static final int PROGRESS_WIDTH = 28;
    private static final int PROGRESS_HEIGHT = 9;

    private static final int HEAT_X = 80;
    private static final int HEAT_Y = 56;
    private static final int HEAT_U = 176;
    private static final int HEAT_V = 28;
    private static final int HEAT_WIDTH = 14;
    private static final int HEAT_HEIGHT = 14;

    public RoasterScreen(RoasterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int progress = this.menu.getProgressionScaled(PROGRESS_WIDTH);
        if (progress > 0) {
            graphics.blit(TEXTURE, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_U, PROGRESS_V, progress, PROGRESS_HEIGHT);
        }

        if (this.menu.isHeated()) {
            graphics.blit(TEXTURE, this.leftPos + HEAT_X, this.topPos + HEAT_Y, HEAT_U, HEAT_V, HEAT_WIDTH, HEAT_HEIGHT);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int progressLeft = this.leftPos + PROGRESS_X;
        int progressTop = this.topPos + PROGRESS_Y;
        if (isMouseAbove(mouseX, mouseY, progressLeft, progressTop, PROGRESS_WIDTH, PROGRESS_HEIGHT)) {
            graphics.renderTooltip(this.font, Component.translatable("growthcraft_cellar.tooltip.roaster.progress", this.menu.getPercentProgress()), mouseX, mouseY);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        Component levelText = Component.translatable("label.growthcraft_cellar.roaster_level", this.menu.getRoastingLevel());
        graphics.drawString(this.font, levelText, (this.imageWidth - this.font.width(levelText)) / 2, 20, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
