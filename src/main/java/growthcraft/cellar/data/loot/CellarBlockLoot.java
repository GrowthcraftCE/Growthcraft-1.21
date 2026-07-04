package growthcraft.cellar.data.loot;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
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
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GrowthcraftCellarBlocks.BLOCKS.getEntries().stream()
                .map(entry -> entry.get())
                .collect(Collectors.toList());
    }
}
