package growthcraft.milk.compat.jei;

import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.recipe.MixingVatRecipe;
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

import java.util.Arrays;
import java.util.List;

public class MixingVatRecipeCategory implements IRecipeCategory<RecipeHolder<MixingVatRecipe>> {
    private static final int WIDTH = 160;
    private static final int HEIGHT = 78;
    private static final int MAIN_INPUT_X = 10;
    private static final int MAIN_INPUT_Y = 8;
    private static final int MAIN_OUTPUT_X = 122;
    private static final int MAIN_OUTPUT_Y = 8;
    private static final int MAIN_TANK_WIDTH = 14;
    private static final int MAIN_TANK_HEIGHT = 48;
    private static final int SIDE_INPUT_X = 34;
    private static final int SIDE_INPUT_Y = 22;
    private static final int SIDE_OUTPUT_X = 144;
    private static final int SIDE_OUTPUT_Y = 22;
    private static final int SIDE_TANK_WIDTH = 14;
    private static final int SIDE_TANK_HEIGHT = 24;
    private static final int[] INGREDIENT_X = new int[] { 58, 78, 98 };
    private static final int INGREDIENT_Y = 8;
    private static final int ACTIVATOR_X = 78;
    private static final int ACTIVATOR_Y = 34;
    private static final int ITEM_OUTPUT_X = 132;
    private static final int ITEM_OUTPUT_Y = 16;
    private static final int COLLECTOR_X = 132;
    private static final int COLLECTOR_Y = 42;
    private static final int ARROW_X = 104;
    private static final int ARROW_Y = 30;
    private static final int ARROW_WIDTH = 14;

    private final IDrawableStatic background;
    private final IDrawable icon;

