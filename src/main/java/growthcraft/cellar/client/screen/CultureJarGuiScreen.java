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
        // Explicitly disable menu background blur for this non-container in-world GUI
        Minecraft.getInstance().gameRenderer.shutdownEffect();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Ensure any menu background blur is disabled for this screen on each frame
        Minecraft.getInstance().gameRenderer.shutdownEffect();
        // Let the base Screen render once (this will invoke our non-blur background)
        super.render(graphics, mouseX, mouseY, partialTick);
        // Now draw our GUI texture and labels on top so they are not darkened by the background overlay
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        graphics.drawString(this.font, this.title, this.leftPos + 8, this.topPos + 6, 4210752, false);
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
