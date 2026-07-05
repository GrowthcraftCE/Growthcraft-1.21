package growthcraft.apples.item;

import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AppleSeedsItem extends Item {
    public AppleSeedsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos saplingPos = pos.above();
        BlockState sapling = GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get().defaultBlockState();

        if (!level.getBlockState(pos).is(Blocks.GRASS_BLOCK)
                || !level.getBlockState(saplingPos).isAir()
                || !sapling.canSurvive(level, saplingPos)) {
            return InteractionResult.PASS;
        }

        level.playSound(context.getPlayer(), saplingPos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide) {
            level.setBlock(saplingPos, sapling, 11);
            if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
