package growthcraft.cellar.block;

import growthcraft.cellar.block.support.VineGrowthHelper;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.block.RopeBlock2;
import growthcraft.core.block.RopeBlock2Base;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HopsCropBlock extends RopeBlock2Base implements BonemealableBlock
{
    private static final VoxelShape SEEDLING_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 5.0D, 10.0D);
    private static final VoxelShape POST_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    public HopsCropBlock()
    {
        super(BlockBehaviour.Properties.of()
                .randomTicks()
                .noCollission()
                .instabreak()
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
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);  // in addition to NORTH, EAST, WEST, SOUTH, UP, DOWN
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) < 4 ? SEEDLING_SHAPE : POST_SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || random.nextInt(4) != 0) {
            return;
        }
        if (state.getValue(AGE) < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), Block.UPDATE_ALL);
            return;
        }
        this.tryGrowNewVine(state, level, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE || level.getBlockState(pos.above()).getBlock() instanceof RopeBlock2;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) { return true; }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int previousAge = state.getValue(AGE);
        if (previousAge < MAX_AGE) {
            int age = Math.min(previousAge + Mth.nextInt(random, 1, 2), MAX_AGE);
            level.setBlock(pos, state.setValue(AGE, age), Block.UPDATE_ALL);
        }
        else {
            tryGrowNewVine(state, level, pos);
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(GrowthcraftCellarItems.HOPS_SEEDS.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(AGE) != MAX_AGE) {
            return InteractionResult.PASS;
        }
        if (player.getItemInHand(player.getUsedItemHand()).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        }

        popResource(level, pos, new ItemStack(GrowthcraftCellarItems.HOPS.get()));
        level.setBlock(pos, state.setValue(AGE, MAX_AGE - 1), Block.UPDATE_ALL);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void tryGrowNewVine(BlockState state, ServerLevel level, BlockPos pos) {
        if (state.getValue(AGE) == MAX_AGE) {
            Direction directionToExpand = VineGrowthHelper.tryHopsExpand(level, pos);
            if (directionToExpand != null) {
                BlockPos posToExpand = pos.relative(directionToExpand);
                BlockState newState = this.getStateForPlacement(level, posToExpand);
                if (! directionToExpand.equals(Direction.UP)) {
                    newState = newState.setValue(AGE, 5); // todo this age is temporary to account for no model
                }
                level.setBlock(posToExpand, newState, Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        return VineGrowthHelper.canHopsSurvive(level, pos);
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
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if (this.shouldRestoreRopeOnRemove(state, level, pos, newState)) {
            level.setBlock(pos, ((RopeBlock2) GrowthcraftBlocks.ROPE_LINEN2.get()).getStateForPlacement(level, pos), Block.UPDATE_ALL);
        }
    }

    @Override
    public boolean shouldRestoreRopeOnRemove(BlockState state, Level level, BlockPos pos, BlockState newState)
    {
        return !newState.is(state.getBlock())
            && !(newState.getBlock() instanceof RopeBlock2Base);
    }
}
