package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.CheesePressRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiCheesePressRecipe extends BasicEmiRecipe {
    public EmiCheesePressRecipe(RecipeHolder<CheesePressRecipe> holder) {
        super(EmiPlugin.CHEESE_PRESS, holder.id(), 82, 20);
        CheesePressRecipe recipe = holder.value();
        this.inputs.add(EmiStack.of(recipe.getInputItem()));
        this.outputs.add(EmiStack.of(recipe.getResultItem()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
    }
}
