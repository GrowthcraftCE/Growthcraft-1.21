package growthcraft.core.init;

import growthcraft.core.config.Reference;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Crowbar variants (16 colors)
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_WHITE, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_LIGHT_GRAY = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_GRAY, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_GRAY = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_GRAY, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_BLACK = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BLACK, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_BROWN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BROWN, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_RED = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_RED, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_ORANGE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_ORANGE, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_YELLOW = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_YELLOW, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_LIME = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIME, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_GREEN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_GREEN, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_CYAN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_CYAN, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_LIGHT_BLUE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_BLUE, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_BLUE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BLUE, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_PURPLE, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_MAGENTA = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_MAGENTA, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
    public static final DeferredItem<net.minecraft.world.item.Item> CROWBAR_PINK = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_PINK, () -> new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));
}
