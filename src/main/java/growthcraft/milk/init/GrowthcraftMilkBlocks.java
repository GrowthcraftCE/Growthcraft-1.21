package growthcraft.milk.init;

import growthcraft.milk.block.CheesePressBlock;
import growthcraft.milk.block.CheeseCurdBlock;
import growthcraft.milk.block.CheeseWheelBlock;
import growthcraft.milk.block.ChurnBlock;
import growthcraft.milk.block.MixingVatBlock;
import growthcraft.milk.block.PancheonBlock;
import growthcraft.milk.block.ThistleCropBlock;
import growthcraft.milk.config.Reference;
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

    private static DeferredBlock<CheeseWheelBlock> registerCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese", CheeseWheelBlock::new);
    }

    private static DeferredBlock<CheeseWheelBlock> registerAgedCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_aged", CheeseWheelBlock::new);
    }

    private static DeferredBlock<CheeseWheelBlock> registerWaxedCheese(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_waxed", CheeseWheelBlock::new);
    }

    private static DeferredBlock<CheeseCurdBlock> registerCheeseCurds(String cheeseName) {
        return BLOCKS.register(cheeseName + "_cheese_curds", CheeseCurdBlock::new);
    }
}
