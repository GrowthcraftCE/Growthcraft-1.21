package growthcraft.cellar.menu;

import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class CultureJarMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = CultureJarBlockEntity.SLOT_INPUT;
    public static final int OUTPUT_SLOT = CultureJarBlockEntity.SLOT_OUTPUT;

    private final Container container;
    @Nullable
    private final CultureJarBlockEntity jarBE; // only present on server side

    // Client-side cached fields for synced tank data
    private int clientAmount = 0;
    private int clientFluidId = -1;

    // Synced client-side via data slots
    private final ContainerData tankData = new ContainerData() {
        // 0: amount, 1: fluid raw id (BuiltInRegistries.FLUID)
        @Override
        public int get(int index) {
            if (jarBE == null) {
                return switch (index) {
                    case 0 -> clientAmount;
                    case 1 -> clientFluidId;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> jarBE.getTank().getFluidAmount();
                case 1 -> jarBE.getTank().getFluid().isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(jarBE.getTank().getFluid().getFluid());
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            // client-side receives values here
            if (jarBE == null) {
                if (index == 0) clientAmount = value;
                else if (index == 1) clientFluidId = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    // Client ctor (menu type supplier uses this)
    public CultureJarMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(CultureJarBlockEntity.SLOT_COUNT));
    }

    // Server ctor, pass the real container (BE)
    public CultureJarMenu(int containerId, Inventory playerInventory, Container container) {
        super(GrowthcraftCellarMenus.CULTURE_JAR.get(), containerId);
        this.container = container;
        this.jarBE = container instanceof CultureJarBlockEntity be ? be : null;

        // Culture Jar slots: place input and output
        // Coordinates based on 176x166 texture: left area
        this.addSlot(new Slot(container, INPUT_SLOT, 57, 35));
        this.addSlot(new Slot(container, OUTPUT_SLOT, 103, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // output only
            }
        });

        // Player inventory (3 rows)
        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }
        // Hotbar
        int hotbarY = startY + 58;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, startX + col * 18, hotbarY));
        }

        this.addDataSlots(tankData);
    }

    public Container getContainer() {
        return container;
    }

    public int getTankCapacity() {
        return CultureJarBlockEntity.TANK_CAPACITY;
    }

    public int getFluidAmount() {
        return this.tankData.get(0);
    }

    public int getFluidRawId() {
        return this.tankData.get(1);
    }

    public FluidStack getClientFluidStack() {
        int id = getFluidRawId();
        if (id < 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, getFluidAmount());
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            int beSlots = 2; // input + output
            int playerInvStart = beSlots;
            int playerInvEnd = playerInvStart + 27; // 27 inv
            int hotbarStart = playerInvEnd;
            int hotbarEnd = hotbarStart + 9;

            if (index == OUTPUT_SLOT) {
                // Moving from output to player inventory
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stackInSlot, itemstack);
            } else if (index < beSlots) {
                // From input slot -> player inventory
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < hotbarEnd) {
                // From player inventory -> try input slot
                if (!this.moveItemStackTo(stackInSlot, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return itemstack;
    }
}
