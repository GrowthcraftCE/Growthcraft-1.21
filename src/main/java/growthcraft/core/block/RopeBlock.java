package growthcraft.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

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
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        // Show a knot by default when placed directly
        state = state.setValue(KNOT, Boolean.TRUE);
        // Reset vertical strands by default; they can be toggled by neighbor logic later if desired
        return state.setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE);
    }

    @Override
    public boolean connectsTo(BlockState neighborState, boolean neighborIsFullBlock, Direction side) {
        Block neighbor = neighborState.getBlock();
        // Connect to our own rope blocks and rope fences. Do not connect to vanilla fences/walls automatically.
        if (neighbor instanceof RopeBlock) return true;
        if (neighbor instanceof RopeFenceBlock) return true;
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Maintain FenceBlock logic for horizontal updates and waterlogging
        state = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        // Keep vertical ropes off for now
        if (direction == Direction.UP) {
            return state.setValue(UP, Boolean.FALSE);
        } else if (direction == Direction.DOWN) {
            return state.setValue(DOWN, Boolean.FALSE);
        }
        return state;
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
}
