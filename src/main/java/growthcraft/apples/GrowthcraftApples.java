package growthcraft.apples;

import com.mojang.logging.LogUtils;
import growthcraft.apples.config.GrowthcraftApplesConfig;
import growthcraft.apples.config.Reference;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.apples.init.GrowthcraftApplesFluids;
import growthcraft.apples.init.GrowthcraftApplesItems;
import growthcraft.core.init.GrowthcraftCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.slf4j.Logger;

@Mod(GrowthcraftApples.MODID)
public class GrowthcraftApples {
    public static final String MODID = Reference.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public GrowthcraftApples(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        GrowthcraftApplesBlocks.BLOCKS.register(modEventBus);
        GrowthcraftApplesItems.ITEMS.register(modEventBus);
        GrowthcraftApplesFluids.FLUID_TYPES.register(modEventBus);
        GrowthcraftApplesFluids.FLUIDS.register(modEventBus);
        GrowthcraftApplesFluids.BLOCKS.register(modEventBus);
        modEventBus.addListener(this::buildCreativeTab);
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftApplesConfig.SPEC);

        LOGGER.info("{} module initialized", Reference.NAME);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(GrowthcraftApplesItems::registerCompostables);
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
            event.accept(GrowthcraftApplesItems.APPLE_SEEDS.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_FRUIT.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_LEAVES.get());
            event.accept(GrowthcraftApplesItems.APPLE_TREE_SAPLING.get());
            for (var container : GrowthcraftApplesFluids.ALL) {
                event.accept(container.bucket.get());
            }
        }
    }

    @SubscribeEvent
    public void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.AXE_STRIP) {
            return;
        }

        BlockState state = event.getState();
        BlockState strippedState = null;
        if (state.is(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get())) {
            strippedState = GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get().defaultBlockState();
        } else if (state.is(GrowthcraftApplesBlocks.APPLE_WOOD.get())) {
            strippedState = GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get().defaultBlockState();
        }

        if (strippedState != null) {
            event.setFinalState(strippedState.setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
        }
    }
}
