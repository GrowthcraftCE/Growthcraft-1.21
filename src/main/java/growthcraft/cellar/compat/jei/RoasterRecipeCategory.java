package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.RoasterRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RoasterRecipeCategory implements IRecipeCategory<RecipeHolder<RoasterRecipe>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/gui/roaster_screen.png");
    private static final int WIDTH = 116;
    private static final int HEIGHT = 64;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic heat;

    public RoasterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, 30, 20, WIDTH, HEIGHT)
                .setTextureSize(256, 256)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftCellarItems.ROASTER.get()));
        this.progress = guiHelper.drawableBuilder(TEXTURE, 176, 0, 28, 9)
                .setTextureSize(256, 256)
                .buildAnimated(120, IDrawableAnimated.StartDirection.LEFT, false);
        this.heat = guiHelper.drawableBuilder(TEXTURE, 176, 28, 14, 14)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<RecipeHolder<RoasterRecipe>> getRecipeType() {
        return GrowthcraftCellarJeiPlugin.ROASTER;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.roaster");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<RoasterRecipe> holder, IFocusGroup focuses) {
        RoasterRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 23).addItemStack(recipe.getInputItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 23).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<RoasterRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        progress.draw(graphics, 47, 27);
        heat.draw(graphics, 50, 39);

        Component level = Component.translatable("label.growthcraft_cellar.roaster_level", holder.value().getRoastingLevel());
        graphics.drawString(net.minecraft.client.Minecraft.getInstance().font, level, 18, 0, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<RoasterRecipe> holder) {
        return holder.id();
    }
}
