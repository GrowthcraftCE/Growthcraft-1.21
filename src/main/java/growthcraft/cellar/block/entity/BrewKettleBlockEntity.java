package growthcraft.cellar.block.entity;

import growthcraft.cellar.block.BrewKettleBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.input.BrewKettleInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class BrewKettleBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int TANK_CAPACITY = 4000;

    private static final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private static final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private static final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private final FluidTank inputTank = createTank();
    private final FluidTank outputTank = createTank();

    private int processTime;
    private int processTimeTotal;

    public BrewKettleBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.BREW_KETTLE.get(), pos, state);
    }

    private FluidTank createTank() {
        return new FluidTank(TANK_CAPACITY) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide) {
                    BlockState state = getBlockState();
                    level.sendBlockUpdated(worldPosition, state, state, 3);
                }
            }
        };
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public boolean isHeated() {
        return getBlockState().getValue(BrewKettleBlock.LIT);
    }

    public boolean hasLid() {
        return getBlockState().getValue(BrewKettleBlock.HAS_LID);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BrewKettleBlockEntity kettle) {
        if (level.isClientSide) return;

        if ((level.getGameTime() & 19L) == 0L) {
            BrewKettleBlock.updateLitState(level, pos, state);
            state = level.getBlockState(pos);
        }

        ItemStack input = kettle.getItem(SLOT_INPUT);
        if (input.isEmpty() || kettle.inputTank.isEmpty()) {
            kettle.resetProgress();
            return;
        }

        Optional<RecipeHolder<BrewKettleRecipe>> match = kettle.findMatch(level, input, state);
        if (match.isEmpty()) {
            kettle.resetProgress();
            return;
        }

        BrewKettleRecipe recipe = match.get().value();
        FluidStack output = kettle.outputFluidStack(recipe);
        if (!output.isEmpty() && kettle.outputTank.fill(output.copy(), IFluidHandler.FluidAction.SIMULATE) < output.getAmount()) {
            kettle.resetProgress();
            return;
        }

        if (!kettle.canOutputByProduct(recipe.getByProduct())) {
            kettle.resetProgress();
            return;
        }

        kettle.processTimeTotal = recipe.getProcessingTime();
        kettle.processTime++;
        if (kettle.processTime >= kettle.processTimeTotal) {
            kettle.completeRecipe(level, pos, state, recipe, output);
        } else if ((kettle.processTime & 15) == 0) {
            kettle.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private Optional<RecipeHolder<BrewKettleRecipe>> findMatch(Level level, ItemStack input, BlockState state) {
        var recipes = level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get());
        for (RecipeHolder<BrewKettleRecipe> holder : recipes) {
            BrewKettleRecipe recipe = holder.value();
            if (!recipe.matches(new BrewKettleInput(input), level)) continue;
            if (recipe.requiresHeat() && !state.getValue(BrewKettleBlock.LIT)) continue;
            if (recipe.requiresLid() != state.getValue(BrewKettleBlock.HAS_LID)) continue;
            if (!hasRequiredInputFluid(recipe)) continue;
            return Optional.of(holder);
        }
        return Optional.empty();
    }

    private boolean hasRequiredInputFluid(BrewKettleRecipe recipe) {
        BrewKettleRecipe.FluidAmount required = recipe.getInputFluid();
        FluidStack current = this.inputTank.getFluid();
        if (current.getAmount() < required.amount()) return false;
        return matchesFluidId(required.fluidId(), current.getFluid());
    }

    private boolean matchesFluidId(ResourceLocation requiredId, net.minecraft.world.level.material.Fluid current) {
        ResourceLocation currentId = BuiltInRegistries.FLUID.getKey(current);
        if (requiredId.equals(currentId)) return true;

        net.minecraft.world.level.material.Fluid required = BuiltInRegistries.FLUID.get(requiredId);
        if (required != Fluids.EMPTY && required.getFluidType() == current.getFluidType()) return true;

        ResourceLocation sourceId = ResourceLocation.fromNamespaceAndPath(requiredId.getNamespace(), requiredId.getPath() + "_fluid_source");
        ResourceLocation flowingId = ResourceLocation.fromNamespaceAndPath(requiredId.getNamespace(), requiredId.getPath() + "_fluid_flowing");
        return sourceId.equals(currentId) || flowingId.equals(currentId);
    }

    private FluidStack outputFluidStack(BrewKettleRecipe recipe) {
        BrewKettleRecipe.FluidAmount output = recipe.getOutputFluid();
        if (output.amount() <= 0 || ResourceLocation.withDefaultNamespace("air").equals(output.fluidId())) {
            return FluidStack.EMPTY;
        }
        var fluid = BuiltInRegistries.FLUID.get(output.fluidId());
        if (fluid == Fluids.EMPTY) return FluidStack.EMPTY;
        return new FluidStack(fluid, output.amount());
    }

    private boolean canOutputByProduct(ItemStack stack) {
        if (stack.isEmpty()) return true;
        ItemStack output = this.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(output, stack)) return false;
        return output.getCount() + stack.getCount() <= output.getMaxStackSize();
    }

    private void completeRecipe(Level level, BlockPos pos, BlockState state, BrewKettleRecipe recipe, FluidStack outputFluid) {
        this.inputTank.drain(recipe.getInputFluid().amount(), IFluidHandler.FluidAction.EXECUTE);
        this.getItem(SLOT_INPUT).shrink(recipe.getInputItem().count());
        if (!outputFluid.isEmpty()) {
            this.outputTank.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
        }
        maybeInsertByProduct(level, recipe);
        this.resetProgress();
        this.setChanged();
        level.sendBlockUpdated(pos, state, state, 3);
    }

    private void maybeInsertByProduct(Level level, BrewKettleRecipe recipe) {
        ItemStack byProduct = recipe.getByProduct();
        if (byProduct.isEmpty() || recipe.getByProductChance() <= 0) return;
        if (level.random.nextInt(100) >= recipe.getByProductChance()) return;

        ItemStack output = this.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) {
            this.setItem(SLOT_OUTPUT, byProduct);
        } else if (ItemStack.isSameItemSameComponents(output, byProduct)) {
            output.grow(byProduct.getCount());
        }
    }

    private void resetProgress() {
        if (this.processTime != 0 || this.processTimeTotal != 0) {
            this.processTime = 0;
            this.processTimeTotal = 0;
            setChanged();
        }
    }

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
        ItemStack result = ContainerHelper.removeItem(items, index, count);
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
        return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
        return index == SLOT_INPUT;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_OUTPUT;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_cellar.brew_kettle");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BrewKettleMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, this.items, provider);
        tag.put("Items", itemsTag);

        CompoundTag inputTankTag = new CompoundTag();
        this.inputTank.writeToNBT(provider, inputTankTag);
        tag.put("InputTank", inputTankTag);

        CompoundTag outputTankTag = new CompoundTag();
        this.outputTank.writeToNBT(provider, outputTankTag);
        tag.put("OutputTank", outputTankTag);

        tag.putInt("ProcessTime", this.processTime);
        tag.putInt("ProcessTimeTotal", this.processTimeTotal);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), this.items, provider);
        this.inputTank.readFromNBT(provider, tag.getCompound("InputTank"));
        this.outputTank.readFromNBT(provider, tag.getCompound("OutputTank"));
        this.processTime = tag.getInt("ProcessTime");
        this.processTimeTotal = tag.getInt("ProcessTimeTotal");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        this.loadAdditional(tag, provider);
    }
}
