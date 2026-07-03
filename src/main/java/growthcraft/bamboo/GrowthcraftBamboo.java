package growthcraft.bamboo;

import com.mojang.logging.LogUtils;
import growthcraft.bamboo.config.Reference;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.init.GrowthcraftBambooItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftBamboo.MODID)
public class GrowthcraftBamboo {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftBamboo(IEventBus modEventBus) {
        GrowthcraftBambooBlocks.BLOCKS.register(modEventBus);
        GrowthcraftBambooItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            event.accept(GrowthcraftBambooItems.BAMBOO_POST_VERTICAL.get());
        }
    }
}
