package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.RoasterRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class EmiRoasterRecipe extends BasicEmiRecipe {
    private static final EmiTexture TIME_ICON = new EmiTexture(
            ResourceLocation.fromNamespaceAndPath("growthcraft_cellar", "textures/gui/brew_kettle_screen.png"),
            54, 184, 11, 11);

    private final int roastingLevel;

    public EmiRoasterRecipe(RecipeHolder<RoasterRecipe> holder) {
        super(EmiPlugin.ROASTER, holder.id(), 125, 30);
        RoasterRecipe recipe = holder.value();
        this.inputs.add(EmiStack.of(recipe.getInputItem()));
        this.outputs.add(EmiStack.of(recipe.getResult()));
        this.roastingLevel = recipe.getRoastingLevel();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 23, 1);
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addSlot(outputs.get(0), 52, 0).recipeContext(this);
        widgets.addText(Component.literal("Level " + roastingLevel), 4, 20, 0xFF444433, false);
        widgets.addTexture(TIME_ICON, 84, 3);
    }
}
