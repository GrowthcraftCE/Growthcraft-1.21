package growthcraft.apiary.client;

import growthcraft.apiary.GrowthcraftApiary;
import growthcraft.apiary.init.GrowthcraftApiaryFluids;
import growthcraft.lib.fluid.FluidRegistryContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

@EventBusSubscriber(modid = GrowthcraftApiary.MODID, value = Dist.CLIENT)
public final class GrowthcraftApiaryClient {
    private GrowthcraftApiaryClient() {
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        for (FluidRegistryContainer container : GrowthcraftApiaryFluids.ALL) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 0) {
                    return 0xFFFFFFFF;
                }
                return IClientFluidTypeExtensions.of(container.source.get()).getTintColor();
            }, container.bucket.get());
        }
        for (var wax : GrowthcraftApiaryFluids.WAXES) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 0) {
                    return 0xFFFFFFFF;
                }
                return IClientFluidTypeExtensions.of(wax.source.get()).getTintColor();
            }, wax.bucket.get());
        }
    }
}
