package growthcraft.milk.init;

import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
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
    public static final FluidRegistryContainer BUTTER_MILK = registerFluid(Reference.UnlocalizedName.BUTTER_MILK, Reference.FluidColor.BUTTER_MILK);
    public static final FluidRegistryContainer CREAM = registerFluid(Reference.UnlocalizedName.CREAM, Reference.FluidColor.CREAM);
    public static final FluidRegistryContainer CULTURED_MILK = registerFluid(Reference.UnlocalizedName.CULTURED_MILK, Reference.FluidColor.CULTURED_MILK);
    public static final FluidRegistryContainer RENNET = registerFluid(Reference.UnlocalizedName.RENNET, Reference.FluidColor.RENNET);
    public static final FluidRegistryContainer SKIM_MILK = registerFluid(Reference.UnlocalizedName.SKIM_MILK, Reference.FluidColor.SKIM_MILK);
    public static final FluidRegistryContainer WHEY = registerFluid(Reference.UnlocalizedName.WHEY, Reference.FluidColor.WHEY);

    public static final FluidRegistryContainer[] ALL = new FluidRegistryContainer[] {
            MILK, BUTTER_MILK, CREAM, CULTURED_MILK, RENNET, SKIM_MILK, WHEY
    };

    private static FluidRegistryContainer registerMilk() {
        FluidRegistryContainer container = registerFluid(Reference.UnlocalizedName.MILK, Reference.FluidColor.MILK, false);
        // Tie the milk fluid to our custom milk bucket item so vanilla empty buckets can be filled from tanks/blocks.
        container.getProperties().bucket(() -> growthcraft.milk.init.GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
        return container;
    }

    private static FluidRegistryContainer registerFluid(String name, ColorUtils.GrowthcraftColor color) {
        return registerFluid(name, color, true);
    }

    private static FluidRegistryContainer registerFluid(String name, ColorUtils.GrowthcraftColor color, boolean registerBucket) {
        FluidType.Properties typeProps = FluidType.Properties.create()
                .canDrown(false)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name)
                .tint(color);

        BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of()
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(net.minecraft.world.level.block.SoundType.EMPTY);

        Item.Properties itemProps = new Item.Properties().stacksTo(1);
        FluidRegistryContainer.AdditionalProperties additionalProperties =
                new FluidRegistryContainer.AdditionalProperties()
                        .tickRate(5)
                        .slopeFindDistance(4)
                        .levelDecreasePerBlock(1)
                        .explosionResistance(100f);
        if (!registerBucket) {
            additionalProperties.noBucket();
        }

        return new FluidRegistryContainer(
                name,
                typeProps,
                () -> FluidRegistryContainer.createExtension(client),
                additionalProperties,
                blockProps,
                itemProps,
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }
}
