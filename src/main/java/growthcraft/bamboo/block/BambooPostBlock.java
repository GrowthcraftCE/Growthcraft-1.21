package growthcraft.bamboo.block;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooPostBlock extends RotatedPillarBlock {
    private static final VoxelShape VISUAL_SHAPE_Y = Shapes.box(3 / 16.0D, 0.0D, 3 / 16.0D, 13 / 16.0D, 1.0D, 13 / 16.0D);
    private static final VoxelShape VISUAL_SHAPE_Z = Shapes.box(3 / 16.0D, 3 / 16.0D, 0.0D, 13 / 16.0D, 13 / 16.0D, 1.0D);
    private static final VoxelShape VISUAL_SHAPE_X = Shapes.box(0.0D, 3 / 16.0D, 3 / 16.0D, 1.0D, 13 / 16.0D, 13 / 16.0D);
    private static final VoxelShape COLLISION_SHAPE_Y = Shapes.box(5 / 16.0D, 0.0D, 5 / 16.0D, 11 / 16.0D, 1.0D, 11 / 16.0D);
    private static final VoxelShape COLLISION_SHAPE_Z = Shapes.box(5 / 16.0D, 5 / 16.0D, 0.0D, 11 / 16.0D, 11 / 16.0D, 1.0D);
    private static final VoxelShape COLLISION_SHAPE_X = Shapes.box(0.0D, 5 / 16.0D, 5 / 16.0D, 1.0D, 11 / 16.0D, 11 / 16.0D);

    public BambooPostBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.SAND)
                .instrument(NoteBlockInstrument.CHIME)
                .strength(1.3F)
                .sound(SoundType.BAMBOO_WOOD)
                .ignitedByLava());
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> VISUAL_SHAPE_X;
            case Y -> VISUAL_SHAPE_Y;
            case Z -> VISUAL_SHAPE_Z;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> COLLISION_SHAPE_X;
            case Y -> COLLISION_SHAPE_Y;
            case Z -> COLLISION_SHAPE_Z;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        Direction.Axis axis = context.getClickedFace().getAxis();
        Block block = axis == Direction.Axis.Y
                ? GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get()
                : GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get();
        return block.defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(BlockStateProperties.WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get().asItem().getDefaultInstance();
    }
}