    public MixingVatRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(GrowthcraftMilkItems.MIXING_VAT.get()));
    }

    @Override
    public RecipeType<RecipeHolder<MixingVatRecipe>> getRecipeType() {
        return GrowthcraftMilkJeiPlugin.MIXING_VAT;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.growthcraft_milk.category.mixing_vat");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MixingVatRecipe> holder, IFocusGroup focuses) {
        MixingVatRecipe recipe = holder.value();
        addFluid(builder, RecipeIngredientRole.INPUT, recipe.getInputFluid(), MAIN_INPUT_X, MAIN_INPUT_Y, 4000, MAIN_TANK_WIDTH, MAIN_TANK_HEIGHT);
        recipe.getReagentFluid().ifPresent(fluid -> addFluid(builder, RecipeIngredientRole.INPUT, fluid, SIDE_INPUT_X, SIDE_INPUT_Y, 1000, SIDE_TANK_WIDTH, SIDE_TANK_HEIGHT));

        List<MixingVatRecipe.IngredientStack> ingredients = recipe.getIngredientStacks();
        for (int i = 0; i < ingredients.size() && i < INGREDIENT_X.length; i++) {
            MixingVatRecipe.IngredientStack ingredient = ingredients.get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, INGREDIENT_X[i], INGREDIENT_Y)
                    .addItemStacks(withCount(ingredient));
        }

        if (!recipe.getActivationTool().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, ACTIVATOR_X, ACTIVATOR_Y)
                    .addItemStack(recipe.getActivationTool());
        }

        if (recipe.getResultType() == MixingVatRecipe.ResultType.FLUID) {
            recipe.getResultFluid().ifPresent(fluid -> addFluid(builder, RecipeIngredientRole.OUTPUT, fluid, MAIN_OUTPUT_X, MAIN_OUTPUT_Y, 4000, MAIN_TANK_WIDTH, MAIN_TANK_HEIGHT));
            recipe.getResultFluidWaste().ifPresent(fluid -> addFluid(builder, RecipeIngredientRole.OUTPUT, fluid, SIDE_OUTPUT_X, SIDE_OUTPUT_Y, 1000, SIDE_TANK_WIDTH, SIDE_TANK_HEIGHT));
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, ITEM_OUTPUT_X, ITEM_OUTPUT_Y)
                    .addItemStack(recipe.getResultItemStack());
            if (!recipe.getResultActivationTool().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.INPUT, COLLECTOR_X, COLLECTOR_Y)
                        .addItemStack(recipe.getResultActivationTool());
            }
        }
    }

    private static void addFluid(IRecipeLayoutBuilder builder, RecipeIngredientRole role, MixingVatRecipe.FluidAmount amount,
                                 int x, int y, int capacity, int width, int height) {
        Fluid fluid = BuiltInRegistries.FLUID.get(amount.fluidId());
        if (fluid != Fluids.EMPTY && amount.amount() > 0) {
            builder.addSlot(role, x, y)
                    .setFluidRenderer(capacity, true, width, height)
                    .addFluidStack(fluid, amount.amount());
            ItemStack bucket = fluid.getBucket().getDefaultInstance();
            if (! bucket.isEmpty()) {
                builder.addInvisibleIngredients(role).addItemStack(bucket);
            }
        }
    }

    private static List<ItemStack> withCount(MixingVatRecipe.IngredientStack ingredient) {
        return Arrays.stream(ingredient.ingredient().getItems())
                .map(stack -> stack.copyWithCount(ingredient.count()))
                .toList();
    }

    @Override
    public void draw(RecipeHolder<MixingVatRecipe> holder, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        MixingVatRecipe recipe = holder.value();
        drawTankFrame(graphics, MAIN_INPUT_X, MAIN_INPUT_Y, MAIN_TANK_WIDTH, MAIN_TANK_HEIGHT);
        recipe.getReagentFluid().ifPresent(fluid -> drawTankFrame(graphics, SIDE_INPUT_X, SIDE_INPUT_Y, SIDE_TANK_WIDTH, SIDE_TANK_HEIGHT));
        for (int i = 0; i < recipe.getIngredientStacks().size() && i < INGREDIENT_X.length; i++) {
            drawSlotFrame(graphics, INGREDIENT_X[i], INGREDIENT_Y);
        }
        drawSlotFrame(graphics, ACTIVATOR_X, ACTIVATOR_Y);
        drawArrow(graphics);

        if (recipe.getResultType() == MixingVatRecipe.ResultType.FLUID) {
            drawTankFrame(graphics, MAIN_OUTPUT_X, MAIN_OUTPUT_Y, MAIN_TANK_WIDTH, MAIN_TANK_HEIGHT);
            recipe.getResultFluidWaste().ifPresent(fluid -> drawTankFrame(graphics, SIDE_OUTPUT_X, SIDE_OUTPUT_Y, SIDE_TANK_WIDTH, SIDE_TANK_HEIGHT));
        } else {
            drawSlotFrame(graphics, ITEM_OUTPUT_X, ITEM_OUTPUT_Y);
            drawSlotFrame(graphics, COLLECTOR_X, COLLECTOR_Y);
        }

        Font font = Minecraft.getInstance().font;
        Component time = Component.literal(formatTicks(recipe.getProcessingTime()));
        graphics.drawString(font, time, (WIDTH - font.width(time)) / 2, 64, 4210752, false);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<MixingVatRecipe> holder) {
        return holder.id();
    }

    private static void drawTankFrame(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF6B6B6B);
        graphics.fill(x, y, x + width, y + height, 0xFFE5E0CF);
    }

    private static void drawSlotFrame(GuiGraphics graphics, int x, int y) {
        graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xFF6B6B6B);
        graphics.fill(x, y, x + 16, y + 16, 0xFFE5E0CF);
        graphics.fill(x + 1, y + 1, x + 15, y + 15, 0xFFB8B8B8);
        graphics.fill(x + 2, y + 2, x + 14, y + 14, 0xFFEDEDED);
    }

    private static void drawArrow(GuiGraphics graphics) {
        int centerY = ARROW_Y + 4;
        graphics.fill(ARROW_X, centerY - 1, ARROW_X + ARROW_WIDTH - 7, centerY + 1, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 7, centerY - 4, ARROW_X + ARROW_WIDTH - 5, centerY + 4, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 5, centerY - 3, ARROW_X + ARROW_WIDTH - 3, centerY + 3, 0xFF6B6B6B);
        graphics.fill(ARROW_X + ARROW_WIDTH - 3, centerY - 2, ARROW_X + ARROW_WIDTH - 1, centerY + 2, 0xFF6B6B6B);
    }

    private static String formatTicks(int ticks) {
        int seconds = Math.max(1, ticks / 20);
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes > 0 ? minutes + "m " + remainingSeconds + "s" : seconds + "s";
    }
}
