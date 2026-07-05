package growthcraft.milk.init;

import growthcraft.apiary.init.GrowthcraftApiaryItems;
import growthcraft.milk.block.CheesePressBlock;
import growthcraft.milk.block.CheeseCurdBlock;
import growthcraft.milk.block.CheeseWheelBlock;
import growthcraft.milk.block.ChurnBlock;
import growthcraft.milk.block.MixingVatBlock;
import growthcraft.milk.block.PancheonBlock;
import growthcraft.milk.block.ThistleCropBlock;
import growthcraft.milk.block.signs.ShopCeilingHangingSignBlock;
import growthcraft.milk.block.signs.ShopWallHangingSignBlock;
import growthcraft.milk.config.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkBlocks {
    private GrowthcraftMilkBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<CheesePressBlock> CHEESE_PRESS = BLOCKS.register(Reference.UnlocalizedName.CHEESE_PRESS, CheesePressBlock::new);
    public static final DeferredBlock<ChurnBlock> CHURN = BLOCKS.register(Reference.UnlocalizedName.CHURN, ChurnBlock::new);
    public static final DeferredBlock<MixingVatBlock> MIXING_VAT = BLOCKS.register(Reference.UnlocalizedName.MIXING_VAT, MixingVatBlock::new);
    public static final DeferredBlock<PancheonBlock> PANCHEON = BLOCKS.register(Reference.UnlocalizedName.PANCHEON, PancheonBlock::new);

    public static final DeferredBlock<CheeseWheelBlock> APPENZELLER_CHEESE = registerCheese(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseWheelBlock> ASIAGO_CHEESE = registerCheese(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseWheelBlock> CASU_MARZU_CHEESE = registerCheese(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE = registerCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> EMMENTALER_CHEESE = registerCheese(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseWheelBlock> GORGONZOLA_CHEESE = registerCheese(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE = registerCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE = registerCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PARMESAN_CHEESE = registerCheese(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE = registerCheese(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseWheelBlock> APPENZELLER_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseWheelBlock> ASIAGO_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseWheelBlock> CASU_MARZU_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> EMMENTALER_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseWheelBlock> GORGONZOLA_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PARMESAN_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE_AGED = registerAgedCheese(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseWheelBlock> CHEDDAR_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseWheelBlock> GOUDA_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseWheelBlock> MONTEREY_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseWheelBlock> PROVOLONE_CHEESE_WAXED = registerWaxedCheese(Reference.UnlocalizedName.PROVOLONE);

    public static final DeferredBlock<CheeseCurdBlock> APPENZELLER_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.APPENZELLER);
    public static final DeferredBlock<CheeseCurdBlock> ASIAGO_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.ASIAGO);
    public static final DeferredBlock<CheeseCurdBlock> CASU_MARZU_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.CASU_MARZU);
    public static final DeferredBlock<CheeseCurdBlock> CHEDDAR_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.CHEDDAR);
    public static final DeferredBlock<CheeseCurdBlock> EMMENTALER_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.EMMENTALER);
    public static final DeferredBlock<CheeseCurdBlock> GORGONZOLA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.GORGONZOLA);
    public static final DeferredBlock<CheeseCurdBlock> GOUDA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.GOUDA);
    public static final DeferredBlock<CheeseCurdBlock> MONTEREY_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.MONTEREY);
    public static final DeferredBlock<CheeseCurdBlock> PARMESAN_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.PARMESAN);
    public static final DeferredBlock<CheeseCurdBlock> PROVOLONE_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.PROVOLONE);
    public static final DeferredBlock<CheeseCurdBlock> RICOTTA_CHEESE_CURDS = registerCheeseCurds(Reference.UnlocalizedName.RICOTTA);

    public static final DeferredBlock<ThistleCropBlock> THISTLE_CROP = BLOCKS.register(
            Reference.UnlocalizedName.THISTLE_CROP,
            ThistleCropBlock::new
    );
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_OAK = BLOCKS.register(
            Reference.UnlocalizedName.HANGING_SIGN_1_OAK,
            () -> new ShopCeilingHangingSignBlock(WoodType.OAK, hangingSignProperties(), (SignBlock) Blocks.OAK_HANGING_SIGN));
    public static final DeferredBlock<CeilingHangingSignBlock> HANGING_SIGN_1_SPRUCE = BLOCKS.register(
            Reference.UnlocalizedName.HANGING_SIGN_1_SPRUCE,
            () -> new ShopCeilingHangingSignBlock(WoodType.SPRUCE, hangingSignProperties(), (SignBlock) Blocks.SPRUCE_HANGING_SIGN));
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_OAK = BLOCKS.register(
            Reference.UnlocalizedName.HANGING_SIGN_2_OAK,
            () -> new ShopWallHangingSignBlock(WoodType.OAK, hangingSignProperties(), (SignBlock) Blocks.OAK_WALL_HANGING_SIGN));
    public static final DeferredBlock<WallHangingSignBlock> HANGING_SIGN_2_SPRUCE = BLOCKS.register(
            Reference.UnlocalizedName.HANGING_SIGN_2_SPRUCE,
            () -> new ShopWallHangingSignBlock(WoodType.SPRUCE, hangingSignProperties(), (SignBlock) Blocks.SPRUCE_WALL_HANGING_SIGN));

    private static DeferredBlock<CheeseWheelBlock> registerCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese", () -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                false,
                getFreshAgedBlock(cheeseName),
                getWaxItem(cheeseName),
                getWaxedBlock(cheeseName)));
    }

    private static DeferredBlock<CheeseWheelBlock> registerAgedCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_aged", () -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                true,
                () -> null,
                () -> null,
                () -> null));
    }

    private static DeferredBlock<CheeseWheelBlock> registerWaxedCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_waxed", () -> new CheeseWheelBlock(
                getSliceItem(cheeseName),
                true,
                getAgedBlock(cheeseName),
                () -> null,
                () -> null));
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getFreshAgedBlock(String cheeseName) {
        return switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR,
                 Reference.UnlocalizedName.GOUDA,
                 Reference.UnlocalizedName.MONTEREY,
                 Reference.UnlocalizedName.PROVOLONE -> () -> null;
            default -> getAgedBlock(cheeseName);
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getAgedBlock(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.APPENZELLER -> GrowthcraftMilkBlocks.APPENZELLER_CHEESE_AGED.get();
            case Reference.UnlocalizedName.ASIAGO -> GrowthcraftMilkBlocks.ASIAGO_CHEESE_AGED.get();
            case Reference.UnlocalizedName.CASU_MARZU -> GrowthcraftMilkBlocks.CASU_MARZU_CHEESE_AGED.get();
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkBlocks.CHEDDAR_CHEESE_AGED.get();
            case Reference.UnlocalizedName.EMMENTALER -> GrowthcraftMilkBlocks.EMMENTALER_CHEESE_AGED.get();
            case Reference.UnlocalizedName.GORGONZOLA -> GrowthcraftMilkBlocks.GORGONZOLA_CHEESE_AGED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkBlocks.GOUDA_CHEESE_AGED.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkBlocks.MONTEREY_CHEESE_AGED.get();
            case Reference.UnlocalizedName.PARMESAN -> GrowthcraftMilkBlocks.PARMESAN_CHEESE_AGED.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkBlocks.PROVOLONE_CHEESE_AGED.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.level.block.Block> getWaxedBlock(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkBlocks.CHEDDAR_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkBlocks.GOUDA_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkBlocks.MONTEREY_CHEESE_WAXED.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkBlocks.PROVOLONE_CHEESE_WAXED.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.item.Item> getWaxItem(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftApiaryItems.BEES_WAX_RED.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftApiaryItems.BEES_WAX.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftApiaryItems.BEES_WAX_BLACK.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftApiaryItems.BEES_WAX_WHITE.get();
            default -> null;
        };
    }

    private static java.util.function.Supplier<? extends net.minecraft.world.item.Item> getSliceItem(String cheeseName) {
        return () -> switch (cheeseName) {
            case Reference.UnlocalizedName.APPENZELLER -> GrowthcraftMilkItems.APPENZELLER_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.ASIAGO -> GrowthcraftMilkItems.ASIAGO_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.CASU_MARZU -> GrowthcraftMilkItems.CASU_MARZU_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.CHEDDAR -> GrowthcraftMilkItems.CHEDDAR_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.EMMENTALER -> GrowthcraftMilkItems.EMMENTALER_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.GORGONZOLA -> GrowthcraftMilkItems.GORGONZOLA_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.GOUDA -> GrowthcraftMilkItems.GOUDA_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.MONTEREY -> GrowthcraftMilkItems.MONTEREY_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.PARMESAN -> GrowthcraftMilkItems.PARMESAN_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.PROVOLONE -> GrowthcraftMilkItems.PROVOLONE_CHEESE_SLICE.get();
            case Reference.UnlocalizedName.RICOTTA -> GrowthcraftMilkItems.RICOTTA_CHEESE_SLICE.get();
            default -> null;
        };
    }

    private static DeferredBlock<CheeseCurdBlock> registerCheeseCurds(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_curds", CheeseCurdBlock::new);
    }

    private static BlockBehaviour.Properties hangingSignProperties() {
        return BlockBehaviour.Properties.of()
                .forceSolidOn()
                .noCollission()
                .strength(1.0F)
                .ignitedByLava();
    }

    public static Block getShopSignFromOriginal(SignBlock original) {
        if (original == Blocks.OAK_HANGING_SIGN) return HANGING_SIGN_1_OAK.get();
        if (original == Blocks.SPRUCE_HANGING_SIGN) return HANGING_SIGN_1_SPRUCE.get();
        if (original == Blocks.OAK_WALL_HANGING_SIGN) return HANGING_SIGN_2_OAK.get();
        if (original == Blocks.SPRUCE_WALL_HANGING_SIGN) return HANGING_SIGN_2_SPRUCE.get();
        return null;
    }
}
