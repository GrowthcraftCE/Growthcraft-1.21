package growthcraft.cellar.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public final class BrewKettleInput implements RecipeInput {
    private final ItemStack item;

    public BrewKettleInput(ItemStack item) {
        this.item = item;
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
