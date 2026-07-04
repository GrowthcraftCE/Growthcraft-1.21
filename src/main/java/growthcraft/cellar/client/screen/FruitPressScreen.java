package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FruitPressMenu;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FruitPressScreen extends AbstractContainerScreen<FruitPressMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/fruit_press_screen.png");

    private static final int TANK_X = 72;
    private static final int TANK_Y = 17;
    private static final int TANK_W = 50;
    private static final int TANK_H = 52;

    private static final int PROGRESS_X = 51;
    private static final int PROGRESS_Y = 20;
    private static final int PROGRESS_U = 188;
    private static final int PROGRESS_V = 0;
    private static final int PROGRESS_W = 8;
    private static final int PROGRESS_H = 28;
    private static final int OUTPUT_SLOT_X = 141;
    private static final int OUTPUT_SLOT_Y = 53;
    private static final int SLOT_U = 7;
    private static final int SLOT_V = 83;
    private static final int SLOT_SIZE = 18;

    private final FluidTankRenderer tankRenderer;

    public FruitPressScreen(FruitPressMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.blit(TEXTURE, this.leftPos + OUTPUT_SLOT_X - 1, this.topPos + OUTPUT_SLOT_Y - 1,
                SLOT_U, SLOT_V, SLOT_SIZE, SLOT_SIZE);

        int progress = this.menu.getProgressionScaled(PROGRESS_H);
        if (progress > 0) {
            graphics.blit(TEXTURE, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y + PROGRESS_H - progress,
                    PROGRESS_U, PROGRESS_V + PROGRESS_H - progress, PROGRESS_W, progress);
        }

        this.tankRenderer.render(graphics, this.leftPos + TANK_X, this.topPos + TANK_Y, this.menu.getFluidStack());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int tankLeft = this.leftPos + TANK_X;
        int tankTop = this.topPos + TANK_Y;
        if (isMouseAbove(mouseX, mouseY, tankLeft, tankTop, TANK_W, TANK_H)) {
            var stack = this.menu.getFluidStack();
            Component name = stack.isEmpty() ? Component.translatable("gui.growthcraft_cellar.empty") : stack.getHoverName();
            Component amount = Component.literal(this.menu.getFluidAmount() + " mB / " + this.menu.getTankCapacity() + " mB");
            graphics.renderTooltip(this.font, Component.empty().append(name).append(Component.literal(" ")).append(amount), mouseX, mouseY);
        }

        int progressLeft = this.leftPos + PROGRESS_X;
        int progressTop = this.topPos + PROGRESS_Y;
        if (isMouseAbove(mouseX, mouseY, progressLeft, progressTop, PROGRESS_W, PROGRESS_H)) {
            graphics.renderTooltip(this.font, Component.translatable("growthcraft_cellar.tooltip.fruit_press.progress", this.menu.getPercentProgress()), mouseX, mouseY);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
