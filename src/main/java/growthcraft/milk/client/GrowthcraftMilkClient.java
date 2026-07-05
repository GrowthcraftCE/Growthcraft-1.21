package growthcraft.milk.client;

import growthcraft.milk.client.screen.MixingVatScreen;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import growthcraft.lib.client.screen.MachineScreen;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
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
    private static final ResourceLocation CHEESE_SLICED_PROPERTY =
            ResourceLocation.fromNamespaceAndPath(Reference.MODID, "cheese_sliced");

    private GrowthcraftMilkClient() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(GrowthcraftMilkClient::registerCheeseModelProperties);
    }

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
        registerItemTint(event, Reference.ItemColor.APPENZELLER_CHEESE, GrowthcraftMilkItems.APPENZELLER_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.ASIAGO_CHEESE, GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.CASU_MARZU_CHEESE, GrowthcraftMilkItems.CASU_MARZU_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.CHEDDAR_CHEESE, GrowthcraftMilkItems.CHEDDAR_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.EMMENTALER_CHEESE, GrowthcraftMilkItems.EMMENTALER_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.GORGONZOLA_CHEESE, GrowthcraftMilkItems.GORGONZOLA_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.GOUDA_CHEESE, GrowthcraftMilkItems.GOUDA_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.MONTEREY_CHEESE, GrowthcraftMilkItems.MONTEREY_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.PARMESAN_CHEESE, GrowthcraftMilkItems.PARMESAN_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.PROVOLONE_CHEESE, GrowthcraftMilkItems.PROVOLONE_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.RICOTTA_CHEESE, GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS);
        registerItemTint(event, Reference.ItemColor.APPENZELLER_CHEESE, GrowthcraftMilkItems.APPENZELLER_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.ASIAGO_CHEESE, GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.CASU_MARZU_CHEESE, GrowthcraftMilkItems.CASU_MARZU_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.CHEDDAR_CHEESE, GrowthcraftMilkItems.CHEDDAR_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.EMMENTALER_CHEESE, GrowthcraftMilkItems.EMMENTALER_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.GORGONZOLA_CHEESE, GrowthcraftMilkItems.GORGONZOLA_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.GOUDA_CHEESE, GrowthcraftMilkItems.GOUDA_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.MONTEREY_CHEESE, GrowthcraftMilkItems.MONTEREY_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.PARMESAN_CHEESE, GrowthcraftMilkItems.PARMESAN_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.PROVOLONE_CHEESE, GrowthcraftMilkItems.PROVOLONE_CHEESE_CURDS_DRAINED);
        registerItemTint(event, Reference.ItemColor.RICOTTA_CHEESE, GrowthcraftMilkItems.RICOTTA_CHEESE_CURDS_DRAINED);
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
        registerBlockTint(event, Reference.BlockColor.APPENZELLER_CHEESE, GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.ASIAGO_CHEESE, GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.CASU_MARZU_CHEESE, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.CHEDDAR_CHEESE, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.EMMENTALER_CHEESE, GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.GORGONZOLA_CHEESE, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.GOUDA_CHEESE, GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.MONTEREY_CHEESE, GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.PARMESAN_CHEESE, GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.PROVOLONE_CHEESE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS);
        registerBlockTint(event, Reference.BlockColor.RICOTTA_CHEESE, GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GrowthcraftMilkMenus.CHEESE_PRESS.get(), MachineScreen::new);
        event.register(GrowthcraftMilkMenus.CHURN.get(), MachineScreen::new);
        event.register(GrowthcraftMilkMenus.MIXING_VAT.get(), MixingVatScreen::new);
        event.register(GrowthcraftMilkMenus.PANCHEON.get(), MachineScreen::new);
    }

    private static void registerContentsTint(RegisterColorHandlersEvent.Item event,
                                             DeferredHolder<Item, ? extends Item> item) {
        int milkColor = growthcraft.milk.config.Reference.FluidColor.MILK.toIntValue();
        event.register((stack, tintIndex) -> tintIndex == 0 ? milkColor : 0xFFFFFFFF, item.get());
    }

    private static void registerItemTint(RegisterColorHandlersEvent.Item event,
                                         ColorUtils.GrowthcraftColor color,
                                         DeferredHolder<Item, ? extends Item> item) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? opaqueColor(color) : 0xFFFFFFFF, item.get());
    }

    private static void registerBlockTint(RegisterColorHandlersEvent.Block event,
                                          ColorUtils.GrowthcraftColor color,
                                          DeferredHolder<Block, ? extends Block> block) {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? color.toIntValue() : 0xFFFFFFFF, block.get());
    }

    private static int opaqueColor(ColorUtils.GrowthcraftColor color) {
        return color.toIntValue() | 0xFF000000;
    }

    private static void registerCheeseModelProperties() {
        registerCheeseSlicedProperty(GrowthcraftMilkItems.APPENZELLER_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.ASIAGO_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.CASU_MARZU_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.CHEDDAR_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.EMMENTALER_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.GORGONZOLA_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.GOUDA_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.MONTEREY_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.PARMESAN_CHEESE_AGED);
        registerCheeseSlicedProperty(GrowthcraftMilkItems.PROVOLONE_CHEESE_AGED);
    }

    private static void registerCheeseSlicedProperty(DeferredHolder<Item, ? extends Item> item) {
        ItemProperties.register(item.get(), CHEESE_SLICED_PROPERTY, (stack, level, entity, seed) -> isCheeseSliced(stack) ? 1.0F : 0.0F);
    }

    private static boolean isCheeseSliced(ItemStack stack) {
        CustomData blockEntityData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        if (blockEntityData.isEmpty()) {
            return false;
        }

        return blockEntityData.copyTag().getInt("slicesbottom") < 4;
    }
}
