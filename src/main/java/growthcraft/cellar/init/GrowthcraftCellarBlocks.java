package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.FermentationBarrelBlock;
import growthcraft.cellar.block.FruitPressBlock;
import growthcraft.cellar.block.FruitPressPistonBlock;
import growthcraft.cellar.block.GrapeVineCropBlock;
import growthcraft.cellar.block.GrapeVineFruitBlock;
import growthcraft.cellar.block.GrapeVineLeavesCropBlock;
import growthcraft.cellar.block.HopsCropBlock;
import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
    public static final DeferredBlock<GrapeVineLeavesCropBlock> PURPLE_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesCropBlock(PURPLE_GRAPE_VINE_FRUIT));
    public static final DeferredBlock<GrapeVineLeavesCropBlock> RED_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.RED_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesCropBlock(RED_GRAPE_VINE_FRUIT));
    public static final DeferredBlock<GrapeVineLeavesCropBlock> WHITE_GRAPE_VINE_LEAVES = BLOCKS.register(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE_LEAVES,
            () -> new GrapeVineLeavesCropBlock(WHITE_GRAPE_VINE_FRUIT));
    public static final DeferredBlock<GrapeVineCropBlock> PURPLE_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.PURPLE_GRAPE_VINE,
            () -> new GrapeVineCropBlock(PURPLE_GRAPE_VINE_LEAVES));
    public static final DeferredBlock<GrapeVineCropBlock> RED_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.RED_GRAPE_VINE,
            () -> new GrapeVineCropBlock(RED_GRAPE_VINE_LEAVES));
    public static final DeferredBlock<GrapeVineCropBlock> WHITE_GRAPE_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.WHITE_GRAPE_VINE,
            () -> new GrapeVineCropBlock(WHITE_GRAPE_VINE_LEAVES));
    public static final DeferredBlock<HopsCropBlock> HOPS_VINE = BLOCKS.register(Reference.UnlocalizedName.Block.HOPS_VINE, HopsCropBlock::new);

    private GrowthcraftCellarBlocks() {}
}
