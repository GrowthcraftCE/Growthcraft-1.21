package growthcraft.milk.client.screen;

import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.menu.MixingVatMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class MixingVatScreen extends AbstractContainerScreen<MixingVatMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_milk", "textures/gui/mixing_vat_screen.png");

    private static final int MAIN_TANK_X = 49;
    private static final int MAIN_TANK_Y = 32;
    private static final int MAIN_TANK_W = 16;
    private static final int MAIN_TANK_H = 38;
    private static final int SIDE_TANK_X = 49;
    private static final int SIDE_TANK_Y = 18;
    private static final int SIDE_TANK_W = 16;
    private static final int SIDE_TANK_H = 11;
    private static final int PROGRESS_X = 100;
    private static final int PROGRESS_Y = 21;
    private static final int PROGRESS_W = 11;
    private static final int PROGRESS_H = 27;
    private static final int HEAT_X = 99;
    private static final int HEAT_Y = 57;
    private static final int HEAT_U = 176;
    private static final int HEAT_V = 28;
    private static final int HEAT_W = 13;
    private static final int HEAT_H = 13;
    private static final int[][] BUBBLE_PIXELS = new int[][] {
            { 102, 21 },
            { 101, 22 },
            { 102, 22 },
            { 107, 25 },
            { 106, 26 },
            { 107, 26 },
            { 103, 29 },
            { 102, 30 },
            { 103, 30 },
            { 107, 33 },
            { 108, 33 },
            { 102, 34 },
            { 106, 34 },
            { 107, 34 },
            { 108, 34 },
            { 101, 35 },
            { 102, 35 },
            { 106, 35 },
            { 107, 35 },
            { 108, 35 },
            { 101, 38 },
            { 102, 38 },
            { 100, 39 },
            { 101, 39 },
            { 102, 39 },
            { 108, 39 },
            { 100, 40 },
            { 101, 40 },
            { 102, 40 },
            { 107, 40 },
            { 108, 40 },
            { 104, 43 },
            { 105, 43 },
            { 106, 43 },
            { 103, 44 },
            { 104, 44 },
            { 105, 44 },
            { 106, 44 },
            { 103, 45 },
            { 104, 45 },
            { 105, 45 },
            { 106, 45 },
            { 103, 46 },
            { 104, 46 },
            { 105, 46 },
            { 106, 46 },
            { 104, 47 },
            { 105, 47 },
            { 106, 47 }
    };

    private final FluidTankRenderer mainTankRenderer;
    private final FluidTankRenderer sideTankRenderer;

    public MixingVatScreen(MixingVatMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.mainTankRenderer = new FluidTankRenderer(MAIN_TANK_W, MAIN_TANK_H, menu.getMainTankCapacity(), 0.85F);
        this.sideTankRenderer = new FluidTankRenderer(SIDE_TANK_W, SIDE_TANK_H, menu.getSideTankCapacity(), 0.85F);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.mainTankRenderer.render(graphics, this.leftPos + MAIN_TANK_X, this.topPos + MAIN_TANK_Y, this.menu.getMainFluidStack());
        this.sideTankRenderer.render(graphics, this.leftPos + SIDE_TANK_X, this.topPos + SIDE_TANK_Y, this.menu.getSideFluidStack());

        drawProgress(graphics);

        if (this.menu.isHeated()) {
            graphics.blit(TEXTURE, this.leftPos + HEAT_X, this.topPos + HEAT_Y, HEAT_U, HEAT_V, HEAT_W, HEAT_H);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        renderTankTooltip(graphics, mouseX, mouseY, MAIN_TANK_X, MAIN_TANK_Y, MAIN_TANK_W, MAIN_TANK_H,
                this.menu.getMainFluidStack(), this.menu.getMainTankCapacity());
        renderTankTooltip(graphics, mouseX, mouseY, SIDE_TANK_X, SIDE_TANK_Y, SIDE_TANK_W, SIDE_TANK_H,
                this.menu.getSideFluidStack(), this.menu.getSideTankCapacity());
        if (isMouseAbove(mouseX, mouseY, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, PROGRESS_W, PROGRESS_H)) {
            graphics.renderTooltip(this.font, Component.literal(this.menu.getPercentProgress() + "%"), mouseX, mouseY);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {

        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getSlotIndex() == 3 && this.hoveredSlot.hasItem()) {
            // result slot
            ItemStack finalizerItem = this.getMenu().getResultActivationTool();
            ItemStack resultItemStack = this.hoveredSlot.getItem();
            if (this.lastResultItem != resultItemStack.hashCode()) {
                this.tooltipLines = this.getTooltipFromContainerItem(resultItemStack);
                if (finalizerItem != null) {
                    Component finalizerText = finalizerItem.isEmpty() ? Component.translatable("message.growthcraft_milk.get_using_item_empty_hand").withStyle(Style.EMPTY.withColor(0xffffff88)) : finalizerItem.getHoverName().copy().withStyle(Style.EMPTY.withColor(0xffffff88));
                    this.lastComponent = Component.translatable("message.growthcraft_milk.get_using_item", finalizerText).withStyle(Style.EMPTY.withColor(0xffddbb44));
                    this.tooltipLines.add(this.lastComponent);
                }
                this.lastResultItem = resultItemStack.hashCode();
            }
            guiGraphics.renderTooltip(this.font, tooltipLines, resultItemStack.getTooltipImage(), resultItemStack, x, y);
        }
        else {
            // not a result slot
            super.renderTooltip(guiGraphics, x, y);
        }
    }
    private int lastResultItem = 0;
    private Component lastComponent = null;
    private List<Component> tooltipLines = null;

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

        int minY = PROGRESS_Y + PROGRESS_H - progress;
        for (int[] pixel : BUBBLE_PIXELS) {
            int x = pixel[0];
            int y = pixel[1];
            if (y >= minY) {
                graphics.fill(this.leftPos + x, this.topPos + y, this.leftPos + x + 1, this.topPos + y + 1, 0xFFEDEDED);
            }
        }
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
