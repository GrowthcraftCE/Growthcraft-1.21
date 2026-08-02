package growthcraft.core.config;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class GrowthcraftConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    private static final String CATEGORY_WORLDGEN = "worldgen";

    // Master toggle for all Growthcraft Salt Ore generation
    private static final ModConfigSpec.BooleanValue SALT_ORE_GEN_ENABLED = SERVER_BUILDER
            .comment("Set to false to disable all Growthcraft Salt Ore Generation.")
            .define(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenEnabled"), true);

    // Dimension/target specific toggles
    private static final ModConfigSpec.BooleanValue saltOreGenTheEndEnabled = SERVER_BUILDER
            .comment("Set to false to disable Growthcraft Salt Ore Generation in The End.")
            .define(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenTheEndEnabled"), true);

    private static final ModConfigSpec.BooleanValue saltOreGenNetherEnabled = SERVER_BUILDER
            .comment("Set to false to disable Growthcraft Salt Ore Generation in the Nether.")
            .define(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenNetherEnabled"), true);

    private static final ModConfigSpec.BooleanValue saltOreGenDeepslateEnabled = SERVER_BUILDER
            .comment("Set to false to disable Growthcraft Salt Ore Generation in Deepslate (Overworld).")
            .define(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenDeepslateEnabled"), true);

    // Generation parameters
    private static final ModConfigSpec.IntValue saltOreGenVeinSize = SERVER_BUILDER
            .comment("Salt ore vein size (number of blocks per vein).")
            .defineInRange(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenVeinSize"), 5, 1, 10);

    private static final ModConfigSpec.IntValue saltOreGenHeightMin = SERVER_BUILDER
            .comment("Minimum Y height for salt ore generation.")
            .defineInRange(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenHeightMin"), -32, -64, 255);

    private static final ModConfigSpec.IntValue saltOreGenHeightMax = SERVER_BUILDER
            .comment("Maximum Y height for salt ore generation.")
            .defineInRange(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenHeightMax"), 64, -64, 255);

    private static final ModConfigSpec.IntValue saltOreGenSpreadAmount = SERVER_BUILDER
            .comment("Number of salt ore veins per chunk (spread amount).")
            .defineInRange(String.format("%s.%s", CATEGORY_WORLDGEN, "saltOreGenSpreadAmount"), 10, 1, 20);

    private static final ModConfigSpec.BooleanValue debugEnabled = SERVER_BUILDER
            .comment("Set to true to add additional Growthcraft Core debug logging.")
            .define("debug.enabled", false);

    private static final ModConfigSpec.BooleanValue worldgenDebugEnabled = SERVER_BUILDER
            .comment("Set to true to add additional debug logging for Growthcraft Core world generation.")
            .define("debug.worldgen.enabled", false);

    private static final ModConfigSpec.BooleanValue ropesDebugEnabled = SERVER_BUILDER
            .comment("Set to true to add additional debug logging for Growthcraft ropes and rope fences.")
            .define("debug.ropes.enabled", false);

    private static final ModConfigSpec.BooleanValue shopSignsDebugEnabled = SERVER_BUILDER
            .comment("Set to true to add additional debug logging for Growthcraft shop sign transforms.")
            .define("debug.shop_signs.enabled", false);

    private static ModConfigSpec.BooleanValue crowbarsEnabled; // Placeholder for future config

    public static final ModConfigSpec SPEC = SERVER_BUILDER.build();

    // Public getters for worldgen values (used by datagen and runtime where applicable)
    public static boolean saltOreGenEnabled() { return SALT_ORE_GEN_ENABLED.get(); }
    public static boolean saltOreGenEndEnabled() { return saltOreGenTheEndEnabled.get(); }
    public static boolean saltOreGenNetherEnabled() { return saltOreGenNetherEnabled.get(); }
    public static boolean saltOreGenDeepslateEnabled() { return saltOreGenDeepslateEnabled.get(); }
    public static int saltOreVeinSize() { return saltOreGenVeinSize.get(); }
    public static int saltOreHeightMin() { return saltOreGenHeightMin.get(); }
    public static int saltOreHeightMax() { return saltOreGenHeightMax.get(); }
    public static int saltOreSpreadAmount() { return saltOreGenSpreadAmount.get(); }
    public static boolean isDebugEnabled() { return debugEnabled.get(); }
    public static boolean isWorldgenDebugEnabled() { return worldgenDebugEnabled.get(); }
    public static boolean isRopesDebugEnabled() { return ropesDebugEnabled.get(); }
    public static boolean isShopSignsDebugEnabled() { return shopSignsDebugEnabled.get(); }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
