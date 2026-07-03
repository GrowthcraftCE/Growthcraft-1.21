package growthcraft.rice.config;

import growthcraft.lib.utils.ColorUtils;

public final class Reference {
    public static final String MODID = "growthcraft_rice";
    public static final String NAME = "Growthcraft Rice";
    public static final String NAME_SHORT = "rice";
    public static final String VERSION = growthcraft.core.config.Reference.VERSION;

    private Reference() {}

    public static final class UnlocalizedName {
        public static final class Block {
            public static final String CULTIVATED_FARMLAND = "cultivated_farmland";
            public static final String RICE_CROP = "rice_crop";

            private Block() {}
        }

        public static final class Item {
            public static final String CHICKEN_RICE = "chicken_rice";
            public static final String CULTIVATOR = "cultivator";
            public static final String KNIFE = "knife";
            public static final String ONIGIRI = "onigiri";
            public static final String RICE = "rice";
            public static final String RICE_COOKED = "rice_cooked";
            public static final String RICE_GRAINS = "rice_grains";
            public static final String RICE_STALK = "rice_stalk";
            public static final String SUSHI_ROLL = "sushi_roll";
            public static final String YEAST_SEISHU = "yeast_seishu";

            private Item() {}
        }

        private UnlocalizedName() {}
    }

    public static final class FluidColor {
        public static final ColorUtils.GrowthcraftColor RICE_WATER = new ColorUtils.GrowthcraftColor(0xFFF6F8ED);
        public static final ColorUtils.GrowthcraftColor RICE_WINE = new ColorUtils.GrowthcraftColor(0xFFD9DADB);
        public static final ColorUtils.GrowthcraftColor SAKE = new ColorUtils.GrowthcraftColor(0xFFEAECEC);

        private FluidColor() {}
    }
}
