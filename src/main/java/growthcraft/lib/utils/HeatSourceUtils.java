package growthcraft.lib.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public final class HeatSourceUtils {
    public static final TagKey<Block> HEAT_SOURCES = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "heat_sources"));

    private HeatSourceUtils() {}

    public static boolean hasHeatSourceBelow(Level level, BlockPos pos) {
        return level != null && isHeatSource(level, pos.below());
    }

    public static boolean isHeatSource(Level level, BlockPos pos) {
        if (level == null) return false;

        BlockState state = level.getBlockState(pos);
        if (state.is(HEAT_SOURCES)
                || state.is(Blocks.MAGMA_BLOCK)
                || state.is(Blocks.FIRE)
                || state.is(Blocks.SOUL_FIRE)) {
            return true;
        }
        if (state.hasProperty(BlockStateProperties.LIT)
                && Boolean.TRUE.equals(state.getValue(BlockStateProperties.LIT))) {
            return true;
        }

        FluidState fluid = level.getFluidState(pos);
        return !fluid.isEmpty() && (fluid.is(FluidTags.LAVA) || fluid.getType() == Fluids.LAVA);
    }
}
