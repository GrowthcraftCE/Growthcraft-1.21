package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.MixingVatRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiMixingVatRecipe extends BasicEmiRecipe {
    private final int itemInputCount;
    private final boolean hasReagentFluid;
    private final boolean hasResultFluid;
    private final boolean hasResultItem;

    public EmiMixingVatRecipe(RecipeHolder<MixingVatRecipe> holder) {
        super(EmiPlugin.MIXING_VAT, holder.id(), 146, 42);
        MixingVatRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getInputFluid().fluidId(), recipe.getInputFluid().amount()));
        recipe.getReagentFluid().ifPresent(fluid -> this.inputs.add(EmiRecipeUtil.fluid(fluid.fluidId(), fluid.amount())));
        for (MixingVatRecipe.IngredientStack ingredient : recipe.getIngredientStacks()) {
            this.inputs.add(EmiRecipeUtil.ingredient(ingredient.ingredient(), ingredient.count()));
        }
        if (!recipe.getActivationTool().isEmpty()) {
            this.inputs.add(EmiStack.of(recipe.getActivationTool()));
        }

        recipe.getResultFluid().ifPresent(fluid -> this.outputs.add(EmiRecipeUtil.fluid(fluid.fluidId(), fluid.amount())));
        if (!recipe.getResultItemStack().isEmpty()) {
            this.outputs.add(EmiStack.of(recipe.getResultItemStack()));
        }
        if (!recipe.getResultActivationTool().isEmpty()) {
            this.outputs.add(EmiStack.of(recipe.getResultActivationTool()));
        }

        this.hasReagentFluid = recipe.getReagentFluid().isPresent();
        this.itemInputCount = recipe.getIngredientStacks().size();
        this.hasResultFluid = recipe.getResultFluid().isPresent();
        this.hasResultItem = !recipe.getResultItemStack().isEmpty();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 75, 12);
        widgets.addSlot(inputs.get(0), 0, 12);
        int inputIndex = 1;
        if (hasReagentFluid) {
            widgets.addSlot(inputs.get(inputIndex++), 24, 12);
        }
        for (int i = 0; i < itemInputCount; i++) {
            widgets.addSlot(inputs.get(inputIndex++), 24 + i * 20, 0);
        }
        if (inputIndex < inputs.size()) {
            widgets.addSlot(inputs.get(inputIndex), 24, 24);
        }

        int outputIndex = 0;
        if (hasResultFluid) {
            widgets.addSlot(outputs.get(outputIndex++), 107, 12).recipeContext(this);
        }
        if (hasResultItem) {
            widgets.addSlot(outputs.get(outputIndex++), 127, 0).recipeContext(this);
        }
        if (outputIndex < outputs.size()) {
            widgets.addSlot(outputs.get(outputIndex), 127, 24).recipeContext(this);
        }
    }
}
