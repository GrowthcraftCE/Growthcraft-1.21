package growthcraft.bamboo;

import com.mojang.logging.LogUtils;
import growthcraft.bamboo.config.GrowthcraftBambooConfig;
import growthcraft.bamboo.config.Reference;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.bamboo.init.GrowthcraftBambooItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftBamboo.MODID)
public class GrowthcraftBamboo {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftBamboo(IEventBus modEventBus, ModContainer modContainer) {
        GrowthcraftBambooBlocks.BLOCKS.register(modEventBus);
        GrowthcraftBambooItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);
        modEventBus.addListener(this::addPacks);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftBambooConfig.SPEC);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        CreativeModeTab tab = event.getTab();
        if (tab == GrowthcraftCreativeTabs.MAIN.get()) {
            event.accept(GrowthcraftBambooItems.BAMBOO_POST_VERTICAL.get());
        }
    }

    private void addPacks(AddPackFindersEvent event) {
        event.addPackFinders(
                ResourceLocation.fromNamespaceAndPath(Reference.MODID, "assets/packs/legacy_bamboo"),
                PackType.CLIENT_RESOURCES,
                Component.literal("GrowthCraft Bamboo"),
                PackSource.BUILT_IN, false,
                Pack.Position.TOP);
    }
}
