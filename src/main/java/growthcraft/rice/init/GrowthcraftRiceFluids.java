package growthcraft.rice.init;

import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import growthcraft.rice.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GrowthcraftRiceFluids {
    private GrowthcraftRiceFluids() {}

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);
    public static final DeferredRegister<Item> ITEMS = GrowthcraftRiceItems.ITEMS;

    public static final FluidRegistryContainer RICE_WATER = registerFluid("rice_water", Reference.FluidColor.RICE_WATER);
    public static final FluidRegistryContainer RICE_WINE = registerFluid("rice_wine", Reference.FluidColor.RICE_WINE);
    public static final FluidRegistryContainer SAKE = registerFluid("sake", Reference.FluidColor.SAKE);

    public static final FluidRegistryContainer[] ALL = new FluidRegistryContainer[] {
            RICE_WATER, RICE_WINE, SAKE
    };

    private static FluidRegistryContainer registerFluid(String name, ColorUtils.GrowthcraftColor color) {
        FluidType.Properties typeProps = FluidType.Properties.create()
                .canDrown(false)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name)
                .tint(color);

        BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of()
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY);

        FluidRegistryContainer.AdditionalProperties additionalProperties =
                new FluidRegistryContainer.AdditionalProperties()
                        .tickRate(5)
                        .slopeFindDistance(4)
                        .levelDecreasePerBlock(1)
                        .explosionResistance(100f);

        return new FluidRegistryContainer(
                name,
                typeProps,
                () -> FluidRegistryContainer.createExtension(client),
                additionalProperties,
                blockProps,
                new Item.Properties().stacksTo(1),
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }
}
