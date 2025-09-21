package growthcraft.core.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * Crowbar item that behaves like a sword. The attack damage adjustment (-2) is
 * applied via an OffsetTier wrapper at registration time, allowing vanilla to
 * generate dynamic attribute tooltips correctly.
 */
public class CrowbarItem extends SwordItem {
    public CrowbarItem(Tier tier, Properties properties) {
        super(tier, properties);
    }
}
