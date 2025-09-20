package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Registers all Cellar fluids and their linked buckets/blocks.
 */
public class GrowthcraftCellarFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);

    // Reuse the Cellar Items register so buckets appear under the same deferred items registry.
    public static final DeferredRegister<Item> ITEMS = GrowthcraftCellarItems.ITEMS;

    // Beers, Lagers, Wines, Juices and Worts
    public static final FluidRegistryContainer AMBER_ALE = register(Reference.UnlocalizedName.Fluid.AMBER_ALE, Reference.FluidColor.AMBER_ALE);
    public static final FluidRegistryContainer AMBER_LAGER = register(Reference.UnlocalizedName.Fluid.AMBER_LAGER, Reference.FluidColor.AMBER_LAGER);
    public static final FluidRegistryContainer AMBER_WORT = register(Reference.UnlocalizedName.Fluid.AMBER_WORT, Reference.FluidColor.AMBER_WORT);
    public static final FluidRegistryContainer BROWN_ALE = register(Reference.UnlocalizedName.Fluid.BROWN_ALE, Reference.FluidColor.BROWN_ALE);
    public static final FluidRegistryContainer BROWN_LAGER = register(Reference.UnlocalizedName.Fluid.BROWN_LAGER, Reference.FluidColor.BROWN_LAGER);
    public static final FluidRegistryContainer BROWN_WORT = register(Reference.UnlocalizedName.Fluid.BROWN_WORT, Reference.FluidColor.BROWN_WORT);
    public static final FluidRegistryContainer COPPER_ALE = register(Reference.UnlocalizedName.Fluid.COPPER_ALE, Reference.FluidColor.COPPER_ALE);
    public static final FluidRegistryContainer COPPER_LAGER = register(Reference.UnlocalizedName.Fluid.COPPER_LAGER, Reference.FluidColor.COPPER_LAGER);
    public static final FluidRegistryContainer COPPER_WORT = register(Reference.UnlocalizedName.Fluid.COPPER_WORT, Reference.FluidColor.COPPER_WORT);
    public static final FluidRegistryContainer DARK_LAGER = register(Reference.UnlocalizedName.Fluid.DARK_LAGER, Reference.FluidColor.DARK_LAGER);
    public static final FluidRegistryContainer DARK_WORT = register(Reference.UnlocalizedName.Fluid.DARK_WORT, Reference.FluidColor.DARK_WORT);
    public static final FluidRegistryContainer DEEP_AMBER_WORT = register(Reference.UnlocalizedName.Fluid.DEEP_AMBER_WORT, Reference.FluidColor.DEEP_AMBER_WORT);
    public static final FluidRegistryContainer DEEP_COPPER_WORT = register(Reference.UnlocalizedName.Fluid.DEEP_COPPER_WORT, Reference.FluidColor.DEEP_COPPER_WORT);
    public static final FluidRegistryContainer GOLDEN_WORT = register(Reference.UnlocalizedName.Fluid.GOLDEN_WORT, Reference.FluidColor.GOLDEN_WORT);
    public static final FluidRegistryContainer HOPPED_GOLDEN_WORT = register(Reference.UnlocalizedName.Fluid.HOPPED_GOLDEN_WORT, Reference.FluidColor.HOPPED_GOLDEN_WORT);
    public static final FluidRegistryContainer IPA_ALE = register(Reference.UnlocalizedName.Fluid.IPA_ALE, Reference.FluidColor.IPA_ALE);
    public static final FluidRegistryContainer OLD_PORT_ALE = register(Reference.UnlocalizedName.Fluid.OLD_PORT_ALE, Reference.FluidColor.OLD_PORT_ALE);
    public static final FluidRegistryContainer PALE_ALE = register(Reference.UnlocalizedName.Fluid.PALE_ALE, Reference.FluidColor.PALE_ALE);
    public static final FluidRegistryContainer PALE_GOLDEN_WORT = register(Reference.UnlocalizedName.Fluid.PALE_GOLDEN_WORT, Reference.FluidColor.PALE_GOLDEN_WORT);
    public static final FluidRegistryContainer PALE_LAGER = register(Reference.UnlocalizedName.Fluid.PALE_LAGER, Reference.FluidColor.PALE_LAGER);
    public static final FluidRegistryContainer PILSNER_LAGER = register(Reference.UnlocalizedName.Fluid.PILSNER_LAGER, Reference.FluidColor.PILSNER_LAGER);
    public static final FluidRegistryContainer PURPLE_GRAPE_JUICE = register(Reference.UnlocalizedName.Fluid.PURPLE_GRAPE_JUICE, Reference.FluidColor.PURPLE_GRAPE_JUICE);
    public static final FluidRegistryContainer PURPLE_GRAPE_WINE = register(Reference.UnlocalizedName.Fluid.PURPLE_GRAPE_WINE, Reference.FluidColor.PURPLE_GRAPE_WINE);
    public static final FluidRegistryContainer RED_GRAPE_JUICE = register(Reference.UnlocalizedName.Fluid.RED_GRAPE_JUICE, Reference.FluidColor.RED_GRAPE_JUICE);
    public static final FluidRegistryContainer RED_GRAPE_WINE = register(Reference.UnlocalizedName.Fluid.RED_GRAPE_WINE, Reference.FluidColor.RED_GRAPE_WINE);
    public static final FluidRegistryContainer WHITE_GRAPE_JUICE = register(Reference.UnlocalizedName.Fluid.WHITE_GRAPE_JUICE, Reference.FluidColor.WHITE_GRAPE_JUICE);
    public static final FluidRegistryContainer WHITE_GRAPE_WINE = register(Reference.UnlocalizedName.Fluid.WHITE_GRAPE_WINE, Reference.FluidColor.WHITE_GRAPE_WINE);
    public static final FluidRegistryContainer WORT = register(Reference.UnlocalizedName.Fluid.WORT, Reference.FluidColor.WORT);

    // Potion fluids (if used later)
    public static final FluidRegistryContainer POTION_ALE = register(Reference.UnlocalizedName.Fluid.POTION_ALE, Reference.FluidColor.PALE_ALE);
    public static final FluidRegistryContainer POTION_LAGER = register(Reference.UnlocalizedName.Fluid.POTION_LAGER, Reference.FluidColor.PALE_LAGER);
    public static final FluidRegistryContainer POTION_WINE = register(Reference.UnlocalizedName.Fluid.POTION_WINE, Reference.FluidColor.WHITE_GRAPE_WINE);

    private static FluidRegistryContainer register(String fluidName, ColorUtils.GrowthcraftColor color) {
        // Basic type properties for drink-like fluids
        FluidType.Properties typeProps = FluidType.Properties.create()
                .canDrown(true)
                .lightLevel(0);

        // Client visuals: use per-fluid textures under assets/growthcraft_cellar/block/fluid/{name}_*
        int tint = color.toIntValue();
        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, fluidName)
                .tint(tint);

        // Liquid block properties (no collision, strong resistance, no drops)
        BlockBehaviour.Properties blockProps = BlockBehaviour.Properties.of()
                .noCollission()
                .strength(100.0F)
                .noLootTable();

        // Bucket item: stack size 1
        Item.Properties itemProps = new Item.Properties().stacksTo(1);

        return new FluidRegistryContainer(
                fluidName,
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

    private GrowthcraftCellarFluids() {}
}
