package growthcraft.milk.init;

import growthcraft.lib.item.GrowthcraftBowlFoodItem;
import growthcraft.lib.item.GrowthcraftFoodItem;
import growthcraft.milk.config.Reference;
import growthcraft.milk.item.CheeseCurdsBlockItem;
import growthcraft.milk.item.CheeseCurdsDrainedItem;
import growthcraft.milk.item.GrowthcraftMilkBucketItem;
import growthcraft.milk.item.MilkingBucketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public final class GrowthcraftMilkItems {
    private GrowthcraftMilkItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Reference.MODID);

    public static final DeferredHolder<Item, Item> BUTTER =
            ITEMS.register(Reference.UnlocalizedName.BUTTER, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> BUTTER_SALTED =
            ITEMS.register(Reference.UnlocalizedName.BUTTER_SALTED, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CHEESE_CLOTH =
            ITEMS.register(Reference.UnlocalizedName.CHEESE_CLOTH, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STARTER_CULTURE =
            ITEMS.register(Reference.UnlocalizedName.STARTER_CULTURE, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STOMACH =
            ITEMS.register(Reference.UnlocalizedName.STOMACH, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> THISTLE =
            ITEMS.register(Reference.UnlocalizedName.THISTLE, () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, ItemNameBlockItem> THISTLE_SEED =
            ITEMS.register(Reference.UnlocalizedName.THISTLE_SEED, () -> new ItemNameBlockItem(
                    GrowthcraftMilkBlocks.THISTLE_CROP.get(),
                    new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> CHEESE_PRESS =
            registerBlockItem(Reference.UnlocalizedName.CHEESE_PRESS, GrowthcraftMilkBlocks.CHEESE_PRESS);
    public static final DeferredHolder<Item, BlockItem> CHURN =
            registerBlockItem(Reference.UnlocalizedName.CHURN, GrowthcraftMilkBlocks.CHURN);
    public static final DeferredHolder<Item, BlockItem> MIXING_VAT =
            registerBlockItem(Reference.UnlocalizedName.MIXING_VAT, GrowthcraftMilkBlocks.MIXING_VAT);
    public static final DeferredHolder<Item, BlockItem> PANCHEON =
            registerBlockItem(Reference.UnlocalizedName.PANCHEON, GrowthcraftMilkBlocks.PANCHEON);

    public static final DeferredHolder<Item, BlockItem> APPENZELLER_CHEESE =
            registerCheese(Reference.UnlocalizedName.APPENZELLER, GrowthcraftMilkBlocks.APPENZELLER_CHEESE);
    public static final DeferredHolder<Item, BlockItem> ASIAGO_CHEESE =
            registerCheese(Reference.UnlocalizedName.ASIAGO, GrowthcraftMilkBlocks.ASIAGO_CHEESE);
    public static final DeferredHolder<Item, BlockItem> CASU_MARZU_CHEESE =
            registerCheese(Reference.UnlocalizedName.CASU_MARZU, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE);
    public static final DeferredHolder<Item, BlockItem> CHEDDAR_CHEESE =
            registerCheese(Reference.UnlocalizedName.CHEDDAR, GrowthcraftMilkBlocks.CHEDDAR_CHEESE);
    public static final DeferredHolder<Item, BlockItem> EMMENTALER_CHEESE =
            registerCheese(Reference.UnlocalizedName.EMMENTALER, GrowthcraftMilkBlocks.EMMENTALER_CHEESE);
    public static final DeferredHolder<Item, BlockItem> GORGONZOLA_CHEESE =
            registerCheese(Reference.UnlocalizedName.GORGONZOLA, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE);
    public static final DeferredHolder<Item, BlockItem> GOUDA_CHEESE =
            registerCheese(Reference.UnlocalizedName.GOUDA, GrowthcraftMilkBlocks.GOUDA_CHEESE);
    public static final DeferredHolder<Item, BlockItem> MONTEREY_CHEESE =
            registerCheese(Reference.UnlocalizedName.MONTEREY, GrowthcraftMilkBlocks.MONTEREY_CHEESE);
    public static final DeferredHolder<Item, BlockItem> PARMESAN_CHEESE =
            registerCheese(Reference.UnlocalizedName.PARMESAN, GrowthcraftMilkBlocks.PARMESAN_CHEESE);
    public static final DeferredHolder<Item, BlockItem> PROVOLONE_CHEESE =
            registerCheese(Reference.UnlocalizedName.PROVOLONE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE);
    public static final DeferredHolder<Item, BlockItem> APPENZELLER_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.APPENZELLER, GrowthcraftMilkBlocks.APPENZELLER_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> ASIAGO_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.ASIAGO, GrowthcraftMilkBlocks.ASIAGO_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> CASU_MARZU_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.CASU_MARZU, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> CHEDDAR_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.CHEDDAR, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> EMMENTALER_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.EMMENTALER, GrowthcraftMilkBlocks.EMMENTALER_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> GORGONZOLA_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.GORGONZOLA, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> GOUDA_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.GOUDA, GrowthcraftMilkBlocks.GOUDA_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> MONTEREY_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.MONTEREY, GrowthcraftMilkBlocks.MONTEREY_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> PARMESAN_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.PARMESAN, GrowthcraftMilkBlocks.PARMESAN_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> PROVOLONE_CHEESE_AGED =
            registerAgedCheese(Reference.UnlocalizedName.PROVOLONE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_AGED);
    public static final DeferredHolder<Item, BlockItem> CHEDDAR_CHEESE_WAXED =
            registerWaxedCheese(Reference.UnlocalizedName.CHEDDAR, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_WAXED);
    public static final DeferredHolder<Item, BlockItem> GOUDA_CHEESE_WAXED =
            registerWaxedCheese(Reference.UnlocalizedName.GOUDA, GrowthcraftMilkBlocks.GOUDA_CHEESE_WAXED);
    public static final DeferredHolder<Item, BlockItem> MONTEREY_CHEESE_WAXED =
            registerWaxedCheese(Reference.UnlocalizedName.MONTEREY, GrowthcraftMilkBlocks.MONTEREY_CHEESE_WAXED);
    public static final DeferredHolder<Item, BlockItem> PROVOLONE_CHEESE_WAXED =
            registerWaxedCheese(Reference.UnlocalizedName.PROVOLONE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_WAXED);

    public static final DeferredHolder<Item, CheeseCurdsBlockItem> APPENZELLER_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.APPENZELLER, GrowthcraftMilkBlocks.APPENZELLER_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> APPENZELLER_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> ASIAGO_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.ASIAGO, GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> ASIAGO_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> CASU_MARZU_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.CASU_MARZU, GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> CASU_MARZU_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> CHEDDAR_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.CHEDDAR, GrowthcraftMilkBlocks.CHEDDAR_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> CHEDDAR_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> EMMENTALER_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.EMMENTALER, GrowthcraftMilkBlocks.EMMENTALER_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> EMMENTALER_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> GORGONZOLA_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.GORGONZOLA, GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> GORGONZOLA_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> GOUDA_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.GOUDA, GrowthcraftMilkBlocks.GOUDA_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> GOUDA_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.GOUDA);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> MONTEREY_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.MONTEREY, GrowthcraftMilkBlocks.MONTEREY_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> MONTEREY_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> PARMESAN_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.PARMESAN, GrowthcraftMilkBlocks.PARMESAN_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> PARMESAN_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> PROVOLONE_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.PROVOLONE, GrowthcraftMilkBlocks.PROVOLONE_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> PROVOLONE_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredHolder<Item, CheeseCurdsBlockItem> RICOTTA_CHEESE_CURDS =
            registerCheeseCurds(Reference.UnlocalizedName.RICOTTA, GrowthcraftMilkBlocks.RICOTTA_CHEESE_CURDS);
    public static final DeferredHolder<Item, CheeseCurdsDrainedItem> RICOTTA_CHEESE_CURDS_DRAINED =
            registerDrainedCheeseCurds(Reference.UnlocalizedName.RICOTTA);

    public static final DeferredHolder<Item, GrowthcraftFoodItem> APPENZELLER_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> ASIAGO_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> CASU_MARZU_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> CHEDDAR_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> EMMENTALER_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> GORGONZOLA_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> GOUDA_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.GOUDA);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> MONTEREY_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> PARMESAN_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredHolder<Item, GrowthcraftFoodItem> PROVOLONE_CHEESE_SLICE =
            registerCheeseSlice(Reference.UnlocalizedName.PROVOLONE);

    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> RICOTTA_CHEESE_SLICE =
            registerBowlFood(Reference.UnlocalizedName.RICOTTA + "_cheese_slice", 4, 0.4F, 16);

    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_APPLE =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_APPLE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_CHOCOLATE =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_CHOCOLATE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_GRAPE_PURPLE =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_GRAPE_PURPLE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_GRAPE_RED =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_GRAPE_RED, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_GRAPE_WHITE =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_GRAPE_WHITE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_HONEY =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_HONEY, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_PUMPKIN =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_PUMPKIN, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> ICE_CREAM_WATERMELON =
            registerBowlFood(Reference.UnlocalizedName.ICE_CREAM_WATERMELON, 6, 0.5F, 8);

    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_APPLE =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_APPLE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_CHOCOLATE =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_CHOCOLATE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_GRAPE_PURPLE =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_GRAPE_PURPLE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_GRAPE_RED =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_GRAPE_RED, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_GRAPE_WHITE =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_GRAPE_WHITE, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_HONEY =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_HONEY, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_PLAIN =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_PLAIN, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_PUMPKIN =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_PUMPKIN, 6, 0.5F, 8);
    public static final DeferredHolder<Item, GrowthcraftBowlFoodItem> YOGURT_WATERMELON =
            registerBowlFood(Reference.UnlocalizedName.YOGURT_WATERMELON, 6, 0.5F, 8);

    // The reusable milking bucket tools (empty). Different materials may have different textures/recipes.
    public static final DeferredHolder<Item, MilkingBucketItem> MILKING_BUCKET_IRON =
            ITEMS.register(Reference.UnlocalizedName.MILKING_BUCKET_IRON, () -> new MilkingBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));


    // Filled milk bucket variants (both point to the same milk fluid)
    public static final DeferredHolder<Item, GrowthcraftMilkBucketItem> MILK_BUCKET_IRON =
            ITEMS.register("milk_fluid_bucket", () -> new GrowthcraftMilkBucketItem(
                    GrowthcraftMilkFluids.MILK.source.get(),
                    () -> MILKING_BUCKET_IRON.get(),
                    new Item.Properties().stacksTo(1)));

    private static DeferredHolder<Item, GrowthcraftBowlFoodItem> registerBowlFood(String name, int nutrition, float saturationModifier, int maxStackSize) {
        return ITEMS.register(name, () -> new GrowthcraftBowlFoodItem(nutrition, saturationModifier, maxStackSize));
    }

    private static DeferredHolder<Item, GrowthcraftFoodItem> registerCheeseSlice(String cheeseName) {
        return ITEMS.register(cheeseName + "_cheese_slice", () -> new GrowthcraftFoodItem(8, 0.5F, 64));
    }

    private static DeferredHolder<Item, BlockItem> registerCheese(String cheeseName, DeferredBlock<?> block) {
        return ITEMS.register(cheeseName + "_cheese", () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, BlockItem> registerAgedCheese(String cheeseName, DeferredBlock<?> block) {
        return ITEMS.register(cheeseName + "_cheese_aged", () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, BlockItem> registerWaxedCheese(String cheeseName, DeferredBlock<?> block) {
        return ITEMS.register(cheeseName + "_cheese_waxed", () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, CheeseCurdsBlockItem> registerCheeseCurds(String cheeseName, DeferredBlock<?> block) {
        return ITEMS.register(cheeseName + "_cheese_curds", () -> new CheeseCurdsBlockItem(block.get()));
    }

    private static DeferredHolder<Item, CheeseCurdsDrainedItem> registerDrainedCheeseCurds(String cheeseName) {
        return ITEMS.register(cheeseName + "_cheese_curds_drained", CheeseCurdsDrainedItem::new);
    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<?> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /////////////////////////////////////////////////////////////////////////

    private static List<CheeseEntry> cheeseRegistry = null; // on first request; item won't be created right away.

    public static List<CheeseEntry> getCheeseRegistry()
    {
        if (cheeseRegistry == null)
        {
            cheeseRegistry = new ArrayList<>();
            cheeseRegistry.add(new CheeseEntry(APPENZELLER_CHEESE.get(), null, APPENZELLER_CHEESE_AGED.get(), APPENZELLER_CHEESE_CURDS.get(),
                    APPENZELLER_CHEESE_CURDS_DRAINED.get(), APPENZELLER_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.APPENZELLER_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(ASIAGO_CHEESE.get(), null, ASIAGO_CHEESE_AGED.get(), ASIAGO_CHEESE_CURDS.get(),
                    ASIAGO_CHEESE_CURDS_DRAINED.get(), ASIAGO_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.ASIAGO_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(CASU_MARZU_CHEESE.get(), null, CASU_MARZU_CHEESE_AGED.get(), CASU_MARZU_CHEESE_CURDS.get(),
                    CASU_MARZU_CHEESE_CURDS_DRAINED.get(), CASU_MARZU_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.CASU_MARZU_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(CHEDDAR_CHEESE.get(), CHEDDAR_CHEESE_WAXED.get(), CHEDDAR_CHEESE_AGED.get(), CHEDDAR_CHEESE_CURDS.get(),
                    CHEDDAR_CHEESE_CURDS_DRAINED.get(), CHEDDAR_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.CHEDDAR_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(EMMENTALER_CHEESE.get(), null, EMMENTALER_CHEESE_AGED.get(), EMMENTALER_CHEESE_CURDS.get(),
                    EMMENTALER_CHEESE_CURDS_DRAINED.get(), EMMENTALER_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.EMMENTALER_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(GORGONZOLA_CHEESE.get(), null, GORGONZOLA_CHEESE_AGED.get(), GORGONZOLA_CHEESE_CURDS.get(),
                    GORGONZOLA_CHEESE_CURDS_DRAINED.get(), GORGONZOLA_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.GORGONZOLA_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(GOUDA_CHEESE.get(), GOUDA_CHEESE_WAXED.get(), GOUDA_CHEESE_AGED.get(), GOUDA_CHEESE_CURDS.get(),
                    GOUDA_CHEESE_CURDS_DRAINED.get(), GOUDA_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.GOUDA_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(MONTEREY_CHEESE.get(), MONTEREY_CHEESE_WAXED.get(), MONTEREY_CHEESE_AGED.get(), MONTEREY_CHEESE_CURDS.get(),
                    MONTEREY_CHEESE_CURDS_DRAINED.get(), MONTEREY_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.MONTEREY_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(PARMESAN_CHEESE.get(), null, PARMESAN_CHEESE_AGED.get(), PARMESAN_CHEESE_CURDS.get(),
                    PARMESAN_CHEESE_CURDS_DRAINED.get(), PARMESAN_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.PARMESAN_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(PROVOLONE_CHEESE.get(), PROVOLONE_CHEESE_WAXED.get(), PROVOLONE_CHEESE_AGED.get(), PROVOLONE_CHEESE_CURDS.get(),
                    PROVOLONE_CHEESE_CURDS_DRAINED.get(), PROVOLONE_CHEESE_SLICE.get(), GrowthcraftMilkBlocks.PROVOLONE_CHEESE.get().getWaxingItem()));
            cheeseRegistry.add(new CheeseEntry(null, null, null, RICOTTA_CHEESE_CURDS.get(),
                    RICOTTA_CHEESE_CURDS_DRAINED.get(), RICOTTA_CHEESE_SLICE.get(), null));
        }
        return cheeseRegistry;
    }

    public record CheeseEntry(Item unprocessed, Item waxed, Item aged, Item curds, Item drainedCurds, Item slice, Item waxingItem)   {}
}
