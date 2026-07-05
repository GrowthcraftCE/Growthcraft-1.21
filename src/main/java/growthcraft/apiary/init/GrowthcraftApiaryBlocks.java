package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class GrowthcraftApiaryBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MODID);

    public static final DeferredBlock<Block> CANDLE_BLACK = candle(Reference.UnlocalizedName.CANDLE_BLACK);
    public static final DeferredBlock<Block> CANDLE_BLACK_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_BLACK_WALL);
    public static final DeferredBlock<Block> CANDLE_BLUE = candle(Reference.UnlocalizedName.CANDLE_BLUE);
    public static final DeferredBlock<Block> CANDLE_BLUE_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_BLUE_WALL);
    public static final DeferredBlock<Block> CANDLE_BROWN = candle(Reference.UnlocalizedName.CANDLE_BROWN);
    public static final DeferredBlock<Block> CANDLE_BROWN_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_BROWN_WALL);
    public static final DeferredBlock<Block> CANDLE_CYAN = candle(Reference.UnlocalizedName.CANDLE_CYAN);
    public static final DeferredBlock<Block> CANDLE_CYAN_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_CYAN_WALL);
    public static final DeferredBlock<Block> CANDLE_GRAY = candle(Reference.UnlocalizedName.CANDLE_GRAY);
    public static final DeferredBlock<Block> CANDLE_GRAY_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_GRAY_WALL);
    public static final DeferredBlock<Block> CANDLE_GREEN = candle(Reference.UnlocalizedName.CANDLE_GREEN);
    public static final DeferredBlock<Block> CANDLE_GREEN_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_GREEN_WALL);
    public static final DeferredBlock<Block> CANDLE_LIGHT_BLUE = candle(Reference.UnlocalizedName.CANDLE_LIGHT_BLUE);
    public static final DeferredBlock<Block> CANDLE_LIGHT_BLUE_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_LIGHT_BLUE_WALL);
    public static final DeferredBlock<Block> CANDLE_LIGHT_GRAY = candle(Reference.UnlocalizedName.CANDLE_LIGHT_GRAY);
    public static final DeferredBlock<Block> CANDLE_LIGHT_GRAY_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_LIGHT_GRAY_WALL);
    public static final DeferredBlock<Block> CANDLE_LIME = candle(Reference.UnlocalizedName.CANDLE_LIME);
    public static final DeferredBlock<Block> CANDLE_LIME_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_LIME_WALL);
    public static final DeferredBlock<Block> CANDLE_MAGENTA = candle(Reference.UnlocalizedName.CANDLE_MAGENTA);
    public static final DeferredBlock<Block> CANDLE_MAGENTA_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_MAGENTA_WALL);
    public static final DeferredBlock<Block> CANDLE_ORANGE = candle(Reference.UnlocalizedName.CANDLE_ORANGE);
    public static final DeferredBlock<Block> CANDLE_ORANGE_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_ORANGE_WALL);
    public static final DeferredBlock<Block> CANDLE_PINK = candle(Reference.UnlocalizedName.CANDLE_PINK);
    public static final DeferredBlock<Block> CANDLE_PINK_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_PINK_WALL);
    public static final DeferredBlock<Block> CANDLE_PURPLE = candle(Reference.UnlocalizedName.CANDLE_PURPLE);
    public static final DeferredBlock<Block> CANDLE_PURPLE_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_PURPLE_WALL);
    public static final DeferredBlock<Block> CANDLE_RED = candle(Reference.UnlocalizedName.CANDLE_RED);
    public static final DeferredBlock<Block> CANDLE_RED_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_RED_WALL);
    public static final DeferredBlock<Block> CANDLE_WHITE = candle(Reference.UnlocalizedName.CANDLE_WHITE);
    public static final DeferredBlock<Block> CANDLE_WHITE_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_WHITE_WALL);
    public static final DeferredBlock<Block> CANDLE_YELLOW = candle(Reference.UnlocalizedName.CANDLE_YELLOW);
    public static final DeferredBlock<Block> CANDLE_YELLOW_WALL = wallCandle(Reference.UnlocalizedName.CANDLE_YELLOW_WALL);

    public static final List<CandleBlocks> CANDLES = List.of(
            new CandleBlocks(CANDLE_BLACK, CANDLE_BLACK_WALL),
            new CandleBlocks(CANDLE_BLUE, CANDLE_BLUE_WALL),
            new CandleBlocks(CANDLE_BROWN, CANDLE_BROWN_WALL),
            new CandleBlocks(CANDLE_CYAN, CANDLE_CYAN_WALL),
            new CandleBlocks(CANDLE_GRAY, CANDLE_GRAY_WALL),
            new CandleBlocks(CANDLE_GREEN, CANDLE_GREEN_WALL),
            new CandleBlocks(CANDLE_LIGHT_BLUE, CANDLE_LIGHT_BLUE_WALL),
            new CandleBlocks(CANDLE_LIGHT_GRAY, CANDLE_LIGHT_GRAY_WALL),
            new CandleBlocks(CANDLE_LIME, CANDLE_LIME_WALL),
            new CandleBlocks(CANDLE_MAGENTA, CANDLE_MAGENTA_WALL),
            new CandleBlocks(CANDLE_ORANGE, CANDLE_ORANGE_WALL),
            new CandleBlocks(CANDLE_PINK, CANDLE_PINK_WALL),
            new CandleBlocks(CANDLE_PURPLE, CANDLE_PURPLE_WALL),
            new CandleBlocks(CANDLE_RED, CANDLE_RED_WALL),
            new CandleBlocks(CANDLE_WHITE, CANDLE_WHITE_WALL),
            new CandleBlocks(CANDLE_YELLOW, CANDLE_YELLOW_WALL)
    );

    private GrowthcraftApiaryBlocks() {
    }

    private static DeferredBlock<Block> candle(String name) {
        return BLOCKS.register(name, () -> new TorchBlock(ParticleTypes.FLAME, candleProperties()));
    }

    private static DeferredBlock<Block> wallCandle(String name) {
        return BLOCKS.register(name, () -> new WallTorchBlock(ParticleTypes.FLAME, candleProperties()));
    }

    private static BlockBehaviour.Properties candleProperties() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .instabreak()
                .lightLevel(state -> 14)
                .sound(SoundType.WOOD)
                .pushReaction(PushReaction.DESTROY);
    }

    public record CandleBlocks(DeferredBlock<Block> standing, DeferredBlock<Block> wall) {
    }
}
