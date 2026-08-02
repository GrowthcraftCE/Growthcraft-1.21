package growthcraft.core.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.core.Growthcraft;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.Optional;

public record ConfigValueCondition(String module, String name) implements ICondition {
    public static final MapCodec<ConfigValueCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("module").forGetter(ConfigValueCondition::module),
            Codec.STRING.fieldOf("name").forGetter(ConfigValueCondition::name)
    ).apply(instance, ConfigValueCondition::new));

    @Override
    public boolean test(IContext context) {
        Optional<ModConfigSpec.BooleanValue> configValue = ConfigValueConditionResolver.findBooleanConfigValue(module, name);
        if (configValue.isEmpty()) {
            return false;
        }

        try {
            return configValue.get().get();
        } catch (IllegalStateException exception) {
            Growthcraft.LOGGER.error("Growthcraft condition error: config value {}.{} was read before its config loaded",
                    module, name, exception);
            return false;
        }
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
