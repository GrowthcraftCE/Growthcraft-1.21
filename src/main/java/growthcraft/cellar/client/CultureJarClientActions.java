package growthcraft.cellar.client;

import growthcraft.cellar.client.screen.CultureJarScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class CultureJarClientActions {
    private CultureJarClientActions() {}

    public static void openCultureJarScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new CultureJarScreen());
    }
}
