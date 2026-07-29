package growthcraft.cellar.block.support;

import growthcraft.cellar.block.GrapeVineLeavesBlock;
import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.block.HopsCropBlock;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;

public class VineGrowthHelper
{
    public static class Options  // move this to config
    {
        public static final int GRAPES_MAX_DISTANCE_FROM_STEM_TO_EXPAND = 5;  // so max of that much + 1 leaves blocks away from stem
        public static final int GRAPES_MAX_DISTANCE_FROM_STEM_TO_SURVIVE = 8;
        public static final boolean GRAPES_CAN_EXPAND_VERTICALLY = true;

        public static final int HOPS_MAX_DISTANCE_FROM_GROUND = 6;  // this is max height in simple terms (current block counts)
        public static final boolean HOPS_CAN_EXPAND_HORIZONTALLY = false;
    }

    ////////////////////

    @Nullable
    public static Direction tryGrapeLeavesExpand(Level level, BlockPos pos)
    {
        if (! isConnectedToStem(level, pos, Options.GRAPES_MAX_DISTANCE_FROM_STEM_TO_EXPAND, null)) {
            return null;
        }
        if (Options.GRAPES_CAN_EXPAND_VERTICALLY) {
            if (level.getBlockState(pos.above()).is(GrowthcraftBlocks.ROPE_LINEN2.get())) {
                return Direction.UP;
            }
        }
        Direction direction = Direction.NORTH;
        int rotationCount = level.getRandom().nextInt(4); //to start from random direction
        for (int index = 0; index < rotationCount; index++) {
            direction = direction.getClockWise();
        }
        for (int index = 0; index < 4; index++) { // now the actual thing
            BlockPos spreadPos = pos.relative(direction);
            if (level.getBlockState(spreadPos).is(GrowthcraftBlocks.ROPE_LINEN2.get())) {
                return direction;
            }
        }
        if (Options.GRAPES_CAN_EXPAND_VERTICALLY) {
            if (level.getBlockState(pos.below()).is(GrowthcraftBlocks.ROPE_LINEN2.get())) {
                return Direction.DOWN;
            }
        }
        return null;
    }

    public static boolean canGrapeLeavesSurvive(LevelReader level, BlockPos pos)
    {
        return isConnectedToStem(level, pos, Options.GRAPES_MAX_DISTANCE_FROM_STEM_TO_SURVIVE, null);
    }

    @Nullable
    public static Direction tryHopsExpand(Level level, BlockPos pos)
    {
        if (isConnectedToGround(level, pos, Options.HOPS_MAX_DISTANCE_FROM_GROUND - 1, null)) {
            for (Direction direction : Direction.values()) {
                if (direction.equals(Direction.UP) || Options.HOPS_CAN_EXPAND_HORIZONTALLY) {
                    BlockPos spreadPos = pos.relative(direction);
                    if (level.getBlockState(spreadPos).is(GrowthcraftBlocks.ROPE_LINEN2.get())) {
                        return direction;
                    }
                }
            }
        }
        return null;
    }

    public static boolean canHopsSurvive(LevelReader level, BlockPos pos)
    {
        return isConnectedToGround(level, pos, Options.HOPS_MAX_DISTANCE_FROM_GROUND, null);
    }

    ////////////////////////////////////////////

    private static boolean isConnectedToStem(LevelReader level, BlockPos pos, int maxDist, Direction directionToSkip)
    {
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(pos.relative(direction)).getBlock() instanceof GrapeVineStemBlock) {
                return true;
            }
        }
        if (maxDist < 2) {
            return false;
        }
        for (Direction direction : Direction.values()) {
            if (! direction.equals(directionToSkip)) {
                if (level.getBlockState(pos.relative(direction)).getBlock() instanceof GrapeVineLeavesBlock) {
                    if (isConnectedToStem(level, pos.relative(direction), maxDist - 1, direction.getOpposite())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isConnectedToGround(LevelReader level, BlockPos pos, int maxDist, Direction directionToSkip)
    {
        if (level.getBlockState(pos.below()).is(Tags.Blocks.VILLAGER_FARMLANDS)) {
            return true;
        }
        if (maxDist < 2) {
            return false;
        }
        for (Direction direction : Direction.values()) {
            if (! direction.equals(directionToSkip)) {
                if (level.getBlockState(pos.relative(direction)).getBlock() instanceof HopsCropBlock) {
                    if (isConnectedToGround(level, pos.relative(direction), maxDist - 1, direction.getOpposite())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
