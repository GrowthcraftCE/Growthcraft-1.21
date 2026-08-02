package growthcraft.milk.block.entity;

import growthcraft.milk.block.CheesePressBlock;
import growthcraft.milk.init.GrowthcraftMilkBlockEntities;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.CheesePressRecipe;
import growthcraft.milk.recipe.input.CheesePressInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CheesePressBlockEntity extends BlockEntity implements WorldlyContainer, Clearable {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;

    private static final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private static final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private static final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int processTime;
    private int processTimeTotal;
    private int rotation;

    public CheesePressBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftMilkBlockEntities.CHEESE_PRESS.get(), pos, state);
        this.rotation = state.getValue(CheesePressBlock.ROTATION);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CheesePressBlockEntity press) {
        press.rotation = state.getValue(CheesePressBlock.ROTATION);
        if (press.isOpen() || press.getItem(SLOT_INPUT).isEmpty()) {
            press.resetProgress();
            return;
        }

        Optional<RecipeHolder<CheesePressRecipe>> match = press.findMatch(level, press.getItem(SLOT_INPUT));
        if (match.isEmpty()) {
            press.resetProgress();
            return;
        }

        CheesePressRecipe recipe = match.get().value();
        if (!press.canOutput(recipe.getResultItem())) {
            press.resetProgress();
            return;
        }

        press.processTimeTotal = recipe.getProcessingTime();
        press.processTime++;
        if (press.processTime >= press.processTimeTotal) {
            ItemStack required = recipe.getInputItem();
            press.getItem(SLOT_INPUT).shrink(required.getCount());
            if (press.getItem(SLOT_INPUT).isEmpty()) {
                press.setItem(SLOT_INPUT, ItemStack.EMPTY);
            }
            press.addOutput(recipe.getResultItem());
            press.processTime = 0;
            press.processTimeTotal = 0;
            press.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        } else if ((press.processTime & 15) == 0) {
            press.setChanged();
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CheesePressBlockEntity press) {
        RandomSource random = level.random;
        if (random.nextFloat() < 0.11F) {
            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                CheesePressBlock.makeParticles(level, pos, state);
            }
        }
    }

    private Optional<RecipeHolder<CheesePressRecipe>> findMatch(Level level, ItemStack input) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return Optional.empty();
        }
        CheesePressInput recipeInput = new CheesePressInput(input);
        return serverLevel.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get()).stream()
                .filter(holder -> holder.value().matches(recipeInput, level))
                .findFirst();
    }

    private boolean canOutput(ItemStack result) {
        if (result.isEmpty()) {
            return false;
        }
        ItemStack output = getItem(SLOT_OUTPUT);
        return output.isEmpty() || (ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize());
    }

    private void addOutput(ItemStack result) {
        ItemStack output = getItem(SLOT_OUTPUT);
        if (output.isEmpty()) {
            setItem(SLOT_OUTPUT, result);
        } else {
            output.grow(result.getCount());
            setChanged();
        }
    }

    public int toggleOpenClosed() {
        rotation = isOpen() ? 7 : 0;
        if (isOpen()) {
            resetProgress();
        }
        setChanged();
        return rotation;
    }

    public boolean isOpen() {
        return rotation == 0;
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

    public boolean hasContent() {
        return !getItem(SLOT_INPUT).isEmpty() || !getItem(SLOT_OUTPUT).isEmpty();
    }

    public boolean hasMatchingRecipe(Level level) {
        return findMatch(level, getItem(SLOT_INPUT)).isPresent();
    }

    public boolean canProcessInput(Level level) {
        Optional<RecipeHolder<CheesePressRecipe>> match = findMatch(level, getItem(SLOT_INPUT));
        return match.isPresent() && canOutput(match.get().value().getResultItem());
    }

    private void resetProgress() {
        if (processTime != 0 || processTimeTotal != 0) {
            processTime = 0;
            processTimeTotal = 0;
            setChanged();
        }
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
        if (side == Direction.UP) {
            return TOP_SLOTS;
        }
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        }
        return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_INPUT && isOpen();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        return index == SLOT_OUTPUT && isOpen();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        CompoundTag itemsTag = new CompoundTag();
        ContainerHelper.saveAllItems(itemsTag, items, provider);
        tag.put("Items", itemsTag);
        tag.putInt("ProcessTime", processTime);
        tag.putInt("ProcessTimeTotal", processTimeTotal);
        tag.putInt("Rotation", rotation);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ContainerHelper.loadAllItems(tag.getCompound("Items"), items, provider);
        processTime = tag.getInt("ProcessTime");
        processTimeTotal = tag.getInt("ProcessTimeTotal");
        rotation = tag.getInt("Rotation");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        loadAdditional(pkt.getTag(), lookupProvider);
    }
}
