package growthcraft.cellar.block;

import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.item.GrowthcraftMilkBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FermentationBarrelBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public FermentationBarrelBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof net.minecraft.world.MenuProvider provider) {
                player.openMenu(provider);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (tryMilkBucketInteraction(heldStack, level, pos, player, hand)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (heldStack.getItem() instanceof BottleItem || heldStack.is(GrowthcraftCellarItems.BOTTLE_STAINED)) {
            // ir would be nice and fitting if the green bottle extended BottleItem and could maybe hold other fluids and interact with other blocks...
            // ...but i don't care for it.  this is done to have both colorless and green bottles in parallel.
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FermentationBarrelBlockEntity barrel && !barrel.getResultingPotionItemStack().isEmpty()) {
                if (!level.isClientSide) {
                    fillBottleFromBarrel(state, level, pos, player, hand, heldStack, barrel);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean tryMilkBucketInteraction(ItemStack heldStack, Level level, BlockPos pos, Player player, InteractionHand hand) {
        boolean vanillaMilk = heldStack.is(Items.MILK_BUCKET);
        boolean growthcraftMilk = heldStack.getItem() instanceof GrowthcraftMilkBucketItem;
        if (!vanillaMilk && !growthcraftMilk) {
            return false;
        }

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof FermentationBarrelBlockEntity barrel) {
            int filled = barrel.getTank().fill(new FluidStack(GrowthcraftMilkFluids.MILK.source.get(), 1000),
                    IFluidHandler.FluidAction.EXECUTE);
            if (filled == 1000 && !player.getAbilities().instabuild) {
                ItemStack remainder = vanillaMilk
                        ? new ItemStack(Items.BUCKET)
                        : heldStack.getCraftingRemainingItem();
                player.setItemInHand(hand, remainder);
            }
        }
        return true;
    }

    private void fillBottleFromBarrel(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack heldStack, FermentationBarrelBlockEntity barrel) {
        ItemStack result = barrel.getResultingPotionItemStack();
        if (result.isEmpty()) return;

        if  (heldStack.is(GrowthcraftCellarItems.BOTTLE_STAINED)) {
            ItemStack result1 = result;
            result = GrowthcraftCellarItems.POTION_WINE_STAINED.toStack(1);
            result.set(DataComponents.ITEM_NAME, result1.get(DataComponents.ITEM_NAME));
            result.set(DataComponents.POTION_CONTENTS, result1.get(DataComponents.POTION_CONTENTS));
        }
        barrel.drainBottleAmount(state);
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }

        if (heldStack.isEmpty()) {
            player.setItemInHand(hand, result);
        } else if (!player.getInventory().add(result)) {
            player.drop(result, false);
        }

        level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FermentationBarrelBlockEntity barrel) {
                Containers.dropContents(level, pos, barrel);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FermentationBarrelBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof FermentationBarrelBlockEntity barrel) {
                FermentationBarrelBlockEntity.serverTick(lvl, pos, st, barrel);
            }
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
