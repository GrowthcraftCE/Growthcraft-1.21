package growthcraft.apiary.init;

import growthcraft.apiary.block.BeeBoxBlock;
import growthcraft.apiary.config.Reference;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GrowthcraftApiaryBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> BEE_BOX_ACACIA = beeBox(Reference.UnlocalizedName.BEE_BOX_ACACIA);
    public static final DeferredBlock<Block> BEE_BOX_BAMBOO = beeBox(Reference.UnlocalizedName.BEE_BOX_BAMBOO);
    public static final DeferredBlock<Block> BEE_BOX_BIRCH = beeBox(Reference.UnlocalizedName.BEE_BOX_BIRCH);
    public static final DeferredBlock<Block> BEE_BOX_CHERRY = beeBox(Reference.UnlocalizedName.BEE_BOX_CHERRY);
    public static final DeferredBlock<Block> BEE_BOX_CRIMSON = beeBox(Reference.UnlocalizedName.BEE_BOX_CRIMSON);
    public static final DeferredBlock<Block> BEE_BOX_DARK_OAK = beeBox(Reference.UnlocalizedName.BEE_BOX_DARK_OAK);
    public static final DeferredBlock<Block> BEE_BOX_JUNGLE = beeBox(Reference.UnlocalizedName.BEE_BOX_JUNGLE);
    public static final DeferredBlock<Block> BEE_BOX_MANGROVE = beeBox(Reference.UnlocalizedName.BEE_BOX_MANGROVE);
    public static final DeferredBlock<Block> BEE_BOX_OAK = beeBox(Reference.UnlocalizedName.BEE_BOX_OAK);
    public static final DeferredBlock<Block> BEE_BOX_SPRUCE = beeBox(Reference.UnlocalizedName.BEE_BOX_SPRUCE);
    public static final DeferredBlock<Block> BEE_BOX_WARPED = beeBox(Reference.UnlocalizedName.BEE_BOX_WARPED);

    public static final List<DeferredBlock<Block>> BEE_BOXES = List.of(
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

    private GrowthcraftApiaryBlocks() {
    }

    private static DeferredBlock<Block> beeBox(String name) {
        return BLOCKS.register(name, () -> new BeeBoxBlock(BeeBoxBlock.beeBoxProperties()));
    }
}
