package growthcraft.cellar.block;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import java.util.function.Supplier;

public class GrapeVineStemBlock extends BushBlock implements BonemealableBlock
{
    private static final VoxelShape STEM_S = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 5.0D, 9.0D);
    private static final VoxelShape STEM_L = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 15.98D, 10.0D);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;
    private final Supplier<? extends GrapeVineLeavesBlock> leavesBlock;
    private final Supplier<? extends Item> seedItem;

    public GrapeVineStemBlock(Supplier<? extends GrapeVineLeavesBlock> leavesBlock, Supplier<? extends Item> seedItem) {
        super(BlockBehaviour.Properties.of()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
        this.leavesBlock = leavesBlock;
        this.seedItem = seedItem;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) <= MAX_AGE / 2 ? STEM_S : STEM_L;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

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
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || random.nextInt(4) != 0) {
            return;
        }
        if (state.getValue(AGE) < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_ALL);
            return;
        }

        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (!(aboveState.getBlock() instanceof RopeBlock2)) {
            return;
        }

        BlockState nextState;
        if (canGrowHigher(level, pos) && level.getBlockState(pos.above(2)).getBlock() instanceof RopeBlock2) {
            nextState = this.defaultBlockState();
        } else {
            nextState = leavesBlock.get().getStateForPlacement(level, above);
        }
        level.setBlock(above, nextState, Block.UPDATE_ALL);
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

    @Override
    protected MapCodec<? extends BushBlock> codec() { return null; }

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
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockPos blockpos = pos.below();
        BlockState belowBlockState = level.getBlockState(blockpos);
        return belowBlockState.is(Tags.Blocks.VILLAGER_FARMLANDS) || belowBlockState.is(this) || belowBlockState.getBlock() instanceof GrapeVineLeavesBlock;
    }

    /////////////////

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 75;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return 20;
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
    {
        return true;
    }

    ////////////////////////

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (RopeBlock2Base.shouldRestoreRopeOnRemove(state, level, pos, newState)) {
            level.setBlock(pos, ((RopeBlock2) GrowthcraftBlocks.ROPE_LINEN2.get()).getStateForPlacement(level, pos), Block.UPDATE_ALL);
        }
    }
}
