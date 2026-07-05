package growthcraft.apples.init;

import growthcraft.apples.block.AppleTreeFruitBlock;
import growthcraft.apples.block.AppleTreeLeavesBlock;
import growthcraft.apples.config.Reference;
import growthcraft.apples.world.AppleTreeGrowers;
import growthcraft.core.block.RopeFenceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftApplesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> APPLE_PLANK = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK,
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<ButtonBlock> APPLE_PLANK_BUTTON = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_BUTTON,
            () -> new ButtonBlock(BlockSetType.OAK, 30, BlockBehaviour.Properties.of().noCollission().strength(0.5F))
    );
    public static final DeferredBlock<DoorBlock> APPLE_PLANK_DOOR = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_DOOR,
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(3.0F).noOcclusion())
    );
    public static final DeferredBlock<FenceBlock> APPLE_PLANK_FENCE = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE,
            () -> new FenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<FenceGateBlock> APPLE_PLANK_FENCE_GATE = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_GATE,
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<RopeFenceBlock> APPLE_PLANK_FENCE_ROPE_LINEN = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_ROPE_LINEN,
            () -> new RopeFenceBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<PressurePlateBlock> APPLE_PLANK_PRESSURE_PLATE = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_PRESSURE_PLATE,
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(0.5F))
    );
    public static final DeferredBlock<SlabBlock> APPLE_PLANK_SLAB = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_SLAB,
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<StairBlock> APPLE_PLANK_STAIRS = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_STAIRS,
            () -> new StairBlock(APPLE_PLANK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(2.0F, 3.0F))
    );
    public static final DeferredBlock<TrapDoorBlock> APPLE_PLANK_TRAPDOOR = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_PLANK_TRAPDOOR,
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.of().strength(3.0F).noOcclusion())
    );
    public static final DeferredBlock<AppleTreeFruitBlock> APPLE_TREE_FRUIT = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_TREE_FRUIT,
            () -> new AppleTreeFruitBlock(AppleTreeFruitBlock.fruitProperties())
    );
    public static final DeferredBlock<AppleTreeLeavesBlock> APPLE_TREE_LEAVES = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_TREE_LEAVES,
            () -> new AppleTreeLeavesBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.OAK_LEAVES))
    );
    public static final DeferredBlock<SaplingBlock> APPLE_TREE_SAPLING = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_TREE_SAPLING,
            () -> new SaplingBlock(AppleTreeGrowers.APPLE, BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.OAK_SAPLING))
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_WOOD,
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0F))
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_LOG = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_WOOD_LOG,
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0F))
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_LOG_STRIPPED = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_WOOD_LOG_STRIPPED,
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0F))
    );
    public static final DeferredBlock<RotatedPillarBlock> APPLE_WOOD_STRIPPED = BLOCKS.register(
            Reference.UnlocalizedName.Block.APPLE_WOOD_STRIPPED,
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().strength(2.0F))
    );
    private GrowthcraftApplesBlocks() {}
}
