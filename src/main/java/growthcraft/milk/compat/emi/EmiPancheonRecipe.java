package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.PancheonRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiPancheonRecipe extends BasicEmiRecipe {
    public EmiPancheonRecipe(RecipeHolder<PancheonRecipe> holder) {
        super(EmiPlugin.PANCHEON, holder.id(), 125, 20);
        PancheonRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getInputFluid().fluidId(), recipe.getInputFluid().amount()));
        recipe.getOutputFluids().forEach(output -> this.outputs.add(EmiRecipeUtil.fluid(output.fluidId(), output.amount())));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 27, 1);
        widgets.addTexture(EmiTexture.PLUS, 85, 3);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
        widgets.addSlot(outputs.get(1), 107, 0).recipeContext(this);
    }
}
