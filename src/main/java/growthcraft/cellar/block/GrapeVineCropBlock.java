package growthcraft.cellar.block;

import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.block.RopeBlock;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class GrapeVineCropBlock extends GrowthcraftCropsRopeBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D),
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D),
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D),
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D)
    };
    private final Supplier<? extends GrapeVineLeavesCropBlock> leavesBlock;

    public GrapeVineCropBlock(Supplier<? extends GrapeVineLeavesCropBlock> leavesBlock) {
        super();
        this.leavesBlock = leavesBlock;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public BlockState getActualBlockStateWithAge(BlockGetter level, BlockPos pos, int age) {
        return super.getActualBlockStateWithAge(level, pos, age)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);

        if (!isMaxAge(level.getBlockState(pos))) {
            return;
        }

        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (!(aboveState.getBlock() instanceof RopeBlock)) {
            return;
        }

        BlockState nextState;
        if (canGrowHigher(level, pos) && level.getBlockState(pos.above(2)).getBlock() instanceof RopeBlock) {
            nextState = getActualBlockStateWithAge(level, above, 0);
        } else {
            nextState = leavesBlock.get().getActualBlockStateWithAge(level, above, 0);
        }

        setCropBlock(level, above, nextState);
    }

    private boolean canGrowHigher(BlockGetter level, BlockPos pos) {
        int height = 1;
        BlockPos cursor = pos.below();
        while (level.getBlockState(cursor).is(this)) {
            height++;
            cursor = cursor.below();
        }
        return height < GrowthcraftCellarConfig.getGrapeVineMaxHeight();
    }
}
