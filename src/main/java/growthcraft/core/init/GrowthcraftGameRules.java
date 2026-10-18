package growthcraft.core.init;

import net.minecraft.world.level.GameRules;

/** Registered during common setup, before world creation on either client or server. */
public final class GrowthcraftGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> SALT_GENERATION = GameRules.register(
            "growthcraftSaltGeneration", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    private GrowthcraftGameRules() {}

    public static void register() {
        // Loading this class registers the rule; enqueue this on the common setup thread.
    }
}
