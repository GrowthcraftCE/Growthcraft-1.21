package growthcraft.cellar.block;

import growthcraft.cellar.config.Reference;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
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
}
