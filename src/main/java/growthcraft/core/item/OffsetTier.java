package growthcraft.core.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * A Tier wrapper that offsets the attack damage bonus by a specified amount while
 * delegating all other properties to the base tier. Used to tweak weapon damage
 * without re-implementing attribute component plumbing.
 */
public final class OffsetTier implements Tier {
    private final Tier base;
    private final float attackDamageOffset; // can be negative to reduce damage

    public OffsetTier(Tier base, float attackDamageOffset) {
        this.base = base;
        this.attackDamageOffset = attackDamageOffset;
    }

    @Override
    public int getUses() {
        return base.getUses();
    }

    @Override
    public float getSpeed() {
        return base.getSpeed();
    }

    @Override
    public float getAttackDamageBonus() {
        return base.getAttackDamageBonus() + attackDamageOffset;
    }

    @Override
    public int getEnchantmentValue() {
        return base.getEnchantmentValue();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return base.getRepairIngredient();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return base.getIncorrectBlocksForDrops();
    }

    @Override
    public String toString() {
        return "OffsetTier{" +
                "base=" + base +
                ", attackDamageOffset=" + attackDamageOffset +
                '}';
    }
}
