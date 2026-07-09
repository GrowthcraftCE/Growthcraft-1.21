package growthcraft.milk.compat.jei;

import growthcraft.milk.init.GrowthcraftMilkItems;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CheeseWaxingRecipeCategory extends Base2to1RecipeCategory<CheeseWaxingRecipeCategory.Recipe> {

    public CheeseWaxingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, GrowthcraftMilkJeiPlugin.WAXING);
    }

    @Override  @NotNull
    public Component getTitle() {
        return Component.translatable("emi.category.growthcraft_milk.waxing");
    }

    @Override
    public ItemStack getIconCore()
    {
        return GrowthcraftMilkItems.MONTEREY_CHEESE_WAXED.get().getDefaultInstance();
    }

    public static class Recipe extends Jei2to1Recipe {
        public Recipe(Item input1, Item input2, Item output) { super(input1, input2, output); }

        @Override
        protected String getIdBase() { return "waxing_"; }
    }
}
