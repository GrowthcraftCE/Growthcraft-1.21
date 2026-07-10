package growthcraft.core.config;

import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.Growthcraft;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class ConfigValueConditionResolver {
    private static final Map<String, ModConfigSpec> CONFIG_SPECS = Map.of(
            "core", GrowthcraftConfig.SPEC,
            "growthcraft", GrowthcraftConfig.SPEC,
            "cellar", GrowthcraftCellarConfig.SPEC,
            "growthcraft_cellar", GrowthcraftCellarConfig.SPEC,
            "milk", GrowthcraftMilkConfig.SPEC,
            "growthcraft_milk", GrowthcraftMilkConfig.SPEC
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
