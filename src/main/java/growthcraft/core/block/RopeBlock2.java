package growthcraft.core.block;

import growthcraft.core.init.GrowthcraftItems;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class RopeBlock2 extends Block implements SimpleWaterloggedBlock
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final IntegerProperty NORTH = IntegerProperty.create("north_ex", 0, 2); // 0: no conn, 1: conn to block edge (to other rope), 2: spec conn (to fences)
    private static final IntegerProperty EAST  = IntegerProperty.create("east_ex",  0, 2);
    private static final IntegerProperty SOUTH = IntegerProperty.create("south_ex", 0, 2);
    private static final IntegerProperty WEST  = IntegerProperty.create("west_ex",  0, 2); // 0: no conn, 1: conn to block edge (to other rope), 2: spec conn (to fences)
    private static final IntegerProperty UP = IntegerProperty.create("up_ex", 0, 2);
    private static final IntegerProperty DOWN = IntegerProperty.create("down_ex", 0, 2);
    private static final BooleanProperty KNOT = BooleanProperty.create("knot");
    private final List<VoxelShape> shapeByIndex = new ArrayList<>();
    private final List<VoxelShape> collisionShapeByIndex = new ArrayList<>();

    public RopeBlock2()
    {
        super(BlockBehaviour.Properties.of().strength(0.2F).pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(NORTH, 0)
                .setValue(EAST, 0)
                .setValue(SOUTH, 0)
                .setValue(WEST, 0)
                .setValue(UP, 0)
                .setValue(DOWN, 0)
                .setValue(KNOT, true)
                .setValue(WATERLOGGED, false)
        );
        this.makeShapes();
        // todo sound on place
    }  // todo: flammability

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, KNOT, WATERLOGGED);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !(Boolean)state.getValue(WATERLOGGED);
    }

    private void makeShapes()
    {
        this.shapeByIndex.add(Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D));  // knot fot testing
        this.collisionShapeByIndex.add(Shapes.empty());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shapeByIndex.get(this.getIndexFromState(state));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.collisionShapeByIndex.get(this.getIndexFromState(state));
    }

    private int getIndexFromState(BlockState state)
    {
        return 0;
    }

    //----------------------------------------------//

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getConnectedState(context.getLevel(), context.getClickedPos(), true);
    }

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(GrowthcraftItems.ROPE_LINEN.get());
    }

    private BlockState getConnectedState(LevelAccessor level, BlockPos pos, boolean knot) {
        return this.defaultBlockState()
                .setValue(KNOT, knot)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)
                .setValue(NORTH, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.NORTH))))
                .setValue(SOUTH, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.SOUTH))))
                .setValue(WEST, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.WEST))))
                .setValue(EAST, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.EAST))))
                .setValue(UP, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.UP))))
                .setValue(NORTH, this.getConnectionFromState(level.getBlockState(pos.relative(Direction.UP))));
    }

    public int getConnectionFromState(BlockState state) {
        if (state.getBlock() instanceof GrowthcraftCropsRopeBlock crop || state.is(this)) {
            return 1; // rope
        }
        if (state.is(BlockTags.FENCES)) {
            return 2; // rope and fence wrapping
        }
        return 0;
    }

    private BlockState getUpdatedStateOnDemand(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
    {
        int newConnectionValue = this.getConnectionFromState(level.getBlockState(currentPos.relative(facing)));
        IntegerProperty property = switch (facing)
        {
            case Direction.NORTH -> NORTH;
            case Direction.SOUTH -> SOUTH;
            case Direction.WEST -> WEST;
            case Direction.EAST -> EAST;
            case Direction.UP -> UP;
            case Direction.DOWN -> DOWN;
        };
        BlockState res = state.setValue(property, newConnectionValue);
        return res;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return this.getUpdatedStateOnDemand(state, facing, facingState, level, currentPos, facingPos);
    }
}
