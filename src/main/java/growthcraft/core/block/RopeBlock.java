package growthcraft.core.block;

import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

/**
 * A thin rope block that visually connects like a fence/pane but only
 * connects to other RopeBlock instances and RopeFenceBlock instances.
 *
 * We extend FenceBlock to reuse its connection state logic (N/E/S/W and waterlogging)
 * and override the connection predicate to restrict neighbors we connect to.
 * We also expose UP/DOWN/KNOT properties for rendering, mirroring RopeFenceBlock.
 */
public class RopeBlock extends FenceBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty KNOT = BooleanProperty.create("knot");

    private static final VoxelShape KNOT_SHAPE = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);
    private static final VoxelShape NORTH_SHAPE = Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 7.0D);
    private static final VoxelShape EAST_SHAPE = Block.box(9.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.box(7.0D, 7.0D, 9.0D, 9.0D, 9.0D, 16.0D);
    private static final VoxelShape WEST_SHAPE = Block.box(0.0D, 7.0D, 7.0D, 7.0D, 9.0D, 9.0D);
    private static final VoxelShape UP_SHAPE = Block.box(7.0D, 9.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape DOWN_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 7.0D, 9.0D);

    public RopeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(NORTH, Boolean.FALSE)
            .setValue(EAST, Boolean.FALSE)
            .setValue(SOUTH, Boolean.FALSE)
            .setValue(WEST, Boolean.FALSE)
            .setValue(WATERLOGGED, Boolean.FALSE)
            .setValue(UP, Boolean.FALSE)
            .setValue(DOWN, Boolean.FALSE)
            .setValue(KNOT, Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, DOWN, KNOT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnectedState(context.getLevel(), context.getClickedPos(), true);
    }

    @Override
    public boolean connectsTo(BlockState neighborState, boolean neighborIsFullBlock, Direction side) {
        return canConnect(neighborState);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return withConnections(state, level, pos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // Same as FenceBlock (always can survive); ropes are not gravity-affected here.
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        List<VoxelShape> shapes = new ArrayList<>();
        shapes.add(KNOT_SHAPE);
        if (state.getValue(NORTH)) shapes.add(NORTH_SHAPE);
        if (state.getValue(EAST)) shapes.add(EAST_SHAPE);
        if (state.getValue(SOUTH)) shapes.add(SOUTH_SHAPE);
        if (state.getValue(WEST)) shapes.add(WEST_SHAPE);
        if (state.getValue(UP)) shapes.add(UP_SHAPE);
        if (state.getValue(DOWN)) shapes.add(DOWN_SHAPE);
        return Shapes.or(KNOT_SHAPE, shapes.toArray(VoxelShape[]::new));
    }

    public BlockState getConnectedState(LevelAccessor level, BlockPos pos, boolean knot) {
        BlockState state = this.defaultBlockState()
                .setValue(KNOT, knot)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        return withConnections(state, level, pos);
    }

    public static void refreshConnections(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof RopeBlock) {
            level.setBlock(pos, withConnections(state, level, pos), Block.UPDATE_ALL);
        } else if (state.getBlock() instanceof RopeFenceBlock ropeFenceBlock) {
            level.setBlock(pos, ropeFenceBlock.withRopeConnections(level, pos, state), Block.UPDATE_ALL);
        }
    }

    public static void refreshAdjacentConnections(LevelAccessor level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            refreshConnections(level, pos.relative(direction));
        }
    }

    static BlockState withConnections(BlockState state, LevelAccessor level, BlockPos pos) {
        return state
                .setValue(NORTH, isRopeConnection(level.getBlockState(pos.north())))
                .setValue(EAST, isRopeConnection(level.getBlockState(pos.east())))
                .setValue(SOUTH, isRopeConnection(level.getBlockState(pos.south())))
                .setValue(WEST, isRopeConnection(level.getBlockState(pos.west())))
                .setValue(UP, isRopeConnection(level.getBlockState(pos.above())))
                .setValue(DOWN, isRopeConnection(level.getBlockState(pos.below())));
    }

    public static boolean isRopeConnection(BlockState state) {
        return canConnect(state);
    }

    public static boolean canConnect(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof GrowthcraftCropsRopeBlock crop) {
            return crop.connectsAsRope();
        }
        return block instanceof RopeBlock
                || block instanceof RopeFenceBlock
                || state.is(growthcraft.core.init.GrowthcraftTags.Blocks.ROPE);
    }
}
