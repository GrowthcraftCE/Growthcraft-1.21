package growthcraft.core.init;

import growthcraft.core.config.Reference;
import growthcraft.core.worldgen.SaltOreFeature;
import growthcraft.core.worldgen.SaltOrePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public final class GrowthcraftWorldgen {
    private GrowthcraftWorldgen() {}
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Reference.MODID);
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENTS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Reference.MODID);
    public static final Supplier<SaltOreFeature> SALT_ORE = FEATURES.register("salt_ore", SaltOreFeature::new);
    public static final Supplier<PlacementModifierType<SaltOrePlacement>> SALT_PLACEMENT =
            PLACEMENTS.register("salt_ore", () -> () -> SaltOrePlacement.CODEC);
}
