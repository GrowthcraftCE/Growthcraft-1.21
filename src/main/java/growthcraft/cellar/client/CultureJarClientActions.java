package growthcraft.cellar.client;

import growthcraft.cellar.client.screen.CultureJarGuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class CultureJarClientActions {
    private CultureJarClientActions() {}

    public static void openCultureJarScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new CultureJarGuiScreen());
    }
}
