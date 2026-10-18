package growthcraft.core.worldgen;

import com.mojang.serialization.MapCodec;
import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.init.GrowthcraftGameRules;
import growthcraft.core.init.GrowthcraftWorldgen;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** Reads config during chunk generation, rather than baking it into datagen output. */
public final class SaltOrePlacement extends PlacementModifier {
    public static final SaltOrePlacement INSTANCE = new SaltOrePlacement();
    public static final MapCodec<SaltOrePlacement> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos origin) {
        var dimension = context.getLevel().getLevel().dimension();
        if (!GrowthcraftConfig.saltOreGenEnabled()
                || !context.getLevel().getLevel().getGameRules().getBoolean(GrowthcraftGameRules.SALT_GENERATION)
                || (dimension.equals(Level.NETHER) && !GrowthcraftConfig.saltOreGenNetherEnabled())
                || (dimension.equals(Level.END) && !GrowthcraftConfig.saltOreGenEndEnabled())) {
            return Stream.empty();
        }
        int min = Math.max(context.getMinBuildHeight(), GrowthcraftConfig.saltOreHeightMin());
        int max = Math.min(context.getMinBuildHeight() + context.getGenDepth() - 1, GrowthcraftConfig.saltOreHeightMax());
        if (min > max) return Stream.empty();
        return IntStream.range(0, GrowthcraftConfig.saltOreSpreadAmount()).mapToObj(i ->
                new BlockPos(origin.getX() + random.nextInt(16), min + random.nextInt(max - min + 1),
                        origin.getZ() + random.nextInt(16)));
    }

    @Override
    public PlacementModifierType<?> type() {
        return GrowthcraftWorldgen.SALT_PLACEMENT.get();
    }
}
