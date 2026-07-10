package growthcraft.core.init;

import com.mojang.serialization.MapCodec;
import growthcraft.core.compat.ThirdPartyRecipeCondition;
import growthcraft.core.config.ConfigValueCondition;
import growthcraft.core.config.Reference;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class GrowthcraftConditions {
    private GrowthcraftConditions() {}

    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Reference.MODID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ConfigValueCondition>> CONFIG_VALUE =
            CONDITION_CODECS.register("config_value", () -> ConfigValueCondition.CODEC);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ThirdPartyRecipeCondition>> WRAPPER =
            CONDITION_CODECS.register("third_party_condition", () -> ThirdPartyRecipeCondition.CODEC);
}
