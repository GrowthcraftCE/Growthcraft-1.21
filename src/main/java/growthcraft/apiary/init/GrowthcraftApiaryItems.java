package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GrowthcraftApiaryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<Item> BEES_WAX = register(Reference.UnlocalizedName.BEES_WAX);
    public static final DeferredItem<Item> BEES_WAX_BLACK = register(Reference.UnlocalizedName.BEES_WAX_BLACK);
    public static final DeferredItem<Item> BEES_WAX_BLUE = register(Reference.UnlocalizedName.BEES_WAX_BLUE);
    public static final DeferredItem<Item> BEES_WAX_BROWN = register(Reference.UnlocalizedName.BEES_WAX_BROWN);
    public static final DeferredItem<Item> BEES_WAX_CYAN = register(Reference.UnlocalizedName.BEES_WAX_CYAN);
    public static final DeferredItem<Item> BEES_WAX_GRAY = register(Reference.UnlocalizedName.BEES_WAX_GRAY);
    public static final DeferredItem<Item> BEES_WAX_GREEN = register(Reference.UnlocalizedName.BEES_WAX_GREEN);
    public static final DeferredItem<Item> BEES_WAX_LIGHT_BLUE = register(Reference.UnlocalizedName.BEES_WAX_LIGHT_BLUE);
    public static final DeferredItem<Item> BEES_WAX_LIGHT_GRAY = register(Reference.UnlocalizedName.BEES_WAX_LIGHT_GRAY);
    public static final DeferredItem<Item> BEES_WAX_LIME = register(Reference.UnlocalizedName.BEES_WAX_LIME);
    public static final DeferredItem<Item> BEES_WAX_MAGENTA = register(Reference.UnlocalizedName.BEES_WAX_MAGENTA);
    public static final DeferredItem<Item> BEES_WAX_ORANGE = register(Reference.UnlocalizedName.BEES_WAX_ORANGE);
    public static final DeferredItem<Item> BEES_WAX_PINK = register(Reference.UnlocalizedName.BEES_WAX_PINK);
    public static final DeferredItem<Item> BEES_WAX_PURPLE = register(Reference.UnlocalizedName.BEES_WAX_PURPLE);
    public static final DeferredItem<Item> BEES_WAX_RED = register(Reference.UnlocalizedName.BEES_WAX_RED);
    public static final DeferredItem<Item> BEES_WAX_WHITE = register(Reference.UnlocalizedName.BEES_WAX_WHITE);
    public static final DeferredItem<Item> BEES_WAX_YELLOW = register(Reference.UnlocalizedName.BEES_WAX_YELLOW);
    public static final DeferredItem<Item> HONEY_COMB_EMPTY = register(Reference.UnlocalizedName.HONEY_COMB_EMPTY);
    public static final DeferredItem<Item> HONEY_COMB_FULL = register(Reference.UnlocalizedName.HONEY_COMB_FULL);

    public static final List<DeferredItem<Item>> SIMPLE_ITEMS = List.of(
            BEES_WAX,
            BEES_WAX_BLACK,
            BEES_WAX_BLUE,
            BEES_WAX_BROWN,
            BEES_WAX_CYAN,
            BEES_WAX_GRAY,
            BEES_WAX_GREEN,
            BEES_WAX_LIGHT_BLUE,
            BEES_WAX_LIGHT_GRAY,
            BEES_WAX_LIME,
            BEES_WAX_MAGENTA,
            BEES_WAX_ORANGE,
            BEES_WAX_PINK,
            BEES_WAX_PURPLE,
            BEES_WAX_RED,
            BEES_WAX_WHITE,
            BEES_WAX_YELLOW,
            HONEY_COMB_EMPTY,
            HONEY_COMB_FULL
    );

    private GrowthcraftApiaryItems() {
    }

    private static DeferredItem<Item> register(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
}
