package growthcraft.cellar.config;

import growthcraft.lib.utils.ColorUtils;

/**
 * Reference metadata for the Growthcraft Cellar sub-mod.
 * Based on the 1.20 implementation: holds identifiers only.
 */
public final class Reference {
    public static final String MODID = "growthcraft_cellar";
    public static final String NAME = "Growthcraft Cellar";
    public static final String NAME_SHORT = "cellar";

    // Keep version aligned with the parent/core mod versioning
    public static final String VERSION = growthcraft.core.config.Reference.VERSION;


    public static class UnlocalizedName {
        // Helper to build namespaced IDs for registrations or lookups
        public static String namespace(String path) { return MODID + ":" + path; }

        public static final class Block {
            public static final String BREW_KETTLE = "brew_kettle";
            public static final String CULTURE_JAR = "culture_jar";
            public static final String FERMENT_BARREL = "fermentation_barrel";
            public static final String FERMENT_BARREL_ACACIA = "fermentation_barrel_acacia";
            public static final String FERMENT_BARREL_BIRCH = "fermentation_barrel_birch";
            public static final String FERMENT_BARREL_CRIMSON = "fermentation_barrel_crimson";
            public static final String FERMENT_BARREL_DARK_OAK = "fermentation_barrel_dark_oak";
            public static final String FERMENT_BARREL_JUNGLE = "fermentation_barrel_jungle";
            public static final String FERMENT_BARREL_OAK = "fermentation_barrel_oak";
            public static final String FERMENT_BARREL_SPRUCE = "fermentation_barrel_spruce";
            public static final String FERMENT_BARREL_WARPED = "fermentation_barrel_warped";
            public static final String FRUIT_PRESS = "fruit_press";
            public static final String FRUIT_PRESS_PISTON = "fruit_press_piston";
            public static final String ROASTER = "roaster";

            // Grapes and vines
            public static final String GRAPE_VINE = "grape_vine";
            public static final String GRAPE_VINE_CROP = "grape_vine_crop";
            public static final String GRAPE_VINE_LEAVES = "grape_vine_leaves";
            public static final String HOPS_VINE = "hops_vine";
            public static final String PURPLE_GRAPE_VINE = "purple_grape_vine";
            public static final String PURPLE_GRAPE_VINE_FRUIT = "purple_grape_vine_crop";
            public static final String PURPLE_GRAPE_VINE_LEAVES = "purple_grape_vine_leaves";
            public static final String RED_GRAPE_VINE = "red_grape_vine";
            public static final String RED_GRAPE_VINE_FRUIT = "red_grape_vine_crop";
            public static final String RED_GRAPE_VINE_LEAVES = "red_grape_vine_leaves";
            public static final String WHITE_GRAPE_VINE = "white_grape_vine";
            public static final String WHITE_GRAPE_VINE_FRUIT = "white_grape_vine_crop";
            public static final String WHITE_GRAPE_VINE_LEAVES = "white_grape_vine_leaves";

            // Cork tree blocks
            public static final String CORK_TREE = "cork_tree";
            public static final String CORK_TREE_LEAVES = "cork_tree_leaves";
            public static final String CORK_TREE_SAPLING = "cork_tree_sapling";
            public static final String CORK_WOOD = "cork_wood";
            public static final String CORK_WOOD_STRIPPED = "cork_wood_stripped";
            public static final String CORK_WOOD_LOG = "cork_wood_log";
            public static final String CORK_WOOD_LOG_STRIPPED = "cork_wood_log_stripped";

            private Block() {}
        }

        public static final class Item {
            public static final String GRAIN = "grain";
            public static final String GRAIN_AMBER = "grain_amber";
            public static final String GRAIN_BROWN = "grain_brown";
            public static final String GRAIN_COPPER = "grain_copper";
            public static final String GRAIN_DARK = "grain_dark";
            public static final String GRAIN_DEEP_AMBER = "grain_deep_amber";
            public static final String GRAIN_DEEP_COPPER = "grain_deep_copper";
            public static final String GRAIN_GOLDEN = "grain_golden";
            public static final String GRAIN_PALE_GOLDEN = "grain_pale_golden";

