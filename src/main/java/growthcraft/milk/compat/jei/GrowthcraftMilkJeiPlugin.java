package growthcraft.milk.compat.jei;

import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.CheesePressRecipe;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.MixingVatRecipe;
import growthcraft.milk.recipe.PancheonRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class GrowthcraftMilkJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "jei_plugin");

    public static final RecipeType<RecipeHolder<CheesePressRecipe>> CHEESE_PRESS =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHEESE_PRESS_RECIPE),
                    GrowthcraftMilkJeiPlugin.<CheesePressRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<ChurnRecipe>> CHURN =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHURN_RECIPE),
                    GrowthcraftMilkJeiPlugin.<ChurnRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<PancheonRecipe>> PANCHEON =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.PANCHEON_RECIPE),
                    GrowthcraftMilkJeiPlugin.<PancheonRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<MixingVatRecipe>> MIXING_VAT =
            new RecipeType<>(
                    ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.MIXING_VAT_RECIPE),
                    GrowthcraftMilkJeiPlugin.<MixingVatRecipe>recipeHolderClass());

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T extends Recipe<?>> Class<? extends RecipeHolder<T>> recipeHolderClass() {
        return (Class) RecipeHolder.class;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new CheesePressRecipeCategory(guiHelper),
                new ChurnRecipeCategory(guiHelper),
                new MixingVatRecipeCategory(guiHelper),
                new PancheonRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        registration.addRecipes(CHEESE_PRESS, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get()));
        registration.addRecipes(CHURN, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHURN_TYPE.get()));
        registration.addRecipes(MIXING_VAT, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get()));
        registration.addRecipes(PANCHEON, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.PANCHEON_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.CHEESE_PRESS.get()), CHEESE_PRESS);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.CHURN.get()), CHURN);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.MIXING_VAT.get()), MIXING_VAT);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftMilkItems.PANCHEON.get()), PANCHEON);
    }
}
