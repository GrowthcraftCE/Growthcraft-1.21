package growthcraft.milk.config;

import growthcraft.lib.utils.ColorUtils;

/**
 * Reference metadata and constants for the Growthcraft Milk sub-mod.
 * Includes legacy unlocalized name keys and color definitions for items, blocks, and fluids
 * to ease the 1.21.1 porting effort.
 */
public final class Reference {
    public static final String MODID = "growthcraft_milk";
    public static final String NAME = "Growthcraft Milk";
    public static final String NAME_SHORT = "milk";

    // Align with the core mod version
    public static final String VERSION = growthcraft.core.config.Reference.VERSION;

    private Reference() { /* Prevent instantiation */ }

    public static final class UnlocalizedName {
        public static final String APPENZELLER = "appenzeller";
        public static final String ASIAGO = "asiago";
        public static final String BUTTER = "butter";
        public static final String BUTTER_MILK = "butter_milk";
        public static final String BUTTER_SALTED = "butter_salted";
        public static final String CASU_MARZU = "casu_marzu";
        public static final String CHEDDAR = "cheddar";
        public static final String CHEESE_BASE = "cheese_base";
        public static final String CHEESE_CLOTH = "cheese_cloth";
        public static final String CHEESE_PRESS = "cheese_press";
        public static final String CHEESE_PRESS_RECIPE = "cheese_press_recipe";
        public static final String CHEESE_WHEEL_TILE_ENTITY = "cheese_wheel_tile_entity";
        public static final String CHURN = "churn";
        public static final String CHURN_RECIPE = "churn_recipe";
        public static final String CONDENSED_MILK = "condensed_milk";
        public static final String CREAM = "cream";
        public static final String CULTURED_MILK = "cultured_milk";
        public static final String EMMENTALER = "emmentaler";
        public static final String GORGONZOLA = "gorgonzola";
        public static final String GOUDA = "gouda";
        public static final String HANGING_SIGN_1_OAK = "hanging_sign_1_oak";
        public static final String HANGING_SIGN_1_SPRUCE = "hanging_sign_1_spruce";
        public static final String HANGING_SIGN_2_OAK = "hanging_sign_2_oak";
        public static final String HANGING_SIGN_2_SPRUCE = "hanging_sign_2_spruce";
        public static final String ICE_CREAM_APPLE = "ice_cream_apple";
        public static final String ICE_CREAM_CHOCOLATE = "ice_cream_chocolate";
        public static final String ICE_CREAM_GRAPE_PURPLE = "ice_cream_grape_purple";
        public static final String ICE_CREAM_GRAPE_RED = "ice_cream_grape_red";
        public static final String ICE_CREAM_GRAPE_WHITE = "ice_cream_grape_white";
        public static final String ICE_CREAM_HONEY = "ice_cream_honey";
        public static final String ICE_CREAM_PUMPKIN = "ice_cream_pumpkin";
        public static final String ICE_CREAM_WATERMELON = "ice_cream_watermelon";
        public static final String KUMIS = "kumis";
        public static final String MILK = "milk";
        public static final String MILKING_BUCKET_IRON = "milking_bucket_iron";
        public static final String MIXING_VAT = "mixing_vat";
        public static final String MIXING_VAT_RECIPE = "mixing_vat_recipe";
        public static final String MONTEREY = "monterey";
        public static final String PANCHEON = "pancheon";
        public static final String PANCHEON_RECIPE = "pancheon_recipe";
        public static final String PARMESAN = "parmesan";
        public static final String PRESS = "press";
        public static final String PROVOLONE = "provolone";
        public static final String RENNET = "rennet";
        public static final String RICOTTA = "ricotta";
        public static final String SHOP_SIGN = "shop_sign";
        public static final String SKIM_MILK = "skim_milk";
        public static final String STARTER_CULTURE = "starter_culture";
        public static final String STOMACH = "stomach";
        public static final String THISTLE = "thistle";
        public static final String THISTLE_CROP = "thistle_crop";
        public static final String THISTLE_SEED = "thistle_seed";
        public static final String WHEY = "whey";
        public static final String YOGURT_APPLE = "yogurt_apple";
        public static final String YOGURT_CHOCOLATE = "yogurt_chocolate";
        public static final String YOGURT_GRAPE_PURPLE = "yogurt_grape_purple";
        public static final String YOGURT_GRAPE_RED = "yogurt_grape_red";
        public static final String YOGURT_GRAPE_WHITE = "yogurt_grape_white";
        public static final String YOGURT_HONEY = "yogurt_honey";
        public static final String YOGURT_PLAIN = "yogurt_plain";
        public static final String YOGURT_PUMPKIN = "yogurt_pumpkin";
        public static final String YOGURT_WATERMELON = "yogurt_watermelon";
        public static final String TAG_MILK_BUCKETS = "milk_buckets";
        public static final String TAG_MILKABLE = "milkable";

        private UnlocalizedName() { /* Prevent instantiation */ }
    }

