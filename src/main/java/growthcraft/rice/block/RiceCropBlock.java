package growthcraft.rice.block;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class RiceCropBlock extends CropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
            Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 5.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 5.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 8.0, 14.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 13.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 13.0, 15.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
    };

    public RiceCropBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT)
                .noOcclusion()
                .randomTicks()
                .instabreak()
                .sound(SoundType.CROP));
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return GrowthcraftRiceItems.RICE_GRAINS.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!this.isMaxAge(state)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);

        List<ItemStack> drops = state.getDrops(context);
        for (ItemStack stack : drops) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        }

        level.setBlock(pos, this.getStateForAge(1), Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get())
                && hasSufficientLight(level, pos);
    }
}
