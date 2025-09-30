package growthcraft.milk.client;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Client-only registrations for Growthcraft Milk.
 * - Tints Milking Bucket contents layer to a milk-white color
 * - Tints the copper Milking Bucket base layer to a copper color
 *
 * Model layering expected:
 * layer0 = contents (to be tinted like milk/fluids)
 * layer1 = bucket base (to be tinted for copper variant)
 */
@EventBusSubscriber(modid = Reference.MODID, value = Dist.CLIENT)
public final class GrowthcraftMilkClient {
    private GrowthcraftMilkClient() {}

    // Copper color (opaque ARGB)
    private static final int COPPER_TINT = 0xFFB87333; // classic copper hex

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        // Contents tint: use milk default color for now (tool is empty by design)
        registerContentsTint(event, GrowthcraftMilkItems.MILKING_BUCKET_IRON);
        registerContentsTint(event, GrowthcraftMilkItems.MILKING_BUCKET_COPPER);

        // Base tint: only copper bucket gets a base tint (iron uses the raw texture colors)
        event.register((stack, tintIndex) -> tintIndex == 1 ? COPPER_TINT : 0xFFFFFFFF,
                GrowthcraftMilkItems.MILKING_BUCKET_COPPER.get());
    }

    private static void registerContentsTint(RegisterColorHandlersEvent.Item event,
                                             DeferredHolder<Item, ? extends Item> item) {
        int milkColor = growthcraft.milk.config.Reference.FluidColor.MILK.toIntValue();
        event.register((stack, tintIndex) -> tintIndex == 0 ? milkColor : 0xFFFFFFFF, item.get());
    }
}
