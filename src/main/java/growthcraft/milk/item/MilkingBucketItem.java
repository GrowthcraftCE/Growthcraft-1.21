package growthcraft.milk.item;

import growthcraft.milk.GrowthcraftMilk;
import growthcraft.milk.config.GrowthcraftMilkConfig;
import growthcraft.milk.init.GrowthcraftMilkFluids;
import growthcraft.milk.init.GrowthcraftMilkTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

/**
 * A special bucket item used to milk animals into Growthcraft Milk fluid buckets.
 * When empty, using on a milkable entity gives a Milk Bucket (our fluid bucket) and consumes one.
 * Otherwise behaves like a standard bucket for picking up/placing fluids.
 */
public class MilkingBucketItem extends Item implements DispensibleContainerItem {
    private final Supplier<? extends Fluid> fluidSupplier;

    private static void debug(String message, Object... args) {
        if (GrowthcraftMilkConfig.isBucketsDebugEnabled()) {
            GrowthcraftMilk.LOGGER.debug(message, args);
        }
    }

    public MilkingBucketItem(Supplier<? extends Fluid> fluidSupplier, Properties properties) {
        super(properties);
        this.fluidSupplier = fluidSupplier;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide) return InteractionResult.PASS;

        boolean isEmpty = this.fluidSupplier.get() == Fluids.EMPTY;
        if (!isEmpty) return InteractionResult.PASS;

        // Check tag first, then fallback to vanilla cow
        EntityType<?> type = target.getType();
        boolean milkable = type.is(GrowthcraftMilkTags.EntityTypes.MILKABLE) || target instanceof Cow;
        debug("[MilkingBucket] interactLivingEntity(Server): Player={} Target={} Milkable?={} StackCount={} Hand={}",
                player.getGameProfile().getName(), type.toShortString(), milkable, stack.getCount(), hand);
        if (milkable) {
            ItemStack milkBucket = new ItemStack(growthcraft.milk.init.GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
            boolean added = player.getInventory().add(milkBucket);
            debug("[MilkingBucket] Milking result: created {} addedToInv?={} (will drop if false)", milkBucket.getItem(), added);
            if (!added) {
                player.drop(milkBucket, false);
            }
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Do not handle generic bucket behavior; this tool is mainly for milking.
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public boolean emptyContents(Player player, Level level, BlockPos pos, BlockHitResult hit) {
        return false;
    }

}
