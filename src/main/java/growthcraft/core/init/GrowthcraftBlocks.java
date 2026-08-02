package growthcraft.core.init;

import growthcraft.core.block.RopeBlock2;
import growthcraft.core.config.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    // Rope blocks
    public static final DeferredBlock<Block> ROPE_LINEN2 = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN + "2",
            RopeBlock2::new)
    ;

    // Salt blocks and ores
    public static final DeferredBlock<Block> SALT_BLOCK = BLOCKS.register(Reference.UnlocalizedName.Block.SALT_BLOCK,
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 6.0F)))
    ;

    public static final DeferredBlock<Block> SALT_ORE = BLOCKS.register(Reference.UnlocalizedName.Block.SALT_ORE,
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops()))
    ;

    public static final DeferredBlock<Block> SALT_ORE_DEEPSLATE = BLOCKS.register("salt_ore_deepslate",
            () -> new Block(BlockBehaviour.Properties.of().strength(4.5F, 3.0F).requiresCorrectToolForDrops()))
    ;

    public static final DeferredBlock<Block> SALT_ORE_NETHER = BLOCKS.register("salt_ore_nether",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops()))
    ;

    public static final DeferredBlock<Block> SALT_ORE_END = BLOCKS.register("salt_ore_end",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.0F, 3.0F).requiresCorrectToolForDrops()))
    ;
}
