package growthcraft.cellar.block;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.lib.particle.ColoredDripParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class FruitPressBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final double DRIP_START_Y_OFFSET = 0.34D;
    private static final double DRIP_LANDING_Y_OFFSET = 0.02D;
    private static final float DRIP_LANDING_SCALE = 2.5F;
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D),
            Block.box(0.0D, 3.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            Block.box(1.0D, 7.0D, 1.0D, 15.0D, 16.0D, 15.0D));

    public FruitPressBlock() {
        super(Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F)
                .sound(SoundType.CHAIN)
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
        if (!context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            return null;
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        return above.isAir() || above.is(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get().defaultBlockState()
                .setValue(FruitPressPistonBlock.FACING, state.getValue(FACING))
                .setValue(FruitPressPistonBlock.PRESSED, false), Block.UPDATE_ALL_IMMEDIATE);
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
        BlockState pistonState = level.getBlockState(pos.above());
        if (pistonState.is(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get()) && pistonState.getValue(FruitPressPistonBlock.PRESSED)) {
            return InteractionResult.PASS;
        }
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
        if (FluidUtil.getFluidHandler(heldStack).isPresent() && FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection())) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FruitPressBlockEntity press) {
                Containers.dropContents(level, pos, press);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            if (level.getBlockState(pos.above()).is(GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get())) {
                level.destroyBlock(pos.above(), false);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FruitPressBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return (lvl, pos, st, be) -> {
                if (be instanceof FruitPressBlockEntity press) {
                    FruitPressBlockEntity.clientTick(lvl, pos, st, press);
                }
            };
        }

        return (lvl, pos, st, be) -> {
            if (be instanceof FruitPressBlockEntity press) {
                FruitPressBlockEntity.serverTick(lvl, pos, st, press);
            }
        };
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public static void makeParticles(Level level, BlockPos pos, BlockState state, FruitPressBlockEntity press) {
        if (!press.isProcessing()) {
            return;
        }

        FluidStack output = press.getActiveOutputFluidStack(level);
        if (output.isEmpty()) {
            return;
        }

        RandomSource random = level.getRandom();
        double x = pos.getX() + 0.35D + random.nextDouble() * 0.3D;
        double y = pos.getY() + DRIP_START_Y_OFFSET;
        double z = pos.getZ() + 0.35D + random.nextDouble() * 0.3D;
        double landingY = pos.getY() + DRIP_LANDING_Y_OFFSET;
        level.addParticle(ColoredDripParticleOption.fromTintColor(getDripColor(output), landingY, DRIP_LANDING_SCALE), x, y, z, 0.0D, 0.0D, 0.0D);
    }

    private static int getDripColor(FluidStack fluidStack) {
        int color = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
        return color == 0 ? 0xFFFFFFFF : color;
    }
}
