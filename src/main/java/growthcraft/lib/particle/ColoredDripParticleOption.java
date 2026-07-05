package growthcraft.lib.particle;

import growthcraft.core.init.GrowthcraftParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public record ColoredDripParticleOption(int color, double landingY, float landingScale) implements ParticleOptions {
    public ColoredDripParticleOption(int color) {
        this(color, Double.NaN, 1.0F);
    }

    public ColoredDripParticleOption(int color, double landingY) {
        this(color, landingY, 1.0F);
    }

    public static ColoredDripParticleOption fromTintColor(int tintColor) {
        return new ColoredDripParticleOption(tintColor & 0xFFFFFF);
    }

    public static ColoredDripParticleOption fromTintColor(int tintColor, double landingY) {
        return new ColoredDripParticleOption(tintColor & 0xFFFFFF, landingY);
    }

    public static ColoredDripParticleOption fromTintColor(int tintColor, double landingY, float landingScale) {
        return new ColoredDripParticleOption(tintColor & 0xFFFFFF, landingY, landingScale);
    }

    @Override
    public ParticleType<?> getType() {
        return GrowthcraftParticles.COLORED_DRIP.get();
    }
}
