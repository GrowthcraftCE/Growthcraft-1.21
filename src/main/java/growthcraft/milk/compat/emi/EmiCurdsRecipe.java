package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.config.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class EmiCurdsRecipe extends BasicEmiRecipe
{
    public EmiCurdsRecipe(ItemLike input, Item output) {
        super(EmiPlugin.CURD_DRAINING, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "/d" + (++counter)), 125, 50);
        this.inputs.add(EmiIngredient.of(Ingredient.of(input)));
        this.outputs.add(EmiStack.of(output));
    }
    private static int counter = 0;

    private static final ResourceLocation WIDGETS = ResourceLocation.parse("growthcraft:textures/gui/widgets.png");
    private static final EmiTexture SMALL_BLUE_UP = new EmiTexture(WIDGETS, 141, 1, 14, 12);
    private static final EmiTexture SMALL_ARROW = new EmiTexture(WIDGETS, 44, 0, 20, 15);

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        // two-row label
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.drying1"), 2, 4, 0xFF516F91, false);
        widgetHolder.addText(Component.translatable("emi.caption.growthcraft_milk.drying2"), 38, 15, 0xFF516F91, false);
        // upwards arrow
        widgetHolder.addTexture(SMALL_BLUE_UP, 8, 14);
        // two slots
        widgetHolder.addSlot(inputs.get(0), 6, 31);
        widgetHolder.addSlot(outputs.get(0), 105, 31).recipeContext(this);
        // normal horizontal arrow, big variety
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 58, 32);
    }
}