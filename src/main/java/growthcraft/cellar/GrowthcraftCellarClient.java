package growthcraft.cellar;

import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import growthcraft.cellar.client.screen.BrewKettleScreen;
import growthcraft.cellar.client.screen.CultureJarScreen;
import growthcraft.cellar.client.screen.FermentationBarrelScreen;
import growthcraft.cellar.client.screen.RoasterScreen;
import growthcraft.core.Growthcraft;
import growthcraft.lib.client.screen.MachineScreen;
import growthcraft.lib.fluid.FluidRegistryContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Growthcraft.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Growthcraft.MODID, value = Dist.CLIENT)
public class GrowthcraftCellarClient {
    public GrowthcraftCellarClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Growthcraft.LOGGER.info("HELLO FROM CLIENT SETUP");
        Growthcraft.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // Ensure all Growthcraft Cellar fluids render as translucent like water
        event.enqueueWork(() -> {
            RenderType translucent = RenderType.translucent();
            for (FluidRegistryContainer container : GrowthcraftCellarFluids.ALL) {
                ItemBlockRenderTypes.setRenderLayer(container.source.get(), translucent);
                ItemBlockRenderTypes.setRenderLayer(container.flowing.get(), translucent);
            }

        });
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GrowthcraftCellarMenus.CULTURE_JAR.get(), CultureJarScreen::new);
        event.register(GrowthcraftCellarMenus.BREW_KETTLE.get(), BrewKettleScreen::new);
        event.register(GrowthcraftCellarMenus.FERMENTATION_BARREL.get(), FermentationBarrelScreen::new);
        event.register(GrowthcraftCellarMenus.FRUIT_PRESS.get(), MachineScreen::new);
        event.register(GrowthcraftCellarMenus.ROASTER.get(), RoasterScreen::new);
    }
}
