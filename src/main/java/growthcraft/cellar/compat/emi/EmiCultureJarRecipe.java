package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.CultureJarRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiCultureJarRecipe extends BasicEmiRecipe {
    public EmiCultureJarRecipe(RecipeHolder<CultureJarRecipe> holder) {
        super(EmiPlugin.JAR, holder.id(), 125, 20);
        CultureJarRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getFluid().fluidId(), recipe.getFluid().amount()));
        this.inputs.add(EmiRecipeUtil.ingredient(recipe.getIngredient(), 1));
        this.outputs.add(EmiRecipeUtil.item(recipe.getResult()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.PLUS, 27, 3);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(inputs.get(1), 49, 0);
        widgets.addSlot(outputs.get(0), 107, 0).recipeContext(this);
    }
}
