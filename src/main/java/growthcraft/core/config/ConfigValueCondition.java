package growthcraft.core.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.config.GrowthcraftCellarConfig;
import growthcraft.core.Growthcraft;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ConfigValueCondition(String module, String name) implements ICondition {
    public static final MapCodec<ConfigValueCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("module").forGetter(ConfigValueCondition::module),
            Codec.STRING.fieldOf("name").forGetter(ConfigValueCondition::name)
    ).apply(instance, ConfigValueCondition::new));

    @Override
    public boolean test(IContext context) {
        if ("cellar".equals(module) && "brewing.allow_additional_adjunct_grains".equals(name)) {
            return GrowthcraftCellarConfig.isSecondaryAdjunctGrainsAllowed();
        }

        Growthcraft.LOGGER.error("Growthcraft condition error: invalid config value {}.{}", module, name);
        return false;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
