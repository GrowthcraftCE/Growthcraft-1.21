package growthcraft.cellar.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item class for ethereal-based yeast that should always render with the
 * enchanted glint (shimmer) like enchanted items.
 */
public class EtherealYeastItem extends Item {
    public EtherealYeastItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Always render with the enchanted glint for ethereal yeast
        return true;
    }
}
