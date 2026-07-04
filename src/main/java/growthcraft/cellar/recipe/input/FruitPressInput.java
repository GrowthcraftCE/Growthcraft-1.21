package growthcraft.cellar.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public final class FruitPressInput implements RecipeInput {
    private final ItemStack input;

    public FruitPressInput(ItemStack input) {
        this.input = input;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(index);
        }
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
