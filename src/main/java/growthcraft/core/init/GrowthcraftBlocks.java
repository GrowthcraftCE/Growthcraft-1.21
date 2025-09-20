package growthcraft.core.init;

import growthcraft.core.block.RopeFenceBlock;
import growthcraft.core.config.Reference;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

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

    // Rope Linen Fence variants
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_OAK_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_OAK_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_SPRUCE_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_SPRUCE_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_BIRCH_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_BIRCH_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_JUNGLE_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_JUNGLE_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_DARK_OAK_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_DARK_OAK_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_ACACIA_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_ACACIA_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_MANGROVE_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_MANGROVE_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_CHERRY_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_CHERRY_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_BAMBOO_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_BAMBOO_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_NETHER_BRICK_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_NETHER_BRICK_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 6.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_CRIMSON_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_CRIMSON_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
    public static final DeferredBlock<RopeFenceBlock> ROPE_LINEN_WARPED_FENCE = BLOCKS.register(Reference.UnlocalizedName.Block.ROPE_LINEN_WARPED_FENCE,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F)))
    ;
}
