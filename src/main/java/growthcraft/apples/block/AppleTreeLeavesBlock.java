package growthcraft.apples.block;

import com.mojang.serialization.MapCodec;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AppleTreeLeavesBlock extends LeavesBlock {
    public static final MapCodec<AppleTreeLeavesBlock> CODEC = simpleCodec(AppleTreeLeavesBlock::new);
    private static final int APPLE_CHECK_AREA = 3;
    private static final int MAX_APPLES_IN_AREA = 2;

    public AppleTreeLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        // The vanilla random tick may have decayed this leaf.
        if (!level.getBlockState(pos).is(this) || !level.isAreaLoaded(pos, APPLE_CHECK_AREA + 1)
                || !level.getBlockState(pos.below()).isAir()) {
            return;
        }

        tryGrowAppleFruit(level, pos);
    }

    private void tryGrowAppleFruit(ServerLevel level, BlockPos pos) {
        BlockPos lowerBound = pos.below().south(APPLE_CHECK_AREA).west(APPLE_CHECK_AREA);
        BlockPos upperBound = pos.below().north(APPLE_CHECK_AREA).east(APPLE_CHECK_AREA);
        int applesInArea = 0;

        for (BlockPos candidate : BlockPos.betweenClosed(lowerBound, upperBound)) {
            BlockPos growthPos = candidate.immutable();
            if (level.getBlockState(growthPos).is(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get())) {
                applesInArea++;
            }
        }

        // Only the randomly ticked leaf may grow fruit; nearby leaves must not refill its space.
        if (applesInArea < MAX_APPLES_IN_AREA) {
            level.setBlock(pos.below(), GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get().defaultBlockState(), 2);
        }
    }

}
