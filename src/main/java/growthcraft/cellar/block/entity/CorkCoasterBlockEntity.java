package growthcraft.cellar.block.entity;

import growthcraft.cellar.block.CorkCoasterBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CorkCoasterBlockEntity extends BlockEntity implements Container, Clearable {
    private static final String ITEMS_TAG = "Items";
    private static final int SLOT_COUNT = 1;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public CorkCoasterBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.CORK_COASTER.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack removed = ContainerHelper.removeItem(items, index, count);
        if (!removed.isEmpty()) {
            setChangedAndUpdate();
        }
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChangedAndUpdate();
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index == 0 &&
                (stack.is(GrowthcraftCellarItems.POTION_WINE_STAINED.get()) || stack.is(GrowthcraftCellarItems.POTION_WINE.get()));
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(Vec3.atCenterOf(worldPosition)) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
        setChangedAndUpdate();
    }

    public void setChangedAndUpdate() {
        setChanged();
        Level level = getLevel();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            BlockState updatedState = state.setValue(CorkCoasterBlock.ITEM, !isEmpty());
            if (state != updatedState) {
                level.setBlock(worldPosition, updatedState, 3);
            }
            BlockState currentState = getBlockState();
            level.sendBlockUpdated(worldPosition, currentState, currentState, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, this.items, provider);
        tag.put(ITEMS_TAG, itemsTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        items.clear();
        ContainerHelper.loadAllItems(tag.getCompound(ITEMS_TAG), this.items, provider);
    }

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
