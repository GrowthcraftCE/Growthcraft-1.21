package growthcraft.cellar.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public final class FermentationBarrelInput implements RecipeInput {
    private final ItemStack item;
    private final FluidStack fluid;

    public FermentationBarrelInput(ItemStack item, FluidStack fluid) {
        this.item = item;
        this.fluid = fluid;
    }

    public FluidStack fluid() {
        return fluid;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public ItemStack getItem(int index) {
        return index == 0 ? item : ItemStack.EMPTY;
    }
}
