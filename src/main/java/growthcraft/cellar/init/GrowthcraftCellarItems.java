package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registrations for Growthcraft Cellar (MC 1.21 / NeoForge).
 */
public class GrowthcraftCellarItems {
    // Register items under the Cellar modid
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Grains (base + color variants)
    public static final DeferredItem<Item> GRAIN = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_AMBER = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_AMBER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_BROWN = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_BROWN, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_COPPER = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_COPPER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_DARK = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_DARK, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_DEEP_AMBER = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_DEEP_AMBER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_DEEP_COPPER = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_DEEP_COPPER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_GOLDEN = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_GOLDEN, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAIN_PALE_GOLDEN = ITEMS.register(Reference.UnlocalizedName.Item.GRAIN_PALE_GOLDEN, () -> new Item(new Item.Properties()));

    // Grapes and seeds
    public static final DeferredItem<Item> GRAPE_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_PURPLE, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAPE_RED = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_RED, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAPE_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_WHITE, () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GRAPE_SEEDS_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_PURPLE, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAPE_SEEDS_RED = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_RED, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAPE_SEEDS_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_WHITE, () -> new Item(new Item.Properties()));

    // Hops
    public static final DeferredItem<Item> HOPS = ITEMS.register(Reference.UnlocalizedName.Item.HOPS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOPS_SEEDS = ITEMS.register(Reference.UnlocalizedName.Item.HOPS_SEEDS, () -> new Item(new Item.Properties()));

    // Misc materials
    public static final DeferredItem<Item> KINDLING = ITEMS.register(Reference.UnlocalizedName.Item.KINDLING, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CORK_BARK = ITEMS.register(Reference.UnlocalizedName.Item.CORK_BARK, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CORK_COASTER = ITEMS.register(Reference.UnlocalizedName.Item.CORK_COASTER, () -> new Item(new Item.Properties()));

    // Yeasts
    public static final DeferredItem<Item> YEAST_BAYANUS = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BAYANUS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BAYANUS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BAYANUS_ETHEREAL, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS_ETHEREAL, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_ETHEREAL, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER_ETHEREAL, () -> new Item(new Item.Properties()));

    // Fluid Buckets (placeholder registrations; functional buckets will tie to fluids later)
    public static final DeferredItem<Item> AMBER_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.AMBER_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AMBER_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.AMBER_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AMBER_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.AMBER_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BROWN_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.BROWN_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BROWN_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.BROWN_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BROWN_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.BROWN_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.COPPER_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.COPPER_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COPPER_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.COPPER_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DARK_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.DARK_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DARK_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.DARK_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DEEP_AMBER_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.DEEP_AMBER_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DEEP_COPPER_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.DEEP_COPPER_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.GOLDEN_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOPPED_GOLDEN_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.HOPPED_GOLDEN_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IPA_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.IPA_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OLD_PORT_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.OLD_PORT_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PALE_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PALE_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PALE_GOLDEN_WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PALE_GOLDEN_WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PALE_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PALE_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PILSNER_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PILSNER_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURPLE_GRAPE_JUICE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PURPLE_GRAPE_JUICE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURPLE_GRAPE_WINE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.PURPLE_GRAPE_WINE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RED_GRAPE_JUICE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.RED_GRAPE_JUICE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RED_GRAPE_WINE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.RED_GRAPE_WINE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STOUT_ALE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.STOUT_ALE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VIENNA_LAGER_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.VIENNA_LAGER_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WHITE_GRAPE_JUICE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.WHITE_GRAPE_JUICE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WHITE_GRAPE_WINE_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.WHITE_GRAPE_WINE_FLUID_BUCKET, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> WORT_FLUID_BUCKET = ITEMS.register(Reference.UnlocalizedName.Item.WORT_FLUID_BUCKET, () -> new Item(new Item.Properties()));

    private GrowthcraftCellarItems() {}
}
