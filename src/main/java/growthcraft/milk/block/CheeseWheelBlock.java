package growthcraft.milk.block;

import com.mojang.serialization.MapCodec;
import growthcraft.rice.init.GrowthcraftRiceItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class CheeseWheelBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<CheeseWheelBlock> CODEC = simpleCodec(properties -> new CheeseWheelBlock());
    public static final IntegerProperty SLICE_COUNT_TOP = IntegerProperty.create("slicestop", 0, 4);
    public static final IntegerProperty SLICE_COUNT_BOTTOM = IntegerProperty.create("slicesbottom", 0, 4);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_AGE = 7;

    private static final VoxelShape FULL_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private static final VoxelShape HALF_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D);

    private final Supplier<? extends Item> sliceItem;
    private final boolean sliceable;
    private final Supplier<? extends Block> agedBlock;

    public CheeseWheelBlock() {
        this(() -> null, false, () -> null);
    }

    public CheeseWheelBlock(Supplier<? extends Item> sliceItem, boolean sliceable, Supplier<? extends Block> agedBlock) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE).noOcclusion().randomTicks());
        this.sliceItem = sliceItem;
        this.sliceable = sliceable;
        this.agedBlock = agedBlock;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, net.minecraft.core.Direction.NORTH)
                .setValue(SLICE_COUNT_BOTTOM, 4)
                .setValue(SLICE_COUNT_TOP, 0)
                .setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(SLICE_COUNT_TOP) > 0 ? FULL_SHAPE : HALF_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return agedBlock.get() != null && state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Block aged = agedBlock.get();
        if (aged == null) {
            return;
        }

        int age = state.getValue(AGE);
        if (age < MAX_AGE - 1) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_ALL);
            return;
        }

        level.setBlock(pos, aged.defaultBlockState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(SLICE_COUNT_BOTTOM, state.getValue(SLICE_COUNT_BOTTOM))
                .setValue(SLICE_COUNT_TOP, state.getValue(SLICE_COUNT_TOP))
                .setValue(AGE, MAX_AGE), Block.UPDATE_ALL);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!player.isCrouching()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            takeWholeWheel(state, level, pos, player);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (heldStack.is(this.asItem())) {
            if (!canAddWholeWheel(state)) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            if (!level.isClientSide) {
                level.setBlock(pos, withTotalSlices(state, getTotalSlices(state) + 4), Block.UPDATE_ALL);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (player.isCrouching()) {
            if (!level.isClientSide) {
                takeWholeWheel(state, level, pos, player);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (sliceable && heldStack.is(GrowthcraftRiceItems.KNIFE.get())) {
            Item slice = sliceItem.get();
            if (slice == null || getTotalSlices(state) <= 0) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            if (!level.isClientSide) {
                giveOrDrop(player, new ItemStack(slice));
                setOrDestroy(level, pos, withTotalSlices(state, getTotalSlices(state) - 1));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SLICE_COUNT_BOTTOM, SLICE_COUNT_TOP, AGE);
    }

    private static int getTotalSlices(BlockState state) {
        return state.getValue(SLICE_COUNT_BOTTOM) + state.getValue(SLICE_COUNT_TOP);
    }

    private static BlockState withTotalSlices(BlockState state, int slices) {
        int clampedSlices = Math.clamp(slices, 0, 8);
        int bottom = Math.min(clampedSlices, 4);
        int top = Math.max(0, clampedSlices - 4);
        return state.setValue(SLICE_COUNT_BOTTOM, bottom).setValue(SLICE_COUNT_TOP, top);
    }

    private static void setOrDestroy(Level level, BlockPos pos, BlockState state) {
        if (getTotalSlices(state) <= 0) {
            level.destroyBlock(pos, false);
            return;
        }
        level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    private boolean canAddWholeWheel(BlockState state) {
        return getTotalSlices(state) + 4 <= 8;
    }

    private void takeWholeWheel(BlockState state, Level level, BlockPos pos, Player player) {
        if (getTotalSlices(state) < 4) {
            return;
        }

        giveOrDrop(player, new ItemStack(this.asItem()));
        setOrDestroy(level, pos, withTotalSlices(state, getTotalSlices(state) - 4));
    }

    private static void giveOrDrop(Player player, ItemStack stack) {
        if (!player.addItem(stack)) {
            player.drop(stack, false);
        }
    }
}
