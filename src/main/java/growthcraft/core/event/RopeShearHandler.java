package growthcraft.core.event;

import growthcraft.core.block.RopeBlock;
import growthcraft.core.block.RopeFenceBlock;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Handles shearing of rope: for RopeFenceBlock, drops one rope and restores the original fence.
 * For RopeBlock, drops one rope and clears the rope, preserving water if waterlogged.
 */
@EventBusSubscriber(modid = Reference.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RopeShearHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        ItemStack held = player.getItemInHand(hand);
        if (!(held.getItem() instanceof ShearsItem)) return;

        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        // Case 1: RopeFenceBlock -> revert to vanilla fence and drop rope
        if (state.getBlock() instanceof RopeFenceBlock ropeFence) {
            Block vanillaFence = RopeFenceBlock.getVanillaFenceFor(ropeFence);
            if (vanillaFence == null) return;

            if (level.isClientSide()) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }

            // Build new vanilla fence state copying connections and waterlogged
            BlockState newState = vanillaFence.defaultBlockState();
            if (state.hasProperty(FenceBlock.NORTH)) newState = newState.setValue(FenceBlock.NORTH, state.getValue(FenceBlock.NORTH));
            if (state.hasProperty(FenceBlock.EAST)) newState = newState.setValue(FenceBlock.EAST, state.getValue(FenceBlock.EAST));
            if (state.hasProperty(FenceBlock.SOUTH)) newState = newState.setValue(FenceBlock.SOUTH, state.getValue(FenceBlock.SOUTH));
            if (state.hasProperty(FenceBlock.WEST)) newState = newState.setValue(FenceBlock.WEST, state.getValue(FenceBlock.WEST));
            if (state.hasProperty(BlockStateProperties.WATERLOGGED)) newState = newState.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));

            level.setBlock(pos, newState, Block.UPDATE_ALL);
            Block.popResource(level, pos, new ItemStack(GrowthcraftItems.ROPE_LINEN.get()));

            // Damage shears by 1 use
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            held.hurtAndBreak(1, player, slot);

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }

        // Case 2: RopeBlock -> remove rope and drop rope item (preserve water if waterlogged)
        if (state.getBlock() instanceof RopeBlock) {
            if (level.isClientSide()) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }

            boolean wasWaterlogged = state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED);
            // If waterlogged, set water source; otherwise set to air
            BlockState replacement = wasWaterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
            level.setBlock(pos, replacement, Block.UPDATE_ALL);
            Block.popResource(level, pos, new ItemStack(GrowthcraftItems.ROPE_LINEN.get()));

            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            held.hurtAndBreak(1, player, slot);

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
