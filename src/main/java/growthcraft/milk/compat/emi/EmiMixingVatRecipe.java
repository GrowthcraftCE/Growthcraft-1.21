package growthcraft.milk.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import growthcraft.milk.recipe.MixingVatRecipe;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class EmiMixingVatRecipe extends BasicEmiRecipe {
    private final int itemInputCount;
    private final boolean hasReagentFluid, hasWasteFluid;
    private final boolean hasResultFluid;
    private final boolean hasResultItem;
    private final List<ClientTooltipComponent> tooltipForStick = new ArrayList<>();

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
            this.tooltipForStick.add(ClientTooltipComponent.create(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_starter").append(MutableComponent.create(PlainTextContents.EMPTY).append(recipe.getActivationTool().getHoverName()).withColor(0xeecc99)).getVisualOrderText()));
        }

        recipe.getResultFluid().ifPresent(fluid -> this.outputs.add(EmiRecipeUtil.fluid(fluid.fluidId(), fluid.amount())));
        recipe.getResultFluidWaste().ifPresent(fluid -> this.outputs.add(EmiRecipeUtil.fluid(fluid.fluidId(), fluid.amount())));
        if (!recipe.getResultItemStack().isEmpty()) {
            this.outputs.add(EmiStack.of(recipe.getResultItemStack()));
        }
        if (!recipe.getResultActivationTool().isEmpty()) {
            this.outputs.add(EmiStack.of(recipe.getResultActivationTool()));
            this.tooltipForStick.add(ClientTooltipComponent.create(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_collector").append(MutableComponent.create(PlainTextContents.EMPTY).append(recipe.getResultActivationTool().getHoverName()).withColor(0xeecc99)).getVisualOrderText()));
        }

        this.hasReagentFluid = recipe.getReagentFluid().isPresent();
        this.hasWasteFluid = recipe.getReagentFluid().isPresent();
        this.itemInputCount = recipe.getIngredientStacks().size();
        this.hasResultFluid = recipe.getResultFluid().isPresent();
        this.hasResultItem = !recipe.getResultItemStack().isEmpty();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 82, 12);
        if (!this.tooltipForStick.isEmpty()) {
            widgets.addTooltip(this.tooltipForStick, 82, 12, 24, 16);
        }
        int inputIndex = 1;
        if (!hasReagentFluid)
        {
            widgets.addSlot(inputs.get(0), 0, 12);
        }
        else {
            widgets.addSlot(inputs.get(0), 2, 22);
            widgets.addSlot(inputs.get(inputIndex++), 2, 2).appendTooltip(() -> ClientTooltipComponent.create(Component.translatable("emi.caption.growthcraft_milk.mixing_vat_reagent").getVisualOrderText()));
        }
        for (int i = 0; i < itemInputCount; i++) {
            widgets.addSlot(inputs.get(inputIndex++), 24 + i * 20, 12);
        }
        //if (inputIndex < inputs.size()) {
        //    widgets.addSlot(inputs.get(inputIndex), 90, 4);
        //}  // won't show stick

        int outputIndex = 0;
        if (this.hasResultFluid) {
            if (!this.hasWasteFluid) {
                widgets.addSlot(outputs.get(outputIndex++), 107, 12).recipeContext(this);
            }
            else {
                widgets.addSlot(outputs.get(outputIndex++), 107, 22).recipeContext(this);
                widgets.addSlot(outputs.get(outputIndex++), 107, 2);
            }
        }
        if (hasResultItem) {
            SlotWidget w = widgets.addSlot(outputs.get(outputIndex++), 126, 12);
            w.recipeContext(this);  // it tells you the whole cost, but it makes tooltip ugly.
            w.appendTooltip(() -> ClientTooltipComponent.create(Component.literal(" ").getVisualOrderText()));
            for (int i = 0; i < this.tooltipForStick.size(); i++) {
                int finalI = i;
                w.appendTooltip(() -> this.tooltipForStick.get(finalI));
            }
            w.appendTooltip(() -> ClientTooltipComponent.create(Component.literal(" ").getVisualOrderText()));
        }
        //if (outputIndex < outputs.size()) {
        //    widgets.addSlot(outputs.get(outputIndex), 126, 2).appendTooltip(()->this.tooltipForStick.getLast());
        //}  // cheese cloth.
    }
}
