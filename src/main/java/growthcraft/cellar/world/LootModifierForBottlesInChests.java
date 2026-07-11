package growthcraft.cellar.world;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class LootModifierForBottlesInChests extends LootModifier
{
    private final int loot_chance_pillager_outpost,
      loot_chance_ocean_ruin,
      loot_chance_shipwreck,
      loot_chance_village,
      loot_chance_beach_treasure,
      loot_chance_dark_forest_mansion,
      loot_chance_stronghold;

    protected LootModifierForBottlesInChests(LootItemCondition[] conditionsIn, int chance1, int chance2, int chance3, int chance4, int chance5, int chance6, int chance7)
    {
        super(conditionsIn);
        this.loot_chance_pillager_outpost= chance1;
        this.loot_chance_ocean_ruin= chance2;
        this.loot_chance_shipwreck= chance3;
        this.loot_chance_village= chance4;
        this.loot_chance_beach_treasure= chance5;
        this.loot_chance_dark_forest_mansion= chance6;
        this.loot_chance_stronghold= chance7;
    }

    @Override
    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> lootList, LootContext lootContext)
    {
        // step 1: roll the dice
        int chance = 0;
        ResourceLocation lootTableId = lootContext.getQueriedLootTableId();
        if (lootTableId.equals(BuiltInLootTables.PILLAGER_OUTPOST.location()))
        {
            chance = this.loot_chance_pillager_outpost;
        }
        else if (lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL.location()) || lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG.location()))
        {
            chance = this.loot_chance_ocean_ruin;
        }
        else if (lootTableId.equals(BuiltInLootTables.SHIPWRECK_SUPPLY.location()) || lootTableId.equals(BuiltInLootTables.SHIPWRECK_MAP.location()))
        {
            chance = this.loot_chance_shipwreck;
        }
        else if (lootTableId.getPath().startsWith("chests/village/"))
        {
            chance = this.loot_chance_village;
        }
        else if (lootTableId.equals(BuiltInLootTables.BURIED_TREASURE.location()))
        {
            chance = this.loot_chance_beach_treasure;
        }
        else if (lootTableId.equals(BuiltInLootTables.WOODLAND_MANSION.location()))
        {
            chance = this.loot_chance_dark_forest_mansion;
        }
        else if (lootTableId.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR.location()) || lootTableId.equals(BuiltInLootTables.STRONGHOLD_CROSSING.location()))
        {
            chance = this.loot_chance_stronghold;
        }
        if (!(lootContext.getRandom().nextInt(100) < chance))
        {
            return lootList; // random chance failed, pack it in. chance is 0 for some of these cases anyway.
        }

        // step 2: decide on loot item. if there are multiple bottles in a chest (no reason why not), we want them of the same type so that they don't take more than 1 inventory slot.
        // yes we're repeating branches from above. above, there was a 99% chance the chance would be 0, and we didn't want to do any more logic other than rolling.
        int min = 0, max = 2, color = 0;
        MobEffectInstance effect1 = null, effect2 = null;
        String nameKey = "asd";
        if (lootTableId.equals(BuiltInLootTables.PILLAGER_OUTPOST.location()))
        {
            min = 6; max = 14;
            effect1 = new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 1, true, false);
            effect2 = new MobEffectInstance(MobEffects.SLOW_FALLING, 3600, 0, true, false);
            nameKey = "fluid_type.growthcraft_apiary.honey_mead_fluid";
            color = 0xE5C7A2;
        }
        else if (lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_SMALL.location()) || lootTableId.equals(BuiltInLootTables.UNDERWATER_RUIN_BIG.location()))
        {
            min = 1; max = 4;
            effect1 = new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false);
            effect2 = new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 1200, 0, true, false);
            nameKey = "fluid_type.growthcraft_cellar.white_grape_wine";
            color = 0xdfebd5;    // currently colors in recipe files are broken (lost in porting). i'll just slap something greenish here.
        }
        else if (lootTableId.equals(BuiltInLootTables.SHIPWRECK_SUPPLY.location()) || lootTableId.equals(BuiltInLootTables.SHIPWRECK_MAP.location()))
        {
            min = 4; max = 8;
            effect1 = new MobEffectInstance(MobEffects.LUCK, 6000, 1, true, false);
            nameKey = "fluid_type.growthcraft_cellar.old_port_ale";
            color = 0x805C2F;
        }
        else if (lootTableId.getPath().startsWith("chests/village/"))
        {
            min = 1; max = 3;
            effect1 = new MobEffectInstance(MobEffects.SATURATION, 1200, 1, true, false);
            nameKey = "fluid_type.growthcraft_cellar.white_grape_wine";
            color = 0xdfebd5;    // currently colors in recipe files are broken (lost in porting). i'll just slap something greenish here.
        }
        else if (lootTableId.equals(BuiltInLootTables.BURIED_TREASURE.location()))
        {
            min = 2; max = 4;
            effect1 = new MobEffectInstance(MobEffects.ABSORPTION, 4*1200, 1, true, false); // recipe's 1min is way too short
            nameKey = "fluid_type.growthcraft_cellar.purple_grape_wine";
            color = 0x3c0357;  // currently colors in recipe files are broken (lost in porting). i'll just slap something purple here.
        }
        else if (lootTableId.equals(BuiltInLootTables.WOODLAND_MANSION.location()))
        {
            min = 4; max = 8;
            effect1 = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3*1200, 2, true, false);
            effect2 = new MobEffectInstance(MobEffects.TRIAL_OMEN, 20*1200, 0, true, false);
            nameKey = "fluid_type.growthcraft_cellar.copper_lager";
            color = 0x936B53;
        }
        else if (lootTableId.equals(BuiltInLootTables.STRONGHOLD_CORRIDOR.location()) || lootTableId.equals(BuiltInLootTables.STRONGHOLD_CROSSING.location()))
        {
            min = 2; max = 10;
            effect1 = new MobEffectInstance(MobEffects.ABSORPTION, 6*1200, 1, true, false); // recipe's 1min is way too short
            nameKey = "fluid_type.growthcraft_cellar.purple_grape_wine";
            color = 0x3c0357;
        }

        // step 3: make a stack of bottles
        ItemStack bottles = GrowthcraftCellarItems.POTION_WINE.get().getDefaultInstance();
        bottles.setCount(lootContext.getRandom().nextIntBetweenInclusive(min, max));
        if (effect1 != null) {
            if (effect2 == null) {
                bottles.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), List.of(effect1)));
            }
            else {
                bottles.set(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(color), List.of(effect1, effect2)));
            }
        }
        bottles.set(DataComponents.ITEM_NAME, Component.translatable(nameKey));

        // step 3b: add a stack of bottles
        if (lootList.size() < 27)
        {
            lootList.add(bottles);
        }
        else
        {
            // step 3c: if full, pick an empty slot and add a stack of bottles.
            for (int i = lootList.size() - 1; i >= 0; i--)
            {
                if (lootList.get(i).isEmpty()) // we could replace some useless items (wheat, etc) but that might be too fancy for growthcraft.
                {
                    lootList.set(i, bottles);
                    break;
                }
            }
        }

        return lootList;
    }

    @Override
    @NotNull
    public MapCodec<? extends IGlobalLootModifier> codec()
    {
        return CODEC.get();
    }

    public static final Supplier<MapCodec<LootModifierForBottlesInChests>> CODEC = Suppliers.memoize(() ->
        RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_pillager_outpost").forGetter((m) -> m.loot_chance_pillager_outpost))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_ocean_ruin").forGetter((m) -> m.loot_chance_ocean_ruin))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_shipwreck").forGetter((m) -> m.loot_chance_shipwreck))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_village").forGetter((m) -> m.loot_chance_village))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_beach_treasure").forGetter((m) -> m.loot_chance_beach_treasure))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_dark_forest_mansion").forGetter((m) -> m.loot_chance_dark_forest_mansion))
                .and(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("loot_chance_stronghold").forGetter((m) -> m.loot_chance_stronghold))
                .apply(inst, LootModifierForBottlesInChests::new)));
}
