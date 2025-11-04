package growthcraft.cellar.block;

import com.mojang.serialization.MapCodec;
import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.milk.item.GrowthcraftMilkBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class CultureJarBlock extends HorizontalDirectionalBlock implements EntityBlock, net.minecraft.world.level.block.BucketPickup {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<CultureJarBlock> CODEC = simpleCodec(CultureJarBlock::new);

    // Reduced bounding box to better match the jar model footprint and height
    private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D);

    public CultureJarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends CultureJarBlock> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        // If the player is holding a bucket (vanilla, milk bucket, or milking bucket) in either hand, let useItemOn handle it first
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
        boolean mainIsBucket = main.getItem() instanceof BucketItem || main.getItem() instanceof growthcraft.milk.item.MilkingBucketItem || main.is(Items.MILK_BUCKET);
        boolean offIsBucket = off.getItem() instanceof BucketItem || off.getItem() instanceof growthcraft.milk.item.MilkingBucketItem || off.is(Items.MILK_BUCKET);
        if (mainIsBucket || offIsBucket) {
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useWithoutItem: Player={} MainHand={} OffHand={} mainIsBucket={} offIsBucket={} -> passing to useItemOn", player.getGameProfile().getName(), main.getItem(), off.getItem(), mainIsBucket, offIsBucket);
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof net.minecraft.world.MenuProvider provider) {
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Opening menu at {} for player {}", pos, player.getGameProfile().getName());
                player.openMenu(provider);
            } else {
                GrowthcraftCellar.LOGGER.debug("[CultureJar] No MenuProvider at {} when player {} used without item", pos, player.getGameProfile().getName());
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    // Handle using a bucket on the jar via NeoForge capabilities (fill/drain)
    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Only attempt special handling for buckets or our milking bucket; otherwise, pass to default
        boolean isVanillaBucket = heldStack.getItem() instanceof BucketItem;
        boolean isVanillaMilkBucket = heldStack.is(Items.MILK_BUCKET);
        boolean isMilkingBucket = heldStack.getItem() instanceof growthcraft.milk.item.MilkingBucketItem;
        if (!isVanillaBucket && !isVanillaMilkBucket && !isMilkingBucket && !(heldStack.getItem() instanceof GrowthcraftMilkBucketItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CultureJarBlockEntity jar)) {
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn: No CultureJarBlockEntity at {} (got {}), passing.", pos, be);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Do not prefetch a sided handler here; let FluidUtil resolve the appropriate handler during fallback.

        // Perform interaction ONLY on the server to avoid client/server desync of the held bucket
        if (!level.isClientSide) {
            var before = jar.getTank().getFluid().copy();
            String beforeName = before.isEmpty() ? "<empty>" : before.getHoverName().getString();
            int capacity = jar.getTank().getTankCapacity(0);
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Server): Player={} Hand={} HeldItem={} (class={}) Facing={} TankBefore={}mB/{} {}",
                    player.getGameProfile().getName(), hand, heldStack.getItem(), heldStack.getItem().getClass().getName(), hit.getDirection(), before.getAmount(), capacity, beforeName);

            // 1) If holding a vanilla Milk Bucket, deposit Growthcraft milk into the jar and return an empty vanilla bucket.
            if (heldStack.is(Items.MILK_BUCKET)) {
                net.neoforged.neoforge.fluids.FluidStack toInsert = new net.neoforged.neoforge.fluids.FluidStack(
                        growthcraft.milk.init.GrowthcraftMilkFluids.MILK.source.get(), 1000);
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Vanilla milk bucket deposit: request={}mB spaceAvailable={}mB", 1000, capacity - jar.getTank().getFluidAmount());
                int filled = jar.getTank().fill(toInsert, IFluidHandler.FluidAction.EXECUTE);
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Vanilla milk bucket deposit result: filled={} newTank={}mB", filled, jar.getTank().getFluidAmount());
                if (filled == 1000) {
                    if (!player.getAbilities().instabuild) {
                        ItemStack remainder = new ItemStack(Items.BUCKET);
                        player.setItemInHand(hand, remainder);
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Consumed vanilla milk bucket, returned {}", remainder.getItem());
                    } else {
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Player in creative, not consuming bucket");
                    }
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    GrowthcraftCellar.LOGGER.debug("[CultureJar] Vanilla milk bucket deposit could not insert full 1000mB (inserted={}), will continue", filled);
                }
            }

            // 2) If holding a filled Growthcraft milk bucket, deposit into the jar first to avoid FluidUtil short-circuiting.
            if (heldStack.getItem() instanceof GrowthcraftMilkBucketItem milkBucket) {
                net.neoforged.neoforge.fluids.FluidStack toInsert = new net.neoforged.neoforge.fluids.FluidStack(
                        growthcraft.milk.init.GrowthcraftMilkFluids.MILK.source.get(), 1000);
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Attempting priority milk deposit: request={}mB spaceAvailable={}mB", 1000, capacity - jar.getTank().getFluidAmount());
                int filled = jar.getTank().fill(toInsert, IFluidHandler.FluidAction.EXECUTE);
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Priority milk deposit result: filled={} newTank={}mB", filled, jar.getTank().getFluidAmount());
                if (filled == 1000) {
                    if (!player.getAbilities().instabuild) {
                        ItemStack remainder = milkBucket.getCraftingRemainingItem(heldStack);
                        player.setItemInHand(hand, remainder.copy());
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Consumed filled Growthcraft milk bucket, returned {}", remainder.getItem());
                    } else {
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Player in creative, not consuming bucket");
                    }
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    GrowthcraftCellar.LOGGER.debug("[CultureJar] Priority milk deposit could not insert full 1000mB (inserted={}), will continue", filled);
                }
            }

            // 3) If holding an empty MilkingBucketItem, try filling it from the jar before generic handling.
            if (isMilkingBucket) {
                net.neoforged.neoforge.fluids.FluidStack inTank = jar.getTank().getFluid();
                boolean correctFluid = !inTank.isEmpty() && inTank.getFluid() == growthcraft.milk.init.GrowthcraftMilkFluids.MILK.source.get();
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Milking bucket fill check: hasMilk={} amount={}mB", correctFluid, inTank.getAmount());
                if (correctFluid && inTank.getAmount() >= 1000) {
                    net.neoforged.neoforge.fluids.FluidStack drained = jar.getTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    GrowthcraftCellar.LOGGER.debug("[CultureJar] Drained from jar for milking bucket: {}mB", drained.getAmount());
                    if (drained.getAmount() == 1000) {
                        ItemStack filledStack = new ItemStack(growthcraft.milk.init.GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
                        if (!player.getAbilities().instabuild) {
                            player.setItemInHand(hand, filledStack);
                        }
                        be.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Filled milking bucket from jar. Now {}mB left", jar.getTank().getFluidAmount());
                        return ItemInteractionResult.SUCCESS;
                    } else {
                        GrowthcraftCellar.LOGGER.debug("[CultureJar] Unexpected: drained {}mB for milking bucket (expected 1000)", drained.getAmount());
                    }
                } else {
                    GrowthcraftCellar.LOGGER.debug("[CultureJar] Not enough milk to fill milking bucket or wrong fluid");
                }
            }

            // 4) Fallback to generic fluid interaction (vanilla buckets and others)
            GrowthcraftCellar.LOGGER.debug("[CultureJar] Fallback FluidUtil.interactWithFluidHandler: player={} hand={} face={} item={} (class={})",
                    player.getGameProfile().getName(), hand, hit.getDirection(), heldStack.getItem(), heldStack.getItem().getClass().getName());
            boolean acted = FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection());

            var after = jar.getTank().getFluid();
            String afterName = after.isEmpty() ? "<empty>" : after.getHoverName().getString();
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Server): acted={} AfterTank={}mB {}", acted, jar.getTank().getFluidAmount(), afterName);
            if (acted) {
                be.setChanged();
                // notify clients so GUIs/models can refresh
                level.sendBlockUpdated(pos, state, state, 3);
                return ItemInteractionResult.SUCCESS;
            }
            GrowthcraftCellar.LOGGER.debug("[CultureJar] FluidUtil fallback did not act; passing to default block interaction");
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        // On client, do not consume the interaction; allow item (e.g., vanilla BucketItem) to handle if needed.
        GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Client): passing to default item handling. Player={} Hand={} Item={} (class={})", player.getGameProfile().getName(), hand, heldStack.getItem(), heldStack.getItem().getClass().getName());
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new growthcraft.cellar.block.entity.CultureJarBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null; // no ticking yet
    }

    // Vanilla bucket support: allow empty vanilla buckets to pick up milk from the jar
    @Override
    public net.minecraft.world.item.ItemStack pickupBlock(net.minecraft.world.entity.player.Player player, net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CultureJarBlockEntity jar)) {
            GrowthcraftCellar.LOGGER.debug("[CultureJar] BucketPickup: No CultureJarBlockEntity at {} (got {}), returning EMPTY", pos, be);
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        var tank = jar.getTank();
        net.neoforged.neoforge.fluids.FluidStack inTank = tank.getFluid();
        boolean isMilk = !inTank.isEmpty() && inTank.getFluid() == growthcraft.milk.init.GrowthcraftMilkFluids.MILK.source.get();
        int amount = inTank.getAmount();
        GrowthcraftCellar.LOGGER.debug("[CultureJar] BucketPickup: isMilk={} amount={}mB", isMilk, amount);
        if (isMilk && amount >= 1000) {
            net.neoforged.neoforge.fluids.FluidStack drained = tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            GrowthcraftCellar.LOGGER.debug("[CultureJar] BucketPickup: drained {}mB", drained.getAmount());
            if (drained.getAmount() == 1000) {
                if (level instanceof Level lvl) {
                    be.setChanged();
                    lvl.sendBlockUpdated(pos, state, state, 3);
                }
                return new net.minecraft.world.item.ItemStack(growthcraft.milk.init.GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
            }
        }
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    @Override
    public java.util.Optional<net.minecraft.sounds.SoundEvent> getPickupSound() {
        return java.util.Optional.of(net.minecraft.sounds.SoundEvents.BUCKET_FILL);
    }
}
