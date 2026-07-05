package growthcraft.milk.block.signs;

import growthcraft.milk.block.entity.ShopSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class ShopCeilingHangingSignBlock extends CeilingHangingSignBlock {
    private final SignBlock originalBlock;

    public ShopCeilingHangingSignBlock(WoodType woodType, Properties properties, SignBlock originalBlock) {
        super(woodType, properties);
        this.originalBlock = originalBlock;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = ShopSignBehavior.useWithoutItem(state, level, pos, player, hitResult, originalBlock);
        return result == InteractionResult.PASS ? super.useWithoutItem(state, level, pos, player, hitResult) : result;
    }

    @Override
    protected net.minecraft.world.ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        net.minecraft.world.ItemInteractionResult result = ShopSignBehavior.useItemOn(heldStack, state, level, pos, player, hand);
        return result == net.minecraft.world.ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                ? super.useItemOn(heldStack, state, level, pos, player, hand, hitResult)
                : result;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return originalBlock.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public String getDescriptionId() {
        return originalBlock.getDescriptionId();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShopSignBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    public Block getOriginalBlock() {
        return originalBlock;
    }
}
