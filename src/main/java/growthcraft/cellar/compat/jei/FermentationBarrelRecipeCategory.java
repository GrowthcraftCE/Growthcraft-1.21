package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
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

public class FermentationBarrelRecipeCategory implements IRecipeCategory<RecipeHolder<FermentationBarrelRecipe>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/gui/fermentation_barrel_screen.png");
    private static final int BACKGROUND_U = 30;
    private static final int BACKGROUND_V = 10;
    private static final int WIDTH = 116;
    private static final int HEIGHT = 70;
    private static final int PROGRESS_X = 51 - BACKGROUND_U;
    private static final int PROGRESS_Y = 20 - BACKGROUND_V;
    private static final int INPUT_TANK_X = 72 - BACKGROUND_U;
    private static final int INPUT_TANK_Y = 17 - BACKGROUND_V;
    private static final int INPUT_TANK_WIDTH = 50;
    private static final int INPUT_TANK_HEIGHT = 52;
    private static final int OUTPUT_X = 98;
    private static final int OUTPUT_Y = 43;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic timeIcon;

    public FermentationBarrelRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, BACKGROUND_U, BACKGROUND_V, WIDTH, HEIGHT)
                .setTextureSize(256, 256)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get()));
        this.progress = guiHelper.drawableBuilder(TEXTURE, 188, 0, 8, 28)
                .setTextureSize(256, 256)
                .buildAnimated(120, IDrawableAnimated.StartDirection.TOP, false);
        this.timeIcon = guiHelper.drawableBuilder(TEXTURE, 54, 184, 11, 11)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<RecipeHolder<FermentationBarrelRecipe>> getRecipeType() {
        return GrowthcraftCellarJeiPlugin.FERMENTATION_BARREL;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.fermentation_barrel");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FermentationBarrelRecipe> holder, IFocusGroup focuses) {
        FermentationBarrelRecipe recipe = holder.value();

        var inputFluid = BuiltInRegistries.FLUID.get(recipe.getIngredientFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getIngredientFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_TANK_X, INPUT_TANK_Y)
                    .setFluidRenderer(4000, true, INPUT_TANK_WIDTH, INPUT_TANK_HEIGHT)
                    .addFluidStack(inputFluid, recipe.getIngredientFluid().amount());
            ItemStack bucket = inputFluid.getBucket().getDefaultInstance();
            if (! bucket.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(bucket);
            }
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 23, 43)
                .addIngredients(recipe.getIngredientItem().ingredient());

        var outputFluid = BuiltInRegistries.FLUID.get(recipe.getResult().fluidId());
        if (outputFluid != Fluids.EMPTY && recipe.getResult().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                    .setFluidRenderer(recipe.getResult().amount(), false, 16, 16)
                    .addFluidStack(outputFluid, recipe.getResult().amount());
            ItemStack bucket = outputFluid.getBucket().getDefaultInstance();
            if (! bucket.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(bucket);
            }
        }

        ItemStack bottle = recipe.getBottle();
        if (!bottle.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                    .addItemStack(bottle);
        }
    }

    @Override
    public void draw(RecipeHolder<FermentationBarrelRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        progress.draw(graphics, PROGRESS_X, PROGRESS_Y);

        Font font = Minecraft.getInstance().font;
        timeIcon.draw(graphics, 2, 57);
        graphics.drawString(font, formatTicks(holder.value().getProcessingTime()), 15, 62, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<FermentationBarrelRecipe> holder) {
        return holder.id();
    }

    private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
