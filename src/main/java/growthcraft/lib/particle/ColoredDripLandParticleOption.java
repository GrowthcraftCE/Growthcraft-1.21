package growthcraft.lib.particle;

import growthcraft.core.init.GrowthcraftParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripLandParticleOption(int color, int lingerTicks, float scale) implements ParticleOptions {
    public ColoredDripLandParticleOption(int color) {
        this(color, 24, 1.0F);
    }

    public ColoredDripLandParticleOption(int color, int lingerTicks) {
        this(color, lingerTicks, 1.0F);
    }

    @Override
    public ParticleType<?> getType() {
        return GrowthcraftParticles.COLORED_DRIP_LAND.get();
    }
}
