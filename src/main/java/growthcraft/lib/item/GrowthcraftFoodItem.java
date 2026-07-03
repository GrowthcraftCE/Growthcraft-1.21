package growthcraft.lib.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class GrowthcraftFoodItem extends Item {
    public GrowthcraftFoodItem(int nutrition, float saturationModifier, int maxStackSize) {
        super(new Item.Properties()
                .stacksTo(maxStackSize)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .build()));
    }
}
