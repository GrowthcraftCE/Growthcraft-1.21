package growthcraft.cellar.init;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Registers NeoForge capabilities for the Cellar module.
 */
public final class GrowthcraftCellarCapabilities {
    private GrowthcraftCellarCapabilities() {}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Expose the Culture Jar's internal tank as a fluid handler capability
        debug("[Capabilities] Registering FluidHandler for Culture Jar BE");
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftCellarBlockEntities.CULTURE_JAR.get(),
                (CultureJarBlockEntity be, net.minecraft.core.Direction side) -> be.getTank());

        debug("[Capabilities] Registering FluidHandlers for Brew Kettle BE");
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftCellarBlockEntities.BREW_KETTLE.get(),
                (BrewKettleBlockEntity be, Direction side) -> side == Direction.UP ? be.getInputTank() : be.getOutputTank());

        debug("[Capabilities] Registering FluidHandler for Fermentation Barrel BE");
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftCellarBlockEntities.FERMENTATION_BARREL.get(),
                (FermentationBarrelBlockEntity be, Direction side) -> be.getTank());

        debug("[Capabilities] Registering FluidHandler for Fruit Press BE");
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(),
                (FruitPressBlockEntity be, Direction side) -> be.getTank());
    }

    private static void debug(String message, Object... args) {
        if (GrowthcraftCellarConfig.Debug.isCapabilitiesDebugEnabled()) {
            GrowthcraftCellar.LOGGER.debug(message, args);
        }
    }
}
