package growthcraft.milk.init;

import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.milk.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Registers Milk fluid and its bucket/block for the Milk module.
 */
public final class GrowthcraftMilkFluids {
    private GrowthcraftMilkFluids() {}

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);

    // Reuse the milk Items register so the bucket lives under the same mod's item registry
    public static final DeferredRegister<Item> ITEMS = GrowthcraftMilkItems.ITEMS;

    public static final FluidRegistryContainer MILK = registerMilk();

    private static FluidRegistryContainer registerMilk() {
        String name = "milk";
        // Basic type properties
        FluidType.Properties typeProps = FluidType.Properties.create().canDrown(false).lightLevel(0);

        // Client visuals: milk textures expected at assets/growthcraft_milk/block/fluid/milk_*
        int whiteTint = 0xFFFFFFFF;
        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name)
                .tint(whiteTint);

        BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of()
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(net.minecraft.world.level.block.SoundType.EMPTY);

        Item.Properties itemProps = new Item.Properties().stacksTo(1);

        return new FluidRegistryContainer(
                name,
                typeProps,
                () -> FluidRegistryContainer.createExtension(client),
                new FluidRegistryContainer.AdditionalProperties().tickRate(5).slopeFindDistance(4).levelDecreasePerBlock(1).explosionResistance(100f),
                blockProps,
                itemProps,
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }
}
