package growthcraft.cellar.block;

import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.Nullable;

public class CorkLogBlock extends RotatedPillarBlock {
    public static final BooleanProperty REGROW = BooleanProperty.create("regrow");

    public CorkLogBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(REGROW, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(AXIS, REGROW);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis()).setValue(REGROW, false);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.is(GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get()) && state.getValue(REGROW);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(13) == 0) {
            level.setBlockAndUpdate(pos, GrowthcraftCellarBlocks.CORK_WOOD_LOG.get().defaultBlockState()
                    .setValue(AXIS, state.getValue(AXIS))
                    .setValue(REGROW, true));
        }
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (itemAbility == ItemAbilities.AXE_STRIP && context.getItemInHand().canPerformAction(itemAbility)) {
            if (state.is(GrowthcraftCellarBlocks.CORK_WOOD_LOG.get())) {
                dropCorkBark(context, simulate);
                return GrowthcraftCellarBlocks.CORK_WOOD_LOG_STRIPPED.get().defaultBlockState()
                        .setValue(AXIS, state.getValue(AXIS))
                        .setValue(REGROW, state.getValue(REGROW));
            }

            if (state.is(GrowthcraftCellarBlocks.CORK_WOOD.get())) {
                dropCorkBark(context, simulate);
                return GrowthcraftCellarBlocks.CORK_WOOD_STRIPPED.get().defaultBlockState()
                        .setValue(AXIS, state.getValue(AXIS))
                        .setValue(REGROW, state.getValue(REGROW));
            }
        }

        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    private static void dropCorkBark(UseOnContext context, boolean simulate) {
        if (!simulate && context.getLevel() instanceof ServerLevel) {
            popResource(context.getLevel(), context.getClickedPos().relative(context.getClickedFace()), new ItemStack(GrowthcraftCellarItems.CORK_BARK.get()));
        }
    }
}
