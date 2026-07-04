package growthcraft.milk.item;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CheeseCurdsDrainedItem extends Item {
    public CheeseCurdsDrainedItem() {
        super(new Item.Properties());
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return GrowthcraftMilkItems.CHEESE_CLOTH.get().getDefaultInstance();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