            public static final String GRAPE_PURPLE = "grape_purple";
            public static final String GRAPE_RED = "grape_red";
            public static final String GRAPE_WHITE = "grape_white";

            public static final String GRAPE_SEEDS_PURPLE = "grape_seeds_purple";
            public static final String GRAPE_SEEDS_RED = "grape_seeds_red";
            public static final String GRAPE_SEEDS_WHITE = "grape_seeds_white";

            public static final String HOPS = "hops";
            public static final String HOPS_SEEDS = "hops_seeds";

            public static final String CORK_BARK = "cork_bark";
            public static final String CORK_COASTER = "cork_coaster";

            // Yeasts
            public static final String YEAST_BAYANUS = "yeast_bayanus";
            public static final String YEAST_BAYANUS_ETHEREAL = "yeast_bayanus_ethereal";
            public static final String YEAST_BREWERS = "yeast_brewers";
            public static final String YEAST_BREWERS_ETHEREAL = "yeast_brewers_ethereal";
            public static final String YEAST_ETHEREAL = "yeast_ethereal";
            public static final String YEAST_LAGER = "yeast_lager";
            public static final String YEAST_LAGER_ETHEREAL = "yeast_lager_ethereal";

            // Cultures
            public static final String STARTER_CULTURE = "starter_culture";

            // Serving containers
            public static final String POTION_ALE = "potion_ale";
            public static final String POTION_LAGER = "potion_lager";
            public static final String POTION_WINE = "potion_wine";
            public static final String POTION_WINE_STAINED = "potion_wine_stained";
            public static final String BOTTLE_STAINED = "bottle_stained";

            // Fluid Buckets (match item model names)
            public static final String AMBER_ALE_FLUID_BUCKET = "amber_ale_fluid_bucket";
            public static final String AMBER_LAGER_FLUID_BUCKET = "amber_lager_fluid_bucket";
            public static final String AMBER_WORT_FLUID_BUCKET = "amber_wort_fluid_bucket";
            public static final String BROWN_ALE_FLUID_BUCKET = "brown_ale_fluid_bucket";
            public static final String BROWN_LAGER_FLUID_BUCKET = "brown_lager_fluid_bucket";
            public static final String BROWN_WORT_FLUID_BUCKET = "brown_wort_fluid_bucket";
            public static final String COPPER_ALE_FLUID_BUCKET = "copper_ale_fluid_bucket";
            public static final String COPPER_LAGER_FLUID_BUCKET = "copper_lager_fluid_bucket";
            public static final String COPPER_WORT_FLUID_BUCKET = "copper_wort_fluid_bucket";
            public static final String DARK_LAGER_FLUID_BUCKET = "dark_lager_fluid_bucket";
            public static final String DARK_WORT_FLUID_BUCKET = "dark_wort_fluid_bucket";
            public static final String DEEP_AMBER_WORT_FLUID_BUCKET = "deep_amber_wort_fluid_bucket";
            public static final String DEEP_COPPER_WORT_FLUID_BUCKET = "deep_copper_wort_fluid_bucket";
            public static final String GOLDEN_WORT_FLUID_BUCKET = "golden_wort_fluid_bucket";
            public static final String HOPPED_GOLDEN_WORT_FLUID_BUCKET = "hopped_golden_wort_fluid_bucket";
            public static final String IPA_ALE_FLUID_BUCKET = "ipa_ale_fluid_bucket";
            public static final String OLD_PORT_ALE_FLUID_BUCKET = "old_port_ale_fluid_bucket";
            public static final String PALE_ALE_FLUID_BUCKET = "pale_ale_fluid_bucket";
            public static final String PALE_GOLDEN_WORT_FLUID_BUCKET = "pale_golden_wort_fluid_bucket";
            public static final String PALE_LAGER_FLUID_BUCKET = "pale_lager_fluid_bucket";
            public static final String PILSNER_LAGER_FLUID_BUCKET = "pilsner_lager_fluid_bucket";
            public static final String POTION_ALE_FLUID_BUCKET = "potion_ale_fluid_bucket";
            public static final String POTION_LAGER_FLUID_BUCKET = "potion_lager_fluid_bucket";
            public static final String POTION_WINE_FLUID_BUCKET = "potion_wine_fluid_bucket";
            public static final String PURPLE_GRAPE_JUICE_FLUID_BUCKET = "purple_grape_juice_fluid_bucket";
            public static final String PURPLE_GRAPE_WINE_FLUID_BUCKET = "purple_grape_wine_fluid_bucket";
            public static final String RED_GRAPE_JUICE_FLUID_BUCKET = "red_grape_juice_fluid_bucket";
            public static final String RED_GRAPE_WINE_FLUID_BUCKET = "red_grape_wine_fluid_bucket";
            public static final String STOUT_ALE_FLUID_BUCKET = "stout_ale_fluid_bucket";
            public static final String VIENNA_LAGER_FLUID_BUCKET = "vienna_lager_fluid_bucket";
            public static final String WHITE_GRAPE_JUICE_FLUID_BUCKET = "white_grape_juice_fluid_bucket";
            public static final String WHITE_GRAPE_WINE_FLUID_BUCKET = "white_grape_wine_fluid_bucket";
            public static final String WORT_FLUID_BUCKET = "wort_fluid_bucket";

