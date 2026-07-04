package growthcraft.cellar.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;

public class CellarPotionItem extends PotionItem {
    public CellarPotionItem(Properties properties) {
        super(properties.stacksTo(16).component(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.getDescriptionId() + ".effect.empty";
    }
}
