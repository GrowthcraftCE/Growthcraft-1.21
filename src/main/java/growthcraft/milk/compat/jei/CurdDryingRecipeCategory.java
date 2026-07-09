package growthcraft.milk.compat.jei;

import growthcraft.milk.init.GrowthcraftMilkItems;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurdDryingRecipeCategory extends Base1to1RecipeCategory<CurdDryingRecipeCategory.Recipe> {

    public CurdDryingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, GrowthcraftMilkJeiPlugin.CURDS);
    }

    @Override  @NotNull
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.curds");
    }

    @Override
    public ItemStack getIconCore()
    {
        return GrowthcraftMilkItems.ASIAGO_CHEESE_CURDS.get().getDefaultInstance();
    }

    @Override
    public void draw(Base1to1RecipeCategory.Jei1to1Recipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        if (this.tooltip == null)
        {
            this.tooltip = new ArrayList<>();
            this.tooltip.add(Component.translatable("emi.caption.growthcraft_milk.drying1"));
            this.tooltip.add(Component.translatable("emi.caption.growthcraft_milk.drying2"));
        }
        Font font = Minecraft.getInstance().font;
        //graphics.drawString(font, time, (WIDTH - font.width(time)) / 2, 58, 4210752, false);
        if (mouseX > 28 && mouseX < 28+24 && mouseY > 8 && mouseY < 8+17)
        {
            graphics.renderTooltip(font, this.tooltip, Optional.empty(), (int) mouseX, (int) mouseY);
        }
    }
    private List<Component> tooltip = null;


    public static class Recipe extends Base1to1RecipeCategory.Jei1to1Recipe {
        public Recipe(Item input, Item output) { super(input, output); }

        @Override
        protected String getIdBase() { return "curds_"; }
    }
}
