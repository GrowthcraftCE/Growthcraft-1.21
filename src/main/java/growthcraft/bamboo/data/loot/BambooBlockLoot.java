package growthcraft.bamboo.data.loot;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class BambooBlockLoot extends BlockLootSubProvider {
    public BambooBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
        dropOther(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get(), GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftBambooBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
