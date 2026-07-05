package growthcraft.milk.block.signs;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

final class ShopSignBehavior {
    private ShopSignBehavior() {
    }

    static InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, Block originalBlock) {
        if (!(level.getBlockEntity(pos) instanceof ShopSignBlockEntity signBlockEntity) || signBlockEntity.isWaxed()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            BlockState newState = originalBlock.withPropertiesOf(state);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
            newState.useWithoutItem(level, player, hitResult);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    static ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!(level.getBlockEntity(pos) instanceof ShopSignBlockEntity signBlockEntity) || hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (signBlockEntity.isWaxed()) {
            return ItemInteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            if (!signBlockEntity.getItem().isEmpty() && isWax(heldStack)) {
                signBlockEntity.setWaxed(true);
                level.levelEvent(player, 3003, pos, 0);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            } else {
                signBlockEntity.setItem(heldStack);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    static boolean isWax(ItemStack stack) {
        return stack.is(Items.HONEYCOMB)
                || stack.is(GrowthcraftApiaryItems.BEES_WAX.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_BLACK.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_BLUE.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_BROWN.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_CYAN.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_GRAY.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_GREEN.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_LIGHT_BLUE.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_LIGHT_GRAY.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_LIME.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_MAGENTA.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_ORANGE.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_PINK.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_PURPLE.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_RED.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_WHITE.get())
                || stack.is(GrowthcraftApiaryItems.BEES_WAX_YELLOW.get());
    }
}
