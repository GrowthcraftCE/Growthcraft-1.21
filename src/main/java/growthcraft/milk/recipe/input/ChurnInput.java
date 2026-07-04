package growthcraft.milk.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public final class ChurnInput implements RecipeInput {
    private final FluidStack fluid;

    public ChurnInput(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidStack fluid() {
        return fluid;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }
}
