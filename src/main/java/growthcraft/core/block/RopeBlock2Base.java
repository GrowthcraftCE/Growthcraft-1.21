package growthcraft.core.block;

import growthcraft.cellar.block.GrapeVineStemBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.neoforged.neoforge.common.Tags;

public abstract class RopeBlock2Base extends Block
{
    protected static final IntegerProperty NORTH = IntegerProperty.create("north_ex", 0, 2); // 0: no conn, 1: conn to block edge (to other rope), 2: spec conn (to fences)
    protected static final IntegerProperty EAST  = IntegerProperty.create("east_ex",  0, 2);
    protected static final IntegerProperty SOUTH = IntegerProperty.create("south_ex", 0, 2);
    protected static final IntegerProperty WEST  = IntegerProperty.create("west_ex",  0, 2); // 0: no conn, 1: conn to block edge (to other rope), 2: spec conn (to fences)
    protected static final IntegerProperty UP = IntegerProperty.create("up_ex", 0, 2);
    protected static final IntegerProperty DOWN = IntegerProperty.create("down_ex", 0, 2);

    public RopeBlock2Base(Properties properties)
    {
        super(properties);
    }

    /////////////////////////////////////////////////////////////

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnectedState(context.getLevel(), context.getClickedPos());
    }

    public BlockState getStateForPlacement(LevelAccessor level, BlockPos pos) {
        return getConnectedState(level, pos);
    }

    protected BlockState getConnectedState(LevelAccessor level, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(NORTH, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.NORTH)), false))
                .setValue(SOUTH, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.SOUTH)), false))
                .setValue(WEST, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.WEST)), false))
                .setValue(EAST, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.EAST)), false))
                .setValue(UP, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.UP)), true))
                .setValue(DOWN, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.DOWN)), true));
    }

    protected int getConnectionFromState(BlockState state, boolean vertical) {
        if (state.getBlock() instanceof RopeBlock2Base) {
            return 1; // rope or leaves
        }
        if (state.getBlock() instanceof GrapeVineStemBlock && vertical) {
            return 1; // rope or leaves
        }
        if (state.is(BlockTags.FENCES)) {
            return 2; // rope and fence wrapping
        }
        return 0;
    }

    public static IntegerProperty getPropertyFromDirection(Direction facing) {
        return switch (facing)
        {
            case Direction.NORTH -> NORTH;
            case Direction.SOUTH -> SOUTH;
            case Direction.WEST -> WEST;
            case Direction.EAST -> EAST;
            case Direction.UP -> UP;
            case Direction.DOWN -> DOWN;
        };
    }

    private BlockState getUpdatedStateOnDemand(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        boolean vertical = facing.equals(Direction.DOWN) || facing.equals(Direction.UP);
        int newConnectionValue = this.getConnectionFromState(level.getBlockState(currentPos.relative(facing)), vertical);
        IntegerProperty property = getPropertyFromDirection(facing);
        return state.setValue(property, newConnectionValue);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return this.getUpdatedStateOnDemand(state, facing, facingState, level, currentPos, facingPos);
    }

    public static boolean shouldRestoreRopeOnRemove(BlockState state, Level level, BlockPos pos, BlockState newState) {
        return !newState.is(state.getBlock())
                && !(newState.getBlock() instanceof RopeBlock2Base)
                && !(level.getBlockState(pos.below()).is(Tags.Blocks.VILLAGER_FARMLANDS));
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 75;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 10;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return true;
    }
}
