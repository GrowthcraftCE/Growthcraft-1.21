package growthcraft.milk.compat.jei;

import growthcraft.milk.init.GrowthcraftMilkItems;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CheesePreppingRecipeCategory extends Base2to1RecipeCategory<CheesePreppingRecipeCategory.Recipe> {

    public CheesePreppingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, GrowthcraftMilkJeiPlugin.OTHER);
    }

    @Override  @NotNull
    public Component getTitle() {
        return Component.translatable("emi.category.growthcraft_milk.other");
    }

    @Override
    public ItemStack getIconCore()
    {
        return GrowthcraftMilkItems.PARMESAN_CHEESE_SLICE.get().getDefaultInstance();
    }

    public static class Recipe extends Jei2to1Recipe {
        public Recipe(Item input1, Item input2, Item output) { super(input1, input2, output); }
        public Recipe(Item input1, TagKey<Item> input2, Item output) { super(input1, input2, output); }

        @Override
        protected String getIdBase() { return "other_"; }
    }
}
