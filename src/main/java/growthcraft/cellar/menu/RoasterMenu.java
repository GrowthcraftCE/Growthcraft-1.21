package growthcraft.cellar.menu;

import growthcraft.cellar.block.entity.RoasterBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RoasterMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public RoasterMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(RoasterBlockEntity.SLOT_COUNT), new SimpleContainerData(3));
    }

    public RoasterMenu(int containerId, Inventory playerInventory, Container container, ContainerData data) {
        super(GrowthcraftCellarMenus.ROASTER.get(), containerId);
        this.container = container;
        this.data = data;

        this.addSlot(new Slot(container, RoasterBlockEntity.SLOT_INPUT, 56, 35));
        this.addSlot(new Slot(container, RoasterBlockEntity.SLOT_OUTPUT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }

        int hotbarY = startY + 58;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, startX + col * 18, hotbarY));
        }

        this.addDataSlots(data);
    }

    public int getRoastingLevel() {
        return data.get(2);
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(1);
        if (total <= 0) return 0;
        return Math.min(pixels, (data.get(0) * pixels) / total);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            int machineSlots = RoasterBlockEntity.SLOT_COUNT;
            int playerInvStart = machineSlots;
            int playerInvEnd = playerInvStart + 27;
            int hotbarStart = playerInvEnd;
            int hotbarEnd = hotbarStart + 9;

            if (index == RoasterBlockEntity.SLOT_OUTPUT) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(stackInSlot, itemstack);
            } else if (index < machineSlots) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) return ItemStack.EMPTY;
            } else if (index < hotbarEnd) {
                if (!this.moveItemStackTo(stackInSlot, RoasterBlockEntity.SLOT_INPUT, RoasterBlockEntity.SLOT_INPUT + 1, false)) return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }
}
