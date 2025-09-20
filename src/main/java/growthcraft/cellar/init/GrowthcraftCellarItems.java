package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.item.EtherealYeastItem;
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
    public static final DeferredItem<Item> YEAST_BAYANUS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BAYANUS_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));


    private GrowthcraftCellarItems() {}
}
