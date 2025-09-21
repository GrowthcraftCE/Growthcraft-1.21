package growthcraft.core.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.Locale;

/**
 * Crowbar item that behaves like a sword. The attack damage adjustment (-2) is
 * applied via an OffsetTier wrapper at registration time, allowing vanilla to
 * generate dynamic attribute tooltips correctly.
 */
public class CrowbarItem extends SwordItem {
    // Keep a reference to the tier so we can compute display values for the tooltip.
    private final Tier gcTier;

    public CrowbarItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.gcTier = tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        // Flavor line
        tooltip.add(Component.translatable("tooltip.growthcraft.crowbar").withStyle(ChatFormatting.GRAY));

        // Compute display values similar to vanilla attribute tooltip numbers.
        // For swords: display damage = tier bonus + 3.0 (sword base). Vanilla shows this value (not +1 base).
        float displayDamage = gcTier.getAttackDamageBonus() + 3.0F;
        // For swords: attack speed modifier is -2.4; vanilla displays total = 4.0 (player base) + (-2.4) = 1.6
        float displaySpeed = 4.0F + (-2.4F);

        // Prepend numbers to attribute names
        tooltip.add(Component.translatable("item.modifiers.mainhand")
                .append(Component.literal(":"))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" " + formatNumber(displayDamage) + " ")
                .append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal(" " + formatNumber(displaySpeed) + " ")
                .append(Component.translatable("attribute.name.generic.attack_speed"))
                .withStyle(ChatFormatting.DARK_GREEN));
    }

    private static String formatNumber(float value) {
        if (Math.abs(value - Math.round(value)) < 1.0e-4) {
            return Integer.toString(Math.round(value));
        }
        return String.format(Locale.ROOT, "%.1f", value);
    }
}
