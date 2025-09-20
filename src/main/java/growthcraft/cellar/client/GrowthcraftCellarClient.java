package growthcraft.cellar.client;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Client-only registrations for Growthcraft Cellar.
 * Registers item color handlers for tinting grain items using Reference.GrainColor values,
 * and applies fluid tinting to our fluid buckets' overlay layer.
 */
@EventBusSubscriber(modid = GrowthcraftCellar.MODID, value = Dist.CLIENT)
public final class GrowthcraftCellarClient {
    private GrowthcraftCellarClient() {}

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        // Base grain
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN, Reference.GrainColor.GRAIN);
        // Variants
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_AMBER, Reference.GrainColor.GRAIN_AMBER);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_BROWN, Reference.GrainColor.GRAIN_BROWN);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_COPPER, Reference.GrainColor.GRAIN_COPPER);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_DARK, Reference.GrainColor.GRAIN_DARK);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_DEEP_AMBER, Reference.GrainColor.GRAIN_DEEP_AMBER);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_DEEP_COPPER, Reference.GrainColor.GRAIN_DEEP_COPPER);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_GOLDEN, Reference.GrainColor.GRAIN_GOLDEN);
        registerGrainColor(event, GrowthcraftCellarItems.GRAIN_PALE_GOLDEN, Reference.GrainColor.GRAIN_PALE_GOLDEN);

        // Fluid buckets overlay tint (layer 1): use the fluid's client tint color
        for (FluidRegistryContainer container : GrowthcraftCellarFluids.ALL) {
            event.register((stack, tintIndex) -> {
                // Only tint the overlay layer
                if (tintIndex != 1) return 0xFFFFFFFF;
                // Obtain tint from the fluid's client extensions
                var fluid = container.source.get();
                int color = IClientFluidTypeExtensions.of(fluid).getTintColor();
                return color == 0 ? 0xFFFFFFFF : color;
            }, container.bucket.get());
        }
    }

    private static void registerGrainColor(RegisterColorHandlersEvent.Item event,
                                           DeferredItem<Item> item,
                                           ColorUtils.GrowthcraftColor growthcraftColor) {
        event.register(growthcraftColor.toItemColor(), item.get());
    }
}
