package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
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

public final class GrowthcraftApiaryFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);
    public static final DeferredRegister<Item> ITEMS = GrowthcraftApiaryItems.ITEMS;

    public static final FluidRegistryContainer HONEY = register(Reference.UnlocalizedName.HONEY, Reference.FluidColor.HONEY);
    public static final FluidRegistryContainer HONEY_MEAD = register(Reference.UnlocalizedName.HONEY_MEAD, Reference.FluidColor.HONEY_MEAD);
    public static final FluidRegistryContainer HONEY_MEAD_MUST = register(Reference.UnlocalizedName.HONEY_MEAD_MUST, Reference.FluidColor.HONEY_MEAD_MUST);

    public static final FluidRegistryContainer[] ALL = new FluidRegistryContainer[] {
            HONEY,
            HONEY_MEAD,
            HONEY_MEAD_MUST
    };

    private GrowthcraftApiaryFluids() {
    }

    private static FluidRegistryContainer register(String name, ColorUtils.GrowthcraftColor color) {
        FluidType.Properties typeProperties = FluidType.Properties.create()
                .canSwim(true)
                .canDrown(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name)
                .tint(color)
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

        FluidRegistryContainer.AdditionalProperties additionalProperties =
                new FluidRegistryContainer.AdditionalProperties()
                        .tickRate(5)
                        .slopeFindDistance(4)
                        .levelDecreasePerBlock(1)
                        .explosionResistance(100.0F);

        return new FluidRegistryContainer(
                name,
                typeProperties,
                () -> FluidRegistryContainer.createExtension(client),
                additionalProperties,
                blockProperties,
                new Item.Properties().stacksTo(1),
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }
}
