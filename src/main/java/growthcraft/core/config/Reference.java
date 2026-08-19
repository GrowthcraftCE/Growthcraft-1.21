package growthcraft.core.config;

public class Reference {
    public static final String MODID = "growthcraft";
    public static final String NAME = "Growthcraft";
    public static final String NAME_SHORT = "core";
    public static final String VERSION = "1.21.1.8";

    private Reference() { /* Prevent default public constructor */ }

    /**
     * Centralized keys used for registry/resource paths and translation keys.
     */
    public static class UnlocalizedName {
        // Helper to build namespaced IDs for registrations or lookups
        public static String ns(String path) { return MODID + ":" + path; }

        public static final class CreativeTab {
            public static final String TAB = "tab";

            private CreativeTab() {}
        }

        public static final class Item {
            public static final String SALT = "salt";
            // Rope items
            public static final String ROPE = "rope";
            public static final String ROPE_LINEN = "rope_linen";

            // Crowbar variants
            public static final String CROWBAR_BLACK = "crowbar_black";
            public static final String CROWBAR_BLUE = "crowbar_blue";
            public static final String CROWBAR_BROWN = "crowbar_brown";
            public static final String CROWBAR_CYAN = "crowbar_cyan";
            public static final String CROWBAR_GRAY = "crowbar_gray";
            public static final String CROWBAR_GREEN = "crowbar_green";
            public static final String CROWBAR_LIGHT_BLUE = "crowbar_light_blue";
            public static final String CROWBAR_LIGHT_GRAY = "crowbar_light_gray";
            public static final String CROWBAR_LIME = "crowbar_lime";
            public static final String CROWBAR_MAGENTA = "crowbar_magenta";
            public static final String CROWBAR_ORANGE = "crowbar_orange";
            public static final String CROWBAR_PINK = "crowbar_pink";
            public static final String CROWBAR_PURPLE = "crowbar_purple";
            public static final String CROWBAR_RED = "crowbar_red";
            public static final String CROWBAR_WHITE = "crowbar_white";
            public static final String CROWBAR_YELLOW = "crowbar_yellow";
            public static final String WRENCH = "wrench";

            private Item() {}
        }

        public static final class Block {
            public static final String SALT_BLOCK = "salt_block";
            public static final String SALT_ORE = "salt_ore";
            public static final String SALT_ORE_PLACED = "salt_ore_placed";

            // Rope blocks
            public static final String ROPE = "rope";
            public static final String ROPE_LINEN = "rope_linen";
            public static final String ROPE_LINEN_ACACIA_FENCE = "rope_linen_acacia_fence";
            public static final String ROPE_LINEN_BAMBOO_FENCE = "rope_linen_bamboo_fence";
            public static final String ROPE_LINEN_BIRCH_FENCE = "rope_linen_birch_fence";
            public static final String ROPE_LINEN_CHERRY_FENCE = "rope_linen_cherry_fence";
            public static final String ROPE_LINEN_CRIMSON_FENCE = "rope_linen_crimson_fence";
            public static final String ROPE_LINEN_DARK_OAK_FENCE = "rope_linen_dark_oak_fence";
            public static final String ROPE_LINEN_JUNGLE_FENCE = "rope_linen_jungle_fence";
            public static final String ROPE_LINEN_MANGROVE_FENCE = "rope_linen_mangrove_fence";
            public static final String ROPE_LINEN_NETHER_BRICK_FENCE = "rope_linen_nether_brick_fence";
            public static final String ROPE_LINEN_OAK_FENCE = "rope_linen_oak_fence";
            public static final String ROPE_LINEN_SPRUCE_FENCE = "rope_linen_spruce_fence";
            public static final String ROPE_LINEN_WARPED_FENCE = "rope_linen_warped_fence";

            private Block() {}
        }

        public static final class Tag {
            public static final String CROPS_RICE = "crops/rice";
            public static final String GRAIN_RICE = "grain/rice";
            public static final String HEATSOURCES = "heatsources";
            public static final String KNIVES = "tools/knives";
            public static final String ROASTER_WRENCH = "roaster_wrench";
            public static final String ROPE = "rope";
            public static final String ROPE_FENCE = "rope_fence";
            public static final String SALT = "salt";
            public static final String SEEDS_RICE = "seeds/rice";
            public static final String DUSTS_SALT = "dusts/salt";

            private Tag() {}
        }

        // Serene Seasons compatibility
        public static final class Compat {
            public static final String SPRING_CROPS = "spring_crops";
            public static final String SUMMER_CROPS = "summer_crops";
            public static final String AUTUMN_CROPS = "autumn_crops";
            public static final String WINTER_CROPS = "winter_crops";

            private Compat() {}
        }

        private UnlocalizedName() { /* Disable default public constructor. */ }
    }
}
