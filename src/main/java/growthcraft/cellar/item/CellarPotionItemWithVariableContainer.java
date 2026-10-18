package growthcraft.cellar.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class CellarPotionItemWithVariableContainer extends CellarPotionItem {
    private final Supplier<Item> container; // bottle, pint...

    public CellarPotionItemWithVariableContainer(Properties properties, Supplier<Item> container) {
        super(properties);
        this.container = container;
    }

    ///  had to copy/paste the method because some smart cookie hard-coded vanilla bottle in two places.
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        Player player = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if (!level.isClientSide) {
            PotionContents potioncontents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
            potioncontents.forEachEffect(p_330883_ -> {
                if (p_330883_.getEffect().value().isInstantenous()) {
                    p_330883_.getEffect().value().applyInstantenousEffect(player, player, entityLiving, p_330883_.getAmplifier(), 1.0);
                } else {
                    entityLiving.addEffect(p_330883_);
                }
            });
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        if (player == null || !player.hasInfiniteMaterials()) {
            if (stack.isEmpty()) {
                return new ItemStack(this.container.get());
            }

            if (player != null) {
                player.getInventory().add(new ItemStack(this.container.get()));
            }
        }

        entityLiving.gameEvent(GameEvent.DRINK);
        return stack;
    }
}
