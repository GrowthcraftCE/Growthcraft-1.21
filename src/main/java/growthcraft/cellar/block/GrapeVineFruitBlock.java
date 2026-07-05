package growthcraft.cellar.block;

import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.function.Supplier;

public class GrapeVineFruitBlock extends GrowthcraftCropsRopeBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 4.0D, 4.0D, 12.0D, 16.0D, 12.0D)
    };
    private final Supplier<? extends Item> fruitItem;

    public GrapeVineFruitBlock(Supplier<? extends Item> fruitItem) {
        super();
        this.fruitItem = fruitItem;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public boolean connectsAsRope() {
        return false;
    }

    @Override
    public BlockState getActualBlockStateWithAge(BlockGetter level, BlockPos pos, int age) {
        return super.getActualBlockStateWithAge(level, pos, age)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).getBlock() instanceof GrapeVineLeavesCropBlock;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || level.getRawBrightness(pos, 0) < 9 || isMaxAge(state)) {
            return;
        }

        if (CommonHooks.canCropGrow(level, pos, state, random.nextInt(4) == 0)) {
            level.setBlock(pos, getActualBlockStateWithAge(level, pos, getAge(state) + 1), Block.UPDATE_ALL);
            CommonHooks.fireCropGrowPost(level, pos, state);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!isMaxAge(state)) {
            return InteractionResult.PASS;
        }

        popResource(level, pos, new ItemStack(fruitItem.get(), Mth.nextInt(level.random, 1, 3)));
        level.setBlock(pos, getActualBlockStateWithAge(level, pos, 0), Block.UPDATE_ALL);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected boolean shouldRestoreRopeOnRemove(BlockState state, Level level, BlockPos pos, BlockState newState) {
        return false;
    }
}
