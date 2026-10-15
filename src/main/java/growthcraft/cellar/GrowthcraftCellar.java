package growthcraft.cellar;

import com.mojang.logging.LogUtils;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.*;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GrowthcraftCellar.MODID)
public class GrowthcraftCellar {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftCellar(IEventBus modEventBus, ModContainer modContainer) {
        // Register Deferred Registers
        GrowthcraftCellarItems.ITEMS.register(modEventBus);
        GrowthcraftCellarBlocks.BLOCKS.register(modEventBus);
        GrowthcraftCellarFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftCellarFluids.FLUIDS.register(modEventBus);
        GrowthcraftCellarFluids.BLOCKS.register(modEventBus);
        GrowthcraftCellarBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        GrowthcraftCellarMenus.MENUS.register(modEventBus);
        GrowthcraftCellarRecipes.SERIALIZERS.register(modEventBus);
        GrowthcraftCellarRecipes.TYPES.register(modEventBus);
        GrowthcraftCellarLootModifiers.init(modEventBus);

        // Capabilities registration
        modEventBus.addListener(GrowthcraftCellarCapabilities::registerCapabilities);

        // Add creative tab contributions
        modEventBus.addListener(this::buildCreativeTab);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftCellarConfig.SPEC);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            // Blocks
            event.accept(GrowthcraftCellarItems.BREW_KETTLE);
            event.accept(GrowthcraftCellarItems.CULTURE_JAR);
            event.accept(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK);
            event.accept(GrowthcraftCellarItems.FRUIT_PRESS);
            event.accept(GrowthcraftCellarItems.ROASTER);

            // Grains
            event.accept(GrowthcraftCellarItems.GRAIN);
            event.accept(GrowthcraftCellarItems.GRAIN_AMBER);
            event.accept(GrowthcraftCellarItems.GRAIN_BROWN);
            event.accept(GrowthcraftCellarItems.GRAIN_COPPER);
            event.accept(GrowthcraftCellarItems.GRAIN_DARK);
            event.accept(GrowthcraftCellarItems.GRAIN_DEEP_AMBER);
            event.accept(GrowthcraftCellarItems.GRAIN_DEEP_COPPER);
            event.accept(GrowthcraftCellarItems.GRAIN_GOLDEN);
            event.accept(GrowthcraftCellarItems.GRAIN_PALE_GOLDEN);

            // Grapes
            event.accept(GrowthcraftCellarItems.GRAPE_PURPLE);
            event.accept(GrowthcraftCellarItems.GRAPE_RED);
            event.accept(GrowthcraftCellarItems.GRAPE_WHITE);

            // Seeds
            event.accept(GrowthcraftCellarItems.GRAPE_SEEDS_PURPLE);
            event.accept(GrowthcraftCellarItems.GRAPE_SEEDS_RED);
            event.accept(GrowthcraftCellarItems.GRAPE_SEEDS_WHITE);
            event.accept(GrowthcraftCellarItems.HOPS_SEEDS);

            // Hops & misc
            event.accept(GrowthcraftCellarItems.HOPS);
            event.accept(GrowthcraftCellarItems.CORK_BARK);
            event.accept(GrowthcraftCellarItems.CORK_COASTER);
            event.accept(GrowthcraftCellarItems.CORK_TREE_SAPLING);
            event.accept(GrowthcraftCellarItems.CORK_TREE_LEAVES);
            event.accept(GrowthcraftCellarItems.CORK_WOOD_LOG);
            event.accept(GrowthcraftCellarItems.CORK_WOOD_LOG_STRIPPED);
            event.accept(GrowthcraftCellarItems.CORK_WOOD);
            event.accept(GrowthcraftCellarItems.CORK_WOOD_STRIPPED);

            // Yeasts
            event.accept(GrowthcraftCellarItems.YEAST_BAYANUS);
            event.accept(GrowthcraftCellarItems.YEAST_BAYANUS_ETHEREAL);
            event.accept(GrowthcraftCellarItems.YEAST_BREWERS);
            event.accept(GrowthcraftCellarItems.YEAST_BREWERS_ETHEREAL);
            event.accept(GrowthcraftCellarItems.YEAST_ETHEREAL);
            event.accept(GrowthcraftCellarItems.YEAST_LAGER);
            event.accept(GrowthcraftCellarItems.YEAST_LAGER_ETHEREAL);

            // Fluid buckets
            for (var container : GrowthcraftCellarFluids.ALL) {
                event.accept(container.bucket.get());
            }

            // other
            event.accept(GrowthcraftCellarItems.BOTTLE_STAINED);
        }
    }
}
