package growthcraft.cellar.item;

import growthcraft.cellar.block.GrapeVineCropBlock;
import growthcraft.core.block.RopeBlock;
import growthcraft.lib.block.GrowthcraftCropsRopeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GrapeSeedsItem extends Item {
    private final java.util.function.Supplier<? extends GrapeVineCropBlock> grapeVineCropBlock;

    public GrapeSeedsItem(Properties properties, java.util.function.Supplier<? extends GrapeVineCropBlock> grapeVineCropBlock) {
        super(properties);
        this.grapeVineCropBlock = grapeVineCropBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos plantPos = pos.above();
        Block block = level.getBlockState(pos).getBlock();

        if (block instanceof FarmBlock
                && level.getBlockState(plantPos).isAir()
                && RopeBlock.canConnect(level.getBlockState(pos.above(2)))) {
            if (!level.isClientSide) {
                BlockState state = grapeVineCropBlock.get().getActualBlockStateWithAge(level, plantPos, 0)
                        .setValue(GrowthcraftCropsRopeBlock.UP, true)
                        .setValue(GrowthcraftCropsRopeBlock.DOWN, false);
                level.setBlock(plantPos, state, Block.UPDATE_ALL);
                growthcraft.core.block.RopeBlock.refreshAdjacentConnections(level, plantPos);
                if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
