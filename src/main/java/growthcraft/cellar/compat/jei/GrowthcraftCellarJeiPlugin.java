package growthcraft.cellar.compat.jei;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
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
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class GrowthcraftCellarJeiPlugin implements IModPlugin {
    public static final ResourceLocation PLUGIN_UID = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "jei_plugin");
    public static final RecipeType<RecipeHolder<RoasterRecipe>> ROASTER =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.ROASTER_RECIPE), recipeHolderClass());

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Class<? extends RecipeHolder<RoasterRecipe>> recipeHolderClass() {
        return (Class) RecipeHolder.class;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RoasterRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        registration.addRecipes(ROASTER, minecraft.level.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.ROASTER_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(GrowthcraftCellarItems.ROASTER.get()), ROASTER);
    }
}
