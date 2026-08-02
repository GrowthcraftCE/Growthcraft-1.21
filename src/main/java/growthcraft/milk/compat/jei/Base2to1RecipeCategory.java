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
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class Base2to1RecipeCategory<T extends Base2to1RecipeCategory.Jei2to1Recipe> implements IRecipeCategory<T> {
    private final IDrawableStatic background;
    private final IDrawable icon;
    protected final IGuiHelper guiHelper;
    private final RecipeType<T> recipeType;

    public Base2to1RecipeCategory(IGuiHelper guiHelper, RecipeType<T> recipeType) {
        this.background = guiHelper.createDrawable(Jei2to1Recipe.BACKGROUND, 0, 0, 160, 26);
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
    public void setRecipe(IRecipeLayoutBuilder builder, Jei2to1Recipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 4).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 4).addIngredients(recipe.getInput2());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 4).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(Jei2to1Recipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        //Font font = Minecraft.getInstance().font;
        //graphics.drawString(font, time, (WIDTH - font.width(time)) / 2, 58, 4210752, false);
    }
    //private Component time = Component.literal(formatTicks(1234));

    @Override
    public ResourceLocation getRegistryName(Jei2to1Recipe recipe) {
        return recipe.getId();
    }


    protected static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }

    ///////////////////////////////////////

    public static abstract class Jei2to1Recipe
    {
        public static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath("growthcraft", "textures/gui/jei2to1.png");

        public Jei2to1Recipe(Item input1, Item input2, Item output)
        {
            this(input1, Ingredient.of(input2), output);
        }
        public Jei2to1Recipe(Item input1, TagKey<Item> input2, Item output)
        {
            this(input1, Ingredient.of(input2), output);
        }
        private Jei2to1Recipe(Item input1, Ingredient input2, Item output)
        {
            this.input1 = input1.getDefaultInstance();
            this.input2 = input2;
            this.output = output.getDefaultInstance();
            counter += 1;
            this.id = ResourceLocation.fromNamespaceAndPath("growthcraft_milk", this.getIdBase() + counter);
        }
        private final ItemStack input1, output;
        private final Ingredient input2;
        private final ResourceLocation id;
        private static int counter = 0;

        protected abstract String getIdBase();
        //------------//
        public ResourceLocation getId() { return this.id; }
        public ItemStack getInput1() { return this.input1; }
        public Ingredient getInput2() { return this.input2; }
        public ItemStack getOutput() { return this.output; }
    }
}
