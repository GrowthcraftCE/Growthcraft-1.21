package growthcraft.milk.block.entity;

import growthcraft.milk.block.MixingVatBlock;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.menu.MixingVatMenu;
import growthcraft.milk.recipe.MixingVatRecipe;
import growthcraft.milk.recipe.input.MixingVatInput;
import growthcraft.lib.utils.HeatSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class MixingVatBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, MenuProvider {
    public static final int SLOT_INPUT_0 = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_INPUT_2 = 2;
    public static final int SLOT_RESULT = 3;
    public static final int SLOT_RESULT_TOOL = 4;
    public static final int SLOT_COUNT = 5;
    public static final int MAIN_TANK_CAPACITY = 4000;
    public static final int SIDE_TANK_CAPACITY = 1000;

    private static final int[] SLOTS = new int[] { SLOT_INPUT_0, SLOT_INPUT_1, SLOT_INPUT_2, SLOT_RESULT };

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final FluidTank mainTank = createTank(MAIN_TANK_CAPACITY);
    private final FluidTank sideTank = createTank(SIDE_TANK_CAPACITY);
    private final IFluidHandler fluidHandler = new MixingVatFluidHandler();

    private int processTime;
    private int processTimeTotal;
    private boolean activated;

    public MixingVatBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.MIXING_VAT.get(), pos, state);
    }

    private FluidTank createTank(int capacity) {
        return new FluidTank(capacity) {
            @Override
            protected void onContentsChanged() {
                setChangedAndUpdate();
            }
        };
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MixingVatBlockEntity vat) {
        if ((level.getGameTime() & 19L) == 0L) {    // todo: ????
            updateLitState(level, pos, state);
            state = level.getBlockState(pos);
        }

        Optional<RecipeHolder<MixingVatRecipe>> match = vat.findMatch(level, state);  // todo: horrible perf issue here
        if (match.isEmpty() || !vat.activated || !vat.canOutput(match.get().value())) {
            if (match.isEmpty()) {
                vat.resetProgress();
            }
            return;
        }

        MixingVatRecipe recipe = match.get().value();
        vat.processTimeTotal = recipe.getProcessingTime(); // todo  only needed once on start
        vat.processTime++;        // todo keeps inc after total
        if (vat.processTime >= vat.processTimeTotal) {
            vat.completeRecipe(level, pos, state, recipe); // todo math allows it done multiple times, relies on recipe check
        } else if ((vat.processTime & 15) == 0) {  // todo why?
            vat.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MixingVatBlockEntity vat) {
        if (!vat.isProcessing()) {
            return;
        }

        RandomSource random = level.getRandom();
        if (random.nextInt(5) != 0) {
            return;
        }

        double x = pos.getX() + 0.35D + random.nextDouble() * 0.3D;
        double y = pos.getY() + 0.9D;
        double z = pos.getZ() + 0.35D + random.nextDouble() * 0.3D;
        double xSpeed = (random.nextDouble() - 0.5D) * 0.01D;
        double ySpeed = 0.015D + random.nextDouble() * 0.015D;
        double zSpeed = (random.nextDouble() - 0.5D) * 0.01D;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    private static void updateLitState(Level level, BlockPos pos, BlockState state) {
        boolean heated = isHeated(level, pos);
        if (state.getValue(MixingVatBlock.LIT) != heated) {
            level.setBlock(pos, state.setValue(MixingVatBlock.LIT, heated), Block.UPDATE_ALL);
        }
    }

    private Optional<RecipeHolder<MixingVatRecipe>> findMatch(Level level, BlockState state) {
        MixingVatInput input = new MixingVatInput(mainTank.getFluid(), sideTank.getFluid(), items, isHeated(level, worldPosition));
        return level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(input, level))
                .findFirst();
    }

    public Optional<RecipeHolder<MixingVatRecipe>> findCurrentRecipe() {
        if (level == null) {
            return Optional.empty();
        }
        return findMatch(level, getBlockState());
    }

    private boolean canOutput(MixingVatRecipe recipe) {
        if (recipe.getResultType() == MixingVatRecipe.ResultType.ITEM) {
            return getItem(SLOT_RESULT).isEmpty();
        }
        FluidStack output = recipe.getResultFluidStack();
        FluidStack waste = recipe.getWasteFluidStack();
        if (output.isEmpty()) {
            return false;
        }
        return output.getAmount() <= mainTank.getCapacity() && (waste.isEmpty() || waste.getAmount() <= sideTank.getCapacity());
    }

    private void completeRecipe(Level level, BlockPos pos, BlockState state, MixingVatRecipe recipe) {
        consumeInputs(recipe);
        if (recipe.getResultType() == MixingVatRecipe.ResultType.FLUID) {
            mainTank.setFluid(recipe.getResultFluidStack());
            sideTank.setFluid(recipe.getWasteFluidStack());
        } else {
            mainTank.setFluid(FluidStack.EMPTY);
            sideTank.setFluid(FluidStack.EMPTY);
            setItem(SLOT_RESULT, recipe.getResultItemStack());
            setItem(SLOT_RESULT_TOOL, recipe.getResultActivationTool());
        }
        activated = false;
        processTime = 0;
        processTimeTotal = 0;
        setChanged();
        level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
    }

    private void consumeInputs(MixingVatRecipe recipe) {
        for (MixingVatRecipe.IngredientStack ingredient : recipe.getIngredientStacks()) {
            int remaining = ingredient.count();
            for (int slot = SLOT_INPUT_0; slot <= SLOT_INPUT_2 && remaining > 0; slot++) {
                ItemStack stack = getItem(slot);
                if (ingredient.ingredient().test(stack)) {
                    int consumed = Math.min(remaining, stack.getCount());
                    stack.shrink(consumed);
                    remaining -= consumed;
                    if (stack.isEmpty()) {
                        setItem(slot, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    public boolean activate(ItemStack heldStack) {
        if (activated || isProcessing()) {
            return false;
        }
        Optional<RecipeHolder<MixingVatRecipe>> match = findCurrentRecipe();
        if (match.isEmpty() || !ItemStack.isSameItem(match.get().value().getActivationTool(), heldStack)) {
            return false;
        }
        activated = true;
        setChangedAndUpdate();
        return true;
    }

    public boolean collectResult(Player player, ItemStack heldStack) {
        ItemStack result = getItem(SLOT_RESULT);
        if (result.isEmpty()) {
            return false;
        }

        ItemStack resultActivationItem = getItem(SLOT_RESULT_TOOL);
        if (!resultActivationItem.isEmpty() && !ItemStack.isSameItem(resultActivationItem, heldStack)) {
            return false;
        }

        ItemStack toGive = result.copy();
        setItem(SLOT_RESULT, ItemStack.EMPTY);
        setItem(SLOT_RESULT_TOOL, ItemStack.EMPTY);
        if (!resultActivationItem.isEmpty() && !player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
        if (!player.addItem(toGive)) {
            player.drop(toGive, false);
        }
        return true;
    }

    public boolean insertIngredient(ItemStack heldStack) {
        if (heldStack.isEmpty() || !getItem(SLOT_RESULT).isEmpty()) {
            return false;
        }
        for (int slot = SLOT_INPUT_0; slot <= SLOT_INPUT_2; slot++) {
            ItemStack existing = getItem(slot);
            if (existing.isEmpty()) {
                setItem(slot, heldStack.copyWithCount(1));
                heldStack.shrink(1);
                setChangedAndUpdate();
                return true;
            }
            if (ItemStack.isSameItemSameComponents(existing, heldStack) && existing.getCount() < existing.getMaxStackSize()) {
                existing.grow(1);
                heldStack.shrink(1);
                setChangedAndUpdate();
                return true;
            }
        }
        return false;
    }

    private void resetProgress() {
        if (processTime != 0 || processTimeTotal != 0 || activated) {
            processTime = 0;
            processTimeTotal = 0;
            activated = false;
            setChangedAndUpdate();
        }
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null && !level.isClientSide) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public static boolean isHeated(Level level, BlockPos pos) {
        return HeatSourceUtils.hasHeatSourceBelow(level, pos);
    }

    public boolean isProcessing() {
        return processTime > 0 && processTimeTotal > 0;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isHeated() {
        return level != null && isHeated(level, worldPosition);
    }

    public FluidTank getMainTank() {
        return mainTank;
    }

    public FluidTank getSideTank() {
        return sideTank;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = items.get(index);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        items.set(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == SLOT_RESULT && stack.isEmpty()) {
            this.setItem(SLOT_RESULT_TOOL, ItemStack.EMPTY);
        }
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null
                && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction side) {
        return index >= SLOT_INPUT_0 && index <= SLOT_INPUT_2;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_milk.mixing_vat");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MixingVatMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, items, provider);

        CompoundTag mainTankTag = new CompoundTag();
        mainTank.writeToNBT(provider, mainTankTag);
        tag.put("MainTank", mainTankTag);

        CompoundTag sideTankTag = new CompoundTag();
        sideTank.writeToNBT(provider, sideTankTag);
        tag.put("SideTank", sideTankTag);

        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessTimeTotal", processTimeTotal);
        tag.putBoolean("Activated", activated);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag, items, provider);
        mainTank.readFromNBT(provider, tag.getCompound("MainTank"));
        sideTank.readFromNBT(provider, tag.getCompound("SideTank"));
        processTime = tag.getInt("ProcessTime");
        processTimeTotal = tag.getInt("ProcessTimeTotal");
        activated = tag.getBoolean("Activated");
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

    private class MixingVatFluidHandler implements IFluidHandler {
        @Override
        public int getTanks() {
            return 2;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return tank == 0 ? mainTank.getFluid() : sideTank.getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tank == 0 ? mainTank.getCapacity() : sideTank.getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return true;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            int mainFilled = mainTank.fill(resource, FluidAction.SIMULATE);
            if (mainFilled == resource.getAmount()) {
                return mainTank.fill(resource, action);
            }
            return sideTank.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            FluidStack sideDrained = sideTank.drain(resource, action);
            if (!sideDrained.isEmpty()) {
                return sideDrained;
            }
            return mainTank.drain(resource, action);
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack sideDrained = sideTank.drain(maxDrain, action);
            if (!sideDrained.isEmpty()) {
                return sideDrained;
            }
            return mainTank.drain(maxDrain, action);
        }
    }
}
