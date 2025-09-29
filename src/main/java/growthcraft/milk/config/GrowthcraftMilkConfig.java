package growthcraft.milk.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Configuration for the Growthcraft Milk module (ported from 1.20.x).
 * Key names and defaults mirror the legacy module to ease migration.
 */
public final class GrowthcraftMilkConfig {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // Master switches
    private static ModConfigSpec.BooleanValue moduleEnabled; // _master_switch_.module_enabled
    private static ModConfigSpec.BooleanValue featureEnabledBeverages; // _master_switch_.feature_exception_beverages

    // GUIs and debug
    private static ModConfigSpec.BooleanValue churnGuiEnabled;           // churn.guiEnabled
    private static ModConfigSpec.BooleanValue pancheonGuiEnabled;        // pancheon.guiEnabled
    private static ModConfigSpec.BooleanValue mixingVatGuiEnabled;       // mixing_vat.guiEnabled
    private static ModConfigSpec.BooleanValue mixingVatDebugEnabled;     // mixing_vat.debugEnabled
    private static ModConfigSpec.BooleanValue mixingVatConsumeActivator; // mixing_vat.consumeMixingVatActivator

    // Loot
    private static ModConfigSpec.BooleanValue stomachLootEnabled;        // loot_modifiers.stomachLootEnabled
    private static ModConfigSpec.IntValue stomachLootChance;             // loot_modifiers.stomachLootChance (0..100)

    // Cheese
    private static ModConfigSpec.BooleanValue cheeseDebugEnabled;        // cheese.debugEnabled

    static {
        // Master switch block
        SERVER_BUILDER.push("_master_switch_");
        moduleEnabled = SERVER_BUILDER
                .comment("This master-switch lets you disable the entire Milk module - blocks, items, and drops.")
                .define("module_enabled", true);
        featureEnabledBeverages = SERVER_BUILDER
                .comment("Exception: allow beverages/fluids interactions even if the module is disabled.")
                .define("feature_exception_beverages", true);
        SERVER_BUILDER.pop();

        // Churn
        SERVER_BUILDER.push("churn");
        churnGuiEnabled = SERVER_BUILDER
                .comment("Set to true to allow users to access the Churn GUI.")
                .define("guiEnabled", true);
        SERVER_BUILDER.pop();

        // Mixing Vat
        SERVER_BUILDER.push("mixing_vat");
        mixingVatGuiEnabled = SERVER_BUILDER
                .comment("Set to true to allow users to access the Mixing Vat GUI.")
                .define("guiEnabled", true);
        mixingVatDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the Mixing Vat.")
                .define("debugEnabled", false);
        mixingVatConsumeActivator = SERVER_BUILDER
                .comment("Set to true to consume the activation item when opening the Mixing Vat GUI.")
                .define("consumeMixingVatActivator", false);
        SERVER_BUILDER.pop();

        // Pancheon
        SERVER_BUILDER.push("pancheon");
        pancheonGuiEnabled = SERVER_BUILDER
                .comment("Set to true to allow users to access the Pancheon GUI.")
                .define("guiEnabled", true);
        SERVER_BUILDER.pop();

        // Loot Modifiers
        SERVER_BUILDER.push("loot_modifiers");
        stomachLootEnabled = SERVER_BUILDER
                .comment("Set to true to enable looting of stomach from cows.")
                .define("stomachLootEnabled", true);
        stomachLootChance = SERVER_BUILDER
                .comment("Chance (0-100) to loot a stomach from a cow. Requires stomachLootEnabled = true.")
                .defineInRange("stomachLootChance", 5, 0, 100);
        SERVER_BUILDER.pop();

        // Cheese
        SERVER_BUILDER.push("cheese");
        cheeseDebugEnabled = SERVER_BUILDER
                .comment("Set to true to add additional logging to debug the cheese wheel and curds blocks.")
                .define("debugEnabled", false);
        SERVER_BUILDER.pop();

        SPEC = SERVER_BUILDER.build();
    }

    // Accessors mirroring 1.20.x naming
    public static boolean getModuleEnabled() { return moduleEnabled.get(); }
    public static boolean getFeatureEnabledBeverages() { return featureEnabledBeverages.get(); }

    public static boolean isChurnGuiEnabled() { return churnGuiEnabled.get(); }
    public static boolean isPancheonGuiEnabled() { return pancheonGuiEnabled.get(); }
    public static boolean isMixingVatGuiEnabled() { return mixingVatGuiEnabled.get(); }
    public static boolean isMixingDebugEnabled() { return mixingVatDebugEnabled.get(); }
    public static boolean isConsumeMixingVatActivator() { return mixingVatConsumeActivator.get(); }

    public static boolean isStomachLootingEnabled() { return stomachLootEnabled.get(); }
    public static int getStomachLootChance() { return Boolean.TRUE.equals(stomachLootEnabled.get()) ? stomachLootChance.get() : 0; }

    public static boolean isCheeseDebugEnabled() { return cheeseDebugEnabled.get(); }

    private GrowthcraftMilkConfig() {}
}
