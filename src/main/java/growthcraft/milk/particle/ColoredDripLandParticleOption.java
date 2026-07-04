package growthcraft.milk.particle;

import growthcraft.milk.init.GrowthcraftMilkParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripLandParticleOption(int color, int lingerTicks) implements ParticleOptions {
    public ColoredDripLandParticleOption(int color) {
        this(color, 24);
    }

    @Override
    public ParticleType<?> getType() {
        return GrowthcraftMilkParticles.COLORED_DRIP_LAND.get();
    }
}
