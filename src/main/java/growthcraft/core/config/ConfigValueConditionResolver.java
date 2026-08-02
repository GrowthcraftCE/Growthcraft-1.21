package growthcraft.core.config;

import growthcraft.apiary.config.GrowthcraftApiaryConfig;
import growthcraft.apples.config.GrowthcraftApplesConfig;
import growthcraft.bamboo.config.GrowthcraftBambooConfig;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.Growthcraft;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import growthcraft.rice.config.GrowthcraftRiceConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class ConfigValueConditionResolver {
    private static final Map<String, ModConfigSpec> CONFIG_SPECS = Map.ofEntries(
            Map.entry("core", GrowthcraftConfig.SPEC),
            Map.entry("growthcraft", GrowthcraftConfig.SPEC),
            Map.entry("cellar", GrowthcraftCellarConfig.SPEC),
            Map.entry("growthcraft_cellar", GrowthcraftCellarConfig.SPEC),
            Map.entry("milk", GrowthcraftMilkConfig.SPEC),
            Map.entry("growthcraft_milk", GrowthcraftMilkConfig.SPEC),
            Map.entry("apiary", GrowthcraftApiaryConfig.SPEC),
            Map.entry("growthcraft_apiary", GrowthcraftApiaryConfig.SPEC),
            Map.entry("apples", GrowthcraftApplesConfig.SPEC),
            Map.entry("growthcraft_apples", GrowthcraftApplesConfig.SPEC),
            Map.entry("bamboo", GrowthcraftBambooConfig.SPEC),
            Map.entry("growthcraft_bamboo", GrowthcraftBambooConfig.SPEC),
            Map.entry("rice", GrowthcraftRiceConfig.SPEC),
            Map.entry("growthcraft_rice", GrowthcraftRiceConfig.SPEC)
    );

    private ConfigValueConditionResolver() {}

    static Optional<ModConfigSpec.BooleanValue> findBooleanConfigValue(String module, String name) {
        ModConfigSpec spec = CONFIG_SPECS.get(module);
        if (spec == null) {
            Growthcraft.LOGGER.error("Growthcraft condition error: unknown config module {}", module);
            return Optional.empty();
        }

        List<String> path = Arrays.asList(name.split("\\."));
        Object value = spec.getValues().get(path);
        if (value == null) {
            Growthcraft.LOGGER.error("Growthcraft condition error: invalid config value {}.{}", module, name);
            return Optional.empty();
        }
        if (!(value instanceof ModConfigSpec.BooleanValue booleanValue)) {
            Growthcraft.LOGGER.error("Growthcraft condition error: config value {}.{} is not boolean", module, name);
            return Optional.empty();
        }

        return Optional.of(booleanValue);
    }
}
