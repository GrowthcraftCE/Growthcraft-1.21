package growthcraft.bamboo.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GrowthcraftBambooConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    private static ModConfigSpec.BooleanValue debugEnabled;
    private static ModConfigSpec.BooleanValue blocksDebugEnabled;

    static {
        SERVER_BUILDER.push("debug");
        debugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional Growthcraft Bamboo debug logging.")
                .define("enabled", false);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("blocks");
        blocksDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional debug logging for Bamboo blocks.")
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

    private GrowthcraftBambooConfig() {}
}
