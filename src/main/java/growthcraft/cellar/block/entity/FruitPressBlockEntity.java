package growthcraft.cellar.block.entity;

import growthcraft.cellar.block.FruitPressPistonBlock;
import growthcraft.cellar.block.FruitPressBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.menu.FruitPressMenu;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.recipe.input.FruitPressInput;
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
import net.minecraft.util.RandomSource;
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class FruitPressBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int TANK_CAPACITY = 4000;

    private static final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private static final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private static final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

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

    public FruitPressBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.FRUIT_PRESS.get(), pos, state);
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getProcessTimeTotal() {
        return processTimeTotal;
    }

    public boolean isPressed() {
        if (level == null) return false;
        BlockState pistonState = level.getBlockState(worldPosition.above());
        return pistonState.is(growthcraft.cellar.init.GrowthcraftCellarBlocks.FRUIT_PRESS_PISTON.get())
                && pistonState.getValue(FruitPressPistonBlock.PRESSED);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FruitPressBlockEntity press) {
        if (level.isClientSide) return;

        if (!press.isPressed()) {
            press.resetProgress(level, pos, state);
            return;
        }

        ItemStack input = press.getItem(SLOT_INPUT);
        if (input.isEmpty()) {
            press.resetProgress(level, pos, state);
            return;
        }

        Optional<RecipeHolder<FruitPressRecipe>> match = press.findMatch(level, input);
        if (match.isEmpty()) {
            press.resetProgress(level, pos, state);
            return;
        }

        FruitPressRecipe recipe = match.get().value();
        FluidStack output = press.outputFluidStack(recipe);
        if (output.isEmpty() || press.tank.fill(output.copy(), IFluidHandler.FluidAction.SIMULATE) < output.getAmount()) {
            press.resetProgress(level, pos, state);
            return;
        }

        if (recipe.getByProductChance() >= 100 && !press.canOutputByProduct(recipe.getByProduct())) {
            press.resetProgress(level, pos, state);
            return;
        }

        press.processTimeTotal = recipe.getProcessingTime();
        press.processTime++;
        if (press.processTime >= press.processTimeTotal) {
            press.completeRecipe(level, pos, state, recipe, output);
        } else if ((press.processTime & 15) == 0) {
            press.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, FruitPressBlockEntity press) {
        if (!press.isProcessing() || !press.isPressed()) {
            return;
        }

        RandomSource random = level.random;
        if (random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                FruitPressBlock.makeParticles(level, pos, state, press);
            }
        }
    }

    private Optional<RecipeHolder<FruitPressRecipe>> findMatch(Level level, ItemStack input) {
        FruitPressInput recipeInput = new FruitPressInput(input);
        return level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(recipeInput, level))
                .findFirst();
    }

    private FluidStack outputFluidStack(FruitPressRecipe recipe) {
        FruitPressRecipe.FluidAmount output = recipe.getOutputFluid();
        if (output.amount() <= 0 || ResourceLocation.withDefaultNamespace("air").equals(output.fluidId())) {
            return FluidStack.EMPTY;
        }
        var fluid = BuiltInRegistries.FLUID.get(output.fluidId());
        if (fluid == Fluids.EMPTY) return FluidStack.EMPTY;
        return new FluidStack(fluid, output.amount());
    }

    public FluidStack getActiveOutputFluidStack(Level level) {
        if (!isProcessing()) {
            return FluidStack.EMPTY;
        }
        return findMatch(level, getItem(SLOT_INPUT))
                .map(holder -> outputFluidStack(holder.value()))
                .orElse(FluidStack.EMPTY);
    }

    private boolean canOutputByProduct(ItemStack stack) {
        if (stack.isEmpty()) return true;
        ItemStack output = this.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(output, stack)) return false;
        return output.getCount() + stack.getCount() <= output.getMaxStackSize();
    }

    private void completeRecipe(Level level, BlockPos pos, BlockState state, FruitPressRecipe recipe, FluidStack outputFluid) {
        this.getItem(SLOT_INPUT).shrink(recipe.getInputItem().count());
        this.tank.fill(outputFluid, IFluidHandler.FluidAction.EXECUTE);
        maybeInsertByProduct(level, recipe);
        this.resetProgress();
        this.setChanged();
        level.sendBlockUpdated(pos, state, state, 3);
    }

    private void maybeInsertByProduct(Level level, FruitPressRecipe recipe) {
        ItemStack byProduct = recipe.getByProduct();
        if (byProduct.isEmpty() || recipe.getByProductChance() <= 0) return;
        if (level.random.nextInt(100) >= recipe.getByProductChance()) return;
        if (!canOutputByProduct(byProduct)) return;

        ItemStack output = this.getItem(SLOT_OUTPUT);
        if (output.isEmpty()) {
            this.setItem(SLOT_OUTPUT, byProduct);
        } else if (ItemStack.isSameItemSameComponents(output, byProduct)) {
            output.grow(byProduct.getCount());
        }
    }

    private boolean resetProgress() {
        if (this.processTime != 0 || this.processTimeTotal != 0) {
            this.processTime = 0;
            this.processTimeTotal = 0;
            setChanged();
            return true;
        }
        return false;
    }

    private void resetProgress(Level level, BlockPos pos, BlockState state) {
        if (resetProgress()) {
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public boolean isProcessing() {
        return processTime > 0 && processTimeTotal > 0;
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
        return Component.translatable("container.growthcraft_cellar.fruit_press");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FruitPressMenu(containerId, playerInventory, this);
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
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), this.items, provider);
        this.tank.readFromNBT(provider, tag.getCompound("Tank"));
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
