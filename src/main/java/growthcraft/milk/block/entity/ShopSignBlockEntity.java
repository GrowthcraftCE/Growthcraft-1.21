package growthcraft.milk.block.entity;

import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class ShopSignBlockEntity extends BlockEntity {
    private static final String ITEMS_TAG = "Items";
    private static final String WAXED_TAG = "isWaxed";

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private boolean waxed;

    public ShopSignBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.SHOP_SIGN.get(), pos, state);
    }

    public ItemStack getItem() {
        return items.get(0);
    }

    public void setItem(ItemStack stack) {
        ItemStack displayStack = stack.copy();
        displayStack.setCount(1);
        displayStack.remove(net.minecraft.core.component.DataComponents.ENCHANTMENTS);
        items.set(0, displayStack);
        setChangedAndUpdate();
    }

    public boolean isWaxed() {
        return waxed;
    }

    public void setWaxed(boolean waxed) {
        this.waxed = waxed;
        setChangedAndUpdate();
    }

    private void setChangedAndUpdate() {
        setChanged();
        Level level = getLevel();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, items, provider);
        tag.put(ITEMS_TAG, itemsTag);
        tag.putBoolean(WAXED_TAG, waxed);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound(ITEMS_TAG), items, provider);
        waxed = tag.getBoolean(WAXED_TAG);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        loadAdditional(tag, provider);
    }
}
