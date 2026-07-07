package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiBarrelRecipe extends BasicEmiRecipe {
    public EmiBarrelRecipe(RecipeHolder<FermentationBarrelRecipe> holder) {
        super(EmiPlugin.BARREL, holder.id(), 125, 20);
        FermentationBarrelRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getIngredientFluid().fluidId(), recipe.getIngredientFluid().amount()));
        this.inputs.add(EmiRecipeUtil.ingredient(recipe.getIngredientItem().ingredient(), recipe.getIngredientItem().count()));
        this.outputs.add(EmiRecipeUtil.fluid(recipe.getResult().fluidId(), recipe.getResult().amount()));
        this.outputs.add(EmiRecipeUtil.item(recipe.getBottle()));
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
