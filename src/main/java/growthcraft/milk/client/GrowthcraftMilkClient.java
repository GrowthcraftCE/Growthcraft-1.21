package growthcraft.milk.client;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.world.level.block.Block;
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
        registerItemTint(event, Reference.ItemColor.APPENZELLER_CHEESE, GrowthcraftMilkItems.APPENZELLER_CHEESE);
        registerItemTint(event, Reference.ItemColor.ASIAGO_CHEESE, GrowthcraftMilkItems.ASIAGO_CHEESE);
        registerItemTint(event, Reference.ItemColor.CASU_MARZU_CHEESE, GrowthcraftMilkItems.CASU_MARZU_CHEESE);
        registerItemTint(event, Reference.ItemColor.CHEDDAR_CHEESE, GrowthcraftMilkItems.CHEDDAR_CHEESE);
        registerItemTint(event, Reference.ItemColor.EMMENTALER_CHEESE, GrowthcraftMilkItems.EMMENTALER_CHEESE);
        registerItemTint(event, Reference.ItemColor.GORGONZOLA_CHEESE, GrowthcraftMilkItems.GORGONZOLA_CHEESE);
        registerItemTint(event, Reference.ItemColor.GOUDA_CHEESE, GrowthcraftMilkItems.GOUDA_CHEESE);
        registerItemTint(event, Reference.ItemColor.MONTEREY_CHEESE, GrowthcraftMilkItems.MONTEREY_CHEESE);
        registerItemTint(event, Reference.ItemColor.PARMESAN_CHEESE, GrowthcraftMilkItems.PARMESAN_CHEESE);
        registerItemTint(event, Reference.ItemColor.PROVOLONE_CHEESE, GrowthcraftMilkItems.PROVOLONE_CHEESE);
        registerItemTint(event, Reference.ItemColor.APPENZELLER_CHEESE, GrowthcraftMilkItems.APPENZELLER_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.ASIAGO_CHEESE, GrowthcraftMilkItems.ASIAGO_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.CASU_MARZU_CHEESE, GrowthcraftMilkItems.CASU_MARZU_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.CHEDDAR_CHEESE, GrowthcraftMilkItems.CHEDDAR_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.EMMENTALER_CHEESE, GrowthcraftMilkItems.EMMENTALER_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.GORGONZOLA_CHEESE, GrowthcraftMilkItems.GORGONZOLA_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.GOUDA_CHEESE, GrowthcraftMilkItems.GOUDA_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.MONTEREY_CHEESE, GrowthcraftMilkItems.MONTEREY_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.PARMESAN_CHEESE, GrowthcraftMilkItems.PARMESAN_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.PROVOLONE_CHEESE, GrowthcraftMilkItems.PROVOLONE_CHEESE_AGED);
        registerItemTint(event, Reference.ItemColor.CHEDDAR_CHEESE, GrowthcraftMilkItems.CHEDDAR_CHEESE_WAXED);
        registerItemTint(event, Reference.ItemColor.GOUDA_CHEESE, GrowthcraftMilkItems.GOUDA_CHEESE_WAXED);
        registerItemTint(event, Reference.ItemColor.MONTEREY_CHEESE, GrowthcraftMilkItems.MONTEREY_CHEESE_WAXED);
        registerItemTint(event, Reference.ItemColor.PROVOLONE_CHEESE, GrowthcraftMilkItems.PROVOLONE_CHEESE_WAXED);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        registerBlockTint(event, Reference.BlockColor.APPENZELLER_CHEESE, GrowthcraftMilkBlocks.APPENZELLER_CHEESE);
        registerBlockTint(event, Reference.BlockColor.ASIAGO_CHEESE, GrowthcraftMilkBlocks.ASIAGO_CHEESE);
        registerBlockTint(event, Reference.BlockColor.CASU_MARZU_CHEESE, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE);
        registerBlockTint(event, Reference.BlockColor.CHEDDAR_CHEESE, GrowthcraftMilkBlocks.CHEDDAR_CHEESE);
        registerBlockTint(event, Reference.BlockColor.EMMENTALER_CHEESE, GrowthcraftMilkBlocks.EMMENTALER_CHEESE);
        registerBlockTint(event, Reference.BlockColor.GORGONZOLA_CHEESE, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE);
        registerBlockTint(event, Reference.BlockColor.GOUDA_CHEESE, GrowthcraftMilkBlocks.GOUDA_CHEESE);
        registerBlockTint(event, Reference.BlockColor.MONTEREY_CHEESE, GrowthcraftMilkBlocks.MONTEREY_CHEESE);
        registerBlockTint(event, Reference.BlockColor.PARMESAN_CHEESE, GrowthcraftMilkBlocks.PARMESAN_CHEESE);
        registerBlockTint(event, Reference.BlockColor.PROVOLONE_CHEESE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE);
        registerBlockTint(event, Reference.BlockColor.APPENZELLER_CHEESE, GrowthcraftMilkBlocks.APPENZELLER_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.ASIAGO_CHEESE, GrowthcraftMilkBlocks.ASIAGO_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.CASU_MARZU_CHEESE, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.CHEDDAR_CHEESE, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.EMMENTALER_CHEESE, GrowthcraftMilkBlocks.EMMENTALER_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.GORGONZOLA_CHEESE, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.GOUDA_CHEESE, GrowthcraftMilkBlocks.GOUDA_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.MONTEREY_CHEESE, GrowthcraftMilkBlocks.MONTEREY_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.PARMESAN_CHEESE, GrowthcraftMilkBlocks.PARMESAN_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.PROVOLONE_CHEESE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_AGED);
        registerBlockTint(event, Reference.BlockColor.CHEDDAR_CHEESE, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_WAXED);
        registerBlockTint(event, Reference.BlockColor.GOUDA_CHEESE, GrowthcraftMilkBlocks.GOUDA_CHEESE_WAXED);
        registerBlockTint(event, Reference.BlockColor.MONTEREY_CHEESE, GrowthcraftMilkBlocks.MONTEREY_CHEESE_WAXED);
        registerBlockTint(event, Reference.BlockColor.PROVOLONE_CHEESE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_WAXED);
    }

    private static void registerContentsTint(RegisterColorHandlersEvent.Item event,
                                             DeferredHolder<Item, ? extends Item> item) {
        int milkColor = growthcraft.milk.config.Reference.FluidColor.MILK.toIntValue();
        event.register((stack, tintIndex) -> tintIndex == 0 ? milkColor : 0xFFFFFFFF, item.get());
    }

    private static void registerItemTint(RegisterColorHandlersEvent.Item event,
                                         ColorUtils.GrowthcraftColor color,
                                         DeferredHolder<Item, ? extends Item> item) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? color.toIntValue() : 0xFFFFFFFF, item.get());
    }

    private static void registerBlockTint(RegisterColorHandlersEvent.Block event,
                                          ColorUtils.GrowthcraftColor color,
                                          DeferredHolder<Block, ? extends Block> block) {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? color.toIntValue() : 0xFFFFFFFF, block.get());
    }
}
