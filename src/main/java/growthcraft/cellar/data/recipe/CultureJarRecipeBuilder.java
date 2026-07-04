package growthcraft.cellar.data.recipe;

import growthcraft.cellar.config.Reference;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Data generator helper for Culture Jar recipes.
 */
public class CultureJarRecipeBuilder {
    private final Ingredient ingredient;
    private final ResourceLocation fluidId;
    private final int amount;
    private final Item result;
    private final int count;
    private Integer time; // optional, default 300
    private Boolean requiresHeat; // optional, default true

    private CultureJarRecipeBuilder(Ingredient ingredient, ResourceLocation fluidId, int amount, Item result, int count) {
        this.ingredient = ingredient;
        this.fluidId = fluidId;
        this.amount = amount;
        this.result = result;
        this.count = count;
    }

    public static CultureJarRecipeBuilder culture(Ingredient ingredient, ResourceLocation fluidId, int amount, Item result, int count) {
        return new CultureJarRecipeBuilder(ingredient, fluidId, amount, result, count);
    }

    // Convenience for the first recipe id
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MODID, path);
    }

    public CultureJarRecipeBuilder time(int ticks) {
        this.time = ticks;
        return this;
    }

    public CultureJarRecipeBuilder requiresHeat(boolean value) {
        this.requiresHeat = value;
        return this;
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        // TODO: Implement FinishedRecipe-compatible emission using RecipeOutput for MC 1.21.1
        // Temporarily no-op to keep compilation green until full datagen integration step.
    }
}
