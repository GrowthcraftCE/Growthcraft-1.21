package growthcraft.milk.menu;

import growthcraft.milk.block.entity.PancheonBlockEntity;
import growthcraft.milk.init.GrowthcraftMilkMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class PancheonMenu extends AbstractContainerMenu {
    @Nullable
    private final PancheonBlockEntity pancheon;

    private int clientInputAmount;
    private int clientInputFluidId = -1;
    private int clientOutput0Amount;
    private int clientOutput0FluidId = -1;
    private int clientOutput1Amount;
    private int clientOutput1FluidId = -1;
    private int clientProcess;
    private int clientProcessTotal;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (pancheon == null) {
                return switch (index) {
                    case 0 -> clientInputAmount;
                    case 1 -> clientInputFluidId;
                    case 2 -> clientOutput0Amount;
                    case 3 -> clientOutput0FluidId;
                    case 4 -> clientOutput1Amount;
                    case 5 -> clientOutput1FluidId;
                    case 6 -> clientProcess;
                    case 7 -> clientProcessTotal;
                    default -> 0;
                };
            }
            return switch (index) {
                case 0 -> pancheon.getInputTank().getFluidAmount();
                case 1 -> fluidId(pancheon.getInputTank().getFluid());
                case 2 -> pancheon.getOutputTank0().getFluidAmount();
                case 3 -> fluidId(pancheon.getOutputTank0().getFluid());
                case 4 -> pancheon.getOutputTank1().getFluidAmount();
                case 5 -> fluidId(pancheon.getOutputTank1().getFluid());
                case 6 -> pancheon.getProcessTime();
                case 7 -> pancheon.getProcessTimeTotal();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (pancheon != null) return;
            switch (index) {
                case 0 -> clientInputAmount = value;
                case 1 -> clientInputFluidId = value;
                case 2 -> clientOutput0Amount = value;
                case 3 -> clientOutput0FluidId = value;
                case 4 -> clientOutput1Amount = value;
                case 5 -> clientOutput1FluidId = value;
                case 6 -> clientProcess = value;
                case 7 -> clientProcessTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 8;
        }
    };

    public PancheonMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null, new SimpleContainerData(8));
    }

    public PancheonMenu(int containerId, Inventory playerInventory, PancheonBlockEntity pancheon) {
        this(containerId, playerInventory, pancheon, null);
    }

    private PancheonMenu(int containerId, Inventory playerInventory, @Nullable PancheonBlockEntity pancheon, @Nullable ContainerData ignoredClientData) {
        super(GrowthcraftMilkMenus.PANCHEON.get(), containerId);
        this.pancheon = pancheon;

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

    public int getInputTankCapacity() {
        return PancheonBlockEntity.INPUT_TANK_CAPACITY;
    }

    public int getOutputTankCapacity() {
        return PancheonBlockEntity.OUTPUT_TANK_CAPACITY;
    }

    public int getProgressionScaled(int pixels) {
        int total = data.get(7);
        if (total <= 0) return 0;
        return Math.min(pixels, (data.get(6) * pixels) / total);
    }

    public int getPercentProgress() {
        int total = data.get(7);
        if (total <= 0) return 0;
        return Math.min(100, (data.get(6) * 100) / total);
    }

    public FluidStack getInputFluidStack() {
        return clientFluidStack(data.get(1), data.get(0));
    }

    public FluidStack getOutput0FluidStack() {
        return clientFluidStack(data.get(3), data.get(2));
    }

    public FluidStack getOutput1FluidStack() {
        return clientFluidStack(data.get(5), data.get(4));
    }

    private static FluidStack clientFluidStack(int id, int amount) {
        if (id < 0 || amount <= 0) return FluidStack.EMPTY;
        var fluid = BuiltInRegistries.FLUID.byId(id);
        if (fluid == null) return FluidStack.EMPTY;
        return new FluidStack(fluid, amount);
    }

    @Override
    public boolean stillValid(Player player) {
        return pancheon == null
                || (pancheon.getLevel() != null
                && pancheon.getLevel().getBlockEntity(pancheon.getBlockPos()) == pancheon
                && player.distanceToSqr(pancheon.getBlockPos().getX() + 0.5D, pancheon.getBlockPos().getY() + 0.5D, pancheon.getBlockPos().getZ() + 0.5D) <= 64.0D);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
