package growthcraft.core.block;

import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class RopeBlock2 extends RopeBlock2Base implements SimpleWaterloggedBlock
{
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final BooleanProperty KNOT = BooleanProperty.create("knot");
    private final ArrayList<VoxelShape> shapeByIndex = new ArrayList<>();
    private final List<VoxelShape> collisionShapeByIndex = new ArrayList<>();

    public RopeBlock2()
    {
        super(BlockBehaviour.Properties.of().strength(0.2F).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).noOcclusion());
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
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(KNOT, WATERLOGGED);  // in addition to NORTH, EAST, WEST, SOUTH, UP, DOWN
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !(Boolean)state.getValue(WATERLOGGED);
    }

    private void makeShapes()
    {
        for (int knot = 0; knot <= 1; knot++)
        {
            for (int up = 0; up <= 2; up++)
            {
                for (int down = 0; down <= 2; down++)
                {
                    for (int west = 0; west <= 2; west++)
                    {
                        for (int south = 0; south <= 2; south++)
                        {
                            for (int east = 0; east <= 2; east++)
                            {
                                for (int north = 0; north <= 2; north++)  // we could have assumed 4 variants per connection. that would allow simple bitwise math instead of these loops at the cost of 4x more memory.
                                {
                                    VoxelShape mainShape = knot == 1 ? SHAPE_CENTER : SHAPE_CENTER_NO_KNOT;
                                    VoxelShape collShape = Shapes.empty();
                                    if (north == 1 || north == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_Z_NEGA);
                                        collShape = Shapes.or(collShape, SHAPE_HORI_Z_NEGA_COLL);
                                    }
                                    if (south == 1 || south == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_Z_POSI);
                                        collShape = Shapes.or(collShape, SHAPE_HORI_Z_POSI_COLL);
                                    }
                                    if (east == 1 || east == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_X_POSI);
                                        collShape = Shapes.or(collShape, SHAPE_HORI_X_POSI_COLL);
                                    }
                                    if (west == 1 || west == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_X_NEGA);
                                        collShape = Shapes.or(collShape, SHAPE_HORI_X_NEGA_COLL);
                                    }
                                    if (south == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_Z_POSI_EX1);
                                        // mainShape = Shapes.or(mainShape, SHAPE_HORI_Z_POSI_EX2); wrapping - we're giving up on that...
                                    }
                                    if (north == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_Z_NEGA_EX1); // ...actually even these EX1 parts are pointless, nut i won't delete them now/
                                    }
                                    if (east == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_X_POSI_EX1);
                                    }
                                    if (west == 2) {
                                        mainShape = Shapes.or(mainShape, SHAPE_HORI_X_NEGA_EX1);
                                    }
                                    if (up == 1) {
                                        mainShape = Shapes.or(mainShape, SHAPE_VERT_POSI);
                                        // no collision
                                    }
                                    if (down == 1) {
                                        mainShape = Shapes.or(mainShape, SHAPE_VERT_NEGA);
                                        // no collision
                                    }
                                    this.shapeByIndex.add(mainShape);
                                    this.collisionShapeByIndex.add(collShape);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    VoxelShape SHAPE_CENTER = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);
    VoxelShape SHAPE_CENTER_NO_KNOT = Block.box(7.5D, 7.5D, 7.5D, 8.5D, 8.5D, 8.5D);
    VoxelShape SHAPE_HORI_Z_POSI = Block.box(7.5D, 7.5D, 9.0D, 8.5D, 8.5D, 16.0D);
    VoxelShape SHAPE_VERT_POSI = Block.box(7.5D, 9.0D, 7.5D, 8.5D, 16.0D, 8.5D);
    VoxelShape SHAPE_HORI_X_POSI = Block.box(9.0D, 7.5D, 7.5D, 16.0D, 8.5D, 8.5D);
    VoxelShape SHAPE_VERT_NEGA = SHAPE_VERT_POSI.move(0, -9/16d, 0);
    VoxelShape SHAPE_HORI_Z_NEGA = SHAPE_HORI_Z_POSI.move(0, 0, -9/16d);
    VoxelShape SHAPE_HORI_X_NEGA = SHAPE_HORI_X_POSI.move(-9/16d, 0, 0);

    VoxelShape SHAPE_HORI_Z_POSI_EX1 = Block.box(7.5D, 7.5D, 16.0D, 8.5D, 8.5D, 21.0D);
    VoxelShape SHAPE_HORI_X_POSI_EX1 = Block.box(16.0D, 7.5D, 7.5D, 21.0D, 8.5D, 8.5D);
    VoxelShape SHAPE_HORI_Z_POSI_EX2 = Block.box(5.0D, 4.0D, 21.0D, 11.0D, 12.0D, 27.0D);
    VoxelShape SHAPE_HORI_Z_NEGA_EX1 = SHAPE_HORI_Z_POSI_EX1.move(0, 0, -21/16d);
    VoxelShape SHAPE_HORI_X_NEGA_EX1 = SHAPE_HORI_X_POSI_EX1.move(-21/16d, 0, 0);

    VoxelShape SHAPE_HORI_Z_POSI_COLL = Block.box(7.5D, 8.0D, 9.0D, 8.5D, 9.0D, 16.0D);
    VoxelShape SHAPE_HORI_X_POSI_COLL = Block.box(9.0D, 8.0D, 7.5D, 16.0D, 9.0D, 8.5D);
    VoxelShape SHAPE_HORI_Z_NEGA_COLL = SHAPE_HORI_Z_POSI_COLL.move(0, 0, -9/16d);
    VoxelShape SHAPE_HORI_X_NEGA_COLL = SHAPE_HORI_X_POSI_COLL.move(-9/16d, 0, 0);

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
        int result = state.getValue(KNOT) ? 1 : 0;
        result = result * 3 + state.getValue(UP);
        result = result * 3 + state.getValue(DOWN);
        result = result * 3 + state.getValue(WEST);
        result = result * 3 + state.getValue(SOUTH);
        result = result * 3 + state.getValue(EAST);
        result = result * 3 + state.getValue(NORTH);
        return result;
    }

    //----------------------------------------------//

    @Override
    protected FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(GrowthcraftItems.ROPE_LINEN2.get());
    }

    @Override
    protected BlockState getConnectedState(LevelAccessor level, BlockPos pos) {
        return super.getConnectedState(level, pos)
                .setValue(KNOT, true)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    /////////////////

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 15;  // a little lower than expected in order to give it chance to set other ropes on fire.
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 50;  // quite high. wool is 30.
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return true;
    }
}
