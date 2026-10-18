package growthcraft.cellar.block;

import growthcraft.cellar.block.support.VineGrowthHelper;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.block.RopeBlock2;
import growthcraft.core.block.RopeBlock2Base;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GrapeVineLeavesBlock extends RopeBlock2Base implements BonemealableBlock {
    private static final VoxelShape SHAPE_INT = Block.box(0.0D, 1.0D, 0.0D, 16.0D, 15.9D, 16.0D);
    private static final List<VoxelShape> collisionShapeByIndex = new ArrayList<>(); // can be static, 3 blocks have the same collision
    private static final List<VoxelShape> interactionShapeByIndex = new ArrayList<>(); // for thin variant
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    private final Supplier<? extends GrapeVineFruitBlock> fruitBlock;
    private final Supplier<? extends Item> seedItem;

    public GrapeVineLeavesBlock(Supplier<? extends GrapeVineFruitBlock> fruitBlock, Supplier<? extends Item> seedItem) {
        super(BlockBehaviour.Properties.of()
                .randomTicks()
                .strength(0.1f)
                .noOcclusion().forceSolidOff()
                .sound(SoundType.CROP));
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(NORTH, 0)
                        .setValue(EAST, 0)
                        .setValue(SOUTH, 0)
                        .setValue(WEST, 0)
                        .setValue(UP, 0)
                        .setValue(DOWN, 0)
                        .setValue(AGE, 0)
        );
        this.fruitBlock = fruitBlock;
        this.seedItem = seedItem;
        GrapeVineLeavesBlock.makeShapes();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);  // in addition to NORTH, EAST, WEST, SOUTH, UP, DOWN
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }


    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (! GrowthcraftCellarConfig.shouldUseThinGrapeModels()) {
            return SHAPE_INT;
        }
        else {
            return interactionShapeByIndex.get(getIndexFromState(state, false));
        }
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return collisionShapeByIndex.get(getIndexFromState(state, true));
    }
    private static final VoxelShape SHAPE_HORI_Z_POSI = Block.box(6.0D, 0.05D, 10.0D, 10.0D, 15.95D, 16.0D);
    private static final VoxelShape SHAPE_HORI_X_POSI = Block.box(10.0D, 0.05D, 6.0D, 16.0D, 15.95D, 10.0D);
    private static final VoxelShape SHAPE_HORI_Z_NEGA = SHAPE_HORI_Z_POSI.move(0, 0, -10/16d);
    private static final VoxelShape SHAPE_HORI_X_NEGA = SHAPE_HORI_X_POSI.move(-10/16d, 0, 0);

    private static final VoxelShape SHAPE_THIN_CENTER = Block.box(4.0D, 0.00D, 4.0D, 12.0D, 10.0D, 12.0D);
    private static final VoxelShape SHAPE_HORI_Z_POSI_2 = Block.box(6.0D, 0.05D, 10.0D, 10.0D, 10.00D, 16.0D);
    private static final VoxelShape SHAPE_HORI_X_POSI_2 = Block.box(10.0D, 0.05D, 6.0D, 16.0D, 10.00D, 10.0D);
    private static final VoxelShape SHAPE_HORI_Z_NEGA_2 = SHAPE_HORI_Z_POSI_2.move(0, 0, -10/16d);
    private static final VoxelShape SHAPE_HORI_X_NEGA_2 = SHAPE_HORI_X_POSI_2.move(-10/16d, 0, 0);



    private static int getIndexFromState(BlockState state, boolean forCollision)
    {
        int result = 0;
        if (forCollision) {
            result = result * 2 + (state.getValue(WEST) > 0 ? 1 : 0);
            result = result * 2 + (state.getValue(SOUTH) > 0 ? 1 : 0);
            result = result * 2 + (state.getValue(EAST) > 0 ? 1 : 0);
            result = result * 2 + (state.getValue(NORTH) > 0 ? 1 : 0);
        }
        else {  // for interaction
            result = result * 2 + (state.getValue(WEST) == 1 ? 1 : 0);
            result = result * 2 + (state.getValue(SOUTH) == 1 ? 1 : 0);
            result = result * 2 + (state.getValue(EAST) == 1 ? 1 : 0);
            result = result * 2 + (state.getValue(NORTH) == 1 ? 1 : 0);
        }
        return result;
    }

    private static void makeShapes()
    {
        for (int west = 0; west <= 1; west++)
        {
            for (int south = 0; south <= 1; south++)
            {
                for (int east = 0; east <= 1; east++)
                {
                    for (int north = 0; north <= 1; north++)  // we could have assumed 4 variants per connection. that would allow simple bitwise math instead of these loops at the cost of 4x more memory.
                    {
                        VoxelShape collShape = Shapes.empty();
                        VoxelShape intShape = Shapes.empty();
                        intShape = Shapes.or(intShape, SHAPE_THIN_CENTER);
                        if (north == 1) {
                            collShape = Shapes.or(collShape, SHAPE_HORI_Z_NEGA);
                            intShape = Shapes.or(intShape, SHAPE_HORI_Z_NEGA_2);
                        }
                        if (south == 1) {
                            collShape = Shapes.or(collShape, SHAPE_HORI_Z_POSI);
                            intShape = Shapes.or(intShape, SHAPE_HORI_Z_POSI_2);
                        }
                        if (east == 1) {
                            collShape = Shapes.or(collShape, SHAPE_HORI_X_POSI);
                            intShape = Shapes.or(intShape, SHAPE_HORI_X_POSI_2);
                        }
                        if (west == 1) {
                            collShape = Shapes.or(collShape, SHAPE_HORI_X_NEGA);
                            intShape = Shapes.or(intShape, SHAPE_HORI_X_NEGA_2);
                        }
                        collisionShapeByIndex.add(collShape);
                        interactionShapeByIndex.add(intShape);
                    }
                }
            }
        }
    }

    //----------------------------------------------//

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(seedItem.get());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos.above(), 0) < 9 || random.nextInt(4) != 0) {
            return;
        }
        if (state.getValue(AGE) < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_ALL);
            return;
        }

        BlockPos fruitPos = pos.below();
        if (level.getBlockState(fruitPos).isAir()) {
            level.setBlock(fruitPos, fruitBlock.get().defaultBlockState(), 3);
        }

        Direction directionToExpand = VineGrowthHelper.tryGrapeLeavesExpand(level, pos);
        if (directionToExpand != null) {
            BlockPos posToExpand = pos.relative(directionToExpand);
            level.setBlock(posToExpand, this.getStateForPlacement(level, posToExpand),3);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return VineGrowthHelper.canGrapeLeavesSurvive(level, pos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
    {
        if (! this.canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(state.getValue(AGE) + Mth.nextInt(random, 1, 2), MAX_AGE);
        level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_ALL);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (this.shouldRestoreRopeOnRemove(state, level, pos, newState)) {
            level.setBlock(pos, ((RopeBlock2) GrowthcraftBlocks.ROPE_LINEN2.get()).getStateForPlacement(level, pos), Block.UPDATE_ALL);
        }
    }
}
