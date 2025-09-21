package growthcraft.cellar.init;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Registers NeoForge capabilities for the Cellar module.
 */
public final class GrowthcraftCellarCapabilities {
    private GrowthcraftCellarCapabilities() {}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Expose the Culture Jar's internal tank as a fluid handler capability
        GrowthcraftCellar.LOGGER.debug("[Capabilities] Registering FluidHandler for Culture Jar BE");
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftCellarBlockEntities.CULTURE_JAR.get(),
                (CultureJarBlockEntity be, net.minecraft.core.Direction side) -> be.getTank());
    }
}
