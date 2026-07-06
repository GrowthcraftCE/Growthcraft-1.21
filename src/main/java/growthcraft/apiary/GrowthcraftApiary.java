package growthcraft.apiary;

import com.mojang.logging.LogUtils;
import growthcraft.apiary.config.Reference;
import growthcraft.apiary.init.GrowthcraftApiaryBlocks;
import growthcraft.apiary.init.GrowthcraftApiaryFluids;
import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftApiary.MODID)
public class GrowthcraftApiary {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftApiary(IEventBus modEventBus) {
        GrowthcraftApiaryBlocks.BLOCKS.register(modEventBus);
        GrowthcraftApiaryItems.ITEMS.register(modEventBus);
        GrowthcraftApiaryFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftApiaryFluids.FLUIDS.register(modEventBus);
        GrowthcraftApiaryFluids.BLOCKS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            GrowthcraftApiaryItems.SIMPLE_ITEMS.forEach(item -> event.accept(item.get()));
            for (var container : GrowthcraftApiaryFluids.ALL) {
                event.accept(container.bucket.get());
            }
            for (var wax : GrowthcraftApiaryFluids.WAXES) {
                event.accept(wax.bucket.get());
            }
        }
    }
}
