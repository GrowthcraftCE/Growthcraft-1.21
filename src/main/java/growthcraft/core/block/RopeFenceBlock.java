package growthcraft.core.block;

import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fence-like block that represents a vanilla fence with a rope overlay.
 *
 * It mirrors the connection logic of FenceBlock for the four horizontal directions
 * and adds extra boolean properties that are only used by our blockstate JSON to
 * render rope parts (UP/DOWN and KNOT). Geometry and collision are inherited from FenceBlock.
 */
public class RopeFenceBlock extends FenceBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty KNOT = BooleanProperty.create("knot");


    public RopeFenceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(NORTH, Boolean.FALSE)
            .setValue(EAST, Boolean.FALSE)
            .setValue(SOUTH, Boolean.FALSE)
            .setValue(WEST, Boolean.FALSE)
            .setValue(WATERLOGGED, Boolean.FALSE)
            .setValue(UP, Boolean.FALSE)
            .setValue(DOWN, Boolean.FALSE)
            .setValue(KNOT, Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UP, DOWN, KNOT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        // Default to having a knot when placed directly, can be adjusted by item logic
        state = state.setValue(KNOT, Boolean.TRUE);
        return state.setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        // Keep FenceBlock connection updates for horizontal directions and waterlogging
        state = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        // Optionally compute UP/DOWN rope connections: we keep it simple for now, off by default.
        if (direction == Direction.UP) {
            return state.setValue(UP, Boolean.FALSE);
        } else if (direction == Direction.DOWN) {
            return state.setValue(DOWN, Boolean.FALSE);
        }
        return state;
    }

    @Override
    public boolean connectsTo(BlockState neighborState, boolean neighborIsFullBlock, Direction side) {
        // Allow normal fence connections plus connect to RopeBlock
        if (neighborState.getBlock() instanceof RopeBlock) return true;
        return super.connectsTo(neighborState, neighborIsFullBlock, side);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        // Always drop one rope item and the underlying vanilla fence
        List<ItemStack> drops = new ArrayList<>(2);
        drops.add(new ItemStack(GrowthcraftItems.ROPE_LINEN.get()));

        Block self = state.getBlock();
        Block vanillaFence = getVanillaFenceFor(self);
        if (vanillaFence != null) {
            drops.add(new ItemStack(vanillaFence));
        }
        return drops;
    }

    public static Block getVanillaFenceFor(Block ropeFenceBlock) {
        // Map by comparing against our registered rope fence blocks; safe after registry binding
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get()) return Blocks.OAK_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get()) return Blocks.SPRUCE_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get()) return Blocks.BIRCH_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get()) return Blocks.JUNGLE_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get()) return Blocks.DARK_OAK_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get()) return Blocks.ACACIA_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get()) return Blocks.MANGROVE_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get()) return Blocks.CHERRY_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get()) return Blocks.BAMBOO_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get()) return Blocks.NETHER_BRICK_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get()) return Blocks.CRIMSON_FENCE;
        if (ropeFenceBlock == GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get()) return Blocks.WARPED_FENCE;
        return null;
    }
}