    public static final class ItemColor {
        public static final ColorUtils.GrowthcraftColor APPENZELLER_CHEESE = new ColorUtils.GrowthcraftColor(0xE9DA9A);
        public static final ColorUtils.GrowthcraftColor ASIAGO_CHEESE = new ColorUtils.GrowthcraftColor(0xC1B9A0);
        public static final ColorUtils.GrowthcraftColor CASU_MARZU_CHEESE = new ColorUtils.GrowthcraftColor(0x886C33);
        public static final ColorUtils.GrowthcraftColor CHEDDAR_CHEESE = new ColorUtils.GrowthcraftColor(0xF2BE6F);
        public static final ColorUtils.GrowthcraftColor EMMENTALER_CHEESE = new ColorUtils.GrowthcraftColor(0xF9F3CC);
        public static final ColorUtils.GrowthcraftColor GORGONZOLA_CHEESE = new ColorUtils.GrowthcraftColor(0xD0C3B9);
        public static final ColorUtils.GrowthcraftColor GOUDA_CHEESE = new ColorUtils.GrowthcraftColor(0xB99F3B);
        public static final ColorUtils.GrowthcraftColor MONTEREY_CHEESE = new ColorUtils.GrowthcraftColor(0xF4F2DB);
        public static final ColorUtils.GrowthcraftColor PARMESAN_CHEESE = new ColorUtils.GrowthcraftColor(0xE3D7B9);
        public static final ColorUtils.GrowthcraftColor PROVOLONE_CHEESE = new ColorUtils.GrowthcraftColor(0xC3BCA3);
        public static final ColorUtils.GrowthcraftColor RICOTTA_CHEESE = new ColorUtils.GrowthcraftColor(0xEEEDEC);

        private ItemColor() { /* Prevent instantiation */ }
    }

    public static final class BlockColor {
        public static final ColorUtils.GrowthcraftColor APPENZELLER_CHEESE = new ColorUtils.GrowthcraftColor(0xE9DA9A);
        public static final ColorUtils.GrowthcraftColor ASIAGO_CHEESE = new ColorUtils.GrowthcraftColor(0xC1B9A0);
        public static final ColorUtils.GrowthcraftColor CASU_MARZU_CHEESE = new ColorUtils.GrowthcraftColor(0x886C33);
        public static final ColorUtils.GrowthcraftColor CHEDDAR_CHEESE = new ColorUtils.GrowthcraftColor(0xF2BE6F);
        public static final ColorUtils.GrowthcraftColor EMMENTALER_CHEESE = new ColorUtils.GrowthcraftColor(0xF9F3CC);
        public static final ColorUtils.GrowthcraftColor GORGONZOLA_CHEESE = new ColorUtils.GrowthcraftColor(0xD0C3B9);
        public static final ColorUtils.GrowthcraftColor GOUDA_CHEESE = new ColorUtils.GrowthcraftColor(0xB99F3B);
        public static final ColorUtils.GrowthcraftColor MONTEREY_CHEESE = new ColorUtils.GrowthcraftColor(0xF4F2DB);
        public static final ColorUtils.GrowthcraftColor PARMESAN_CHEESE = new ColorUtils.GrowthcraftColor(0xE3D7B9);
        public static final ColorUtils.GrowthcraftColor PROVOLONE_CHEESE = new ColorUtils.GrowthcraftColor(0xC3BCA3);
        public static final ColorUtils.GrowthcraftColor RICOTTA_CHEESE = new ColorUtils.GrowthcraftColor(0xEEEDEC);

        private BlockColor() { /* Prevent instantiation */ }
    }

    public static final class FluidColor {
        public static final ColorUtils.GrowthcraftColor BUTTER_MILK = new ColorUtils.GrowthcraftColor(0xFFFEF1B5);
        public static final ColorUtils.GrowthcraftColor CHEESE_BASE = new ColorUtils.GrowthcraftColor(0xFFFDD0);
        public static final ColorUtils.GrowthcraftColor CONDENSED_MILK = new ColorUtils.GrowthcraftColor(0xFFFFFFFA);
        public static final ColorUtils.GrowthcraftColor CREAM = new ColorUtils.GrowthcraftColor(0xFFFFFDD0);
        public static final ColorUtils.GrowthcraftColor CULTURED_MILK = new ColorUtils.GrowthcraftColor(0xFFF7D99E);
        public static final ColorUtils.GrowthcraftColor KUMIS = new ColorUtils.GrowthcraftColor(0xFFF9F9F9);
        public static final ColorUtils.GrowthcraftColor MILK = new ColorUtils.GrowthcraftColor(0xFFF6F8ED);
        public static final ColorUtils.GrowthcraftColor RENNET = new ColorUtils.GrowthcraftColor(0xFF877243);
        public static final ColorUtils.GrowthcraftColor SKIM_MILK = new ColorUtils.GrowthcraftColor(0xFFFFFFFA);
        public static final ColorUtils.GrowthcraftColor WHEY = new ColorUtils.GrowthcraftColor(0xFF94a860);

        private FluidColor() { /* Prevent instantiation */ }
    }
}
