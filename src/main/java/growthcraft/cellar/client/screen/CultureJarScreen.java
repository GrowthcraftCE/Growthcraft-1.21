package growthcraft.cellar.client.screen;

import growthcraft.cellar.menu.CultureJarMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CultureJarScreen extends AbstractContainerScreen<CultureJarMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/culture_jar_screen.png");

    public CultureJarScreen(CultureJarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void init() {
        super.init();
        // Explicitly disable menu background blur for this in-world container screen
        Minecraft.getInstance().gameRenderer.shutdownEffect();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Ensure any menu background blur is disabled for this screen on each frame
        Minecraft.getInstance().gameRenderer.shutdownEffect();
        // Follow vanilla container render order to avoid filter/blur artifacts
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, this.title, 8, 6, 4210752, false);
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // Override background rendering to avoid any menu blur being applied by the base Screen implementation
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, this.width, this.height, 0xA0000000);
    }
}
