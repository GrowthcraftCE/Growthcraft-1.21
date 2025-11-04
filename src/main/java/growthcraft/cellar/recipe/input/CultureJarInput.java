package growthcraft.cellar.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Simple single-slot recipe input used by the Culture Jar recipe matching.
 * The Culture Jar block entity provides the actual item stack from its input slot.
 */
public final class CultureJarInput implements RecipeInput {
    private final ItemStack item;

    public CultureJarInput(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
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
