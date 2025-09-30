package growthcraft.core.data.recipe;

import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftRecipeProvider extends RecipeProvider {
    public GrowthcraftRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // Shaped: 3x3 salt -> 1 salt_block
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GrowthcraftItems.SALT_BLOCK.get())
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .define('s', GrowthcraftItems.SALT.get())
                .unlockedBy(getHasName(GrowthcraftItems.SALT.get()), has(GrowthcraftItems.SALT.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "salt_block"));

        // Shapeless: 1 salt_block -> 9 salt
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, GrowthcraftItems.SALT.get(), 9)
                .requires(GrowthcraftItems.SALT_BLOCK.get())
                .unlockedBy(getHasName(GrowthcraftItems.SALT_BLOCK.get()), has(GrowthcraftItems.SALT_BLOCK.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "salt_from_block"));

        // Rope (linen) recipe: 8x rope_linen from string and lead
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GrowthcraftItems.ROPE_LINEN.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.STRING)
                .define('B', Items.LEAD)
                .group("growthcraft")
                .unlockedBy(getHasName(Items.STRING), has(Items.STRING))
                .unlockedBy(getHasName(Items.LEAD), has(Items.LEAD))
                .save(output, ResourceLocation.fromNamespaceAndPath(Reference.MODID, "rope_linen"));

        // Common iron ingots tag used by crowbar recipes
        TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/iron"));

        // Crowbar recipes (shaped) using common tags for NeoForge
        TagKey<Item> IRON_NUGGETS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "nuggets/iron"));

        // Pattern:
        //   "  A"
        //   "CBC"
        //   "A  "
        // A = iron nuggets, B = iron ingots, C = color matching carpet
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BLACK.get(), Blocks.BLACK_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_black");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BLUE.get(), Blocks.BLUE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_blue");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_BROWN.get(), Blocks.BROWN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_brown");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_CYAN.get(), Blocks.CYAN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_cyan");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_GRAY.get(), Blocks.GRAY_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_gray");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_GREEN.get(), Blocks.GREEN_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_green");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIGHT_BLUE.get(), Blocks.LIGHT_BLUE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_light_blue");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIGHT_GRAY.get(), Blocks.LIGHT_GRAY_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_light_gray");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_LIME.get(), Blocks.LIME_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_lime");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_MAGENTA.get(), Blocks.MAGENTA_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_magenta");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_ORANGE.get(), Blocks.ORANGE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_orange");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_PINK.get(), Blocks.PINK_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_pink");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_PURPLE.get(), Blocks.PURPLE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_purple");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_RED.get(), Blocks.RED_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_red");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_WHITE.get(), Blocks.WHITE_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_white");
        addCrowbarRecipe(output, GrowthcraftItems.CROWBAR_YELLOW.get(), Blocks.YELLOW_CARPET.asItem(), IRON_NUGGETS, IRON_INGOTS, "crowbar_yellow");

        // --- Milk module recipes ---
        // Common copper tags (NeoForge 'c')
        TagKey<Item> COPPER_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/copper"));

        // Iron Milking Bucket
        net.minecraft.data.recipes.ShapedRecipeBuilder.shaped(RecipeCategory.MISC, growthcraft.milk.init.GrowthcraftMilkItems.MILKING_BUCKET_IRON.get())
                .pattern("NNN")
                .pattern("I I")
                .pattern(" I ")
                .define('N', IRON_NUGGETS)
                .define('I', IRON_INGOTS)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(growthcraft.milk.config.Reference.MODID, "milking_bucket_iron"));

        // Copper Milking Bucket
        net.minecraft.data.recipes.ShapedRecipeBuilder.shaped(RecipeCategory.MISC, growthcraft.milk.init.GrowthcraftMilkItems.MILKING_BUCKET_COPPER.get())
                .pattern("NNN")
                .pattern("I I")
                .pattern(" I ")
                .define('N', IRON_NUGGETS)
                .define('I', COPPER_INGOTS)
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output, ResourceLocation.fromNamespaceAndPath(growthcraft.milk.config.Reference.MODID, "milking_bucket_copper"));
    }

    private static void addCrowbarRecipe(RecipeOutput output, Item result, Item carpet, TagKey<Item> nuggets, TagKey<Item> ingots, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result)
                .pattern("  A")
                .pattern("CBC")
                .pattern("A  ")
                .define('A', nuggets)
                .define('B', ingots)
                .define('C', carpet)
                .unlockedBy("has_iron_ingots", has(ingots))
                .unlockedBy("has_" + name + "_carpet", has(carpet))
                .save(output, ResourceLocation.fromNamespaceAndPath(Reference.MODID, name));
    }
}