            private Item() {}
        }

        public static final class Fluid {
            public static final String AMBER_ALE = "amber_ale";
            public static final String AMBER_LAGER = "amber_lager";
            public static final String AMBER_WORT = "amber_wort";
            public static final String BROWN_ALE = "brown_ale";
            public static final String BROWN_LAGER = "brown_lager";
            public static final String BROWN_WORT = "brown_wort";
            public static final String COPPER_ALE = "copper_ale";
            public static final String COPPER_LAGER = "copper_lager";
            public static final String COPPER_WORT = "copper_wort";
            public static final String DARK_LAGER = "dark_lager";
            public static final String DARK_WORT = "dark_wort";
            public static final String DEEP_AMBER_WORT = "deep_amber_wort";
            public static final String DEEP_COPPER_WORT = "deep_copper_wort";
            public static final String GOLDEN_WORT = "golden_wort";
            public static final String HOPPED_GOLDEN_WORT = "hopped_golden_wort";
            public static final String IPA_ALE = "ipa_ale";
            public static final String OLD_PORT_ALE = "old_port_ale";
            public static final String PALE_ALE = "pale_ale";
            public static final String PALE_GOLDEN_WORT = "pale_golden_wort";
            public static final String PALE_LAGER = "pale_lager";
            public static final String PILSNER_LAGER = "pilsner_lager";
            public static final String POTION_ALE = "potion_ale";
            public static final String POTION_LAGER = "potion_lager";
            public static final String POTION_WINE = "potion_wine";
            public static final String PURPLE_GRAPE_JUICE = "purple_grape_juice";
            public static final String PURPLE_GRAPE_WINE = "purple_grape_wine";
            public static final String RED_GRAPE_JUICE = "red_grape_juice";
            public static final String RED_GRAPE_WINE = "red_grape_wine";
            public static final String STOUT_ALE = "stout_ale";
            public static final String VIENNA_LAGER = "vienna_lager";
            public static final String WHITE_GRAPE_JUICE = "white_grape_juice";
            public static final String WHITE_GRAPE_WINE = "white_grape_wine";
            public static final String WORT = "wort";

            private Fluid() {}
        }

