package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.FruitPressRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiPressRecipe extends BasicEmiRecipe {
    public EmiPressRecipe(RecipeHolder<FruitPressRecipe> holder) {
        super(EmiPlugin.PRESS, holder.id(), 82, 20);
        FruitPressRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.ingredient(recipe.getInputItem().ingredient(), recipe.getInputItem().count()));
        this.outputs.add(EmiRecipeUtil.fluid(recipe.getOutputFluid().fluidId(), recipe.getOutputFluid().amount()));
        if (!recipe.getByProduct().isEmpty()) {
            this.outputs.add(EmiRecipeUtil.item(recipe.getByProduct()));
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}
