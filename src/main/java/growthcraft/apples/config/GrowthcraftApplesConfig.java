package growthcraft.apples.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftApplesConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    private static ModConfigSpec.BooleanValue debugEnabled;
    private static ModConfigSpec.BooleanValue blocksDebugEnabled;
    private static ModConfigSpec.BooleanValue cropsDebugEnabled;
    private static ModConfigSpec.BooleanValue fluidsDebugEnabled;

    static {
        SERVER_BUILDER.push("debug");
        debugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional Growthcraft Apples debug logging.")
                .define("enabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("blocks");
        blocksDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Apple wood and block interactions.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("crops");
        cropsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Apple tree growth and fruit blocks.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("fluids");
        fluidsDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Apples fluids.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    public static boolean isDebugEnabled() {
        return debugEnabled.get();
    }

    public static boolean isBlocksDebugEnabled() {
        return blocksDebugEnabled.get();
    }

    public static boolean isCropsDebugEnabled() {
        return cropsDebugEnabled.get();
    }

    public static boolean isFluidsDebugEnabled() {
        return fluidsDebugEnabled.get();
    }

    private GrowthcraftApplesConfig() {}
}
