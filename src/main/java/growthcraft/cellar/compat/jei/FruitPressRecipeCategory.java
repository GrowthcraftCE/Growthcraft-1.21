package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.recipe.FruitPressRecipe;
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

import java.util.Arrays;
import java.util.List;

public class FruitPressRecipeCategory implements IRecipeCategory<RecipeHolder<FruitPressRecipe>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/gui/fruit_press_screen.png");
    private static final int BACKGROUND_U = 30;
    private static final int BACKGROUND_V = 10;
    private static final int WIDTH = 116;
    private static final int HEIGHT = 70;
    private static final int PROGRESS_X = 51 - BACKGROUND_U;
    private static final int PROGRESS_Y = 20 - BACKGROUND_V;
    private static final int TANK_X = 72 - BACKGROUND_U;
    private static final int TANK_Y = 17 - BACKGROUND_V;
    private static final int TANK_WIDTH = 50;
    private static final int TANK_HEIGHT = 52;
    private static final int INPUT_X = 22;
    private static final int INPUT_Y = 43;
    private static final int BYPRODUCT_X = 99;
    private static final int BYPRODUCT_Y = 43;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;
    private final IDrawableStatic timeIcon;
    private final IDrawableStatic slotBackground;

    public FruitPressRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(TEXTURE, BACKGROUND_U, BACKGROUND_V, WIDTH, HEIGHT)
                .setTextureSize(256, 256)
                .build();
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftCellarItems.FRUIT_PRESS.get()));
        this.progress = guiHelper.drawableBuilder(TEXTURE, 188, 0, 8, 28)
                .setTextureSize(256, 256)
                .buildAnimated(120, IDrawableAnimated.StartDirection.TOP, false);
        this.timeIcon = guiHelper.drawableBuilder(TEXTURE, 54, 184, 11, 11)
                .setTextureSize(256, 256)
                .build();
        this.slotBackground = guiHelper.drawableBuilder(TEXTURE, 7, 83, 18, 18)
                .setTextureSize(256, 256)
                .build();
    }

    @Override
    public RecipeType<RecipeHolder<FruitPressRecipe>> getRecipeType() {
        return GrowthcraftCellarJeiPlugin.FRUIT_PRESS;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_cellar.category.fruit_press");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FruitPressRecipe> holder, IFocusGroup focuses) {
        FruitPressRecipe recipe = holder.value();

        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y)
                .addItemStacks(countedIngredientStacks(recipe));

        var outputFluid = BuiltInRegistries.FLUID.get(recipe.getOutputFluid().fluidId());
        if (outputFluid != Fluids.EMPTY && recipe.getOutputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, TANK_X, TANK_Y)
                    .setFluidRenderer(4000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(outputFluid, recipe.getOutputFluid().amount());
        }

        ItemStack byProduct = recipe.getByProduct();
        if (!byProduct.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, BYPRODUCT_X, BYPRODUCT_Y)
                    .addItemStack(byProduct);
        }
    }

    @Override
    public void draw(RecipeHolder<FruitPressRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        progress.draw(graphics, PROGRESS_X, PROGRESS_Y);
        if (!holder.value().getByProduct().isEmpty()) {
            slotBackground.draw(graphics, BYPRODUCT_X - 1, BYPRODUCT_Y - 1);
        }

        Font font = Minecraft.getInstance().font;
        if (!holder.value().getByProduct().isEmpty() && holder.value().getByProductChance() < 100) {
            graphics.drawString(font, holder.value().getByProductChance() + "%", BYPRODUCT_X, BYPRODUCT_Y - 10, 4210752, false);
        }

        timeIcon.draw(graphics, 2, 57);
        graphics.drawString(font, formatTicks(holder.value().getProcessingTime()), 15, 62, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<FruitPressRecipe> holder) {
        return holder.id();
    }

    private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }

    private static List<ItemStack> countedIngredientStacks(FruitPressRecipe recipe) {
        int count = recipe.getInputItem().count();
        return Arrays.stream(recipe.getInputItem().ingredient().getItems())
                .map(stack -> stack.copyWithCount(count))
                .toList();
    }
}
