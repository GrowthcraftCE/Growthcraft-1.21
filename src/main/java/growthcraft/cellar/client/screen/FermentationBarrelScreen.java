package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.lib.client.screen.renderer.FluidTankRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FermentationBarrelScreen extends AbstractContainerScreen<FermentationBarrelMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/fermentation_barrel_screen.png");
    private static final Component YEAST_WARNING = Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_warning")
            .withStyle(Style.EMPTY.withColor(0xd5bb88));
    private static final Component YEAST_ERROR = Component.translatable("growthcraft_cellar.tooltip.fermentation.yeast_error")
            .withStyle(Style.EMPTY.withColor(0xd68a71));

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

    private final FluidTankRenderer tankRenderer;

    public FermentationBarrelScreen(FermentationBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.tankRenderer = new FluidTankRenderer(TANK_W, TANK_H, menu.getTankCapacity(), 0.85F);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

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
            graphics.renderTooltip(this.font, Component.translatable("growthcraft_cellar.tooltip.fermentation.progress", this.menu.getPercentProgress()), mouseX, mouseY);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.menu.getCarried().isEmpty()
                && this.hoveredSlot != null
                && this.hoveredSlot.hasItem()
                && this.hoveredSlot.index == FermentationBarrelMenu.YEAST_SLOT
                && (this.menu.hasYeastWarning() || this.menu.hasYeastError())) {
            ItemStack stack = this.hoveredSlot.getItem();
            List<Component> tooltip = new ArrayList<>(this.getTooltipFromContainerItem(stack));
            tooltip.add(this.menu.hasYeastWarning() ? YEAST_WARNING : YEAST_ERROR);
            graphics.renderTooltip(this.font, tooltip, stack.getTooltipImage(), stack, mouseX, mouseY);
            return;
        }

        super.renderTooltip(graphics, mouseX, mouseY);
    }

    private static boolean isMouseAbove(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
