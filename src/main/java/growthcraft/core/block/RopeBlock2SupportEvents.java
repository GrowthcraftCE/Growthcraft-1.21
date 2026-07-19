package growthcraft.core.block;

import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber
public class RopeBlock2SupportEvents
{
    ///
    /// when player tries to break the fence block, if it's wrapped, we disconnect the rope (one connection) and leave the fence be.
    ///
    @SubscribeEvent
    public static void onPlayerBreakBlock(BlockEvent.BreakEvent event)
    {
        if (! event.getState().is(BlockTags.FENCES))
        {
            return;
        } // it's a fence, then.
        Direction direction = Direction.NORTH;
        for (int i = 0; i < 4; i++)
        {
            BlockState sideBlock = event.getLevel().getBlockState(event.getPos().relative(direction));
            if (sideBlock.getBlock() instanceof RopeBlock2)
            { // rope or leaves
                if (sideBlock.getValue(RopeBlock2.getPropertyFromDirection(direction.getOpposite())) == 2)
                {
                    event.getLevel().setBlock(event.getPos().relative(direction), sideBlock.setValue(RopeBlock2.getPropertyFromDirection(direction.getOpposite()), 0), RopeBlock.UPDATE_ALL);
                    event.setCanceled(true);
                    break;
                }
            }
            direction = direction.getClockWise();
        }
    }

    ///
    /// opposite of above. connects rope/leaves and fence. rope is required in hand but not spent as the blocks are adjacent.
    ///
    @SubscribeEvent
    public static void onPlayerTryPlaceRope(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getItemStack().is(GrowthcraftItems.ROPE_LINEN2.get())) {
            BlockState clickedState = event.getLevel().getBlockState(event.getPos());
            if (event.getFace() != null && clickedState.is(BlockTags.FENCES)) {
                BlockState adjacent = event.getLevel().getBlockState(event.getPos().relative(event.getFace()));
                if (adjacent.getBlock() instanceof RopeBlock2 && adjacent.getValue(RopeBlock2.getPropertyFromDirection(event.getFace().getOpposite())) == 0) {
                    event.getLevel().setBlock(event.getPos().relative(event.getFace()), adjacent.setValue(RopeBlock2.getPropertyFromDirection(event.getFace().getOpposite()), 2), RopeBlock.UPDATE_ALL);
                    return;
                }
            }
            if (event.getFace() != null && clickedState.getBlock() instanceof RopeBlock2) {
                BlockState adjacent = event.getLevel().getBlockState(event.getPos().relative(event.getFace()));
                if (adjacent.is(BlockTags.FENCES) && clickedState.getValue(RopeBlock2.getPropertyFromDirection(event.getFace())) == 0) {
                    event.getLevel().setBlock(event.getPos(), clickedState.setValue(RopeBlock2.getPropertyFromDirection(event.getFace()), 2), RopeBlock.UPDATE_ALL);
                }
            }
        }
    }
}