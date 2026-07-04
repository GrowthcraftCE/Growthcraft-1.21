package growthcraft.rice.init;

import growthcraft.lib.item.GrowthcraftBowlFoodItem;
import growthcraft.lib.item.GrowthcraftFoodItem;
import growthcraft.rice.config.Reference;
import growthcraft.rice.item.CultivatorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRiceItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<BlockItem> CULTIVATED_FARMLAND = ITEMS.register(
            Reference.UnlocalizedName.Block.CULTIVATED_FARMLAND,
            () -> new BlockItem(GrowthcraftRiceBlocks.CULTIVATED_FARMLAND.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> RICE_CROP = ITEMS.register(
            Reference.UnlocalizedName.Block.RICE_CROP,
            () -> new BlockItem(GrowthcraftRiceBlocks.RICE_CROP.get(), new Item.Properties()));
    public static final DeferredItem<CultivatorItem> CULTIVATOR = ITEMS.register(Reference.UnlocalizedName.Item.CULTIVATOR, CultivatorItem::new);
    public static final DeferredItem<Item> KNIFE = ITEMS.register(Reference.UnlocalizedName.Item.KNIFE, () -> new Item(new Item.Properties()));
    public static final DeferredItem<ItemNameBlockItem> RICE_GRAINS = ITEMS.register(
            Reference.UnlocalizedName.Item.RICE_GRAINS,
            () -> new ItemNameBlockItem(GrowthcraftRiceBlocks.RICE_CROP.get(), new Item.Properties()));
    public static final DeferredItem<Item> RICE = ITEMS.register(Reference.UnlocalizedName.Item.RICE, () -> new Item(new Item.Properties()));
    public static final DeferredItem<GrowthcraftFoodItem> RICE_COOKED = ITEMS.register(Reference.UnlocalizedName.Item.RICE_COOKED, () -> new GrowthcraftFoodItem(6, 0.4F, 64));
    public static final DeferredItem<Item> RICE_STALK = ITEMS.register(Reference.UnlocalizedName.Item.RICE_STALK, () -> new Item(new Item.Properties()));
    public static final DeferredItem<GrowthcraftFoodItem> SUSHI_ROLL = ITEMS.register(Reference.UnlocalizedName.Item.SUSHI_ROLL, () -> new GrowthcraftFoodItem(3, 0.4F, 64));
    public static final DeferredItem<GrowthcraftFoodItem> ONIGIRI = ITEMS.register(Reference.UnlocalizedName.Item.ONIGIRI, () -> new GrowthcraftFoodItem(8, 0.5F, 64));
    public static final DeferredItem<GrowthcraftBowlFoodItem> CHICKEN_RICE = ITEMS.register(Reference.UnlocalizedName.Item.CHICKEN_RICE, () -> new GrowthcraftBowlFoodItem(12, 0.8F, 8));
    public static final DeferredItem<Item> YEAST_SEISHU = ITEMS.register(Reference.UnlocalizedName.Item.YEAST_SEISHU, () -> new Item(new Item.Properties()));

    private GrowthcraftRiceItems() {}
}
