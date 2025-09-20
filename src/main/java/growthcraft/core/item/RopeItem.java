package growthcraft.core.item;

import growthcraft.core.block.RopeBlock;
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


/**
 * When used on a vanilla fence block, converts it to a rope fence variant of matching wood type.
 */
public class RopeItem extends Item {

    public RopeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockState target = level.getBlockState(pos);
        Block targetBlock = target.getBlock();

        if (!(targetBlock instanceof FenceBlock)) {
            // Try placing a rope block adjacent to the clicked face
            BlockPos placePos = pos.relative(context.getClickedFace());
            BlockState existing = level.getBlockState(placePos);
            if (!existing.isAir()) {
                return InteractionResult.PASS;
            }
            Block ropeBlock = GrowthcraftBlocks.ROPE_LINEN.get();
            BlockState ropeState = ropeBlock.defaultBlockState()
                    .setValue(RopeBlock.KNOT, Boolean.TRUE);
            // Waterlogging
            if (level.getFluidState(placePos).isSource()) {
                ropeState = ropeState.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
            }
            // Compute horizontal connections to adjacent rope blocks or rope fences
            for (Direction dir : new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
                BlockPos np = placePos.relative(dir);
                BlockState ns = level.getBlockState(np);
                Block nb = ns.getBlock();
                boolean connect = (nb instanceof growthcraft.core.block.RopeBlock) || (nb instanceof RopeFenceBlock);
                if (dir == Direction.NORTH && ropeState.hasProperty(FenceBlock.NORTH)) ropeState = ropeState.setValue(FenceBlock.NORTH, connect);
                if (dir == Direction.EAST && ropeState.hasProperty(FenceBlock.EAST)) ropeState = ropeState.setValue(FenceBlock.EAST, connect);
                if (dir == Direction.SOUTH && ropeState.hasProperty(FenceBlock.SOUTH)) ropeState = ropeState.setValue(FenceBlock.SOUTH, connect);
                if (dir == Direction.WEST && ropeState.hasProperty(FenceBlock.WEST)) ropeState = ropeState.setValue(FenceBlock.WEST, connect);
            }
            level.setBlock(placePos, ropeState, Block.UPDATE_ALL);
            if (!context.getPlayer().isCreative()) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        Block ropeFence = mapFenceToRope(targetBlock);
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

    private static Block mapFenceToRope(Block vanilla) {
        if (vanilla == Blocks.OAK_FENCE) return GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get();
        if (vanilla == Blocks.SPRUCE_FENCE) return GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get();
        if (vanilla == Blocks.BIRCH_FENCE) return GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get();
        if (vanilla == Blocks.JUNGLE_FENCE) return GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get();
        if (vanilla == Blocks.DARK_OAK_FENCE) return GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get();
        if (vanilla == Blocks.ACACIA_FENCE) return GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get();
        if (vanilla == Blocks.MANGROVE_FENCE) return GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get();
        if (vanilla == Blocks.CHERRY_FENCE) return GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get();
        if (vanilla == Blocks.BAMBOO_FENCE) return GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get();
        if (vanilla == Blocks.NETHER_BRICK_FENCE) return GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get();
        if (vanilla == Blocks.CRIMSON_FENCE) return GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get();
        if (vanilla == Blocks.WARPED_FENCE) return GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get();
        return null;
    }
}
