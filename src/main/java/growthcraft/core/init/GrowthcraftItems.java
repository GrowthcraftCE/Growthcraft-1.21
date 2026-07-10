package growthcraft.core.init;

import growthcraft.core.config.Reference;
import growthcraft.core.item.CrowbarItem;
import growthcraft.core.item.OffsetTier;
import growthcraft.core.item.RopeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    // Simple materials / tools
    public static final DeferredItem<Item> SALT = ITEMS.register(Reference.UnlocalizedName.Item.SALT, () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ROPE_LINEN = ITEMS.register(Reference.UnlocalizedName.Item.ROPE_LINEN, () -> new RopeItem(new Item.Properties()));

    // BlockItems
    public static final DeferredItem<Item> SALT_BLOCK = ITEMS.register(Reference.UnlocalizedName.Block.SALT_BLOCK, () -> new BlockItem(GrowthcraftBlocks.SALT_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_ORE = ITEMS.register(Reference.UnlocalizedName.Block.SALT_ORE, () -> new BlockItem(GrowthcraftBlocks.SALT_ORE.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_ORE_DEEPSLATE = ITEMS.register("salt_ore_deepslate", () -> new BlockItem(GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_ORE_NETHER = ITEMS.register("salt_ore_nether", () -> new BlockItem(GrowthcraftBlocks.SALT_ORE_NETHER.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_ORE_END = ITEMS.register("salt_ore_end", () -> new BlockItem(GrowthcraftBlocks.SALT_ORE_END.get(), new Item.Properties()));

    public static final DeferredItem<Item> CROWBAR_WHITE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_WHITE, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_LIGHT_GRAY = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_GRAY, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_GRAY = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_GRAY, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_BLACK = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BLACK, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_BROWN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BROWN, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_RED = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_RED, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_ORANGE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_ORANGE, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_YELLOW = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_YELLOW, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_LIME = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIME, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_GREEN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_GREEN, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_CYAN = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_CYAN, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_LIGHT_BLUE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_LIGHT_BLUE, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_BLUE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_BLUE, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_PURPLE = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_PURPLE, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_MAGENTA = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_MAGENTA, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> CROWBAR_PINK = ITEMS.register(Reference.UnlocalizedName.Item.CROWBAR_PINK, () -> new CrowbarItem(OffsetTier.IRON_MINUS2, crowbarProps()));
    public static final DeferredItem<Item> WRENCH = ITEMS.register(Reference.UnlocalizedName.Item.WRENCH, () -> new Item(new Item.Properties().stacksTo(1)));

    // Crowbar variants (16 colors) - behave like iron swords (damage/speed/durability)
    private static Item.Properties crowbarProps() {
        return new Item.Properties().durability(Tiers.IRON.getUses());
    }
}
