package growthcraft.cellar.block.entity;

import growthcraft.cellar.GrowthcraftCellar;
import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.init.GrowthcraftCellarBlockEntities;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.CultureJarRecipe;
import growthcraft.cellar.recipe.input.CultureJarInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Clearable;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Optional;

public class CultureJarBlockEntity extends BlockEntity implements WorldlyContainer, Clearable, net.minecraft.world.MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_COUNT = 2;
    public static final int TANK_CAPACITY = 1000; // 1 bucket

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final int[] TOP_SLOTS = new int[] { SLOT_INPUT };
    private final int[] BOTTOM_SLOTS = new int[] { SLOT_OUTPUT };
    private final int[] SIDE_SLOTS = new int[] { SLOT_INPUT, SLOT_OUTPUT };

    private final FluidTank tank = new FluidTank(TANK_CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            var fluid = getFluid();
            String name = fluid.isEmpty() ? "<empty>" : fluid.getHoverName().getString();
            GrowthcraftCellar.LOGGER.debug("[CultureJarBE] Tank changed at {}: {} mB {}", worldPosition, fluid.getAmount(), name);
            // Ensure clients are notified so GUIs and rendering update
            if (level != null && !level.isClientSide) {
                BlockState state = getBlockState();
                level.sendBlockUpdated(worldPosition, state, state, 3);
                // Proactively flag the chunk as changed so the BE data packet is sent reliably
                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.getChunkSource().blockChanged(worldPosition);
                }
                GrowthcraftCellar.LOGGER.debug("[CultureJarBE] Sent block update for GUI sync at {}", worldPosition);
            }
        }
    };

    private int processTime;
    private int processTimeTotal;

    public CultureJarBlockEntity(BlockPos pos, BlockState state) {
        super(GrowthcraftCellarBlockEntities.CULTURE_JAR.get(), pos, state);
        GrowthcraftCellar.LOGGER.info("[CultureJarBE] Constructed at {} (client={})", pos, state.getBlock().defaultMapColor().col);
    }

    // Inventory API
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
        ItemStack result = net.minecraft.world.ContainerHelper.removeItem(items, index, count);
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
        return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
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
        if (index == SLOT_OUTPUT) return false; // don't insert into output
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction side) {
        if (side == Direction.DOWN) return index == SLOT_OUTPUT; // bottom extracts output only
        return true;
    }

    public FluidTank getTank() {
        return tank;
    }

    // MenuProvider
    @Override
    public net.minecraft.network.chat.Component getDisplayName() {
        return net.minecraft.network.chat.Component.translatable("container.growthcraft_cellar.culture_jar");
    }

    @Override
    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int containerId, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        return new growthcraft.cellar.menu.CultureJarMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // Items
        CompoundTag itemsTag = new CompoundTag();
        net.minecraft.world.ContainerHelper.saveAllItems(itemsTag, this.items, provider);
        tag.put("Items", itemsTag);
        // Tank
        CompoundTag tankTag = new CompoundTag();
        this.tank.writeToNBT(provider, tankTag);
        tag.put("Tank", tankTag);
        // Processing
        tag.putInt("ProcessTime", this.processTime);
        tag.putInt("ProcessTimeTotal", this.processTimeTotal);
        GrowthcraftCellar.LOGGER.debug("[CultureJarBE] saveAdditional at {}: items={} tank={}mB time={}/{}", worldPosition, this.items.stream().filter(s -> !s.isEmpty()).count(), this.tank.getFluidAmount(), this.processTime, this.processTimeTotal);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        // Items
        CompoundTag itemsTag = tag.getCompound("Items");
        net.minecraft.world.ContainerHelper.loadAllItems(itemsTag, this.items, provider);
        // Tank
        CompoundTag tankTag = tag.getCompound("Tank");
        this.tank.readFromNBT(provider, tankTag);
        // Processing
        this.processTime = tag.getInt("ProcessTime");
        this.processTimeTotal = tag.getInt("ProcessTimeTotal");
        GrowthcraftCellar.LOGGER.debug("[CultureJarBE] loadAdditional at {}: items={} tank={}mB time={}/{}", worldPosition, this.items.stream().filter(s -> !s.isEmpty()).count(), this.tank.getFluidAmount(), this.processTime, this.processTimeTotal);
    }

    // --- Client sync for renderer/GUI ---
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Triggers handleUpdateTag on client, carrying getUpdateTag contents
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

    // --- Processing logic ---
    public static void serverTick(Level level, BlockPos pos, BlockState state, CultureJarBlockEntity jar) {
        if (level.isClientSide) return;

        // Periodically refresh LIT state from surroundings in case a neighbor change wasn't fired
        if ((level.getGameTime() & 19L) == 0L) { // every 20 ticks approx
            CultureJarBlock.updateLitState(level, pos, state);
            state = level.getBlockState(pos); // refresh local reference if changed
        }

        // must be lit to process
        if (!state.getValue(CultureJarBlock.LIT)) {
            if (jar.processTime != 0) {
                jar.processTime = 0;
                jar.processTimeTotal = 0;
                jar.setChanged();
            }
            return;
        }

        ItemStack input = jar.getItem(SLOT_INPUT);
        if (input.isEmpty()) {
            jar.resetProgress();
            return;
        }

        net.neoforged.neoforge.fluids.FluidStack inTank = jar.tank.getFluid();
        if (inTank.isEmpty()) {
            jar.resetProgress();
            return;
        }

        java.util.Optional<net.minecraft.world.item.crafting.RecipeHolder<CultureJarRecipe>> match = jar.findMatch(level, input, inTank);
        if (match.isEmpty()) {
            jar.resetProgress();
            return;
        }

        CultureJarRecipe recipe = match.get().value();
        // Check output room
        if (!jar.canOutput(recipe.getResult())) {
            jar.resetProgress();
            return;
        }

        // Progress
        jar.processTimeTotal = recipe.getTime();
        jar.processTime++;
        if (jar.processTime >= jar.processTimeTotal) {
            // Complete: consume inputs and produce output
            int toDrain = Math.max(1, recipe.getFluid().amount());
            jar.tank.drain(toDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE);
            input.shrink(1);
            jar.insertOutput(recipe.getResult());
            jar.processTime = 0;
            jar.processTimeTotal = 0;
            jar.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    private void resetProgress() {
        if (this.processTime != 0 || this.processTimeTotal != 0) {
            this.processTime = 0;
            this.processTimeTotal = 0;
            setChanged();
        }
    }

    private boolean canOutput(ItemStack stack) {
        ItemStack out = this.getItem(SLOT_OUTPUT);
        if (out.isEmpty()) return true;
        if (!ItemStack.isSameItem(out, stack)) return false;
        return out.getCount() + stack.getCount() <= out.getMaxStackSize();
    }

    private void insertOutput(ItemStack stack) {
        if (stack.isEmpty()) return;
        ItemStack out = this.getItem(SLOT_OUTPUT);
        if (out.isEmpty()) {
            this.setItem(SLOT_OUTPUT, stack.copy());
        } else if (ItemStack.isSameItem(out, stack)) {
            out.grow(stack.getCount());
        }
    }

    private Optional<RecipeHolder<CultureJarRecipe>> findMatch(Level level, ItemStack input, net.neoforged.neoforge.fluids.FluidStack tankFluid) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return Optional.empty();
        var rm = serverLevel.getRecipeManager();
        java.util.List<RecipeHolder<CultureJarRecipe>> list = rm.getAllRecipesFor(GrowthcraftCellarRecipes.CULTURE_JAR_TYPE.get());
        ResourceLocation tankId = net.minecraft.core.registries.BuiltInRegistries.FLUID.getKey(tankFluid.getFluid());
        for (RecipeHolder<CultureJarRecipe> holder : list) {
            CultureJarRecipe r = holder.value();
            if (!r.matches(new CultureJarInput(input), level)) continue;
            // heat requirement
            if (r.requiresHeatSource() && !level.getBlockState(this.worldPosition).getValue(CultureJarBlock.LIT)) continue;
            // fluid check: accept exact ID match, or fluids that share the same FluidType (source/flowing),
            // and be tolerant of recipes that reference base names without the "_source/_flowing" suffix.
            ResourceLocation reqId = r.getFluid().fluidId();
            boolean fluidOk = reqId.equals(tankId);
            if (!fluidOk) {
                net.minecraft.world.level.material.Fluid reqFluid = net.minecraft.core.registries.BuiltInRegistries.FLUID.get(reqId);
                net.minecraft.world.level.material.Fluid tankFluidType = tankFluid.getFluid();
                if (reqFluid != net.minecraft.world.level.material.Fluids.EMPTY) {
                    // Compare by FluidType to allow source/flowing to match
                    fluidOk = reqFluid.getFluidType() == tankFluidType.getFluidType();
                } else {
                    // Try common suffixes when the recipe used a base name
                    ResourceLocation baseSource = ResourceLocation.fromNamespaceAndPath(reqId.getNamespace(), reqId.getPath() + "_source");
                    ResourceLocation baseFlowing = ResourceLocation.fromNamespaceAndPath(reqId.getNamespace(), reqId.getPath() + "_flowing");
                    fluidOk = baseSource.equals(tankId) || baseFlowing.equals(tankId);
                }
            }
            if (!fluidOk) continue;
            if (tankFluid.getAmount() < r.getFluid().amount()) continue;
            return Optional.of(holder);
        }
        return Optional.empty();
    }
}
