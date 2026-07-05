package growthcraft.apples.init;

import growthcraft.apples.config.Reference;
import growthcraft.apples.item.AppleSeedsItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftApplesItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<BlockItem> APPLE_PLANK = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK, GrowthcraftApplesBlocks.APPLE_PLANK);
    public static final DeferredItem<BlockItem> APPLE_PLANK_BUTTON = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_BUTTON, GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON);
    public static final DeferredItem<BlockItem> APPLE_PLANK_DOOR = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_DOOR, GrowthcraftApplesBlocks.APPLE_PLANK_DOOR);
    public static final DeferredItem<BlockItem> APPLE_PLANK_FENCE = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE, GrowthcraftApplesBlocks.APPLE_PLANK_FENCE);
    public static final DeferredItem<BlockItem> APPLE_PLANK_FENCE_GATE = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_FENCE_GATE, GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE);
    public static final DeferredItem<BlockItem> APPLE_PLANK_PRESSURE_PLATE = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_PRESSURE_PLATE, GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> APPLE_PLANK_SLAB = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_SLAB, GrowthcraftApplesBlocks.APPLE_PLANK_SLAB);
    public static final DeferredItem<BlockItem> APPLE_PLANK_STAIRS = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_STAIRS, GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS);
    public static final DeferredItem<BlockItem> APPLE_PLANK_TRAPDOOR = blockItem(Reference.UnlocalizedName.Block.APPLE_PLANK_TRAPDOOR, GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR);
    public static final DeferredItem<BlockItem> APPLE_TREE_FRUIT = blockItem(Reference.UnlocalizedName.Block.APPLE_TREE_FRUIT, GrowthcraftApplesBlocks.APPLE_TREE_FRUIT);
    public static final DeferredItem<BlockItem> APPLE_TREE_LEAVES = blockItem(Reference.UnlocalizedName.Block.APPLE_TREE_LEAVES, GrowthcraftApplesBlocks.APPLE_TREE_LEAVES);
    public static final DeferredItem<BlockItem> APPLE_TREE_SAPLING = blockItem(Reference.UnlocalizedName.Block.APPLE_TREE_SAPLING, GrowthcraftApplesBlocks.APPLE_TREE_SAPLING);
    public static final DeferredItem<BlockItem> APPLE_WOOD = blockItem(Reference.UnlocalizedName.Block.APPLE_WOOD, GrowthcraftApplesBlocks.APPLE_WOOD);
    public static final DeferredItem<BlockItem> APPLE_WOOD_LOG = blockItem(Reference.UnlocalizedName.Block.APPLE_WOOD_LOG, GrowthcraftApplesBlocks.APPLE_WOOD_LOG);
    public static final DeferredItem<BlockItem> APPLE_WOOD_LOG_STRIPPED = blockItem(Reference.UnlocalizedName.Block.APPLE_WOOD_LOG_STRIPPED, GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED);
    public static final DeferredItem<BlockItem> APPLE_WOOD_STRIPPED = blockItem(Reference.UnlocalizedName.Block.APPLE_WOOD_STRIPPED, GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED);
    public static final DeferredItem<Item> APPLE_SEEDS = ITEMS.register(Reference.UnlocalizedName.Item.APPLE_SEEDS, () -> new AppleSeedsItem(new Item.Properties()));

    private GrowthcraftApplesItems() {}

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(APPLE_SEEDS.get(), 0.5F);
    }

    private static DeferredItem<BlockItem> blockItem(String name, net.neoforged.neoforge.registries.DeferredBlock<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
