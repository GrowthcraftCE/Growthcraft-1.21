package growthcraft.milk.block;

import growthcraft.milk.block.entity.ChurnBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class ChurnBlock extends Block implements EntityBlock {
    public static final BooleanProperty PLUNGED = BooleanProperty.create("plunged");
    private static final VoxelShape SHAPE_UP = Shapes.or(
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.box(3.0D, 5.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(7.0D, 4.0D, 7.0D, 9.0D, 28.0D, 9.0D));
    private static final VoxelShape SHAPE_DOWN = Shapes.or(
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 5.0D, 14.0D),
            Block.box(3.0D, 5.0D, 3.0D, 13.0D, 10.0D, 13.0D),
            Block.box(4.0D, 10.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(7.0D, 4.0D, 7.0D, 9.0D, 20.0D, 9.0D));

    public ChurnBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(PLUNGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLUNGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PLUNGED) ? SHAPE_DOWN : SHAPE_UP;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof ChurnBlockEntity churn)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide && churn.collectByProduct(player)) {
            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide) {
            togglePlunger(level, pos, state, churn);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!(level.getBlockEntity(pos) instanceof ChurnBlockEntity churn)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide && churn.collectByProduct(player)) {
            return ItemInteractionResult.CONSUME;
        }

        if (!level.isClientSide) {
            togglePlunger(level, pos, state, churn);
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.SUCCESS;
    }

    private static void togglePlunger(Level level, BlockPos pos, BlockState state, ChurnBlockEntity churn) {
        boolean plunged = state.getValue(PLUNGED);
        if (plunged) {
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            level.playSound(null, pos, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
            churn.tryPlunger();
        }
        level.setBlock(pos, state.setValue(PLUNGED, !plunged), Block.UPDATE_ALL);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ChurnBlockEntity churn) {
            Containers.dropContents(level, pos, churn);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChurnBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : type == GrowthcraftMilkBlockEntities.CHURN.get()
                ? (tickerLevel, pos, tickerState, blockEntity) -> {
                    if (blockEntity instanceof ChurnBlockEntity churn) {
                        ChurnBlockEntity.serverTick(tickerLevel, pos, tickerState, churn);
                    }
                }
                : null;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
