package growthcraft.apples.block;

import com.mojang.serialization.MapCodec;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

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
        if (!level.isAreaLoaded(pos, APPLE_CHECK_AREA + 1) || !level.getBlockState(pos.below()).isAir()) {
            return;
        }

        tryGrowAppleFruit(level, pos, random);
    }

    private void tryGrowAppleFruit(ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos lowerBound = pos.below().south(APPLE_CHECK_AREA).west(APPLE_CHECK_AREA);
        BlockPos upperBound = pos.below().north(APPLE_CHECK_AREA).east(APPLE_CHECK_AREA);
        int applesInArea = 0;
        List<BlockPos> validGrowthPositions = new ArrayList<>();

        for (BlockPos candidate : BlockPos.betweenClosed(lowerBound, upperBound)) {
            BlockPos growthPos = candidate.immutable();
            if (level.getBlockState(growthPos).is(GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get())) {
                applesInArea++;
            } else if (level.getBlockState(growthPos).isAir() && level.getBlockState(growthPos.above()).is(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get())) {
                validGrowthPositions.add(growthPos);
            }
        }

        if (applesInArea < MAX_APPLES_IN_AREA && !validGrowthPositions.isEmpty()) {
            BlockPos spawnPos = validGrowthPositions.get(random.nextInt(validGrowthPositions.size()));
            level.setBlock(spawnPos, GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get().defaultBlockState(), 2);
        }
    }
}
