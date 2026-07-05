package growthcraft.cellar.block;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.core.block.RopeBlock;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HopsCropBlock extends GrowthcraftCropsRopeBlock {
    private static final VoxelShape SEEDLING_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 5.0D, 10.0D);
    private static final VoxelShape POST_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AGE) < 4 ? SEEDLING_SHAPE : POST_SHAPE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        tryGrowNewVine(level, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return super.isValidBonemealTarget(level, pos, state) || level.getBlockState(pos.above()).getBlock() instanceof RopeBlock;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        super.performBonemeal(level, random, pos, state);
        tryGrowNewVine(level, pos);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(GrowthcraftCellarItems.HOPS_SEEDS.get());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!isMaxAge(state)) {
            return InteractionResult.PASS;
        }

        popResource(level, pos, new ItemStack(GrowthcraftCellarItems.HOPS.get()));
        level.setBlock(pos, getActualBlockStateWithAge(level, pos, getMaxAge() - 1), Block.UPDATE_ALL);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void tryGrowNewVine(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);

        if (isMaxAge(state) && aboveState.getBlock() instanceof RopeBlock) {
            setCropBlock(level, above, GrowthcraftCellarBlocks.HOPS_VINE.get().getActualBlockStateWithAge(level, above, 0));
        }
    }
}
