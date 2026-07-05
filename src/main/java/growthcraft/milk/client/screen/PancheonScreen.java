package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.menu.PancheonMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;

public class PancheonScreen extends AbstractContainerScreen<PancheonMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_milk", "textures/gui/pancheon_screen.png");

    private static final int INPUT_TANK_X = 62;
    private static final int INPUT_TANK_Y = 18;
    private static final int INPUT_TANK_W = 16;
    private static final int INPUT_TANK_H = 52;
    private static final int OUTPUT_TANK_X = 98;
    private static final int OUTPUT_0_TANK_Y = 18;
    private static final int OUTPUT_1_TANK_Y = 47;
    private static final int OUTPUT_TANK_W = 16;
    private static final int OUTPUT_TANK_H = 23;
    private static final int PROGRESS_X = 82;
    private static final int PROGRESS_Y = 29;
    private static final int PROGRESS_W = 13;
    private static final int PROGRESS_H = 29;
    private static final int PROGRESS_U = 176;
    private static final int PROGRESS_V = 42;

    private final FluidTankRenderer inputTankRenderer;
    private final FluidTankRenderer outputTankRenderer;

    public PancheonScreen(PancheonMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inputTankRenderer = new FluidTankRenderer(INPUT_TANK_W, INPUT_TANK_H, menu.getInputTankCapacity(), 0.85F);
        this.outputTankRenderer = new FluidTankRenderer(OUTPUT_TANK_W, OUTPUT_TANK_H, menu.getOutputTankCapacity(), 0.85F);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        drawProgress(graphics);

        this.inputTankRenderer.render(graphics, this.leftPos + INPUT_TANK_X, this.topPos + INPUT_TANK_Y, this.menu.getInputFluidStack());
        this.outputTankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + OUTPUT_0_TANK_Y, this.menu.getOutput0FluidStack());
        this.outputTankRenderer.render(graphics, this.leftPos + OUTPUT_TANK_X, this.topPos + OUTPUT_1_TANK_Y, this.menu.getOutput1FluidStack());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        renderTankTooltip(graphics, mouseX, mouseY, INPUT_TANK_X, INPUT_TANK_Y, INPUT_TANK_W, INPUT_TANK_H,
                this.menu.getInputFluidStack(), this.menu.getInputTankCapacity());
        renderTankTooltip(graphics, mouseX, mouseY, OUTPUT_TANK_X, OUTPUT_0_TANK_Y, OUTPUT_TANK_W, OUTPUT_TANK_H,
                this.menu.getOutput0FluidStack(), this.menu.getOutputTankCapacity());
        renderTankTooltip(graphics, mouseX, mouseY, OUTPUT_TANK_X, OUTPUT_1_TANK_Y, OUTPUT_TANK_W, OUTPUT_TANK_H,
                this.menu.getOutput1FluidStack(), this.menu.getOutputTankCapacity());
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
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

        graphics.blit(TEXTURE,
                this.leftPos + PROGRESS_X,
                this.topPos + PROGRESS_Y + PROGRESS_H - progress,
                PROGRESS_U,
                PROGRESS_V + PROGRESS_H - progress,
                PROGRESS_W,
                progress);
    }

    private void renderTankTooltip(GuiGraphics graphics, int mouseX, int mouseY, int x, int y, int width, int height,
                                   FluidStack stack, int capacity) {
        if (!isMouseAbove(mouseX, mouseY, this.leftPos + x, this.topPos + y, width, height)) return;
        Component name = stack.isEmpty() ? Component.translatable("gui.growthcraft_milk.empty") : stack.getHoverName();
        Component amount = Component.literal(stack.getAmount() + " mB / " + capacity + " mB");
        graphics.renderTooltip(this.font, Component.empty().append(name).append(Component.literal(" ")).append(amount), mouseX, mouseY);
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
