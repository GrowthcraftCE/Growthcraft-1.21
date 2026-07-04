package growthcraft.milk.item;

import growthcraft.milk.init.GrowthcraftMilkItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CheeseCurdsBlockItem extends BlockItem {
    public CheeseCurdsBlockItem(Block block) {
        super(block, new Item.Properties());
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
