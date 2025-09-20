package growthcraft.cellar.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GrowthcraftCellarConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // Do we allow rice and corn as adjunct grains in brewing recipes
    private static ModConfigSpec.BooleanValue secondaryAdjunctGrainsAllowed;

    static {
        SERVER_BUILDER.push("brewing");
        secondaryAdjunctGrainsAllowed = SERVER_BUILDER
                .comment("Do we allow rice and corn as adjunct grains")
                .define("allow_additional_adjunct_grains", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    // values can be pulled via recipe conditions. if this is gray, doesn't mean it is unused.
    public static boolean isSecondaryAdjunctGrainsAllowed() {
        return secondaryAdjunctGrainsAllowed.get();
    }

    private GrowthcraftCellarConfig() {}
}
