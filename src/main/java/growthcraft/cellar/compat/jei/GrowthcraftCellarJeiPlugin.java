package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.recipe.RoasterRecipe;
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
public class GrowthcraftCellarJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "jei_plugin");
    public static final RecipeType<RecipeHolder<BrewKettleRecipe>> BREW_KETTLE =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.BREW_KETTLE_RECIPE), GrowthcraftCellarJeiPlugin.<BrewKettleRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<FermentationBarrelRecipe>> FERMENTATION_BARREL =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FERMENT_BARREL_RECIPE), GrowthcraftCellarJeiPlugin.<FermentationBarrelRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<FruitPressRecipe>> FRUIT_PRESS =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FRUIT_PRESS_RECIPE), GrowthcraftCellarJeiPlugin.<FruitPressRecipe>recipeHolderClass());
    public static final RecipeType<RecipeHolder<RoasterRecipe>> ROASTER =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.ROASTER_RECIPE), GrowthcraftCellarJeiPlugin.<RoasterRecipe>recipeHolderClass());

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
                new BrewKettleRecipeCategory(guiHelper),
                new FermentationBarrelRecipeCategory(guiHelper),
                new FruitPressRecipeCategory(guiHelper),
                new RoasterRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        registration.addRecipes(BREW_KETTLE, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get()));
        registration.addRecipes(FERMENTATION_BARREL, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get()));
        registration.addRecipes(FRUIT_PRESS, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get()));
        registration.addRecipes(ROASTER, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.ROASTER_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.BREW_KETTLE.get()), BREW_KETTLE);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.FERMENTATION_BARREL_OAK.get()), FERMENTATION_BARREL);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.FRUIT_PRESS.get()), FRUIT_PRESS);
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.ROASTER.get()), ROASTER);
    }
}
