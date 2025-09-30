package growthcraft.milk.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Bucket item for Growthcraft Milk variants that returns the corresponding
 * empty milking bucket when emptied/used in crafting or fluid transfers.
 */
public class GrowthcraftMilkBucketItem extends BucketItem {
    private final Supplier<Item> emptyReturn;

    public GrowthcraftMilkBucketItem(Fluid content, Supplier<Item> emptyReturn, Properties properties) {
        super(content, properties);
        this.emptyReturn = emptyReturn;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ret = super.use(level, player, hand);
        if (!level.isClientSide && ret.getResult().consumesAction()) {
            ItemStack out = ret.getObject();
            if (!player.getAbilities().instabuild) {
                // Replace vanilla empty bucket with our designated empty return
                return InteractionResultHolder.sidedSuccess(new ItemStack(this.emptyReturn.get()), level.isClientSide);
            }
        }
        return ret;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return new ItemStack(this.emptyReturn.get());
    }

}
