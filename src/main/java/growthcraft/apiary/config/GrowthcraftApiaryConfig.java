package growthcraft.apiary.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftApiaryConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    private static ModConfigSpec.BooleanValue debugEnabled;
    private static ModConfigSpec.BooleanValue fluidsDebugEnabled;

    static {
        SERVER_BUILDER.push("debug");
        debugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional Growthcraft Apiary debug logging.")
                .define("enabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("fluids");
        fluidsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Apiary fluids and waxes.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    public static boolean isDebugEnabled() {
        return debugEnabled.get();
    }

    public static boolean isFluidsDebugEnabled() {
        return fluidsDebugEnabled.get();
    }

    private GrowthcraftApiaryConfig() {}
}
