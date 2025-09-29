package growthcraft.milk;

import com.mojang.logging.LogUtils;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

/**
 * Minimal module stub for Growthcraft Milk.
 * This registers the mod with NeoForge and logs lifecycle events.
 */
@Mod(GrowthcraftMilk.MODID)
public class GrowthcraftMilk {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftMilk(IEventBus modEventBus, ModContainer modContainer) {
        // Register lifecycle listeners
        modEventBus.addListener(this::commonSetup);

        // Register all Milk module registries
        GrowthcraftMilkItems.ITEMS.register(modEventBus);
        GrowthcraftMilkFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftMilkFluids.FLUIDS.register(modEventBus);
        GrowthcraftMilkFluids.BLOCKS.register(modEventBus);

        // Register to the global event bus for general events
        NeoForge.EVENT_BUS.register(this);

        // Register module config
        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftMilkConfig.SPEC);

        LOGGER.info("Growthcraft Milk module initialized");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[{}] Common setup", Reference.NAME);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[{}] Server starting hook", Reference.NAME);
    }
}
