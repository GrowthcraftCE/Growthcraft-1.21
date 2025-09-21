package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
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

    public static final DeferredBlock<Block> CULTURE_JAR = BLOCKS.register(Reference.UnlocalizedName.Block.CULTURE_JAR,
            () -> new CultureJarBlock(Block.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(0.3F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));

    private GrowthcraftCellarBlocks() {}
}
