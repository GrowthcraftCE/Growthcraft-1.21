package growthcraft.milk.init;

import growthcraft.milk.block.CheeseCurdBlock;
import growthcraft.milk.block.ThistleCropBlock;
import growthcraft.milk.config.Reference;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkBlocks {
    private GrowthcraftMilkBlocks() {}

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<ThistleCropBlock> THISTLE_CROP = BLOCKS.register(
            Reference.UnlocalizedName.THISTLE_CROP,
            ThistleCropBlock::new
    );
    public static final DeferredBlock<CheeseCurdBlock> RICOTTA_CHEESE_CURDS = BLOCKS.register(
            Reference.UnlocalizedName.RICOTTA + "_cheese_curds",
            CheeseCurdBlock::new
    );
}
