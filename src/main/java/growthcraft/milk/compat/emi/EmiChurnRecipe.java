package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.ChurnRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class EmiChurnRecipe extends BasicEmiRecipe {
    private final boolean hasByProduct;
    private final int byProductChance;

    public EmiChurnRecipe(RecipeHolder<ChurnRecipe> holder) {
        super(EmiPlugin.CHURN, holder.id(), 125, 20);
        ChurnRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getInputFluid().fluidId(), recipe.getInputFluid().amount()));
        this.outputs.add(EmiRecipeUtil.fluid(recipe.getOutputFluid().fluidId(), recipe.getOutputFluid().amount()));
        if (!recipe.getByProduct().isEmpty()) {
            this.outputs.add(dev.emi.emi.api.stack.EmiStack.of(recipe.getByProduct()));
        }
        this.hasByProduct = !recipe.getByProduct().isEmpty() && recipe.getByProductChance() > 0;
        this.byProductChance = recipe.getByProductChance();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 58, 0).recipeContext(this);
        if (hasByProduct) {
            widgets.addTexture(EmiTexture.PLUS, 84, 3);
            widgets.addSlot(outputs.get(1), 107, 0);
            widgets.addTooltipText(List.of(Component.literal(byProductChance + "%")), 84, 1, 14, 14);
        }
    }
}
