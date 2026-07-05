package growthcraft.milk.block.signs;

import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class ShopSignTransformHandler {
    private ShopSignTransformHandler() {
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack heldStack = event.getItemStack();
        if (level.getBlockEntity(pos) instanceof ShopSignBlockEntity shopSign) {
            handleShopSignUse(event, shopSign, heldStack);
            return;
        }

        if (!event.getEntity().isCreative()) {
            return;
        }

        if (heldStack.isEmpty() || ShopSignBehavior.isWax(heldStack) || event.getFace() == Direction.DOWN) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SignBlock original)) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof SignBlockEntity signBlockEntity && signBlockEntity.isWaxed()) {
            return;
        }

        Block replacement = GrowthcraftMilkBlocks.getShopSignFromOriginal(original);
        if (replacement == null) {
            return;
        }

        if (!level.isClientSide) {
            BlockState newState;
            if (original instanceof CeilingHangingSignBlock) {
                newState = replacement.defaultBlockState()
                        .setValue(CeilingHangingSignBlock.ROTATION, state.getValue(CeilingHangingSignBlock.ROTATION))
                        .setValue(CeilingHangingSignBlock.ATTACHED, state.getValue(CeilingHangingSignBlock.ATTACHED))
                        .setValue(CeilingHangingSignBlock.WATERLOGGED, state.getValue(CeilingHangingSignBlock.WATERLOGGED));
            } else if (original instanceof WallHangingSignBlock) {
                newState = replacement.defaultBlockState()
                        .setValue(WallHangingSignBlock.FACING, state.getValue(WallHangingSignBlock.FACING))
                        .setValue(WallHangingSignBlock.WATERLOGGED, state.getValue(WallHangingSignBlock.WATERLOGGED));
            } else {
                return;
            }

            level.setBlock(pos, newState, Block.UPDATE_ALL);
            if (level.getBlockEntity(pos) instanceof growthcraft.milk.block.entity.ShopSignBlockEntity shopSign) {
                shopSign.setItem(heldStack);
            }
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        event.setCanceled(true);
    }

    private static void handleShopSignUse(PlayerInteractEvent.RightClickBlock event, ShopSignBlockEntity shopSign, ItemStack heldStack) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (shopSign.isWaxed()) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
            return;
        }

        if (!level.isClientSide) {
            if (heldStack.isEmpty()) {
                Block original = null;
                if (state.getBlock() instanceof ShopCeilingHangingSignBlock ceilingSign) {
                    original = ceilingSign.getOriginalBlock();
                } else if (state.getBlock() instanceof ShopWallHangingSignBlock wallSign) {
                    original = wallSign.getOriginalBlock();
                }

                if (original != null) {
                    level.setBlock(pos, original.withPropertiesOf(state), Block.UPDATE_ALL);
                }
            } else if (!shopSign.getItem().isEmpty() && ShopSignBehavior.isWax(heldStack)) {
                shopSign.setWaxed(true);
                level.levelEvent(event.getEntity(), 3003, pos, 0);
                if (!event.getEntity().isCreative()) {
                    heldStack.shrink(1);
                }
            } else {
                shopSign.setItem(heldStack);
            }
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        event.setCanceled(true);
    }
}
