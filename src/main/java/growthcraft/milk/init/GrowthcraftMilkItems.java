package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import growthcraft.milk.item.GrowthcraftMilkBucketItem;
import growthcraft.milk.item.MilkingBucketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

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

    // The reusable milking bucket tools (empty). Different materials may have different textures/recipes.
    public static final DeferredHolder<Item, MilkingBucketItem> MILKING_BUCKET_IRON =
            ITEMS.register(Reference.UnlocalizedName.MILKING_BUCKET_IRON, () -> new MilkingBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));


    // Filled milk bucket variants (both point to the same milk fluid)
    public static final DeferredHolder<Item, GrowthcraftMilkBucketItem> MILK_BUCKET_IRON =
            ITEMS.register("milk_fluid_bucket", () -> new GrowthcraftMilkBucketItem(
                    GrowthcraftMilkFluids.MILK.source.get(),
                    () -> MILKING_BUCKET_IRON.get(),
                    new Item.Properties().stacksTo(1)));

}
