package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.CultureJarRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluids;

public class CultureJarRecipeCategory implements IRecipeCategory<RecipeHolder<CultureJarRecipe>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/gui/culture_jar_screen.png");
    private static final int WIDTH = 116;
    private static final int HEIGHT = 70;
    private static final int TANK_X = 50;
    private static final int TANK_Y = 8;
    private static final int TANK_WIDTH = 16;
    private static final int TANK_HEIGHT = 52;
    private static final int INPUT_X = 27;
    private static final int INPUT_Y = 25;
    private static final int OUTPUT_X = 73;
    private static final int OUTPUT_Y = 10;
    private static final int PROGRESS_X = 52;
    private static final int PROGRESS_Y = 20;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic heat;
    private final IDrawableStatic timeIcon;

    public CultureJarRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 30, 10, WIDTH, HEIGHT)
                .setTextureSize(256, 256)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftCellarItems.CULTURE_JAR.get()));
        this.progress = guiHelper.drawableBuilder(TEXTURE, 176, 0, 27, 27)
                .setTextureSize(256, 256)
                .buildAnimated(120, IDrawableAnimated.StartDirection.TOP, false);
        this.heat = guiHelper.drawableBuilder(TEXTURE, 176, 28, 13, 13)
                .setTextureSize(256, 256)
                .build();
        this.timeIcon = guiHelper.drawableBuilder(TEXTURE, 54, 184, 11, 11)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<RecipeHolder<CultureJarRecipe>> getRecipeType() {
        return GrowthcraftCellarJeiPlugin.CULTURE_JAR;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.culture_jar");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CultureJarRecipe> holder, IFocusGroup focuses) {
        CultureJarRecipe recipe = holder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addIngredients(recipe.getIngredient());

        var inputFluid = BuiltInRegistries.FLUID.get(recipe.getFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, TANK_X, TANK_Y)
                    .setFluidRenderer(1000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(inputFluid, recipe.getFluid().amount());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<CultureJarRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        progress.draw(graphics, PROGRESS_X, PROGRESS_Y);
        if (holder.value().requiresHeatSource()) {
            heat.draw(graphics, 29, 47);
        }

        Font font = Minecraft.getInstance().font;
        timeIcon.draw(graphics, 2, 57);
        graphics.drawString(font, formatTicks(holder.value().getTime()), 15, 62, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<CultureJarRecipe> holder) {
        return holder.id();
    }

    private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
