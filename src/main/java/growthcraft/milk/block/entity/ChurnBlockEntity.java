package growthcraft.milk.block.entity;

import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.input.ChurnInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class ChurnBlockEntity extends BlockEntity implements WorldlyContainer, Clearable {
    public static final int SLOT_BYPRODUCT = 0;
    public static final int SLOT_COUNT = 1;
    public static final int TANK_CAPACITY = 1000;

    private static final int[] SLOTS = new int[] { SLOT_BYPRODUCT };

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

    private int plungeCount;
    private int plungesNeeded;

    public ChurnBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.CHURN.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ChurnBlockEntity churn) {
        // Manual machine. Work happens in tryPlunger().
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getPlungeCount() {
        return plungeCount;
    }

    public int getPlungesNeeded() {
        return plungesNeeded;
    }

    public void tryPlunger() {
        if (level == null || level.isClientSide || tank.isEmpty() || !getItem(SLOT_BYPRODUCT).isEmpty()) {
            return;
        }

        Optional<RecipeHolder<ChurnRecipe>> match = findMatch(level, tank.getFluid());
        if (match.isEmpty()) {
            resetProgress();
            return;
        }

        ChurnRecipe recipe = match.get().value();
        plungesNeeded = recipe.getPlungesNeeded();
        plungeCount++;
        if (plungeCount >= plungesNeeded) {
            tank.setFluid(recipe.getOutputFluidStack());
            maybeSetByProduct(recipe);
            resetProgress();
        }

        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    private Optional<RecipeHolder<ChurnRecipe>> findMatch(Level level, FluidStack fluid) {
        ChurnInput input = new ChurnInput(fluid);
        return level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHURN_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(input, level))
                .findFirst();
    }

    private void maybeSetByProduct(ChurnRecipe recipe) {
        ItemStack byProduct = recipe.getByProduct();
        if (byProduct.isEmpty()) {
            return;
        }
        if (level != null && level.random.nextInt(100) < recipe.getByProductChance()) {
            setItem(SLOT_BYPRODUCT, byProduct);
        }
    }

    private void resetProgress() {
        plungeCount = 0;
        plungesNeeded = 0;
    }

    public boolean collectByProduct(Player player) {
        ItemStack byProduct = getItem(SLOT_BYPRODUCT);
        if (byProduct.isEmpty()) {
            return false;
        }

        ItemStack toGive = byProduct.copy();
        setItem(SLOT_BYPRODUCT, ItemStack.EMPTY);
        if (!player.addItem(toGive)) {
            player.drop(toGive, false);
        }
        return true;
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
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_BYPRODUCT;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, items, provider);
        tag.put("Items", itemsTag);

        CompoundTag tankTag = new CompoundTag();
        tank.writeToNBT(provider, tankTag);
        tag.put("Tank", tankTag);

        tag.putInt("PlungeCount", plungeCount);
        tag.putInt("PlungesNeeded", plungesNeeded);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), items, provider);
        tank.readFromNBT(provider, tag.getCompound("Tank"));
        plungeCount = tag.getInt("PlungeCount");
        plungesNeeded = tag.getInt("PlungesNeeded");
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
}
