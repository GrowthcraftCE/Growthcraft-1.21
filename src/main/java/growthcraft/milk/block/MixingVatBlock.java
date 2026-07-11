package growthcraft.milk.block;

import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class MixingVatBlock extends Block implements EntityBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public MixingVatBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F)
                .sound(SoundType.METAL)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof net.minecraft.world.MenuProvider provider) {
            player.openMenu(provider);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (tryGrowthcraftBucketInteraction(heldStack, level, pos, player, hand)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!(level.getBlockEntity(pos) instanceof MixingVatBlockEntity vat)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        if (vat.collectResult(player, heldStack)) {
            return ItemInteractionResult.SUCCESS;
        }
        if (vat.activate(heldStack)) {
            return ItemInteractionResult.SUCCESS;
        }
        if (heldStack.is(Items.STICK)) {
            return ItemInteractionResult.SUCCESS;
        }
        if (vat.insertIngredient(heldStack)) {
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean tryGrowthcraftBucketInteraction(ItemStack heldStack, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!(level.getBlockEntity(pos) instanceof MixingVatBlockEntity vat)) {
            return false;
        }

        Fluid filledFluid = getFluidFromBucket(heldStack);
        if (filledFluid != Fluids.EMPTY) {
            if (!level.isClientSide) {
                int filled = vat.getFluidHandler().fill(new FluidStack(filledFluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                if (filled == 1000) {
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        replaceHeldItem(player, hand, heldStack, getEmptyBucketRemainder(heldStack));
                    }
                }
            }
            return true;
        }

        if (heldStack.is(Items.BUCKET) || heldStack.is(GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())) {
            FluidStack drained = vat.getFluidHandler().drain(1000, IFluidHandler.FluidAction.SIMULATE);
            ItemStack filledBucket = getBucketForFluid(drained);
            if (drained.getAmount() == 1000 && !filledBucket.isEmpty()) {
                if (!level.isClientSide) {
                    vat.getFluidHandler().drain(drained, IFluidHandler.FluidAction.EXECUTE);
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        replaceHeldItem(player, hand, heldStack, filledBucket);
                    }
                }
                return true;
            }
        }

        return false;
    }

    private static Fluid getFluidFromBucket(ItemStack stack) {
        if (stack.is(Items.MILK_BUCKET) || stack.is(GrowthcraftMilkItems.MILK_BUCKET_IRON.get())) {
            return GrowthcraftMilkFluids.MILK.source.get();
        }

        for (FluidRegistryContainer container : GrowthcraftMilkFluids.ALL) {
            if (container.bucket != null && stack.is(container.bucket.get())) {
                return container.source.get();
            }
        }
        return Fluids.EMPTY;
    }

    private static ItemStack getBucketForFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (fluidStack.getFluid().getFluidType() == GrowthcraftMilkFluids.MILK.source.get().getFluidType()) {
            return new ItemStack(GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
        }

        for (FluidRegistryContainer container : GrowthcraftMilkFluids.ALL) {
            if (container.bucket != null && fluidStack.getFluid().getFluidType() == container.source.get().getFluidType()) {
                return new ItemStack(container.bucket.get());
            }
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getEmptyBucketRemainder(ItemStack stack) {
        ItemStack remainder = stack.getCraftingRemainingItem();
        return remainder.isEmpty() ? new ItemStack(Items.BUCKET) : remainder;
    }

    private static void replaceHeldItem(Player player, InteractionHand hand, ItemStack heldStack, ItemStack replacement) {
        if (heldStack.getCount() == 1) {
            player.setItemInHand(hand, replacement);
            return;
        }

        heldStack.shrink(1);
        if (!player.addItem(replacement)) {
            player.drop(replacement, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MixingVatBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return type == GrowthcraftMilkBlockEntities.MIXING_VAT.get()
                    ? (tickerLevel, pos, tickerState, blockEntity) -> {
                        if (blockEntity instanceof MixingVatBlockEntity vat) {
                            MixingVatBlockEntity.clientTick(tickerLevel, pos, tickerState, vat);
                        }
                    }
                    : null;
        }
        return type == GrowthcraftMilkBlockEntities.MIXING_VAT.get()
                ? (tickerLevel, pos, tickerState, blockEntity) -> {
                    if (blockEntity instanceof MixingVatBlockEntity vat) {
                        MixingVatBlockEntity.serverTick(tickerLevel, pos, tickerState, vat);
                    }
                }
                : null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof MixingVatBlockEntity vat) {
                for(int i = 0; i < vat.getContainerSize(); ++i) {
                    if (i == MixingVatBlockEntity.SLOT_RESULT_TOOL) {
                        continue; // it's a fake/virtual slot for transferring information to screen.
                    }
                    if (i == MixingVatBlockEntity.SLOT_RESULT) {
                        // here's the deal: if there is a pickup-item needed (cloth), no drop. if it can be collected by hand, we drop.
                        if (! vat.getItem(i).isEmpty()) {
                            continue;
                        }
                    }
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), vat.getItem(i));
                }

            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
