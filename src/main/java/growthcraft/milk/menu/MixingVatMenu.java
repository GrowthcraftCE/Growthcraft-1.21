package growthcraft.milk.menu;

import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class MixingVatMenu extends AbstractContainerMenu {
    private final Container container;
    @Nullable
    private final MixingVatBlockEntity vat;

    private int clientMainAmount;
    private int clientMainFluidId = -1;
    private int clientSideAmount;
    private int clientSideFluidId = -1;
    private int clientProcess;
    private int clientProcessTotal;
    private int clientHeated;
    private int clientActivated;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (vat == null) {
                return switch (index) {
                    case 0 -> clientMainAmount;
                    case 1 -> clientMainFluidId;
                    case 2 -> clientSideAmount;
                    case 3 -> clientSideFluidId;
                    case 4 -> clientProcess;
                    case 5 -> clientProcessTotal;
                    case 6 -> clientHeated;
                    case 7 -> clientActivated;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> vat.getMainTank().getFluidAmount();
                case 1 -> fluidId(vat.getMainTank().getFluid());
                case 2 -> vat.getSideTank().getFluidAmount();
                case 3 -> fluidId(vat.getSideTank().getFluid());
                case 4 -> vat.getProcessTime();
                case 5 -> vat.getProcessTimeTotal();
                case 6 -> vat.isHeated() ? 1 : 0;
                case 7 -> vat.isActivated() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (vat != null) return;
            switch (index) {
                case 0 -> clientMainAmount = value;
                case 1 -> clientMainFluidId = value;
                case 2 -> clientSideAmount = value;
                case 3 -> clientSideFluidId = value;
                case 4 -> clientProcess = value;
                case 5 -> clientProcessTotal = value;
                case 6 -> clientHeated = value;
                case 7 -> clientActivated = value;
            }
        }

        @Override
        public int getCount() {
            return 8;
        }
    };

    public MixingVatMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(MixingVatBlockEntity.SLOT_COUNT), new SimpleContainerData(8));
    }

    public MixingVatMenu(int containerId, Inventory playerInventory, Container container) {
        this(containerId, playerInventory, container, null);
    }

    private MixingVatMenu(int containerId, Inventory playerInventory, Container container, @Nullable ContainerData ignoredClientData) {
        super(GrowthcraftMilkMenus.MIXING_VAT.get(), containerId);
        this.container = container;
        this.vat = container instanceof MixingVatBlockEntity be ? be : null;

        this.addSlot(new Slot(container, MixingVatBlockEntity.SLOT_INPUT_0, 71, 18));
        this.addSlot(new Slot(container, MixingVatBlockEntity.SLOT_INPUT_1, 71, 36));
        this.addSlot(new Slot(container, MixingVatBlockEntity.SLOT_INPUT_2, 71, 54));
        this.addSlot(new Slot(container, MixingVatBlockEntity.SLOT_RESULT, 124, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(Player player) {
                return false;
            }
        });
        this.addSlot(new Slot(container, MixingVatBlockEntity.SLOT_RESULT+1, 99999, 99999) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public boolean mayPickup(Player player) { return false; }
            @Override
            public boolean isFake() { return true; }
            @Override
            public boolean isActive() { return false; }
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

    private static int fluidId(FluidStack stack) {
        return stack.isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(stack.getFluid());
    }

    public int getMainTankCapacity() {
        return MixingVatBlockEntity.MAIN_TANK_CAPACITY;
    }

    public int getSideTankCapacity() {
        return MixingVatBlockEntity.SIDE_TANK_CAPACITY;
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(5);
        if (total <= 0) return 0;
        return Math.min(pixels, (data.get(4) * pixels) / total);
    }

    public int getPercentProgress() {
        int total = data.get(5);
        if (total <= 0) return 0;
        return Math.min(100, (data.get(4) * 100) / total);
    }

    public boolean isHeated() {
        return data.get(6) == 1;
    }

    public boolean isActivated() {
        return data.get(7) == 1;
    }

    public FluidStack getMainFluidStack() {
        return clientFluidStack(data.get(1), data.get(0));
    }

    public FluidStack getSideFluidStack() {
        return clientFluidStack(data.get(3), data.get(2));
    }

    private static FluidStack clientFluidStack(int id, int amount) {
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
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

            int machineSlots = MixingVatBlockEntity.SLOT_COUNT;
            int playerInvStart = machineSlots;
            int hotbarEnd = playerInvStart + 36;

            if (index == MixingVatBlockEntity.SLOT_RESULT) {
                return ItemStack.EMPTY;
            } else if (index < machineSlots) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stackInSlot, MixingVatBlockEntity.SLOT_INPUT_0, MixingVatBlockEntity.SLOT_INPUT_2 + 1, false)) {
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

    public ItemStack getResultActivationTool()
    {
        return this.slots.get(MixingVatBlockEntity.SLOT_RESULT_TOOL).getItem();
    }
}
