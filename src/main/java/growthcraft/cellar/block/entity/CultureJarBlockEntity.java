package growthcraft.cellar.block.entity;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.Clearable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class CultureJarBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int TANK_CAPACITY = 1000; // 1 bucket

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

    private final FluidTank tank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            var fluid = getFluid();
            String name = fluid.isEmpty() ? "<empty>" : fluid.getHoverName().getString();
            GrowthcraftCellar.LOGGER.debug("[CultureJarBE] Tank changed at {}: {} mB {}", worldPosition, fluid.getAmount(), name);
            // Ensure clients are notified so GUIs and rendering update
            if (level != null && !level.isClientSide) {
                BlockState state = getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, 3);
                GrowthcraftCellar.LOGGER.debug("[CultureJarBE] Sent block update for GUI sync at {}", worldPosition);
            }
        }
    };

    public CultureJarBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.CULTURE_JAR.get(), pos, state);
    }

    // Inventory API
    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = net.minecraft.world.ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = items.get(index);
        if (stack.isEmpty()) return ItemStack.EMPTY;
        items.set(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) return false;
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) return TOP_SLOTS;
        if (side == Direction.DOWN) return BOTTOM_SLOTS;
        return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction side) {
        if (index == SLOT_OUTPUT) return false; // don't insert into output
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        if (side == Direction.DOWN) return index == SLOT_OUTPUT; // bottom extracts output only
        return true;
    }

    public FluidTank getTank() {
        return tank;
    }

    // MenuProvider
    @Override
    public net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable("container.growthcraft_cellar.culture_jar");
    }

    @Override
    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        return new growthcraft.cellar.menu.CultureJarMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // Items
        CompoundTag itemsTag = new CompoundTag();
        net.minecraft.world.ContainerHelper.saveAllItems(itemsTag, this.items, provider);
        tag.put("Items", itemsTag);
        // Tank
        CompoundTag tankTag = new CompoundTag();
        this.tank.writeToNBT(provider, tankTag);
        tag.put("Tank", tankTag);
        GrowthcraftCellar.LOGGER.debug("[CultureJarBE] saveAdditional at {}: items={} tank={}mB", worldPosition, this.items.stream().filter(s -> !s.isEmpty()).count(), this.tank.getFluidAmount());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        // Items
        CompoundTag itemsTag = tag.getCompound("Items");
        net.minecraft.world.ContainerHelper.loadAllItems(itemsTag, this.items, provider);
        // Tank
        CompoundTag tankTag = tag.getCompound("Tank");
        this.tank.readFromNBT(provider, tankTag);
        GrowthcraftCellar.LOGGER.debug("[CultureJarBE] loadAdditional at {}: items={} tank={}mB", worldPosition, this.items.stream().filter(s -> !s.isEmpty()).count(), this.tank.getFluidAmount());
    }
}
