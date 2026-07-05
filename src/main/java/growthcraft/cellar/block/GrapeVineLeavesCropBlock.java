package growthcraft.cellar.block;

import growthcraft.core.block.RopeBlock;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GrapeVineLeavesCropBlock extends GrowthcraftCropsRopeBlock {
    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final Supplier<? extends GrapeVineFruitBlock> fruitBlock;

    public GrapeVineLeavesCropBlock(Supplier<? extends GrapeVineFruitBlock> fruitBlock) {
        super();
        this.fruitBlock = fruitBlock;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean connectsAsRope() {
        return false;
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

        List<Direction> directions = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
        for (int index = directions.size() - 1; index > 0; index--) {
            int swapIndex = random.nextInt(index + 1);
            Direction direction = directions.get(index);
            directions.set(index, directions.get(swapIndex));
            directions.set(swapIndex, direction);
        }
        for (Direction direction : directions) {
            BlockPos spreadPos = pos.relative(direction);
            if (level.getBlockState(spreadPos).is(GrowthcraftBlocks.ROPE_LINEN.get())) {
                setCropBlock(level, spreadPos, getActualBlockStateWithAge(level, spreadPos, 0));
                return;
            }
        }

        BlockPos fruitPos = pos.below();
        if (level.getBlockState(fruitPos).isAir()) {
            setCropBlock(level, fruitPos, fruitBlock.get().getActualBlockStateWithAge(level, fruitPos, 0));
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockState adjacent = level.getBlockState(pos.relative(direction));
            if (adjacent.getBlock() instanceof GrapeVineCropBlock || adjacent.getBlock() instanceof GrapeVineLeavesCropBlock || RopeBlock.canConnect(adjacent)) {
                return true;
            }
        }
        return false;
    }
}
