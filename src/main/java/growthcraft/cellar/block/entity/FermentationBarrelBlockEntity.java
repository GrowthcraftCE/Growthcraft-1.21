package growthcraft.cellar.block.entity;

import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.recipe.input.FermentationBarrelInput;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class FermentationBarrelBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider {
    public static final int SLOT_YEAST = 0;
    public static final int SLOT_COUNT = 1;
    public static final int TANK_CAPACITY = 4000;
    public static final int BOTTLE_AMOUNT = 500;

    private static final int[] SLOTS = new int[] { SLOT_YEAST };

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private final FluidTank tank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                BlockState state = getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, 3);
            }
        }
    };

    private int processTime;
    private int processTimeTotal;
    private boolean yeastWarning;
    private boolean yeastError;

    public FermentationBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.FERMENTATION_BARREL.get(), pos, state);
    }

    public FluidTank getTank() {
        return tank;
    }

    public ItemStack getResultingPotionItemStack() {
        if (this.level == null || this.tank.getFluidAmount() < BOTTLE_AMOUNT) {
            return ItemStack.EMPTY;
        }

        FluidStack fluid = this.tank.getFluid();
        if (fluid.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return this.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .filter(recipe -> matchesFluidId(recipe.getResult().fluidId(), fluid.getFluid()))
                .map(FermentationBarrelRecipe::getBottle)
                .filter(stack -> !stack.isEmpty())
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    public void drainBottleAmount(BlockState state) {
        this.tank.drain(BOTTLE_AMOUNT, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
        this.resetProgress();
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public boolean hasYeastWarning() {
        return yeastWarning;
    }

    public boolean hasYeastError() {
        return yeastError;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FermentationBarrelBlockEntity barrel) {
        if (level.isClientSide) return;

        ItemStack yeast = barrel.getItem(SLOT_YEAST);
        FluidStack fluid = barrel.tank.getFluid();
        if (fluid.isEmpty() || yeast.isEmpty()) {
            barrel.resetProgress();
            barrel.setYeastState(false, false, state);
            return;
        }

        Optional<RecipeHolder<FermentationBarrelRecipe>> match = barrel.findMatch(level, yeast, fluid);
        if (match.isEmpty()) {
            barrel.resetProgress();
            barrel.updateYeastState(level, state, yeast, fluid);
            return;
        }

        FermentationBarrelRecipe recipe = match.get().value();
        int multiplier = recipe.getOutputMultiplier(new FermentationBarrelInput(yeast, fluid));
        FluidStack output = barrel.outputFluidStack(recipe, multiplier);
        if (output.isEmpty() || output.getAmount() > TANK_CAPACITY) {
            barrel.resetProgress();
            return;
        }

        barrel.setYeastState(false, false, state);
        barrel.processTimeTotal = recipe.getProcessingTime();
        barrel.processTime++;
        if (barrel.processTime >= barrel.processTimeTotal) {
            barrel.completeRecipe(level, pos, state, recipe, output, multiplier);
        } else if ((barrel.processTime & 15) == 0) {
            barrel.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private Optional<RecipeHolder<FermentationBarrelRecipe>> findMatch(Level level, ItemStack yeast, FluidStack fluid) {
        FermentationBarrelInput input = new FermentationBarrelInput(yeast, fluid);
        return level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(input, level))
                .findFirst();
    }

    private void updateYeastState(Level level, BlockState state, ItemStack yeast, FluidStack fluid) {
        boolean fluidRecipeFound = false;
        boolean matchingYeastFound = false;
        for (RecipeHolder<FermentationBarrelRecipe> holder : level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get())) {
            FermentationBarrelRecipe recipe = holder.value();
            if (!matchesFluidId(recipe.getIngredientFluid().fluidId(), fluid.getFluid())) continue;
            int multiplier = recipe.getOutputMultiplier(new FermentationBarrelInput(yeast, fluid));
            if (multiplier <= 0) continue;
            fluidRecipeFound = true;
            if (recipe.getIngredientItem().accepts(yeast)) {
                matchingYeastFound = true;
                if (yeast.getCount() < recipe.getIngredientItem().count() * multiplier) {
                    setYeastState(true, false, state);
                    return;
                }
            }
        }
        setYeastState(false, fluidRecipeFound && !matchingYeastFound, state);
    }

    private void setYeastState(boolean warning, boolean error, BlockState state) {
        if (this.yeastWarning == warning && this.yeastError == error) return;
        this.yeastWarning = warning;
        this.yeastError = error;
        setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    private FluidStack outputFluidStack(FermentationBarrelRecipe recipe, int multiplier) {
        if (multiplier <= 0) return FluidStack.EMPTY;
        FermentationBarrelRecipe.FluidAmount result = recipe.getResult();
        if (result.amount() <= 0 || ResourceLocation.withDefaultNamespace("air").equals(result.fluidId())) {
            return FluidStack.EMPTY;
        }
        var fluid = BuiltInRegistries.FLUID.get(result.fluidId());
        if (fluid == Fluids.EMPTY) return FluidStack.EMPTY;
        return new FluidStack(fluid, result.amount() * multiplier);
    }

    private void completeRecipe(Level level, BlockPos pos, BlockState state, FermentationBarrelRecipe recipe, FluidStack output, int multiplier) {
        this.getItem(SLOT_YEAST).shrink(recipe.getIngredientItem().count() * multiplier);
        this.tank.setFluid(output);
        this.resetProgress();
        this.setYeastState(false, false, state);
        this.setChanged();
        level.sendBlockUpdated(pos, state, state, 3);
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
        return this.items.get(SLOT_YEAST).isEmpty();
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
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_YEAST;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_YEAST;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_cellar.fermentation_barrel");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FermentationBarrelMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, this.items, provider);
        tag.put("Items", itemsTag);

        CompoundTag tankTag = new CompoundTag();
        this.tank.writeToNBT(provider, tankTag);
        tag.put("Tank", tankTag);

        tag.putInt("ProcessTime", this.processTime);
        tag.putInt("ProcessTimeTotal", this.processTimeTotal);
        tag.putBoolean("YeastWarning", this.yeastWarning);
        tag.putBoolean("YeastError", this.yeastError);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), this.items, provider);
        this.tank.readFromNBT(provider, tag.getCompound("Tank"));
        this.processTime = tag.getInt("ProcessTime");
        this.processTimeTotal = tag.getInt("ProcessTimeTotal");
        this.yeastWarning = tag.getBoolean("YeastWarning");
        this.yeastError = tag.getBoolean("YeastError");
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
