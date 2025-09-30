package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import growthcraft.milk.item.MilkingBucketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkItems {
    private GrowthcraftMilkItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Reference.MODID);

    // The reusable milking bucket tools (empty). Different materials may have different textures/recipes.
    public static final net.neoforged.neoforge.registries.DeferredHolder<Item, MilkingBucketItem> MILKING_BUCKET_IRON =
            ITEMS.register(Reference.UnlocalizedName.MILKING_BUCKET_IRON, () -> new MilkingBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));

    public static final net.neoforged.neoforge.registries.DeferredHolder<Item, MilkingBucketItem> MILKING_BUCKET_COPPER =
            ITEMS.register(Reference.UnlocalizedName.MILKING_BUCKET_COPPER, () -> new MilkingBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));
}
