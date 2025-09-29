package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import growthcraft.milk.item.MilkingBucketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GrowthcraftMilkItems {
    private GrowthcraftMilkItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Reference.MODID);

    // The reusable milking bucket tool (empty).
    public static final net.neoforged.neoforge.registries.DeferredHolder<Item, MilkingBucketItem> MILKING_BUCKET =
            ITEMS.register("milking_bucket", () -> new MilkingBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16)));
}
