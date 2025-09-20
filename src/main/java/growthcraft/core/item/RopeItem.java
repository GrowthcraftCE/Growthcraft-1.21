package growthcraft.core.item;

import growthcraft.core.block.RopeFenceBlock;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * When used on a vanilla fence block, converts it to a rope fence variant of matching wood type.
 */
public class RopeItem extends Item {
    private static final Map<Block, Block> FENCE_TO_ROPE = new HashMap<>();

    public RopeItem(Properties properties) {
        super(properties);
        // Lazy map init when class loads
        if (FENCE_TO_ROPE.isEmpty()) {
            FENCE_TO_ROPE.put(Blocks.OAK_FENCE, GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.SPRUCE_FENCE, GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.BIRCH_FENCE, GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.JUNGLE_FENCE, GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.DARK_OAK_FENCE, GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.ACACIA_FENCE, GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.MANGROVE_FENCE, GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.CHERRY_FENCE, GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.BAMBOO_FENCE, GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.NETHER_BRICK_FENCE, GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.CRIMSON_FENCE, GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get());
            FENCE_TO_ROPE.put(Blocks.WARPED_FENCE, GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get());
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockState target = level.getBlockState(pos);
        Block targetBlock = target.getBlock();

        if (!(targetBlock instanceof FenceBlock)) {
            return InteractionResult.PASS;
        }
        Block ropeFence = FENCE_TO_ROPE.get(targetBlock);
        if (ropeFence == null) {
            return InteractionResult.PASS;
        }

        // Build new state mirroring fence connections and waterlogged
        BlockState newState = ropeFence.defaultBlockState()
                .setValue(RopeFenceBlock.KNOT, Boolean.TRUE);

        // Copy connections if present in original
        if (target.hasProperty(FenceBlock.NORTH)) newState = newState.setValue(FenceBlock.NORTH, target.getValue(FenceBlock.NORTH));
        if (target.hasProperty(FenceBlock.EAST)) newState = newState.setValue(FenceBlock.EAST, target.getValue(FenceBlock.EAST));
        if (target.hasProperty(FenceBlock.SOUTH)) newState = newState.setValue(FenceBlock.SOUTH, target.getValue(FenceBlock.SOUTH));
        if (target.hasProperty(FenceBlock.WEST)) newState = newState.setValue(FenceBlock.WEST, target.getValue(FenceBlock.WEST));
        if (target.hasProperty(BlockStateProperties.WATERLOGGED)) newState = newState.setValue(BlockStateProperties.WATERLOGGED, target.getValue(BlockStateProperties.WATERLOGGED));

        level.setBlock(pos, newState, Block.UPDATE_ALL);

        if (!context.getPlayer().isCreative()) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
