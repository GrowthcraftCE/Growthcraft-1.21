package growthcraft.cellar.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GrowthcraftCellarConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // Do we allow rice and corn as adjunct grains in brewing recipes
    private static ModConfigSpec.BooleanValue secondaryAdjunctGrainsAllowed;
    private static ModConfigSpec.IntValue grapeVineMaxHeight;

    static {
        SERVER_BUILDER.push("brewing");
        secondaryAdjunctGrainsAllowed = SERVER_BUILDER
                .comment("Do we allow rice and corn as adjunct grains")
                .define("allow_additional_adjunct_grains", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("crops");
        grapeVineMaxHeight = SERVER_BUILDER
                .comment("Maximum height that grape vines can climb before producing horizontal leaves.")
                .defineInRange("grape_vine_max_height", 8, 1, 32);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    // values can be pulled via recipe conditions. if this is gray, doesn't mean it is unused.
    public static boolean isSecondaryAdjunctGrainsAllowed() {
        return secondaryAdjunctGrainsAllowed.get();
    }

    public static int getGrapeVineMaxHeight() {
        return grapeVineMaxHeight.get();
    }

    private GrowthcraftCellarConfig() {}
}