        public static final class Recipe {
            public static final String BREW_KETTLE_RECIPE = "brew_kettle_recipe";
            public static final String CULTURE_JAR_RECIPE = "culture_jar_recipe";
            public static final String CULTURE_JAR_STARTER_RECIPE = "culture_jar_starter_recipe";
            public static final String FERMENT_BARREL_RECIPE = "fermentation_barrel_recipe";
            public static final String FRUIT_PRESS_RECIPE = "fruit_press_recipe";
            public static final String ROASTER_RECIPE = "roaster_recipe";

            private Recipe() {}
        }

        public static final class Tag {
            public static final String HEATSOURCES = "heat_sources";
            public static final String ROASTED_GRAIN = "roasted_grain";
            public static final String GRAPE_SEEDS = "seeds/grapes";
            public static final String GRAPE_FRUITS = "fruits/grapes";

            private Tag() {}
        }

        public static final class LootSerializer {
            public static final String BLOCK = "global_block_loot_modifier";
            private LootSerializer() {}
        }

        private UnlocalizedName() { /* Disable default public constructor. */ }
    }

    public static class FluidColor {
        public static final ColorUtils.GrowthcraftColor AMBER_ALE = new ColorUtils.GrowthcraftColor(0xFFBC6633);
        public static final ColorUtils.GrowthcraftColor AMBER_LAGER = new ColorUtils.GrowthcraftColor(0xFFAB5E2F);
        public static final ColorUtils.GrowthcraftColor AMBER_WORT = new ColorUtils.GrowthcraftColor(0xFFE5C7A2);
        public static final ColorUtils.GrowthcraftColor BROWN_ALE = new ColorUtils.GrowthcraftColor(0xFF5C311A);
        public static final ColorUtils.GrowthcraftColor BROWN_LAGER = new ColorUtils.GrowthcraftColor(0xFF401F1C);
        public static final ColorUtils.GrowthcraftColor BROWN_WORT = new ColorUtils.GrowthcraftColor(0xFF594732);
        public static final ColorUtils.GrowthcraftColor COPPER_ALE = new ColorUtils.GrowthcraftColor(0xFF8E4B31);
        public static final ColorUtils.GrowthcraftColor COPPER_LAGER = new ColorUtils.GrowthcraftColor(0xFF6F3D1F);
        public static final ColorUtils.GrowthcraftColor COPPER_WORT = new ColorUtils.GrowthcraftColor(0xFF936B53);
        public static final ColorUtils.GrowthcraftColor DARK_LAGER = new ColorUtils.GrowthcraftColor(0xFF211515);
        public static final ColorUtils.GrowthcraftColor DARK_WORT = new ColorUtils.GrowthcraftColor(0xFF452A19);
        public static final ColorUtils.GrowthcraftColor DEEP_AMBER_WORT = new ColorUtils.GrowthcraftColor(0xFFCFA26F);
        public static final ColorUtils.GrowthcraftColor DEEP_COPPER_WORT = new ColorUtils.GrowthcraftColor(0xFF805C2F);
        public static final ColorUtils.GrowthcraftColor GOLDEN_WORT = new ColorUtils.GrowthcraftColor(0xFFF6D02E);
        public static final ColorUtils.GrowthcraftColor HOPPED_GOLDEN_WORT = new ColorUtils.GrowthcraftColor(0xFFF6FE2E);
        public static final ColorUtils.GrowthcraftColor IPA_ALE = new ColorUtils.GrowthcraftColor(0xFFD2BD2C);
        public static final ColorUtils.GrowthcraftColor OLD_PORT_ALE = new ColorUtils.GrowthcraftColor(0xFF8E3616);
        public static final ColorUtils.GrowthcraftColor PALE_ALE = new ColorUtils.GrowthcraftColor(0xFFFBF855);
        public static final ColorUtils.GrowthcraftColor PALE_GOLDEN_WORT = new ColorUtils.GrowthcraftColor(0xFFFBF855);
        public static final ColorUtils.GrowthcraftColor PALE_LAGER = new ColorUtils.GrowthcraftColor(0xFFF3F33F);
        public static final ColorUtils.GrowthcraftColor PILSNER_LAGER = new ColorUtils.GrowthcraftColor(0xFFF6D02E);
        public static final ColorUtils.GrowthcraftColor PURPLE_GRAPE_JUICE = new ColorUtils.GrowthcraftColor(0xFF682961);
        public static final ColorUtils.GrowthcraftColor PURPLE_GRAPE_WINE = new ColorUtils.GrowthcraftColor(0xFF562251);
        public static final ColorUtils.GrowthcraftColor RED_GRAPE_JUICE = new ColorUtils.GrowthcraftColor(0xFFA63F4A);
        public static final ColorUtils.GrowthcraftColor RED_GRAPE_WINE = new ColorUtils.GrowthcraftColor(0xFF8A343D);
        public static final ColorUtils.GrowthcraftColor STOUT_ALE = new ColorUtils.GrowthcraftColor(0xFF0E0A07);
        public static final ColorUtils.GrowthcraftColor VIENNA_LAGER = new ColorUtils.GrowthcraftColor(0xFF904730);
        public static final ColorUtils.GrowthcraftColor WHITE_GRAPE_JUICE = new ColorUtils.GrowthcraftColor(0xFFB4C91C);
        public static final ColorUtils.GrowthcraftColor WHITE_GRAPE_WINE = new ColorUtils.GrowthcraftColor(0xFF96A817);
        public static final ColorUtils.GrowthcraftColor WORT = new ColorUtils.GrowthcraftColor(0xFFD0AF4E);

