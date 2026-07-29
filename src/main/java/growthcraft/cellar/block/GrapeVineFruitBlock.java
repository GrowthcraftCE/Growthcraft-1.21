package growthcraft.cellar.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.function.Supplier;

public class GrapeVineFruitBlock  extends BushBlock implements BonemealableBlock {
    private static final VoxelShape STEM_S = Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape STEM_M = Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape STEM_L = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    private final Supplier<? extends Item> fruitItem;

    public GrapeVineFruitBlock(Supplier<? extends Item> fruitItem) {
        super(BlockBehaviour.Properties.of()
                .randomTicks()
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
        this.fruitItem = fruitItem;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) <= MAX_AGE / 2 ? STEM_S :
                state.getValue(AGE) < MAX_AGE  ? STEM_M :STEM_L;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof GrapeVineLeavesBlock;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(fruitItem.get());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) == MAX_AGE || !level.isAreaLoaded(pos, 1)) {
            return;
        }

        if (CommonHooks.canCropGrow(level, pos, state, random.nextInt(4) == 0)) {
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_ALL);
            CommonHooks.fireCropGrowPost(level, pos, state);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) != MAX_AGE) {
            return InteractionResult.PASS;
        }

        popResource(level, pos, new ItemStack(fruitItem.get(), Mth.nextInt(level.random, 1, 3)));
        level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_ALL);
        return InteractionResult.sidedSuccess(level.isClientSide);
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
        int age = Math.min(state.getValue(AGE) + Mth.nextInt(random, 0, 2) / 2 + 1, MAX_AGE);
        level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_ALL);
    }
}
