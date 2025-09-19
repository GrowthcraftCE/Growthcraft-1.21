package growthcraft.core.data.loot;

import growthcraft.core.init.GrowthcraftBlocks;
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
        // Generate simple self-drop loot tables for all registered Growthcraft blocks
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
