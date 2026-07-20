package growthcraft.core.data.loot;

import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.stream.Collectors;

public class GrowthcraftBlockLoot extends BlockLootSubProvider {
    public GrowthcraftBlockLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected void generate() {
        // Generate loot tables for Growthcraft blocks
        // Rope block should drop the rope item (since there is no BlockItem for the rope block)
        this.add(GrowthcraftBlocks.ROPE_LINEN2.get(), createSingleItemTable(GrowthcraftItems.ROPE_LINEN2.get()));

        // Salt blocks and ores (retain existing behavior; custom JSONs may override at runtime)
        this.dropSelf(GrowthcraftBlocks.SALT_BLOCK.get());
        this.dropSelf(GrowthcraftBlocks.SALT_ORE.get());
        this.dropSelf(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get());
        this.dropSelf(GrowthcraftBlocks.SALT_ORE_NETHER.get());
        this.dropSelf(GrowthcraftBlocks.SALT_ORE_END.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
