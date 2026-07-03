package growthcraft.apples;

import com.mojang.logging.LogUtils;
import growthcraft.apples.config.Reference;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.init.GrowthcraftApplesFluids;
import growthcraft.apples.init.GrowthcraftApplesItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftApples.MODID)
public class GrowthcraftApples {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftApples(IEventBus modEventBus) {
        GrowthcraftApplesBlocks.BLOCKS.register(modEventBus);
        GrowthcraftApplesItems.ITEMS.register(modEventBus);
        GrowthcraftApplesFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftApplesFluids.FLUIDS.register(modEventBus);
        GrowthcraftApplesFluids.BLOCKS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            event.accept(GrowthcraftApplesItems.APPLE_PLANK.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_BUTTON.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_DOOR.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_FENCE.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_FENCE_GATE.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_PRESSURE_PLATE.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_SLAB.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_STAIRS.get());
            event.accept(GrowthcraftApplesItems.APPLE_PLANK_TRAPDOOR.get());
            event.accept(GrowthcraftApplesItems.APPLE_WOOD.get());
            event.accept(GrowthcraftApplesItems.APPLE_WOOD_LOG.get());
            event.accept(GrowthcraftApplesItems.APPLE_WOOD_LOG_STRIPPED.get());
            event.accept(GrowthcraftApplesItems.APPLE_WOOD_STRIPPED.get());
            event.accept(GrowthcraftApplesItems.BEE_BOX_APPLE.get());
            event.accept(GrowthcraftApplesItems.APPLE_SEEDS.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_FRUIT.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_LEAVES.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_SAPLING.get());
            for (var container : GrowthcraftApplesFluids.ALL) {
                event.accept(container.bucket.get());
            }
        }
    }
}
