package growthcraft.rice;

import com.mojang.logging.LogUtils;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import growthcraft.rice.config.GrowthcraftRiceConfig;
import growthcraft.rice.config.Reference;
import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceFluids;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftRice.MODID)
public class GrowthcraftRice {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftRice(IEventBus modEventBus, ModContainer modContainer) {
        GrowthcraftRiceBlocks.BLOCKS.register(modEventBus);
        GrowthcraftRiceItems.ITEMS.register(modEventBus);
        GrowthcraftRiceFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftRiceFluids.FLUIDS.register(modEventBus);
        GrowthcraftRiceFluids.BLOCKS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftRiceConfig.SPEC);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            event.accept(GrowthcraftRiceItems.CULTIVATED_FARMLAND.get());
            event.accept(GrowthcraftRiceItems.CULTIVATOR.get());
            event.accept(GrowthcraftRiceItems.KNIFE.get());
            event.accept(GrowthcraftRiceItems.RICE_GRAINS.get());
            event.accept(GrowthcraftRiceItems.RICE.get());
            event.accept(GrowthcraftRiceItems.RICE_COOKED.get());
            event.accept(GrowthcraftRiceItems.RICE_STALK.get());
            event.accept(GrowthcraftRiceItems.SUSHI_ROLL.get());
            event.accept(GrowthcraftRiceItems.ONIGIRI.get());
            event.accept(GrowthcraftRiceItems.CHICKEN_RICE.get());
            event.accept(GrowthcraftRiceItems.YEAST_SEISHU.get());
            for (var container : GrowthcraftRiceFluids.ALL) {
                event.accept(container.bucket.get());
            }
        }
    }
}
