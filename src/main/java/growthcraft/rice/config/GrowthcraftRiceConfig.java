package growthcraft.rice.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftRiceConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    private static ModConfigSpec.BooleanValue debugEnabled;
    private static ModConfigSpec.BooleanValue cropsDebugEnabled;
    private static ModConfigSpec.BooleanValue fluidsDebugEnabled;

    static {
        SERVER_BUILDER.push("debug");
        debugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional Growthcraft Rice debug logging.")
                .define("enabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("crops");
        cropsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Rice crops and cultivated farmland.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("fluids");
        fluidsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Rice fluids.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    public static boolean isDebugEnabled() {
        return debugEnabled.get();
    }

    public static boolean isCropsDebugEnabled() {
        return cropsDebugEnabled.get();
    }

    public static boolean isFluidsDebugEnabled() {
        return fluidsDebugEnabled.get();
    }

    private GrowthcraftRiceConfig() {}
}
