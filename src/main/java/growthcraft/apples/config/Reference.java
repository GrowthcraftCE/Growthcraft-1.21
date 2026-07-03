package growthcraft.apples.config;

public final class Reference {
    public static final String MODID = "growthcraft_apples";
    public static final String NAME = "Growthcraft Apples";
    public static final String NAME_SHORT = "apples";
    public static final String VERSION = growthcraft.core.config.Reference.VERSION;

    private Reference() {}

    public static final class UnlocalizedName {
        public static final class Fluid {
            public static final String APPLE_CIDER = "apple_cider";
            public static final String APPLE_JUICE = "apple_juice";

            private Fluid() {}
        }

        public static final class Block {
            public static final String APPLE_PLANK = "apple_plank";
            public static final String APPLE_PLANK_BUTTON = "apple_plank_button";
            public static final String APPLE_PLANK_DOOR = "apple_plank_door";
            public static final String APPLE_PLANK_FENCE = "apple_plank_fence";
            public static final String APPLE_PLANK_FENCE_GATE = "apple_plank_fence_gate";
            public static final String APPLE_PLANK_FENCE_ROPE_LINEN = "apple_plank_fence_rope_linen";
            public static final String APPLE_PLANK_PRESSURE_PLATE = "apple_plank_pressure_plate";
            public static final String APPLE_PLANK_SLAB = "apple_plank_slab";
            public static final String APPLE_PLANK_STAIRS = "apple_plank_stairs";
            public static final String APPLE_PLANK_TRAPDOOR = "apple_plank_trapdoor";
            public static final String APPLE_TREE_FRUIT = "apple_tree_fruit";
            public static final String APPLE_TREE_LEAVES = "apple_tree_leaves";
            public static final String APPLE_TREE_SAPLING = "apple_tree_sapling";
            public static final String APPLE_WOOD = "apple_wood";
            public static final String APPLE_WOOD_LOG = "apple_wood_log";
            public static final String APPLE_WOOD_LOG_STRIPPED = "apple_wood_log_stripped";
            public static final String APPLE_WOOD_STRIPPED = "apple_wood_stripped";
            public static final String BEE_BOX_APPLE = "bee_box_apple";

            private Block() {}
        }

        public static final class Item {
            public static final String APPLE_SEEDS = "apple_seeds";

            private Item() {}
        }

        public static final class Worldgen {
            public static final String APPLE_TREE = "apple_tree";

            private Worldgen() {}
        }

        private UnlocalizedName() {}
    }

    public static final class FluidColor {
        public static final growthcraft.lib.utils.ColorUtils.GrowthcraftColor APPLE_CIDER = new growthcraft.lib.utils.ColorUtils.GrowthcraftColor(0xFFDF9C40);
        public static final growthcraft.lib.utils.ColorUtils.GrowthcraftColor APPLE_JUICE = new growthcraft.lib.utils.ColorUtils.GrowthcraftColor(0xFFFFD627);

        private FluidColor() {}
    }
}
