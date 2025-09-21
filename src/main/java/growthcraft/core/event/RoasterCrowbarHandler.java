package growthcraft.core.event;

import growthcraft.core.config.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Prepares for future Crowbar -> Roaster adjustments.
 * For now, this handler only detects the interaction and reports a stub message.
 *
 * Placed in Core because crowbars are Core items. Avoids compile dependency on Cellar
 * by checking the Roaster via raw ResourceLocation strings.
 */
@EventBusSubscriber(modid = Reference.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class RoasterCrowbarHandler {

    private static final String CROWBAR_NAMESPACE = "growthcraft"; // Crowbar items belong to core mod
    private static final String CROWBAR_PREFIX = "crowbar_";

    private static final String ROASTER_NAMESPACE = "growthcraft_cellar"; // Cellar mod id
    private static final String ROASTER_PATH = "roaster"; // Roaster block id

    private RoasterCrowbarHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return; // server-side handling only

        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        if (!isCrowbar(stack.getItem())) return;
        if (!isRoaster(level.getBlockState(event.getPos()).getBlock())) return;

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        // Stub behavior: notify and mark as handled (no state change yet)
        player.displayClientMessage(net.minecraft.network.chat.Component.literal("Roaster adjusted (stub)"), true);

        // Prevent default use and mark success without consuming the item
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);

        // Optional: animate hand swing to give feedback
        player.swing(hand, true);
    }

    private static boolean isCrowbar(Item item) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
        if (key == null) return false;
        return CROWBAR_NAMESPACE.equals(key.getNamespace()) && key.getPath().startsWith(CROWBAR_PREFIX);
    }

    private static boolean isRoaster(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        if (key == null) return false;
        return ROASTER_NAMESPACE.equals(key.getNamespace()) && ROASTER_PATH.equals(key.getPath());
    }
}
