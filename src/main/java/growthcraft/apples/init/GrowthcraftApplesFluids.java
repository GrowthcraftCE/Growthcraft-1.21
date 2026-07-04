package growthcraft.apples.init;

import growthcraft.apples.config.Reference;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GrowthcraftApplesFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);
    public static final DeferredRegister<Item> ITEMS = GrowthcraftApplesItems.ITEMS;

    public static final FluidRegistryContainer APPLE_CIDER = register(Reference.UnlocalizedName.Fluid.APPLE_CIDER, Reference.FluidColor.APPLE_CIDER);
    public static final FluidRegistryContainer APPLE_JUICE = register(Reference.UnlocalizedName.Fluid.APPLE_JUICE, Reference.FluidColor.APPLE_JUICE);

    public static final FluidRegistryContainer[] ALL = new FluidRegistryContainer[] {
            APPLE_CIDER,
            APPLE_JUICE
    };

    private GrowthcraftApplesFluids() {
    }

    private static FluidRegistryContainer register(String fluidName, ColorUtils.GrowthcraftColor color) {
        FluidType.Properties typeProperties = FluidType.Properties.create()
                .canSwim(true)
                .canDrown(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, fluidName)
                .tint(color.toIntValue())
                .fogColor(
                        color.toFloatValues().get("red"),
                        color.toFloatValues().get("green"),
                        color.toFloatValues().get("blue")
                );

        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY);

        return new FluidRegistryContainer(
                fluidName,
                typeProperties,
                () -> FluidRegistryContainer.createExtension(client),
                new FluidRegistryContainer.AdditionalProperties()
                        .explosionResistance(100.0F)
                        .levelDecreasePerBlock(1)
                        .slopeFindDistance(4)
                        .tickRate(5),
                blockProperties,
                new Item.Properties().stacksTo(1),
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }
}
