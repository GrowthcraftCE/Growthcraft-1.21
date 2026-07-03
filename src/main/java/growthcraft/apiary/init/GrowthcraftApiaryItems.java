package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GrowthcraftApiaryItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<Item> BEE = register(Reference.UnlocalizedName.BEE);
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
    public static final DeferredItem<BlockItem> BEE_BOX_ACACIA = blockItem(Reference.UnlocalizedName.BEE_BOX_ACACIA, GrowthcraftApiaryBlocks.BEE_BOX_ACACIA);
    public static final DeferredItem<BlockItem> BEE_BOX_BAMBOO = blockItem(Reference.UnlocalizedName.BEE_BOX_BAMBOO, GrowthcraftApiaryBlocks.BEE_BOX_BAMBOO);
    public static final DeferredItem<BlockItem> BEE_BOX_BIRCH = blockItem(Reference.UnlocalizedName.BEE_BOX_BIRCH, GrowthcraftApiaryBlocks.BEE_BOX_BIRCH);
    public static final DeferredItem<BlockItem> BEE_BOX_CHERRY = blockItem(Reference.UnlocalizedName.BEE_BOX_CHERRY, GrowthcraftApiaryBlocks.BEE_BOX_CHERRY);
    public static final DeferredItem<BlockItem> BEE_BOX_CRIMSON = blockItem(Reference.UnlocalizedName.BEE_BOX_CRIMSON, GrowthcraftApiaryBlocks.BEE_BOX_CRIMSON);
    public static final DeferredItem<BlockItem> BEE_BOX_DARK_OAK = blockItem(Reference.UnlocalizedName.BEE_BOX_DARK_OAK, GrowthcraftApiaryBlocks.BEE_BOX_DARK_OAK);
    public static final DeferredItem<BlockItem> BEE_BOX_JUNGLE = blockItem(Reference.UnlocalizedName.BEE_BOX_JUNGLE, GrowthcraftApiaryBlocks.BEE_BOX_JUNGLE);
    public static final DeferredItem<BlockItem> BEE_BOX_MANGROVE = blockItem(Reference.UnlocalizedName.BEE_BOX_MANGROVE, GrowthcraftApiaryBlocks.BEE_BOX_MANGROVE);
    public static final DeferredItem<BlockItem> BEE_BOX_OAK = blockItem(Reference.UnlocalizedName.BEE_BOX_OAK, GrowthcraftApiaryBlocks.BEE_BOX_OAK);
    public static final DeferredItem<BlockItem> BEE_BOX_SPRUCE = blockItem(Reference.UnlocalizedName.BEE_BOX_SPRUCE, GrowthcraftApiaryBlocks.BEE_BOX_SPRUCE);
    public static final DeferredItem<BlockItem> BEE_BOX_WARPED = blockItem(Reference.UnlocalizedName.BEE_BOX_WARPED, GrowthcraftApiaryBlocks.BEE_BOX_WARPED);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_BLACK = candleItem(Reference.UnlocalizedName.CANDLE_BLACK, GrowthcraftApiaryBlocks.CANDLE_BLACK, GrowthcraftApiaryBlocks.CANDLE_BLACK_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_BLUE = candleItem(Reference.UnlocalizedName.CANDLE_BLUE, GrowthcraftApiaryBlocks.CANDLE_BLUE, GrowthcraftApiaryBlocks.CANDLE_BLUE_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_BROWN = candleItem(Reference.UnlocalizedName.CANDLE_BROWN, GrowthcraftApiaryBlocks.CANDLE_BROWN, GrowthcraftApiaryBlocks.CANDLE_BROWN_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_CYAN = candleItem(Reference.UnlocalizedName.CANDLE_CYAN, GrowthcraftApiaryBlocks.CANDLE_CYAN, GrowthcraftApiaryBlocks.CANDLE_CYAN_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_GRAY = candleItem(Reference.UnlocalizedName.CANDLE_GRAY, GrowthcraftApiaryBlocks.CANDLE_GRAY, GrowthcraftApiaryBlocks.CANDLE_GRAY_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_GREEN = candleItem(Reference.UnlocalizedName.CANDLE_GREEN, GrowthcraftApiaryBlocks.CANDLE_GREEN, GrowthcraftApiaryBlocks.CANDLE_GREEN_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_LIGHT_BLUE = candleItem(Reference.UnlocalizedName.CANDLE_LIGHT_BLUE, GrowthcraftApiaryBlocks.CANDLE_LIGHT_BLUE, GrowthcraftApiaryBlocks.CANDLE_LIGHT_BLUE_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_LIGHT_GRAY = candleItem(Reference.UnlocalizedName.CANDLE_LIGHT_GRAY, GrowthcraftApiaryBlocks.CANDLE_LIGHT_GRAY, GrowthcraftApiaryBlocks.CANDLE_LIGHT_GRAY_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_LIME = candleItem(Reference.UnlocalizedName.CANDLE_LIME, GrowthcraftApiaryBlocks.CANDLE_LIME, GrowthcraftApiaryBlocks.CANDLE_LIME_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_MAGENTA = candleItem(Reference.UnlocalizedName.CANDLE_MAGENTA, GrowthcraftApiaryBlocks.CANDLE_MAGENTA, GrowthcraftApiaryBlocks.CANDLE_MAGENTA_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_ORANGE = candleItem(Reference.UnlocalizedName.CANDLE_ORANGE, GrowthcraftApiaryBlocks.CANDLE_ORANGE, GrowthcraftApiaryBlocks.CANDLE_ORANGE_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_PINK = candleItem(Reference.UnlocalizedName.CANDLE_PINK, GrowthcraftApiaryBlocks.CANDLE_PINK, GrowthcraftApiaryBlocks.CANDLE_PINK_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_PURPLE = candleItem(Reference.UnlocalizedName.CANDLE_PURPLE, GrowthcraftApiaryBlocks.CANDLE_PURPLE, GrowthcraftApiaryBlocks.CANDLE_PURPLE_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_RED = candleItem(Reference.UnlocalizedName.CANDLE_RED, GrowthcraftApiaryBlocks.CANDLE_RED, GrowthcraftApiaryBlocks.CANDLE_RED_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_WHITE = candleItem(Reference.UnlocalizedName.CANDLE_WHITE, GrowthcraftApiaryBlocks.CANDLE_WHITE, GrowthcraftApiaryBlocks.CANDLE_WHITE_WALL);
    public static final DeferredItem<StandingAndWallBlockItem> CANDLE_YELLOW = candleItem(Reference.UnlocalizedName.CANDLE_YELLOW, GrowthcraftApiaryBlocks.CANDLE_YELLOW, GrowthcraftApiaryBlocks.CANDLE_YELLOW_WALL);
    public static final DeferredItem<Item> HONEY_COMB_EMPTY = register(Reference.UnlocalizedName.HONEY_COMB_EMPTY);
    public static final DeferredItem<Item> HONEY_COMB_FULL = register(Reference.UnlocalizedName.HONEY_COMB_FULL);

    public static final List<DeferredItem<BlockItem>> BEE_BOX_ITEMS = List.of(
            BEE_BOX_ACACIA,
            BEE_BOX_BAMBOO,
            BEE_BOX_BIRCH,
            BEE_BOX_CHERRY,
            BEE_BOX_CRIMSON,
            BEE_BOX_DARK_OAK,
            BEE_BOX_JUNGLE,
            BEE_BOX_MANGROVE,
            BEE_BOX_OAK,
            BEE_BOX_SPRUCE,
            BEE_BOX_WARPED
    );

    public static final List<DeferredItem<StandingAndWallBlockItem>> CANDLE_ITEMS = List.of(
            CANDLE_BLACK,
            CANDLE_BLUE,
            CANDLE_BROWN,
            CANDLE_CYAN,
            CANDLE_GRAY,
            CANDLE_GREEN,
            CANDLE_LIGHT_BLUE,
            CANDLE_LIGHT_GRAY,
            CANDLE_LIME,
            CANDLE_MAGENTA,
            CANDLE_ORANGE,
            CANDLE_PINK,
            CANDLE_PURPLE,
            CANDLE_RED,
            CANDLE_WHITE,
            CANDLE_YELLOW
    );

    public static final List<DeferredItem<Item>> SIMPLE_ITEMS = List.of(
            BEE,
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

    private static DeferredItem<BlockItem> blockItem(String name, net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredItem<StandingAndWallBlockItem> candleItem(
            String name,
            net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> block,
            net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> wallBlock
    ) {
        return ITEMS.register(name, () -> new StandingAndWallBlockItem(block.get(), wallBlock.get(), new Item.Properties(), Direction.DOWN));
    }
}
