package growthcraft.milk.recipe.input;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public final class MixingVatInput implements RecipeInput {
    private final FluidStack inputFluid;
    private final FluidStack reagentFluid;
    private final NonNullList<ItemStack> items;
    private final boolean heated;

    public MixingVatInput(FluidStack inputFluid, FluidStack reagentFluid, NonNullList<ItemStack> items, boolean heated) {
        this.inputFluid = inputFluid;
        this.reagentFluid = reagentFluid;
        this.items = items;
        this.heated = heated;
    }

    public FluidStack inputFluid() {
        return inputFluid;
    }

    public FluidStack reagentFluid() {
        return reagentFluid;
    }

    public boolean heated() {
        return heated;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }
}
