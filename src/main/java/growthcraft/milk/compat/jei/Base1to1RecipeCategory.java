package growthcraft.milk.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class Base1to1RecipeCategory<T extends Base1to1RecipeCategory.Jei1to1Recipe> implements IRecipeCategory<T> {
    private final IDrawableStatic background;
    private final IDrawable icon;
    protected final IGuiHelper guiHelper;
    private final RecipeType<T> recipeType;

    public Base1to1RecipeCategory(IGuiHelper guiHelper, RecipeType<T> recipeType) {
        this.background = guiHelper.createDrawable(Jei1to1Recipe.BACKGROUND, 0, 0, 160, 26);
        this.icon = guiHelper.createDrawableItemStack(this.getIconCore());
        this.guiHelper = guiHelper;
        this.recipeType = recipeType;
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    public abstract ItemStack getIconCore();

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Jei1to1Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 4).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 4).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(Jei1to1Recipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        //Font font = Minecraft.getInstance().font;
        //graphics.drawString(font, time, (WIDTH - font.width(time)) / 2, 58, 4210752, false);
    }
    //private Component time = Component.literal(formatTicks(1234));

    @Override
    public ResourceLocation getRegistryName(Jei1to1Recipe recipe) {
        return recipe.getId();
    }


    protected static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }

    ///////////////////////////////////////

    public static abstract class Jei1to1Recipe
    {
        public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("growthcraft", "textures/gui/jei1to1.png");

        public Jei1to1Recipe(Item input, Item output)
        {
            this.input = input.getDefaultInstance();
            this.output = output.getDefaultInstance();
            counter += 1;
            this.id = ResourceLocation.fromNamespaceAndPath("growthcraft_milk", this.getIdBase() + counter);
        }
        private final ItemStack input, output;
        private final ResourceLocation id;
        private static int counter = 0;

        protected abstract String getIdBase();
        //------------//
        public ResourceLocation getId() { return this.id; }
        public ItemStack getInput() { return this.input; }
        public ItemStack getOutput() { return this.output; }
    }
}
