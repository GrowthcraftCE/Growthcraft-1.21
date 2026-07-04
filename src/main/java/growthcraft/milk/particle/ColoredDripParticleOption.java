package growthcraft.milk.particle;

import growthcraft.milk.init.GrowthcraftMilkParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripParticleOption(int color) implements ParticleOptions {
    @Override
    public ParticleType<?> getType() {
        return GrowthcraftMilkParticles.COLORED_DRIP.get();
    }
}
