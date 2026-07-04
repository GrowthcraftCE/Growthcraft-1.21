package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import growthcraft.milk.recipe.CheesePressRecipe;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.MixingVatRecipe;
import growthcraft.milk.recipe.PancheonRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Reference.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CheesePressRecipe>> CHEESE_PRESS_TYPE =
            TYPES.register(Reference.UnlocalizedName.CHEESE_PRESS_RECIPE, () ->
                    RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHEESE_PRESS_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<ChurnRecipe>> CHURN_TYPE =
            TYPES.register(Reference.UnlocalizedName.CHURN_RECIPE, () ->
                    RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.CHURN_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<PancheonRecipe>> PANCHEON_TYPE =
            TYPES.register(Reference.UnlocalizedName.PANCHEON_RECIPE, () ->
                    RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.PANCHEON_RECIPE)));

    public static final DeferredHolder<RecipeType<?>, RecipeType<MixingVatRecipe>> MIXING_VAT_TYPE =
            TYPES.register(Reference.UnlocalizedName.MIXING_VAT_RECIPE, () ->
                    RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Reference.MODID, Reference.UnlocalizedName.MIXING_VAT_RECIPE)));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CheesePressRecipe>> CHEESE_PRESS_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.CHEESE_PRESS_RECIPE, CheesePressRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ChurnRecipe>> CHURN_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.CHURN_RECIPE, ChurnRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PancheonRecipe>> PANCHEON_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.PANCHEON_RECIPE, PancheonRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MixingVatRecipe>> MIXING_VAT_SERIALIZER =
            SERIALIZERS.register(Reference.UnlocalizedName.MIXING_VAT_RECIPE, MixingVatRecipe.Serializer::new);

    private GrowthcraftMilkRecipes() {
    }
}
