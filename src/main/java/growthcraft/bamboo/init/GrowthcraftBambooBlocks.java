package growthcraft.bamboo.init;

import growthcraft.bamboo.block.BambooPostBlock;
import growthcraft.bamboo.config.Reference;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftBambooBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<BambooPostBlock> BAMBOO_POST_VERTICAL = BLOCKS.register(
            Reference.UnlocalizedName.Block.BAMBOO_POST_VERTICAL,
            BambooPostBlock::new
    );
    public static final DeferredBlock<BambooPostBlock> BAMBOO_POST_HORIZONTAL = BLOCKS.register(
            Reference.UnlocalizedName.Block.BAMBOO_POST_HORIZONTAL,
            BambooPostBlock::new
    );

    private GrowthcraftBambooBlocks() {}
}
