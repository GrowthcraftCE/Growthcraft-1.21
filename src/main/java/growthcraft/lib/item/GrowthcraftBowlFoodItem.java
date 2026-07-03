package growthcraft.lib.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class GrowthcraftBowlFoodItem extends Item {
    public GrowthcraftBowlFoodItem(int nutrition, float saturationModifier, int maxStackSize) {
        super(new Item.Properties()
                .stacksTo(maxStackSize)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition)
                        .saturationModifier(saturationModifier)
                        .usingConvertsTo(Items.BOWL)
                        .build()));
    }
}
