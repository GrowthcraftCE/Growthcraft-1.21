package growthcraft.lib.particle;

import growthcraft.core.init.GrowthcraftParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripLandParticleOption(int color, int lingerTicks) implements ParticleOptions {
    public ColoredDripLandParticleOption(int color) {
        this(color, 24);
    }

    @Override
    public ParticleType<?> getType() {
        return GrowthcraftParticles.COLORED_DRIP_LAND.get();
    }
}
