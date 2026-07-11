package growthcraft.cellar.init;

import com.mojang.serialization.MapCodec;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.recipe.*;
import growthcraft.cellar.world.LootModifierForBottlesInChests;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

/**
 * Registers GLMs for Growthcraft Cellar.
 */
public final class GrowthcraftCellarLootModifiers
{
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MODID);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> GLMSerializer1 = LOOT_MODIFIERS.register("bottles_in_loot_chest", LootModifierForBottlesInChests.CODEC);


    public static void init(IEventBus modBus)
    {
        LOOT_MODIFIERS.register(modBus);
    }

    private GrowthcraftCellarLootModifiers() {}
}
