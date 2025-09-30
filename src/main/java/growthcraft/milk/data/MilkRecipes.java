package growthcraft.milk.data;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class MilkRecipes extends RecipeProvider {
    public MilkRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Common tags (NeoForge 'c' convention)
        TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/iron"));
        TagKey<Item> IRON_NUGGETS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "nuggets/iron"));

        // Iron Milking Bucket: uses iron nuggets and ingots, classic bucket-like pattern
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())
                .pattern("NNN")
                .pattern("I I")
                .pattern(" I ")
                .define('N', IRON_NUGGETS)
                .define('I', IRON_INGOTS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(growthcraft.milk.config.Reference.MODID, "milking_bucket_iron"));
    }
}
