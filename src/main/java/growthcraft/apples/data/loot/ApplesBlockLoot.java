package growthcraft.apples.data.loot;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class ApplesBlockLoot extends BlockLootSubProvider {
    public ApplesBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON.get());
        add(GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get(), createDoorTable(GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get()));
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE.get());
        add(GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get(), createSlabItemTable(GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get()));
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get());
        dropSelf(GrowthcraftApplesBlocks.BEE_BOX_APPLE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftApplesBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
