package growthcraft.cellar.world;

import growthcraft.cellar.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class CorkTreeGrowers {
    public static final ResourceKey<ConfiguredFeature<?, ?>> CORK_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Block.CORK_TREE)
    );

    public static final TreeGrower CORK = new TreeGrower("growthcraft_cellar_cork", Optional.empty(), Optional.of(CORK_TREE), Optional.empty());

    private CorkTreeGrowers() {
    }
}
