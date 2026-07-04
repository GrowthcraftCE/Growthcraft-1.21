package growthcraft.rice.init;

import growthcraft.rice.block.CultivatedFarmlandBlock;
import growthcraft.rice.block.RiceCropBlock;
import growthcraft.rice.config.Reference;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRiceBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<CultivatedFarmlandBlock> CULTIVATED_FARMLAND = BLOCKS.register(
            Reference.UnlocalizedName.Block.CULTIVATED_FARMLAND,
            CultivatedFarmlandBlock::new
    );
    public static final DeferredBlock<RiceCropBlock> RICE_CROP = BLOCKS.register(
            Reference.UnlocalizedName.Block.RICE_CROP,
            RiceCropBlock::new
    );

    private GrowthcraftRiceBlocks() {}
}
