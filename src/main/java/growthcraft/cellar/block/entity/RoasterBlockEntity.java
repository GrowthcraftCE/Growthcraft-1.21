package growthcraft.cellar.block.entity;

import growthcraft.cellar.block.RoasterBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.menu.RoasterMenu;
import growthcraft.cellar.recipe.RoasterRecipe;
import growthcraft.cellar.recipe.input.RoasterInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class RoasterBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;
    private static final int TICKS_PER_LEVEL = 600;

    private static final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private static final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private static final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int processTime;
    private int processTimeTotal;

    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> processTime;
                case 1 -> processTimeTotal;
                case 2 -> getBlockState().getValue(RoasterBlock.ROASTING_LEVEL);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> processTime = value;
                case 1 -> processTimeTotal = value;
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public RoasterBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.ROASTER.get(), pos, state);
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
        return Component.translatable("container.growthcraft_cellar.roaster");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RoasterMenu(containerId, playerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, items, provider);
        tag.put("Items", itemsTag);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessTimeTotal", processTimeTotal);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), items, provider);
        processTime = tag.getInt("ProcessTime");
        processTimeTotal = tag.getInt("ProcessTimeTotal");
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, RoasterBlockEntity roaster) {
        if ((level.getGameTime() & 19L) == 0L) {
            RoasterBlock.updateLitState(level, pos, state);
            state = level.getBlockState(pos);
        }

        if (!state.getValue(RoasterBlock.LIT)) {
            roaster.resetProgress();
            return;
        }

        ItemStack input = roaster.getItem(SLOT_INPUT);
        if (input.isEmpty()) {
            roaster.resetProgress();
            return;
        }

        Optional<RecipeHolder<RoasterRecipe>> match = roaster.findMatch(level, input, state.getValue(RoasterBlock.ROASTING_LEVEL));
        if (match.isEmpty()) {
            roaster.resetProgress();
            return;
        }

        RoasterRecipe recipe = match.get().value();
        int batchSize = roaster.getBatchSize(recipe, input);
        if (batchSize <= 0) {
            roaster.resetProgress();
            return;
        }

        roaster.processTimeTotal = Math.max(1, recipe.getRoastingLevel() * TICKS_PER_LEVEL);
        roaster.processTime++;
        if (roaster.processTime >= roaster.processTimeTotal) {
            roaster.completeRecipe(recipe, batchSize);
            roaster.processTime = 0;
            roaster.processTimeTotal = 0;
            roaster.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private Optional<RecipeHolder<RoasterRecipe>> findMatch(Level level, ItemStack input, int roastingLevel) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return Optional.empty();
        return serverLevel.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.ROASTER_TYPE.get()).stream()
                .filter(holder -> holder.value().getRoastingLevel() == roastingLevel)
                .filter(holder -> holder.value().matches(new RoasterInput(input), level))
                .findFirst();
    }

    private int getBatchSize(RoasterRecipe recipe, ItemStack input) {
        ItemStack required = recipe.getInputItem();
        ItemStack result = recipe.getResult();
        if (required.isEmpty() || result.isEmpty()) return 0;

        int possibleFromInput = input.getCount() / required.getCount();
        ItemStack output = getItem(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return Math.min(possibleFromInput, result.getMaxStackSize() / result.getCount());
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) return 0;
        int remainingRoom = output.getMaxStackSize() - output.getCount();
        return Math.min(possibleFromInput, remainingRoom / result.getCount());
    }

    private void completeRecipe(RoasterRecipe recipe, int batchSize) {
        ItemStack required = recipe.getInputItem();
        ItemStack result = recipe.getResult();
        getItem(SLOT_INPUT).shrink(required.getCount() * batchSize);

        ItemStack output = getItem(SLOT_OUTPUT);
        int resultCount = result.getCount() * batchSize;
        if (output.isEmpty()) {
            ItemStack produced = result.copy();
            produced.setCount(resultCount);
            setItem(SLOT_OUTPUT, produced);
        } else {
            output.grow(resultCount);
        }
    }

    private void resetProgress() {
        if (processTime != 0 || processTimeTotal != 0) {
            processTime = 0;
            processTimeTotal = 0;
            setChanged();
        }
    }
}
