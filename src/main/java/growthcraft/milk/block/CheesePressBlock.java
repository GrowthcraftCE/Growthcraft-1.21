package growthcraft.milk.block;

import growthcraft.core.init.GrowthcraftItems;
import growthcraft.milk.block.entity.CheesePressBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CheesePressBlock extends Block implements EntityBlock {
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 7);
    private static final VoxelShape SHAPE = Block.box(0.02D, 0.0D, 0.02D, 15.98D, 15.98D, 15.98D);

    public CheesePressBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
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
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof CheesePressBlockEntity press) || !press.isOpen()) {
            return InteractionResult.PASS;
        }

        if (press.hasContent()) {
            if (!level.isClientSide) {
                extractFromPress(press, player, ItemStack.EMPTY, pos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof CheesePressBlockEntity press)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (heldStack.is(GrowthcraftItems.WRENCH.get())) {
            if (!level.isClientSide) {
                boolean close = press.isOpen();
                level.playSound(null, pos, close ? SoundEvents.CHAIN_PLACE : SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS);
                int rotation = press.toggleOpenClosed();
                level.setBlock(pos, state.setValue(ROTATION, rotation), Block.UPDATE_ALL_IMMEDIATE);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!press.isOpen()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (press.hasContent()) {
            if (!level.isClientSide) {
                extractFromPress(press, player, heldStack, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!heldStack.isEmpty() && !heldStack.is(GrowthcraftMilkItems.CHEESE_CLOTH.get())) {
            if (!level.isClientSide) {
                ItemStack toInsert = heldStack.copyWithCount(1);
                ItemStack remainder = toInsert.getCraftingRemainingItem();
                press.setItem(CheesePressBlockEntity.SLOT_INPUT, toInsert);
                heldStack.shrink(1);
                giveOrDrop(player, remainder, pos);
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static void extractFromPress(CheesePressBlockEntity press, Player player, ItemStack heldStack, BlockPos pos) {
        ItemStack stack = press.getItem(CheesePressBlockEntity.SLOT_INPUT);
        int slot = CheesePressBlockEntity.SLOT_INPUT;
        if (stack.isEmpty()) {
            stack = press.getItem(CheesePressBlockEntity.SLOT_OUTPUT);
            slot = CheesePressBlockEntity.SLOT_OUTPUT;
        }
        if (stack.isEmpty()) {
            return;
        }

        ItemStack requiredContainer = stack.getCraftingRemainingItem();
        if (!requiredContainer.isEmpty() && !ItemStack.isSameItem(heldStack, requiredContainer)) {
            Component containerText = requiredContainer.getHoverName().copy().withStyle(Style.EMPTY.withColor(0xffffff88));
            Component message = Component.translatable("message.growthcraft_milk.get_using_item", containerText).withStyle(Style.EMPTY.withColor(0xffbb9944));
            player.displayClientMessage(message, true);
            return;
        }
        if (requiredContainer.isEmpty() && !heldStack.isEmpty()) {
            Component emptyHand = Component.translatable("message.growthcraft_milk.get_using_item_empty_hand").withStyle(Style.EMPTY.withColor(0xffdddd88));
            Component message = Component.translatable("message.growthcraft_milk.get_using_item", emptyHand).withStyle(Style.EMPTY.withColor(0xffbb9944));
            player.displayClientMessage(message, true);
            return;
        }

        if (!requiredContainer.isEmpty()) {
            heldStack.shrink(1);
        }
        giveOrDrop(player, stack.copy(), pos);
        press.setItem(slot, ItemStack.EMPTY);
        if (press.getLevel() != null) {
            press.getLevel().sendBlockUpdated(pos, press.getBlockState(), press.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static void giveOrDrop(Player player, ItemStack stack, BlockPos pos) {
        if (stack.isEmpty()) {
            return;
        }
        if (!player.addItem(stack)) {
            ItemEntity itemEntity = new ItemEntity(player.level(), pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, stack);
            player.level().addFreshEntity(itemEntity);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CheesePressBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return type == GrowthcraftMilkBlockEntities.CHEESE_PRESS.get()
                    ? (tickerLevel, pos, tickerState, blockEntity) -> {
                        if (blockEntity instanceof CheesePressBlockEntity press) {
                            CheesePressBlockEntity.clientTick(tickerLevel, pos, tickerState, press);
                        }
                    }
                    : null;
        }
        return type == GrowthcraftMilkBlockEntities.CHEESE_PRESS.get()
                ? (tickerLevel, pos, tickerState, blockEntity) -> {
                    if (blockEntity instanceof CheesePressBlockEntity press) {
                        CheesePressBlockEntity.serverTick(tickerLevel, pos, tickerState, press);
                    }
                }
                : null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof CheesePressBlockEntity press) {
            Containers.dropContents(level, pos, press);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void makeParticles(Level level, BlockPos pos, BlockState state) {
        if (state.getValue(ROTATION) < 7 || !(level.getBlockEntity(pos) instanceof CheesePressBlockEntity press) || !press.isProcessing()) {
            return;
        }

        RandomSource random = level.getRandom();
        double x = pos.getX() + random.nextDouble();
        double y = pos.getY() - 0.05D;
        double z = pos.getZ() + random.nextDouble();
        level.addParticle(ParticleTypes.FALLING_HONEY, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
