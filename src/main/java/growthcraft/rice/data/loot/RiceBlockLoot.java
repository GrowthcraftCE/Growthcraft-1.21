package growthcraft.rice.data.loot;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;

import java.util.Set;
import java.util.stream.Collectors;

public class RiceBlockLoot extends BlockLootSubProvider {
    public RiceBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        dropOther(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get(), net.minecraft.world.level.block.Blocks.DIRT);
        add(GrowthcraftRiceBlocks.RICE_CROP.get(), createCropDrops(
                GrowthcraftRiceBlocks.RICE_CROP.get(),
                GrowthcraftRiceItems.RICE_STALK.get(),
                GrowthcraftRiceItems.RICE_GRAINS.get(),
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(GrowthcraftRiceBlocks.RICE_CROP.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftRiceBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
