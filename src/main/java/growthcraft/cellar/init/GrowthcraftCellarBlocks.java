package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.CorkCoasterBlock;
import growthcraft.cellar.block.CorkLogBlock;
import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.FermentationBarrelBlock;
import growthcraft.cellar.block.FruitPressBlock;
import growthcraft.cellar.block.FruitPressPistonBlock;
import growthcraft.cellar.block.GrapeVineStemBlock;
import growthcraft.cellar.block.GrapeVineFruitBlock;
import growthcraft.cellar.block.GrapeVineLeavesBlock;
import growthcraft.cellar.block.HopsCropBlock;
import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.world.CorkTreeGrowers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Block registrations for Growthcraft Cellar (MC 1.21 / NeoForge).
 */
public final class GrowthcraftCellarBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> BREW_KETTLE = BLOCKS.register(Reference.UnlocalizedName.Block.BREW_KETTLE, BrewKettleBlock::new);

    public static final DeferredBlock<Block> CULTURE_JAR = BLOCKS.register(Reference.UnlocalizedName.Block.CULTURE_JAR,
            () -> new CultureJarBlock(Block.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));

    public static final DeferredBlock<Block> FERMENTATION_BARREL_OAK = BLOCKS.register(Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK, FermentationBarrelBlock::new);
    public static final DeferredBlock<Block> FRUIT_PRESS = BLOCKS.register(Reference.UnlocalizedName.Block.FRUIT_PRESS, FruitPressBlock::new);
    public static final DeferredBlock<Block> FRUIT_PRESS_PISTON = BLOCKS.register(Reference.UnlocalizedName.Block.FRUIT_PRESS_PISTON, FruitPressPistonBlock::new);
    public static final DeferredBlock<Block> ROASTER = BLOCKS.register(Reference.UnlocalizedName.Block.ROASTER, RoasterBlock::new);
    public static final DeferredBlock<GrapeVineFruitBlock> PURPLE_GRAPE_VINE_FRUIT = BLOCKS.register(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE_FRUIT,
            () -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_PURPLE));
    public static final DeferredBlock<GrapeVineFruitBlock> RED_GRAPE_VINE_FRUIT = BLOCKS.register(Reference.UnlocalizedName.Block.RED_GRAPE_VINE_FRUIT,
            () -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_RED));
    public static final DeferredBlock<GrapeVineFruitBlock> WHITE_GRAPE_VINE_FRUIT = BLOCKS.register(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE_FRUIT,
            () -> new GrapeVineFruitBlock(GrowthcraftCellarItems.GRAPE_WHITE));
    public static final DeferredBlock<GrapeVineLeavesBlock> PURPLE_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesBlock(PURPLE_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_PURPLE));
    public static final DeferredBlock<GrapeVineLeavesBlock> RED_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.RED_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesBlock(RED_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_RED));
    public static final DeferredBlock<GrapeVineLeavesBlock> WHITE_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesBlock(WHITE_GRAPE_VINE_FRUIT, GrowthcraftCellarItems.GRAPE_SEEDS_WHITE));
    public static final DeferredBlock<GrapeVineStemBlock> PURPLE_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE,
            () -> new GrapeVineStemBlock(PURPLE_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_PURPLE));
    public static final DeferredBlock<GrapeVineStemBlock> RED_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.RED_GRAPE_VINE,
            () -> new GrapeVineStemBlock(RED_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_RED));
    public static final DeferredBlock<GrapeVineStemBlock> WHITE_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE,
            () -> new GrapeVineStemBlock(WHITE_GRAPE_VINE_LEAVES, GrowthcraftCellarItems.GRAPE_SEEDS_WHITE));
    public static final DeferredBlock<HopsCropBlock> HOPS_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.HOPS_VINE, HopsCropBlock::new);

    public static final DeferredBlock<Block> CORK_COASTER = BLOCKS.register(Reference.UnlocalizedName.Item.CORK_COASTER, () -> new CorkCoasterBlock());
    public static final DeferredBlock<LeavesBlock> CORK_TREE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_TREE_LEAVES,
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<SaplingBlock> CORK_TREE_SAPLING = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_TREE_SAPLING,
            () -> new SaplingBlock(CorkTreeGrowers.CORK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_WOOD,
            () -> new CorkLogBlock(corkWoodProperties()));
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_LOG = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_WOOD_LOG,
            () -> new CorkLogBlock(corkWoodProperties()));
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_LOG_STRIPPED = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_WOOD_LOG_STRIPPED,
            () -> new CorkLogBlock(corkWoodProperties()));
    public static final DeferredBlock<CorkLogBlock> CORK_WOOD_STRIPPED = BLOCKS.register(Reference.UnlocalizedName.Block.CORK_WOOD_STRIPPED,
            () -> new CorkLogBlock(corkWoodProperties()));

    private static BlockBehaviour.Properties corkWoodProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).randomTicks();
    }

    private GrowthcraftCellarBlocks() {}
}
