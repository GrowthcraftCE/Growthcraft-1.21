package growthcraft.cellar.client;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarFluids;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
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
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        GrowthcraftCellar.LOGGER.info("[CellarClient] Registering CultureJarBlockEntityRenderer");
        event.registerBlockEntityRenderer(
                growthcraft.cellar.init.GrowthcraftCellarBlockEntities.CULTURE_JAR.get(),
                growthcraft.cellar.client.renderer.CultureJarBlockEntityRenderer::new
        );
    }

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

        // Fluid buckets fluid tint (layer 0): use the fluid's client tint color
        for (FluidRegistryContainer container : GrowthcraftCellarFluids.ALL) {
            event.register((stack, tintIndex) -> {
                // Only tint the fluid layer (layer0)
                if (tintIndex != 0) return 0xFFFFFFFF;
                // Obtain tint from the fluid's client extensions
                var fluid = container.source.get();
                int color = IClientFluidTypeExtensions.of(fluid).getTintColor();
                return color == 0 ? 0xFFFFFFFF : color;
            }, container.bucket.get());
        }

        event.register((stack, tintIndex) -> tintIndex == 0
                        ? 0xFF000000 | stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).getColor()
                        : 0xFFFFFFFF,
                GrowthcraftCellarItems.POTION_ALE.get(),
                GrowthcraftCellarItems.POTION_LAGER.get(),
                GrowthcraftCellarItems.POTION_WINE.get());
    }

    private static void registerGrainColor(RegisterColorHandlersEvent.Item event,
                                           DeferredItem<Item> item,
                                           ColorUtils.GrowthcraftColor growthcraftColor) {
        event.register(growthcraftColor.toItemColor(), item.get());
    }
}
