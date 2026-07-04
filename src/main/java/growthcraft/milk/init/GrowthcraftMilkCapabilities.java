package growthcraft.milk.init;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class GrowthcraftMilkCapabilities {
    private GrowthcraftMilkCapabilities() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftMilkBlockEntities.CHURN.get(),
                (be, side) -> be.getTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftMilkBlockEntities.PANCHEON.get(),
                (be, side) -> be.getFluidHandler());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, GrowthcraftMilkBlockEntities.MIXING_VAT.get(),
                (be, side) -> be.getFluidHandler());
    }
}
