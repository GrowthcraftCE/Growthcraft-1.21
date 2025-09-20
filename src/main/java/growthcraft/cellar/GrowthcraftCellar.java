package growthcraft.cellar;

import com.mojang.logging.LogUtils;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GrowthcraftCellar.MODID)
public class GrowthcraftCellar {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftCellar(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register Deferred Registers
        GrowthcraftCellarItems.ITEMS.register(modEventBus);

        // Add creative tab contributions
        modEventBus.addListener(this::buildCreativeTab);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftCellarConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            event.accept(GrowthcraftCellarItems.GRAIN);
            event.accept(GrowthcraftCellarItems.GRAIN_AMBER);
            event.accept(GrowthcraftCellarItems.GRAIN_BROWN);
            event.accept(GrowthcraftCellarItems.GRAIN_COPPER);
            event.accept(GrowthcraftCellarItems.GRAIN_DARK);
            event.accept(GrowthcraftCellarItems.GRAIN_DEEP_AMBER);
            event.accept(GrowthcraftCellarItems.GRAIN_DEEP_COPPER);
            event.accept(GrowthcraftCellarItems.GRAIN_GOLDEN);
            event.accept(GrowthcraftCellarItems.GRAIN_PALE_GOLDEN);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
