package growthcraft.core.compat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

///
/// here's the problem:
/// We have some recipes for mystical agriculture - I will use MA as an example, this works for any mod -and those recipes have
/// ... condition 1:  neoforge:mod_loaded (MA)  and condition 2: mysticalagriculture:crop_enabled.
/// Now if MA is absent, we will get long stack traces in log because the second condition can not be deserialized.
/// Yes, NF tries to deserialize all conditions at once.
/// So one solution is that we wrap MA's condition with our condition which will evaluate inner condition only if the host mod is present.
///
/// before, we would say:
///   "neoforge:conditions": [
///     {
///       "type": "neoforge:mod_loaded",
///       "modid": "mysticalagriculture"
///     },
///     {
///       "type": "mysticalagriculture:crop_enabled",
///       "crop": "mysticalagriculture:dye"
///     }
///   ]
///
/// now we will say:
///      "neoforge:conditions": [
///     {
///       "type": "neoforge:mod_loaded",
///       "modid": "mysticalagriculture"
///     },
///     {
///       "type": "our_mod:third_party_condition",
///       "target_type": "mysticalagriculture:crop_enabled",
///       "target_content": "{ \"crop\": \"mysticalagriculture:dye\" }",
///       "value_if_not_present": false
///     }
///   ]
///
/// And yes, my careful and thorough reader, last of four values does make mod_loader condition unneeded, but I prefer to leave it for readability.
///


public record ThirdPartyRecipeCondition(String targetType, String targetContent, boolean valueIfModMotPresent) implements ICondition
{
    @Override
    public boolean test(IContext iContext)
    {
        ResourceLocation conditionId = ResourceLocation.parse(this.targetType);
        if (NeoForgeRegistries.CONDITION_SERIALIZERS.containsKey(conditionId))
        {
            MapCodec<? extends ICondition> codec = NeoForgeRegistries.CONDITION_SERIALIZERS.get(conditionId);
            JsonObject jsonObject = JsonParser.parseString(this.targetContent).getAsJsonObject();
            var result = codec.codec().decode(JsonOps.INSTANCE, jsonObject);
            if (result.isSuccess())
            {
                return result.getOrThrow().getFirst().test(iContext);
            }
        }
        return this.valueIfModMotPresent;
    }

    /////////////////////////////////////////////////////

    @Override
    public MapCodec<? extends ICondition> codec()
    {
        return CODEC;
    }

    public static MapCodec<ThirdPartyRecipeCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Codec.STRING.fieldOf("target_type").forGetter(tprc -> tprc.targetType),
                            Codec.STRING.fieldOf("target_content").forGetter(tprc -> tprc.targetContent),
                            Codec.BOOL.fieldOf("value_if_not_present").forGetter(tprc -> tprc.valueIfModMotPresent))
                    .apply(builder, ThirdPartyRecipeCondition::new));
}

