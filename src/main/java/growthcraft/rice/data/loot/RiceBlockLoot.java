package growthcraft.rice.data.loot;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.stream.Collectors;

public class RiceBlockLoot extends BlockLootSubProvider {
    public RiceBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropOther(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get(), net.minecraft.world.level.block.Blocks.DIRT);
        add(GrowthcraftRiceBlocks.RICE_CROP.get(), createRiceCropDrops());
    }

    private LootTable.Builder createRiceCropDrops() {
        Block crop = GrowthcraftRiceBlocks.RICE_CROP.get();
        Item rice = GrowthcraftRiceItems.RICE.get();
        Item stalk = GrowthcraftRiceItems.RICE_STALK.get();
        Item seed = GrowthcraftRiceItems.RICE_GRAINS.get();
        Item seishuYeast = GrowthcraftRiceItems.YEAST_SEISHU.get();
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        return applyExplosionDecay(crop, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(rice)
                                .when(isMatureRice())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .otherwise(LootItem.lootTableItem(seed))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(isMatureRice())
                        .add(LootItem.lootTableItem(stalk)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(isMatureRice())
                        .add(LootItem.lootTableItem(seed)
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(isMatureRice())
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(seishuYeast))));
    }

    private LootItemBlockStatePropertyCondition.Builder isMatureRice() {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(GrowthcraftRiceBlocks.RICE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftRiceBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
