package growthcraft.apples.world;

import growthcraft.apples.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.Optional;

public final class AppleTreeGrowers {
    public static final ResourceKey<ConfiguredFeature<?, ?>> APPLE_TREE = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Worldgen.APPLE_TREE)
    );
    public static final TreeGrower APPLE = new TreeGrower("growthcraft_apples_apple", Optional.empty(), Optional.of(APPLE_TREE), Optional.empty());

    private AppleTreeGrowers() {
    }
}
