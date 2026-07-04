package growthcraft.milk.compat.jei;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.ChurnRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class ChurnRecipeCategory implements IRecipeCategory<RecipeHolder<ChurnRecipe>> {
    private static final int WIDTH = 160;
    private static final int HEIGHT = 70;
    private static final int INPUT_TANK_X = 20;
    private static final int INPUT_TANK_Y = 10;
    private static final int OUTPUT_TANK_X = 114;
    private static final int OUTPUT_TANK_Y = 10;
    private static final int TANK_WIDTH = 14;
    private static final int TANK_HEIGHT = 44;
    private static final int ARROW_X = 50;
    private static final int ARROW_Y = 23;
    private static final int ARROW_WIDTH = 32;
    private static final int BYPRODUCT_X = 140;
    private static final int BYPRODUCT_Y = 25;

    private final IDrawableStatic background;
    private final IDrawable icon;

    public ChurnRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftMilkItems.CHURN.get()));
    }

    @Override
    public RecipeType<RecipeHolder<ChurnRecipe>> getRecipeType() {
        return GrowthcraftMilkJeiPlugin.CHURN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.churn");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ChurnRecipe> holder, IFocusGroup focuses) {
        ChurnRecipe recipe = holder.value();
        Fluid inputFluid = BuiltInRegistries.FLUID.get(recipe.getInputFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getInputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_TANK_X, INPUT_TANK_Y)
                    .setFluidRenderer(1000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(inputFluid, recipe.getInputFluid().amount());
        }

        Fluid outputFluid = BuiltInRegistries.FLUID.get(recipe.getOutputFluid().fluidId());
        if (outputFluid != Fluids.EMPTY && recipe.getOutputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_TANK_X, OUTPUT_TANK_Y)
                    .setFluidRenderer(1000, true, TANK_WIDTH, TANK_HEIGHT)
                    .addFluidStack(outputFluid, recipe.getOutputFluid().amount());
        }

        ItemStack byProduct = recipe.getByProduct();
        if (!byProduct.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, BYPRODUCT_X, BYPRODUCT_Y)
                    .addItemStack(byProduct);
        }
    }

    @Override
    public void draw(RecipeHolder<ChurnRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        drawTankFrame(graphics, INPUT_TANK_X, INPUT_TANK_Y);
        drawTankFrame(graphics, OUTPUT_TANK_X, OUTPUT_TANK_Y);
        drawSlotFrame(graphics, BYPRODUCT_X, BYPRODUCT_Y);
        drawArrow(graphics);

        Font font = Minecraft.getInstance().font;
        Component plunges = Component.translatable("message.growthcraft_milk.churn.jei_plunges", holder.value().getPlungesNeeded());
        graphics.drawString(font, plunges, ARROW_X + (ARROW_WIDTH - font.width(plunges)) / 2, 40, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<ChurnRecipe> holder) {
        return holder.id();
    }

    private static void drawTankFrame(GuiGraphics graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + TANK_WIDTH + 1, y + TANK_HEIGHT + 1, 0xFF6B6B6B);
        graphics.fill(x, y, x + TANK_WIDTH, y + TANK_HEIGHT, 0xFFE5E0CF);
    }

    private static void drawArrow(GuiGraphics graphics) {
        int centerY = ARROW_Y + 4;
        graphics.fill(ARROW_X, centerY - 1, ARROW_X + ARROW_WIDTH - 7, centerY + 1, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 7, centerY - 4, ARROW_X + ARROW_WIDTH - 5, centerY + 4, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 5, centerY - 3, ARROW_X + ARROW_WIDTH - 3, centerY + 3, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 3, centerY - 2, ARROW_X + ARROW_WIDTH - 1, centerY + 2, 0xFF6B6B6B);
    }

    private static void drawSlotFrame(GuiGraphics graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF6B6B6B);
        graphics.fill(x, y, x + 16, y + 16, 0xFFE5E0CF);
        graphics.fill(x + 1, y + 1, x + 15, y + 15, 0xFFB8B8B8);
        graphics.fill(x + 2, y + 2, x + 14, y + 14, 0xFFEDEDED);
    }
}
