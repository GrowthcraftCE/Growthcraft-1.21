package growthcraft.milk.compat.jei;

import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.PancheonRecipe;
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

public class PancheonRecipeCategory implements IRecipeCategory<RecipeHolder<PancheonRecipe>> {
    private static final int WIDTH = 160;
    private static final int HEIGHT = 70;
    private static final int INPUT_TANK_X = 30;
    private static final int INPUT_TANK_Y = 8;
    private static final int INPUT_TANK_WIDTH = 16;
    private static final int INPUT_TANK_HEIGHT = 46;
    private static final int OUTPUT_TANK_X = 112;
    private static final int OUTPUT_TANK_0_Y = 8;
    private static final int OUTPUT_TANK_1_Y = 35;
    private static final int OUTPUT_TANK_WIDTH = 16;
    private static final int OUTPUT_TANK_HEIGHT = 19;
    private static final int SPLIT_X = 70;
    private static final int SPLIT_Y = 31;
    private static final int OUTPUT_LINE_X = 106;

    private final IDrawableStatic background;
    private final IDrawable icon;

    public PancheonRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftMilkItems.PANCHEON.get()));
    }

    @Override
    public RecipeType<RecipeHolder<PancheonRecipe>> getRecipeType() {
        return GrowthcraftMilkJeiPlugin.PANCHEON;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.pancheon");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<PancheonRecipe> holder, IFocusGroup focuses) {
        PancheonRecipe recipe = holder.value();
        Fluid inputFluid = BuiltInRegistries.FLUID.get(recipe.getInputFluid().fluidId());
        if (inputFluid != Fluids.EMPTY && recipe.getInputFluid().amount() > 0) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_TANK_X, INPUT_TANK_Y)
                    .setFluidRenderer(2000, true, INPUT_TANK_WIDTH, INPUT_TANK_HEIGHT)
                    .addFluidStack(inputFluid, recipe.getInputFluid().amount());
        }

        addOutputFluid(builder, recipe, 0, OUTPUT_TANK_0_Y);
        addOutputFluid(builder, recipe, 1, OUTPUT_TANK_1_Y);
    }

    private static void addOutputFluid(IRecipeLayoutBuilder builder, PancheonRecipe recipe, int index, int y) {
        PancheonRecipe.FluidAmount output = recipe.getOutputFluids().get(index);
        Fluid fluid = BuiltInRegistries.FLUID.get(output.fluidId());
        if (fluid != Fluids.EMPTY && output.amount() > 0) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_TANK_X, y)
                    .setFluidRenderer(1000, true, OUTPUT_TANK_WIDTH, OUTPUT_TANK_HEIGHT)
                    .addFluidStack(fluid, output.amount());
        }
    }

    @Override
    public void draw(RecipeHolder<PancheonRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        drawTankFrame(graphics, INPUT_TANK_X, INPUT_TANK_Y, INPUT_TANK_WIDTH, INPUT_TANK_HEIGHT);
        drawTankFrame(graphics, OUTPUT_TANK_X, OUTPUT_TANK_0_Y, OUTPUT_TANK_WIDTH, OUTPUT_TANK_HEIGHT);
        drawTankFrame(graphics, OUTPUT_TANK_X, OUTPUT_TANK_1_Y, OUTPUT_TANK_WIDTH, OUTPUT_TANK_HEIGHT);
        drawSplitArrow(graphics);

        Font font = Minecraft.getInstance().font;
        Component time = Component.literal(formatTicks(holder.value().getProcessingTime()));
        graphics.drawString(font, time, (WIDTH - font.width(time)) / 2, 58, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<PancheonRecipe> holder) {
        return holder.id();
    }

    private static void drawTankFrame(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF6B6B6B);
        graphics.fill(x, y, x + width, y + height, 0xFFE5E0CF);
    }

    private static void drawSplitArrow(GuiGraphics graphics) {
        int topCenterY = OUTPUT_TANK_0_Y + 10;
        int bottomCenterY = OUTPUT_TANK_1_Y + 10;
        graphics.fill(INPUT_TANK_X + INPUT_TANK_WIDTH + 12, SPLIT_Y - 1, SPLIT_X, SPLIT_Y + 1, 0xFF6B6B6B);
        graphics.fill(SPLIT_X, topCenterY, SPLIT_X + 2, bottomCenterY + 1, 0xFF6B6B6B);
        graphics.fill(SPLIT_X, topCenterY - 1, OUTPUT_LINE_X - 6, topCenterY + 1, 0xFF6B6B6B);
        graphics.fill(SPLIT_X, bottomCenterY - 1, OUTPUT_LINE_X - 6, bottomCenterY + 1, 0xFF6B6B6B);
        drawArrowHead(graphics, OUTPUT_LINE_X, topCenterY);
        drawArrowHead(graphics, OUTPUT_LINE_X, bottomCenterY);
    }

    private static void drawArrowHead(GuiGraphics graphics, int x, int centerY) {
        graphics.fill(x - 7, centerY - 4, x - 5, centerY + 4, 0xFF6B6B6B);
        graphics.fill(x - 5, centerY - 3, x - 3, centerY + 3, 0xFF6B6B6B);
        graphics.fill(x - 3, centerY - 2, x - 1, centerY + 2, 0xFF6B6B6B);
    }

    private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
