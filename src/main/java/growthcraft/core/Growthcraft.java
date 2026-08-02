package growthcraft.core;

import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftConditions;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.core.init.GrowthcraftParticles;
import growthcraft.milk.block.signs.ShopSignTransformHandler;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

//
// AIDER_SANITY_TEST_12345
//

@Mod(Growthcraft.MODID)
public class Growthcraft {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public Growthcraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register Deferred Registers
        GrowthcraftItems.ITEMS.register(modEventBus);
        GrowthcraftBlocks.BLOCKS.register(modEventBus);
        GrowthcraftCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        GrowthcraftConditions.CONDITION_CODECS.register(modEventBus);
        GrowthcraftParticles.PARTICLE_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.addListener(ShopSignTransformHandler::onRightClickBlock);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftConfig.SPEC);
    }
}
