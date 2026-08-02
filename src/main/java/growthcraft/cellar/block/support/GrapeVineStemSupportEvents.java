package growthcraft.cellar.block.support;

import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.config.Reference;
import growthcraft.core.block.RopeBlock2;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Reference.MODID)
public class GrapeVineStemSupportEvents
{
    ///
    /// when player breaks a tem, we drop a rope if there isn't a farmland below (because bottom-most block didn't grow over rope.)
    /// problem: if we break the farmland below the stem, stem reacts, checks below and finds air; we don't know whether there was rope initially.
    ///
    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event)
    {
        if (! event.getState().is(Tags.Blocks.VILLAGER_FARMLANDS))
        {
            return;
        } // it's a farmland, then.
        if (! (event.getLevel().getBlockState(event.getPos().above()).getBlock() instanceof GrapeVineStemBlock))
        {
            return;
        }
        event.getLevel().destroyBlock(event.getPos().above(), false); // destroy stem, don't drop seeds.
    }

    ///
    /// Allow player to till dirt which is right below rope
    ///
    @SubscribeEvent
    public static void onPlayerTryHoeBelowRope(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getItemStack().canPerformAction(ItemAbilities.HOE_TILL) && event.getFace() != null && event.getFace().equals(Direction.UP)) {
            BlockState clickedState = event.getLevel().getBlockState(event.getPos());
            if (event.getLevel().getBlockState(event.getPos()).is(BlockTags.DIRT) && event.getLevel().getBlockState(event.getPos().above()).is(GrowthcraftBlocks.ROPE_LINEN2)) {
                event.getLevel().setBlock(event.getPos(), Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
            }
        }
    }
}
