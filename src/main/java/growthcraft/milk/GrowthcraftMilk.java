package growthcraft.milk;

import com.mojang.logging.LogUtils;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
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
        GrowthcraftMilkBlocks.BLOCKS.register(modEventBus);
        GrowthcraftMilkItems.ITEMS.register(modEventBus);
        GrowthcraftMilkFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftMilkFluids.FLUIDS.register(modEventBus);
        GrowthcraftMilkFluids.BLOCKS.register(modEventBus);

        // Contribute items to the creative tab
        modEventBus.addListener(this::buildCreativeTab);

        // Register to the global event bus for general events
        NeoForge.EVENT_BUS.register(this);

        // Register module config
        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftMilkConfig.SPEC);

        LOGGER.info("Growthcraft Milk module initialized");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[{}] Common setup", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            // Ingredients
            event.accept(GrowthcraftMilkItems.BUTTER.get());
            event.accept(GrowthcraftMilkItems.BUTTER_SALTED.get());
            event.accept(GrowthcraftMilkItems.CHEESE_CLOTH.get());
            event.accept(GrowthcraftMilkItems.STARTER_CULTURE.get());
            event.accept(GrowthcraftMilkItems.STOMACH.get());
            event.accept(GrowthcraftMilkItems.THISTLE.get());
            event.accept(GrowthcraftMilkItems.THISTLE_SEED.get());
            event.accept(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS.get());
            event.accept(GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get());

            // Foods
            event.accept(GrowthcraftMilkItems.RICOTTA_CHEESE_SLICE.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_APPLE.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_CHOCOLATE.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_GRAPE_PURPLE.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_GRAPE_RED.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_GRAPE_WHITE.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_HONEY.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_PUMPKIN.get());
            event.accept(GrowthcraftMilkItems.ICE_CREAM_WATERMELON.get());
            event.accept(GrowthcraftMilkItems.YOGURT_APPLE.get());
            event.accept(GrowthcraftMilkItems.YOGURT_CHOCOLATE.get());
            event.accept(GrowthcraftMilkItems.YOGURT_GRAPE_PURPLE.get());
            event.accept(GrowthcraftMilkItems.YOGURT_GRAPE_RED.get());
            event.accept(GrowthcraftMilkItems.YOGURT_GRAPE_WHITE.get());
            event.accept(GrowthcraftMilkItems.YOGURT_HONEY.get());
            event.accept(GrowthcraftMilkItems.YOGURT_PLAIN.get());
            event.accept(GrowthcraftMilkItems.YOGURT_PUMPKIN.get());
            event.accept(GrowthcraftMilkItems.YOGURT_WATERMELON.get());

            // Tools
            event.accept(GrowthcraftMilkItems.MILKING_BUCKET_IRON.get());

            // Fluid buckets (Milk module)
            event.accept(GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[{}] Server starting hook", Reference.NAME);
    }
}
