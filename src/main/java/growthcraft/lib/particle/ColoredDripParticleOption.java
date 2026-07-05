package growthcraft.lib.particle;

import growthcraft.core.init.GrowthcraftParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripParticleOption(int color) implements ParticleOptions {
    public static ColoredDripParticleOption fromTintColor(int tintColor) {
        return new ColoredDripParticleOption(tintColor & 0xFFFFFF);
    }

    @Override
    public ParticleType<?> getType() {
        return GrowthcraftParticles.COLORED_DRIP.get();
    }
}
