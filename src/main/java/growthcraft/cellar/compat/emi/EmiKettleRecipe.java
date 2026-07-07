package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class EmiKettleRecipe extends BasicEmiRecipe {
    private static final ResourceLocation WIDGETS = ResourceLocation.fromNamespaceAndPath("growthcraft", "textures/gui/widgets.png");
    private static final EmiTexture SMALL_PLUS = new EmiTexture(WIDGETS, 83, 1, 10, 10);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);

    private final boolean hasByProduct;
    private final int byProductChance;
    private final boolean requiresLid;

    public EmiKettleRecipe(RecipeHolder<BrewKettleRecipe> holder) {
        super(EmiPlugin.KETTLE, holder.id(), 125, 20);
        BrewKettleRecipe recipe = holder.value();
        this.inputs.add(EmiRecipeUtil.fluid(recipe.getInputFluid().fluidId(), recipe.getInputFluid().amount()));
        this.inputs.add(EmiRecipeUtil.ingredient(recipe.getInputItem().ingredient(), recipe.getInputItem().count()));
        this.outputs.add(EmiRecipeUtil.fluid(recipe.getOutputFluid().fluidId(), recipe.getOutputFluid().amount()));
        if (!recipe.getByProduct().isEmpty()) {
            this.outputs.add(EmiRecipeUtil.item(recipe.getByProduct()));
        }
        this.hasByProduct = !recipe.getByProduct().isEmpty() && recipe.getByProductChance() > 0;
        this.byProductChance = recipe.getByProductChance();
        this.requiresLid = recipe.requiresLid();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(inputs.get(0), 0, 0);
        widgets.addTexture(SMALL_PLUS, 20, 3);
        widgets.addSlot(inputs.get(1), 32, 0);
        widgets.addTexture(SMALL_ARROW, 52, 1);
        widgets.addTooltipText(List.of(Component.translatable(requiresLid
                ? "message.growthcraft_cellar.kettle.jei_info_need_lid"
                : "message.growthcraft_cellar.kettle.jei_info_no_lid")), 50, 0, 24, 17);
        widgets.addSlot(outputs.get(0), 74, 0).recipeContext(this);
        if (hasByProduct) {
            widgets.addTexture(SMALL_PLUS, 94, 3);
            widgets.addSlot(outputs.get(1), 107, 0);
            widgets.addTooltipText(List.of(Component.literal(byProductChance + "%")), 92, 1, 14, 14);
        }
    }
}
