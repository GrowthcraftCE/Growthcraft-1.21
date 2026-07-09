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

public class CheeseAgingRecipeCategory extends Base1to1RecipeCategory<CheeseAgingRecipeCategory.Recipe> {

    public CheeseAgingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, GrowthcraftMilkJeiPlugin.AGING);
    }

    @Override  @NotNull
    public Component getTitle() {
        return Component.translatable("emi.category.growthcraft_milk.aging");
    }

    @Override
    public ItemStack getIconCore()
    {
        return GrowthcraftMilkItems.ASIAGO_CHEESE.get().getDefaultInstance();
    }

    public static class Recipe extends Base1to1RecipeCategory.Jei1to1Recipe {
        public Recipe(Item input, Item output) { super(input, output); }

        @Override
        protected String getIdBase() { return "aging_"; }
    }
}
