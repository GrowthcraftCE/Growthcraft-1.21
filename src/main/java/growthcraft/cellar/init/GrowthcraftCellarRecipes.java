package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.CultureJarRecipe;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.recipe.RoasterRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers custom recipe types and serializers for Growthcraft Cellar.
 */
public final class GrowthcraftCellarRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Reference.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CultureJarRecipe>> CULTURE_JAR_TYPE =
            TYPES.register("culture_jar", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "culture_jar")));

    public static final DeferredHolder<RecipeType<?>, RecipeType<BrewKettleRecipe>> BREW_KETTLE_TYPE =
            TYPES.register(Reference.UnlocalizedName.Recipe.BREW_KETTLE_RECIPE, () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.BREW_KETTLE_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<FermentationBarrelRecipe>> FERMENTATION_BARREL_TYPE =
            TYPES.register(Reference.UnlocalizedName.Recipe.FERMENT_BARREL_RECIPE, () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FERMENT_BARREL_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<FruitPressRecipe>> FRUIT_PRESS_TYPE =
            TYPES.register(Reference.UnlocalizedName.Recipe.FRUIT_PRESS_RECIPE, () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.FRUIT_PRESS_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<RoasterRecipe>> ROASTER_TYPE =
            TYPES.register(Reference.UnlocalizedName.Recipe.ROASTER_RECIPE, () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.Recipe.ROASTER_RECIPE)));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CultureJarRecipe>> CULTURE_JAR_SERIALIZER =
            SERIALIZERS.register("culture_jar", CultureJarRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BrewKettleRecipe>> BREW_KETTLE_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.Recipe.BREW_KETTLE_RECIPE, BrewKettleRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FermentationBarrelRecipe>> FERMENTATION_BARREL_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.Recipe.FERMENT_BARREL_RECIPE, FermentationBarrelRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FruitPressRecipe>> FRUIT_PRESS_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.Recipe.FRUIT_PRESS_RECIPE, FruitPressRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RoasterRecipe>> ROASTER_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.Recipe.ROASTER_RECIPE, RoasterRecipe.Serializer::new);

    private GrowthcraftCellarRecipes() {}
}
