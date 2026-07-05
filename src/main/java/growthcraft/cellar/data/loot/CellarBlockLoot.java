package growthcraft.cellar.data.loot;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class CellarBlockLoot extends BlockLootSubProvider {
    public CellarBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropSelf(GrowthcraftCellarBlocks.BREW_KETTLE.get());
        dropSelf(GrowthcraftCellarBlocks.CULTURE_JAR.get());
        dropSelf(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get());
        dropSelf(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
        add(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get(), noDrop());
        dropSelf(GrowthcraftCellarBlocks.ROASTER.get());
        add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.RED_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE.get(), noDrop());
        add(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get(), noDrop());
        add(GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get(), noDrop());
        add(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get(), noDrop());
        dropOther(GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_PURPLE.get());
        dropOther(GrowthcraftCellarBlocks.RED_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_RED.get());
        dropOther(GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_FRUIT.get(), GrowthcraftCellarItems.GRAPE_WHITE.get());
        dropOther(GrowthcraftCellarBlocks.HOPS_VINE.get(), GrowthcraftCellarItems.HOPS.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_COASTER.get());
        add(GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(), createLeavesDrops(
                GrowthcraftCellarBlocks.CORK_TREE_LEAVES.get(),
                GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get(),
                NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(GrowthcraftCellarBlocks.CORK_TREE_SAPLING.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get());
        dropSelf(GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftCellarBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
