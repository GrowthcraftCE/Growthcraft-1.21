package growthcraft.milk.block;

import growthcraft.milk.block.entity.PancheonBlockEntity;
import growthcraft.lib.block.MachineMenuOpener;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class PancheonBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D),
            Block.box(0.0D, 1.0D, 0.0D, 16.0D, 5.0D, 16.0D));

    public PancheonBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F)
                .sound(SoundType.STONE)
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
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
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
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return MachineMenuOpener.open(level, player, GrowthcraftMilkMenus.PANCHEON.get(),
                Component.translatable("block." + Reference.MODID + "." + Reference.UnlocalizedName.PANCHEON));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (tryGrowthcraftBucketInteraction(heldStack, level, pos, player, hand)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean tryGrowthcraftBucketInteraction(ItemStack heldStack, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!(level.getBlockEntity(pos) instanceof PancheonBlockEntity pancheon)) {
            return false;
        }

        Fluid filledFluid = getFluidFromBucket(heldStack);
        if (filledFluid != Fluids.EMPTY) {
            if (!level.isClientSide) {
                int filled = pancheon.getFluidHandler().fill(new FluidStack(filledFluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                if (filled == 1000 && !player.getAbilities().instabuild) {
                    replaceHeldItem(player, hand, heldStack, getEmptyBucketRemainder(heldStack));
                }
            }
            return true;
        }

        if (heldStack.is(Items.BUCKET) || heldStack.is(GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())) {
            FluidStack drained = pancheon.getFluidHandler().drain(1000, IFluidHandler.FluidAction.SIMULATE);
            ItemStack filledBucket = getBucketForFluid(drained);
            if (drained.getAmount() == 1000 && !filledBucket.isEmpty()) {
                if (!level.isClientSide) {
                    pancheon.getFluidHandler().drain(drained, IFluidHandler.FluidAction.EXECUTE);
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
        if (stack.is(GrowthcraftMilkItems.MILK_BUCKET_IRON.get())) {
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
        return new PancheonBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return type == GrowthcraftMilkBlockEntities.PANCHEON.get()
                    ? (tickerLevel, pos, tickerState, blockEntity) -> {
                        if (blockEntity instanceof PancheonBlockEntity pancheon) {
                            PancheonBlockEntity.clientTick(tickerLevel, pos, tickerState, pancheon);
                        }
                    }
                    : null;
        }
        return type == GrowthcraftMilkBlockEntities.PANCHEON.get()
                ? (tickerLevel, pos, tickerState, blockEntity) -> {
                    if (blockEntity instanceof PancheonBlockEntity pancheon) {
                        PancheonBlockEntity.serverTick(tickerLevel, pos, tickerState, pancheon);
                    }
                }
                : null;
    }

    public static void makeParticles(Level level, BlockPos pos, BlockState state) {
        if (!(level.getBlockEntity(pos) instanceof PancheonBlockEntity pancheon) || !pancheon.isProcessing()) {
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
