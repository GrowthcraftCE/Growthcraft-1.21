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

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        // Contents tint: use milk default color for now (tool is empty by design)
        registerContentsTint(event, GrowthcraftMilkItems.MILKING_BUCKET_IRON);
    }

    private static void registerContentsTint(RegisterColorHandlersEvent.Item event,
                                             DeferredHolder<Item, ? extends Item> item) {
        int milkColor = growthcraft.milk.config.Reference.FluidColor.MILK.toIntValue();
        event.register((stack, tintIndex) -> tintIndex == 0 ? milkColor : 0xFFFFFFFF, item.get());
    }
}
