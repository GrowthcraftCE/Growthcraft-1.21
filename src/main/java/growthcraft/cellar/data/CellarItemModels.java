package growthcraft.cellar.data;

import growthcraft.cellar.config.Reference;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates item models for Growthcraft Cellar items so that hand-authored JSONs are not required.
 */
public class CellarItemModels extends ItemModelProvider {
    public CellarItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Grains share a single base texture and are tinted via item color handlers.
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_AMBER, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_BROWN, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_COPPER, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_DARK, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_DEEP_AMBER, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_DEEP_COPPER, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_GOLDEN, rlTex("item/grain_base"));
        generatedWithTexture(Reference.UnlocalizedName.Item.GRAIN_PALE_GOLDEN, rlTex("item/grain_base"));

        // Limit conversion scope to assets we are certain have textures available right now.
        // Additional items (grapes, hops, yeasts, etc.) can be enabled once their textures exist.

        // Cultures
        generatedWithTexture("starter_culture", ResourceLocation.fromNamespaceAndPath("growthcraft_milk", "item/starter_culture"));

        // Fluid bucket item models (layered bucket textures from core mod assets)
        bucket(Reference.UnlocalizedName.Item.AMBER_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.AMBER_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.AMBER_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.BROWN_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.BROWN_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.BROWN_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.COPPER_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.COPPER_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.COPPER_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.DARK_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.DARK_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.DEEP_AMBER_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.DEEP_COPPER_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.GOLDEN_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.HOPPED_GOLDEN_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.IPA_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.OLD_PORT_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PALE_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PALE_GOLDEN_WORT_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PALE_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PILSNER_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.POTION_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.POTION_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.POTION_WINE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PURPLE_GRAPE_JUICE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.PURPLE_GRAPE_WINE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.RED_GRAPE_JUICE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.RED_GRAPE_WINE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.STOUT_ALE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.VIENNA_LAGER_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.WHITE_GRAPE_JUICE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.WHITE_GRAPE_WINE_FLUID_BUCKET);
        bucket(Reference.UnlocalizedName.Item.WORT_FLUID_BUCKET);
    }

    private void generated(String name) {
        withExistingParent(name, mcLoc("item/generated")).texture("layer0", rlTex("item/" + name));
    }

    private void generatedWithTexture(String name, ResourceLocation texture) {
        withExistingParent(name, mcLoc("item/generated")).texture("layer0", texture);
    }

    private void bucket(String name) {
        // Uses bucket textures from the core mod's assets (namespace "growthcraft") to match existing art.
        ItemModelBuilder builder = withExistingParent(name, mcLoc("item/generated"));
        builder.texture("layer0", ResourceLocation.fromNamespaceAndPath("growthcraft", "item/bucket/bucket_fluid"));
        builder.texture("layer1", ResourceLocation.fromNamespaceAndPath("growthcraft", "item/bucket/bucket_base"));
    }

    private ResourceLocation rlTex(String path) {
        return ResourceLocation.fromNamespaceAndPath(Reference.MODID, path);
    }
}
