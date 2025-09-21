package growthcraft.cellar.block;

import com.mojang.serialization.MapCodec;
import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
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

public class CultureJarBlock extends HorizontalDirectionalBlock implements EntityBlock {
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
        // If the player is holding a bucket in either hand, let useItemOn handle it first
        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
        if (main.getItem() instanceof BucketItem || off.getItem() instanceof BucketItem) {
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useWithoutItem: Player {} holding bucket, passing to useItemOn", player.getGameProfile().getName());
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof net.minecraft.world.MenuProvider provider) {
                GrowthcraftCellar.LOGGER.debug("[CultureJar] Opening menu at {} for player {}", pos, player.getGameProfile().getName());
                player.openMenu(provider);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    // Handle using a bucket on the jar via NeoForge capabilities (fill/drain)
    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Only attempt special handling for buckets; otherwise, pass to default
        if (!(heldStack.getItem() instanceof BucketItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CultureJarBlockEntity jar)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Query the block entity's fluid handler capability (NeoForge capability system)
        IFluidHandler handler = level.getCapability(net.neoforged.neoforge.capabilities.Capabilities.FluidHandler.BLOCK,
                pos, state, be, hit.getDirection());

        if (handler == null) {
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn: No fluid handler at {} side {} (client={})", pos, hit.getDirection(), level.isClientSide);
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        // Perform interaction ONLY on the server to avoid client/server desync of the held bucket
        if (!level.isClientSide) {
            var before = jar.getTank().getFluid().copy();
            String beforeName = before.isEmpty() ? "<empty>" : before.getHoverName().getString();
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Server): Player={} Hand={} HeldItem={} BeforeTank={}mB {}", player.getGameProfile().getName(), hand, heldStack.getItem(), before.getAmount(), beforeName);
            boolean acted = FluidUtil.interactWithFluidHandler(player, hand, handler);
            var after = jar.getTank().getFluid();
            String afterName = after.isEmpty() ? "<empty>" : after.getHoverName().getString();
            GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Server): acted={} AfterTank={}mB {}", acted, jar.getTank().getFluidAmount(), afterName);
            if (acted) {
                be.setChanged();
                // notify clients so GUIs/models can refresh
                level.sendBlockUpdated(pos, state, state, 3);
                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        // On client, optimistically report success if the server will handle it
        GrowthcraftCellar.LOGGER.debug("[CultureJar] useItemOn(Client): deferring to server. Player={} Hand={} Item={}", player.getGameProfile().getName(), hand, heldStack.getItem());
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new growthcraft.cellar.block.entity.CultureJarBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null; // no ticking yet
    }
}
