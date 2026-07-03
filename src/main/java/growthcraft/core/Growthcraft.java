package growthcraft.core;

import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftConditions;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import growthcraft.core.init.GrowthcraftItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Growthcraft.MODID)
public class Growthcraft {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public Growthcraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register Deferred Registers
        GrowthcraftItems.ITEMS.register(modEventBus);
        GrowthcraftBlocks.BLOCKS.register(modEventBus);
        GrowthcraftCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        GrowthcraftConditions.CONDITION_CODECS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
