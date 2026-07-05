package growthcraft.cellar.item;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.block.RopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;

public class HopsSeedsItem extends Item {
    public HopsSeedsItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos plantPos = pos.above();
        Block block = level.getBlockState(pos).getBlock();

        if (block instanceof FarmBlock && level.getBlockState(plantPos).isAir()) {
            if (!level.isClientSide) {
                level.setBlock(plantPos, GrowthcraftCellarBlocks.HOPS_VINE.get().getActualBlockStateWithAge(level, plantPos, 0), Block.UPDATE_ALL);
                RopeBlock.refreshAdjacentConnections(level, plantPos);
                if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
