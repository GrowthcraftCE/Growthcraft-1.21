package growthcraft.apples.block;

import com.mojang.serialization.MapCodec;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.neoforged.neoforge.event.EventHooks;

public class AppleTreeFruitBlock extends BushBlock implements BonemealableBlock {
    public static final MapCodec<AppleTreeFruitBlock> CODEC = simpleCodec(AppleTreeFruitBlock::new);
    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
            Block.box(6.0, 10.0, 6.0, 10.0, 14.0, 10.0),
            Block.box(6.0, 10.0, 6.0, 10.0, 14.0, 10.0),
            Block.box(6.0, 10.0, 6.0, 10.0, 14.0, 10.0),
            Block.box(5.0, 9.0, 5.0, 11.0, 14.0, 11.0),
            Block.box(5.0, 9.0, 5.0, 11.0, 14.0, 11.0),
            Block.box(5.0, 9.0, 5.0, 11.0, 14.0, 11.0),
            Block.box(5.0, 9.0, 5.0, 11.0, 14.0, 11.0),
            Block.box(4.0, 7.0, 4.0, 12.0, 14.0, 12.0)
    };

    public AppleTreeFruitBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || this.isMaxAge(state)) {
            return;
        }

        if (CommonHooks.canCropGrow(level, pos, state, random.nextInt(6) == 0)) {
            level.setBlock(pos, this.getStateForAge(this.getAge(state) + 1), 2);
            CommonHooks.fireCropGrowPost(level, pos, state);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && EventHooks.canEntityGrief(level, entity)) {
            level.destroyBlock(pos, true, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.APPLE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!this.isMaxAge(state)) {
            return InteractionResult.PASS;
        }

        popResource(level, pos, new ItemStack(Items.APPLE));
        level.setBlock(pos, this.getStateForAge(0), 2);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(this.getAge(state) + Mth.nextInt(random, 2, 5), MAX_AGE);
        level.setBlock(pos, this.getStateForAge(age), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    private int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    private boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= MAX_AGE;
    }

    private BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(AGE, age);
    }

    public static BlockBehaviour.Properties fruitProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)
                .randomTicks()
                .sound(SoundType.CROP)
                .noOcclusion()
                .instabreak();
    }
}
