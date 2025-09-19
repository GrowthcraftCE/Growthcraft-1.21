package growthcraft.core;

import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Growthcraft.MODID)
public class Growthcraft {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public Growthcraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::buildCreativeTabs);

        // Register Deferred Registers
        GrowthcraftItems.ITEMS.register(modEventBus);
        GrowthcraftBlocks.BLOCKS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(GrowthcraftItems.CROWBAR_WHITE);
            event.accept(GrowthcraftItems.CROWBAR_LIGHT_GRAY);
            event.accept(GrowthcraftItems.CROWBAR_GRAY);
            event.accept(GrowthcraftItems.CROWBAR_BLACK);
            event.accept(GrowthcraftItems.CROWBAR_BROWN);
            event.accept(GrowthcraftItems.CROWBAR_RED);
            event.accept(GrowthcraftItems.CROWBAR_ORANGE);
            event.accept(GrowthcraftItems.CROWBAR_YELLOW);
            event.accept(GrowthcraftItems.CROWBAR_LIME);
            event.accept(GrowthcraftItems.CROWBAR_GREEN);
            event.accept(GrowthcraftItems.CROWBAR_CYAN);
            event.accept(GrowthcraftItems.CROWBAR_LIGHT_BLUE);
            event.accept(GrowthcraftItems.CROWBAR_BLUE);
            event.accept(GrowthcraftItems.CROWBAR_PURPLE);
            event.accept(GrowthcraftItems.CROWBAR_MAGENTA);
            event.accept(GrowthcraftItems.CROWBAR_PINK);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
