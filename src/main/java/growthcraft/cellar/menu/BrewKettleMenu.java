package growthcraft.cellar.menu;

import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class BrewKettleMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = BrewKettleBlockEntity.SLOT_INPUT;
    public static final int OUTPUT_SLOT = BrewKettleBlockEntity.SLOT_OUTPUT;

    private final Container container;
    @Nullable
    private final BrewKettleBlockEntity kettleBE;

    private int clientInputAmount;
    private int clientInputFluidId = -1;
    private int clientOutputAmount;
    private int clientOutputFluidId = -1;
    private int clientProcess;
    private int clientProcessTotal;
    private int clientLit;
    private int clientHasLid;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (kettleBE == null) {
                return switch (index) {
                    case 0 -> clientInputAmount;
                    case 1 -> clientInputFluidId;
                    case 2 -> clientOutputAmount;
                    case 3 -> clientOutputFluidId;
                    case 4 -> clientProcess;
                    case 5 -> clientProcessTotal;
                    case 6 -> clientLit;
                    case 7 -> clientHasLid;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> kettleBE.getInputTank().getFluidAmount();
                case 1 -> fluidId(kettleBE.getInputTank().getFluid());
                case 2 -> kettleBE.getOutputTank().getFluidAmount();
                case 3 -> fluidId(kettleBE.getOutputTank().getFluid());
                case 4 -> kettleBE.getProcessTime();
                case 5 -> kettleBE.getProcessTimeTotal();
                case 6 -> kettleBE.isHeated() ? 1 : 0;
                case 7 -> kettleBE.hasLid() ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (kettleBE != null) return;
            switch (index) {
                case 0 -> clientInputAmount = value;
                case 1 -> clientInputFluidId = value;
                case 2 -> clientOutputAmount = value;
                case 3 -> clientOutputFluidId = value;
                case 4 -> clientProcess = value;
                case 5 -> clientProcessTotal = value;
                case 6 -> clientLit = value;
                case 7 -> clientHasLid = value;
            }
        }

        @Override
        public int getCount() {
            return 8;
        }
    };

    public BrewKettleMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(BrewKettleBlockEntity.SLOT_COUNT), new SimpleContainerData(8));
    }

    public BrewKettleMenu(int containerId, Inventory playerInventory, Container container) {
        this(containerId, playerInventory, container, null);
    }

    private BrewKettleMenu(int containerId, Inventory playerInventory, Container container, @Nullable ContainerData ignoredClientData) {
        super(GrowthcraftCellarMenus.BREW_KETTLE.get(), containerId);
        this.container = container;
        this.kettleBE = container instanceof BrewKettleBlockEntity be ? be : null;

        this.addSlot(new Slot(container, INPUT_SLOT, 80, 35));
        this.addSlot(new Slot(container, OUTPUT_SLOT, 141, 17) {
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

    private static int fluidId(FluidStack stack) {
        return stack.isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(stack.getFluid());
    }

    public int getTankCapacity() {
        return BrewKettleBlockEntity.TANK_CAPACITY;
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(5);
        if (total <= 0) return 0;
        return Math.min(pixels, (data.get(4) * pixels) / total);
    }

    public boolean isHeated() {
        return data.get(6) == 1;
    }

    public boolean hasLid() {
        return data.get(7) == 1;
    }

    public FluidStack getInputFluidStack() {
        return clientFluidStack(data.get(1), data.get(0));
    }

    public FluidStack getOutputFluidStack() {
        return clientFluidStack(data.get(3), data.get(2));
    }

    private static FluidStack clientFluidStack(int id, int amount) {
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId != 0 || kettleBE == null || kettleBE.getLevel() == null) return false;
        var level = kettleBE.getLevel();
        var pos = kettleBE.getBlockPos();
        var state = level.getBlockState(pos);
        if (state.hasProperty(BrewKettleBlock.HAS_LID)) {
            level.setBlock(pos, state.setValue(BrewKettleBlock.HAS_LID, !state.getValue(BrewKettleBlock.HAS_LID)), 3);
            return true;
        }
        return false;
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

            int beSlots = 2;
            int playerInvStart = beSlots;
            int hotbarEnd = playerInvStart + 36;

            if (index == OUTPUT_SLOT) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(stackInSlot, itemstack);
            } else if (index < beSlots) {
                if (!this.moveItemStackTo(stackInSlot, playerInvStart, hotbarEnd, false)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(stackInSlot, INPUT_SLOT, INPUT_SLOT + 1, false)) {
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
