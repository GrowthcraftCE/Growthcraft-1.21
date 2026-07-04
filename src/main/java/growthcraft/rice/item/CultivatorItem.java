package growthcraft.rice.item;

import growthcraft.rice.init.GrowthcraftRiceBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CultivatorItem extends HoeItem {
    public CultivatorItem() {
        super(Tiers.IRON, new Item.Properties()
                .stacksTo(1)
                .attributes(HoeItem.createAttributes(Tiers.IRON, -2.0F, -1.0F)));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (context.getClickedFace() != Direction.DOWN
                && level.getBlockState(pos.above()).isAir()
                && level.getBlockState(pos).is(Blocks.FARMLAND)) {
            Player player = context.getPlayer();
            BlockState cultivatedFarmland = GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get().defaultBlockState();
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (!level.isClientSide) {
                level.setBlock(pos, cultivatedFarmland, Block.UPDATE_ALL);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, cultivatedFarmland));
                if (player != null) {
                    context.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }
}
