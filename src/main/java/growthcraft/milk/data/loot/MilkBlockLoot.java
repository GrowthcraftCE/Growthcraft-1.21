package growthcraft.milk.data.loot;

import growthcraft.milk.block.CheeseCurdBlock;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.stream.Collectors;

public class MilkBlockLoot extends BlockLootSubProvider {
    public MilkBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropMachineBlocks();
        dropCheeseWheels();
        dropCheeseCurds();
        dropThistleCrop();
        dropShopSigns();
    }

    private void dropMachineBlocks() {
        dropSelf(GrowthcraftMilkBlocks.CHEESE_PRESS.get());
        dropSelf(GrowthcraftMilkBlocks.CHURN.get());
        dropSelf(GrowthcraftMilkBlocks.MIXING_VAT.get());
        dropSelf(GrowthcraftMilkBlocks.PANCHEON.get());
    }

    private void dropCheeseWheels() {
        dropSelf(GrowthcraftMilkBlocks.APPENZELLER_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.ASIAGO_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.CHEDDAR_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.EMMENTALER_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.GOUDA_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.MONTEREY_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.PARMESAN_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.PROVOLONE_CHEESE.get());
        dropSelf(GrowthcraftMilkBlocks.APPENZELLER_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.ASIAGO_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.CHEDDAR_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.EMMENTALER_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.GOUDA_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.MONTEREY_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.PARMESAN_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.PROVOLONE_CHEESE_AGED.get());
        dropSelf(GrowthcraftMilkBlocks.CHEDDAR_CHEESE_WAXED.get());
        dropSelf(GrowthcraftMilkBlocks.GOUDA_CHEESE_WAXED.get());
        dropSelf(GrowthcraftMilkBlocks.MONTEREY_CHEESE_WAXED.get());
        dropSelf(GrowthcraftMilkBlocks.PROVOLONE_CHEESE_WAXED.get());
    }

    private void dropCheeseCurds() {
        dropCheeseCurds(GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS.get(), GrowthcraftMilkItems.APPENZELLER_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get(), GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS.get(), GrowthcraftMilkItems.CASU_MARZU_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS.get(), GrowthcraftMilkItems.CHEDDAR_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS.get(), GrowthcraftMilkItems.EMMENTALER_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS.get(), GrowthcraftMilkItems.GORGONZOLA_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS.get(), GrowthcraftMilkItems.GOUDA_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS.get(), GrowthcraftMilkItems.MONTEREY_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS.get(), GrowthcraftMilkItems.PARMESAN_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS.get(), GrowthcraftMilkItems.PROVOLONE_CHEESE_CURDS_DRAINED.get());
        dropCheeseCurds(GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS.get(), GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED.get());
    }

    private void dropCheeseCurds(Block block, Item drainedCurds) {
        add(block, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(drainedCurds)
                                .when(isCheeseCurdsDrained(block))
                                .otherwise(LootItem.lootTableItem(block)))));
    }

    private void dropThistleCrop() {
        Block crop = GrowthcraftMilkBlocks.THISTLE_CROP.get();
        Item thistle = GrowthcraftMilkItems.THISTLE.get();
        Item seed = GrowthcraftMilkItems.THISTLE_SEED.get();
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        add(crop, applyExplosionDecay(crop, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(thistle)
                                .when(isMatureThistle())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .otherwise(LootItem.lootTableItem(seed))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(isMatureThistle())
                        .add(LootItem.lootTableItem(seed)
                                .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3))))));
    }

    private void dropShopSigns() {
        dropOther(GrowthcraftMilkBlocks.HANGING_SIGN_1_OAK.get(), Items.OAK_HANGING_SIGN);
        dropOther(GrowthcraftMilkBlocks.HANGING_SIGN_1_SPRUCE.get(), Items.SPRUCE_HANGING_SIGN);
        dropOther(GrowthcraftMilkBlocks.HANGING_SIGN_2_OAK.get(), Items.OAK_HANGING_SIGN);
        dropOther(GrowthcraftMilkBlocks.HANGING_SIGN_2_SPRUCE.get(), Items.SPRUCE_HANGING_SIGN);
    }

    private LootItemBlockStatePropertyCondition.Builder isCheeseCurdsDrained(Block block) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CheeseCurdBlock.AGE, CheeseCurdBlock.MAX_AGE));
    }

    private LootItemBlockStatePropertyCondition.Builder isMatureThistle() {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(GrowthcraftMilkBlocks.THISTLE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftMilkBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
