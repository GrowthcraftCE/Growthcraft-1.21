package growthcraft.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * A fence-like block that represents a vanilla fence with a rope overlay.
 *
 * It mirrors the connection logic of FenceBlock for the four horizontal directions
 * and adds extra boolean properties that are only used by our blockstate JSON to
 * render rope parts (UP/DOWN and KNOT). Geometry and collision are inherited from FenceBlock.
 */
public class RopeFenceBlock extends FenceBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty KNOT = BooleanProperty.create("knot");

    public RopeFenceBlock(Properties properties) {
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
        // Default to having a knot when placed directly, can be adjusted by item logic
        state = state.setValue(KNOT, Boolean.TRUE);
        return state.setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Keep FenceBlock connection updates for horizontal directions and waterlogging
        state = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        // Optionally compute UP/DOWN rope connections: we keep it simple for now, off by default.
        if (direction == Direction.UP) {
            return state.setValue(UP, Boolean.FALSE);
        } else if (direction == Direction.DOWN) {
            return state.setValue(DOWN, Boolean.FALSE);
        }
        return state;
    }

}
