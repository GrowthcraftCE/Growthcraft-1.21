package growthcraft.core.worldgen;

import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.init.GrowthcraftGameRules;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public final class SaltOreFeature extends OreFeature {
    public SaltOreFeature() { super(OreConfiguration.CODEC); }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
        var dimension = context.level().getLevel().dimension();
        if (!GrowthcraftConfig.saltOreGenEnabled()
                || !context.level().getLevel().getGameRules().getBoolean(GrowthcraftGameRules.SALT_GENERATION)
                || (dimension.equals(Level.NETHER) && !GrowthcraftConfig.saltOreGenNetherEnabled())
                || (dimension.equals(Level.END) && !GrowthcraftConfig.saltOreGenEndEnabled())) return false;
        var config = runtimeConfiguration(context.config());
        return super.place(new FeaturePlaceContext<>(context.topFeature(), context.level(),
                context.chunkGenerator(), context.random(), context.origin(), config));
    }
    static OreConfiguration runtimeConfiguration(OreConfiguration original) {
        var targets = original.targetStates.stream()
                .filter(target -> GrowthcraftConfig.saltOreGenDeepslateEnabled()
                        || !target.state.is(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get()))
                .toList();
        return new OreConfiguration(targets, GrowthcraftConfig.saltOreVeinSize(),
                original.discardChanceOnAirExposure);
    }

}