        private FluidColor() {
            /* Disable Automatic Creation of Public Constructor */
        }
    }

    public static class GrainColor {
        public static final ColorUtils.GrowthcraftColor GRAIN = new ColorUtils.GrowthcraftColor(0xFF595903);
        public static final ColorUtils.GrowthcraftColor GRAIN_AMBER = new ColorUtils.GrowthcraftColor(0xFFBBA04E);
        public static final ColorUtils.GrowthcraftColor GRAIN_BROWN = new ColorUtils.GrowthcraftColor(0xFF3F2F15);
        public static final ColorUtils.GrowthcraftColor GRAIN_COPPER = new ColorUtils.GrowthcraftColor(0xFF7c6430);
        public static final ColorUtils.GrowthcraftColor GRAIN_DARK = new ColorUtils.GrowthcraftColor(0xFF241802);
        public static final ColorUtils.GrowthcraftColor GRAIN_DEEP_AMBER = new ColorUtils.GrowthcraftColor(0xFF9b813e);
        public static final ColorUtils.GrowthcraftColor GRAIN_DEEP_COPPER = new ColorUtils.GrowthcraftColor(0xFF5d4922);
        public static final ColorUtils.GrowthcraftColor GRAIN_GOLDEN = new ColorUtils.GrowthcraftColor(0xFFdbc05d);
        public static final ColorUtils.GrowthcraftColor GRAIN_PALE_GOLDEN = new ColorUtils.GrowthcraftColor(0xFFfae16e);

        private GrainColor() {
            /* Disable Automatic Creation of Public Constructor */
        }
    }

    public static class LootTable{
        public static final String PILLAGER_OUTPOST_CHEST_LOOT = "pillager_outpost";
        public static final String UNDERWATER_RUIN_LOOT = "underwater_ruin";
        public static final String SHIPWRECK_CHEST_LOOT = "shipwreck";
        public static final String VILLAGE_CHEST_LOOT = "village";
        public static final String BURIED_TREASURE_CHEST_LOOT = "buried_treasure";
        public static final String WOODLAND_MANSION_CHEST_LOOT = "woodland_mansion";
        public static final String STRONGHOLD_CHEST_LOOT = "stronghold";


        private LootTable() {
            /* Disable Automatic Creation of Public Constructor */
        }
    }

    private Reference() { /* no-op */ }
}
