package growthcraft.apples.client;

import growthcraft.apples.GrowthcraftApples;
import growthcraft.apples.init.GrowthcraftApplesFluids;
import growthcraft.lib.fluid.FluidRegistryContainer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

@EventBusSubscriber(modid = GrowthcraftApples.MODID, value = Dist.CLIENT)
public final class GrowthcraftApplesClient {
    private GrowthcraftApplesClient() {
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        for (FluidRegistryContainer container : GrowthcraftApplesFluids.ALL) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 0) {
                    return 0xFFFFFFFF;
                }
                return IClientFluidTypeExtensions.of(container.source.get()).getTintColor();
            }, container.bucket.get());
        }
    }
}
