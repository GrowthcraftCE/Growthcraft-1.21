package growthcraft.apples.data.loot;

import growthcraft.apples.block.AppleTreeFruitBlock;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
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
        add(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(applyExplosionCondition(net.minecraft.world.item.Items.APPLE,
                                LootItem.lootTableItem(net.minecraft.world.item.Items.APPLE)
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(AppleTreeFruitBlock.AGE, AppleTreeFruitBlock.MAX_AGE)))))));
        add(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get(), block -> createOakLeavesDrops(block, GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get());
        dropSelf(GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftApplesBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
