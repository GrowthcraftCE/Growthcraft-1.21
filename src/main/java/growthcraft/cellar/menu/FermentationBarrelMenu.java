package growthcraft.cellar.menu;

import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class FermentationBarrelMenu extends AbstractContainerMenu {
    public static final int YEAST_SLOT = FermentationBarrelBlockEntity.SLOT_YEAST;

    private final Container container;
    @Nullable
    private final FermentationBarrelBlockEntity barrelBE;

    private int clientFluidAmount;
    private int clientFluidId = -1;
    private int clientProcess;
    private int clientProcessTotal;
    private int clientYeastWarning;
    private int clientYeastError;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (barrelBE == null) {
                return switch (index) {
                    case 0 -> clientFluidAmount;
                    case 1 -> clientFluidId;
                    case 2 -> clientProcess;
                    case 3 -> clientProcessTotal;
                    case 4 -> clientYeastWarning;
                    case 5 -> clientYeastError;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> barrelBE.getTank().getFluidAmount();
                case 1 -> barrelBE.getTank().getFluid().isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(barrelBE.getTank().getFluid().getFluid());
                case 2 -> barrelBE.getProcessTime();
                case 3 -> barrelBE.getProcessTimeTotal();
                case 4 -> barrelBE.hasYeastWarning() ? 1 : 0;
                case 5 -> barrelBE.hasYeastError() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (barrelBE != null) return;
            switch (index) {
                case 0 -> clientFluidAmount = value;
                case 1 -> clientFluidId = value;
                case 2 -> clientProcess = value;
                case 3 -> clientProcessTotal = value;
                case 4 -> clientYeastWarning = value;
                case 5 -> clientYeastError = value;
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };

    public FermentationBarrelMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(FermentationBarrelBlockEntity.SLOT_COUNT));
    }

    public FermentationBarrelMenu(int containerId, Inventory playerInventory, Container container) {
        super(GrowthcraftCellarMenus.FERMENTATION_BARREL.get(), containerId);
        this.container = container;
        this.barrelBE = container instanceof FermentationBarrelBlockEntity be ? be : null;

        this.addSlot(new Slot(container, YEAST_SLOT, 52, 53));

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

    public int getTankCapacity() {
        return FermentationBarrelBlockEntity.TANK_CAPACITY;
    }

    public int getFluidAmount() {
        return data.get(0);
    }

    public FluidStack getFluidStack() {
        int id = data.get(1);
        int amount = data.get(0);
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(3);
        if (total <= 0) return 0;
        return Math.min(pixels, (data.get(2) * pixels) / total);
    }

    public int getPercentProgress() {
        int total = data.get(3);
        if (total <= 0) return 0;
        return Math.min(100, (data.get(2) * 100) / total);
    }

    public boolean hasYeastWarning() {
        return data.get(4) == 1;
    }

    public boolean hasYeastError() {
        return data.get(5) == 1;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            int beSlots = 1;
            int playerInvStart = beSlots;
            int hotbarEnd = playerInvStart + 36;

            if (index < beSlots) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stackInSlot, YEAST_SLOT, YEAST_SLOT + 1, false)) {
                return ItemStack.EMPTY;
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
