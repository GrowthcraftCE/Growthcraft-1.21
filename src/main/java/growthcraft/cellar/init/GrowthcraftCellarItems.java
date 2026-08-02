package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.item.CellarPotionItem;
import growthcraft.cellar.item.EtherealYeastItem;
import growthcraft.cellar.item.GrapeSeedsItem;
import growthcraft.cellar.item.HopsSeedsItem;
import growthcraft.lib.item.GrowthcraftFoodItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Item registrations for Growthcraft Cellar (MC 1.21 / NeoForge).
 */
public class GrowthcraftCellarItems {
    // Register items under the Cellar modid
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Blocks (BlockItems)
    public static final DeferredItem<Item> BREW_KETTLE = blockItem(Reference.UnlocalizedName.Block.BREW_KETTLE, GrowthcraftCellarBlocks.BREW_KETTLE);
    public static final DeferredItem<Item> CULTURE_JAR = ITEMS.register(Reference.UnlocalizedName.Block.CULTURE_JAR, () -> new BlockItem(GrowthcraftCellarBlocks.CULTURE_JAR.get(), new Item.Properties()));
    public static final DeferredItem<Item> FERMENTATION_BARREL_OAK = blockItem(Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK, GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK);
    public static final DeferredItem<Item> FRUIT_PRESS = blockItem(Reference.UnlocalizedName.Block.FRUIT_PRESS, GrowthcraftCellarBlocks.FRUIT_PRESS);
    public static final DeferredItem<Item> ROASTER = blockItem(Reference.UnlocalizedName.Block.ROASTER, GrowthcraftCellarBlocks.ROASTER);
    public static final DeferredItem<Item> CORK_COASTER = blockItem(Reference.UnlocalizedName.Item.CORK_COASTER, GrowthcraftCellarBlocks.CORK_COASTER);
    public static final DeferredItem<Item> CORK_TREE_LEAVES = blockItem(Reference.UnlocalizedName.Block.CORK_TREE_LEAVES, GrowthcraftCellarBlocks.CORK_TREE_LEAVES);
    public static final DeferredItem<Item> CORK_TREE_SAPLING = blockItem(Reference.UnlocalizedName.Block.CORK_TREE_SAPLING, GrowthcraftCellarBlocks.CORK_TREE_SAPLING);
    public static final DeferredItem<Item> CORK_WOOD = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD, GrowthcraftCellarBlocks.CORK_WOOD);
    public static final DeferredItem<Item> CORK_WOOD_LOG = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_LOG, GrowthcraftCellarBlocks.CORK_WOOD_LOG);
    public static final DeferredItem<Item> CORK_WOOD_LOG_STRIPPED = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_LOG_STRIPPED, GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED);
    public static final DeferredItem<Item> CORK_WOOD_STRIPPED = blockItem(Reference.UnlocalizedName.Block.CORK_WOOD_STRIPPED, GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED);

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
    public static final DeferredItem<Item> GRAPE_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_PURPLE, () -> new GrowthcraftFoodItem(1, 0.2F, 64));
    public static final DeferredItem<Item> GRAPE_RED = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_RED, () -> new GrowthcraftFoodItem(1, 0.2F, 64));
    public static final DeferredItem<Item> GRAPE_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_WHITE, () -> new GrowthcraftFoodItem(1, 0.2F, 64));

    public static final DeferredItem<Item> GRAPE_SEEDS_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_PURPLE,
            () -> new GrapeSeedsItem(new Item.Properties(), GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE));
    public static final DeferredItem<Item> GRAPE_SEEDS_RED = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_RED,
            () -> new GrapeSeedsItem(new Item.Properties(), GrowthcraftCellarBlocks.RED_GRAPE_VINE));
    public static final DeferredItem<Item> GRAPE_SEEDS_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.GRAPE_SEEDS_WHITE,
            () -> new GrapeSeedsItem(new Item.Properties(), GrowthcraftCellarBlocks.WHITE_GRAPE_VINE));

    // Hops
    public static final DeferredItem<Item> HOPS = ITEMS.register(Reference.UnlocalizedName.Item.HOPS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HOPS_SEEDS = ITEMS.register(Reference.UnlocalizedName.Item.HOPS_SEEDS, () -> new HopsSeedsItem(new Item.Properties()));

    // Misc materials
    public static final DeferredItem<Item> CORK_BARK = ITEMS.register(Reference.UnlocalizedName.Item.CORK_BARK, () -> new Item(new Item.Properties()));

    // Yeasts
    public static final DeferredItem<Item> YEAST_BAYANUS = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BAYANUS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BAYANUS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BAYANUS_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_BREWERS_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_BREWERS_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YEAST_LAGER_ETHEREAL = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_LAGER_ETHEREAL, () -> new EtherealYeastItem(new Item.Properties()));

    // Serving containers used by Fermentation Barrel recipes.
    public static final DeferredItem<Item> POTION_ALE = ITEMS.register(Reference.UnlocalizedName.Item.POTION_ALE, () -> new CellarPotionItem(new Item.Properties()));
    public static final DeferredItem<Item> POTION_LAGER = ITEMS.register(Reference.UnlocalizedName.Item.POTION_LAGER, () -> new CellarPotionItem(new Item.Properties()));
    public static final DeferredItem<Item> POTION_WINE = ITEMS.register(Reference.UnlocalizedName.Item.POTION_WINE, () -> new CellarPotionItem(new Item.Properties()));

    private static DeferredItem<Item> blockItem(String name, net.neoforged.neoforge.registries.DeferredBlock<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private GrowthcraftCellarItems() {}
}
