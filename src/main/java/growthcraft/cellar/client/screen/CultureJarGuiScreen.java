package growthcraft.cellar.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CultureJarGuiScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/culture_jar_screen.png");
    private int imageWidth = 176;
    private int imageHeight = 166;
    private int leftPos;
    private int topPos;

    public CultureJarGuiScreen() {
        super(Component.translatable("container.growthcraft_cellar.culture_jar"));
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Draw a simple translucent background without invoking menu blur
        graphics.fill(0, 0, this.width, this.height, 0xA0000000);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.drawString(this.font, this.title, this.leftPos + 8, this.topPos + 6, 4210752, false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        // Ensure any active post effect (e.g., blur) is shut down when this screen is removed
        Minecraft.getInstance().gameRenderer.shutdownEffect();
        super.removed();
    }

    @Override
    public void onClose() {
        // Also ensure shutdown on close in case different lifecycle path is taken
        Minecraft.getInstance().gameRenderer.shutdownEffect();
        super.onClose();
    }

}
