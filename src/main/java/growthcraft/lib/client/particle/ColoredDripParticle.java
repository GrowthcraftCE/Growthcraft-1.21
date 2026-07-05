package growthcraft.lib.client.particle;

import growthcraft.lib.particle.ColoredDripLandParticleOption;
import growthcraft.lib.particle.ColoredDripParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ColoredDripParticle extends TextureSheetParticle {
    private final int color;
    private final double landingY;
    private final float landingScale;
    private final int landingLingerTicks;

    private ColoredDripParticle(ColoredDripParticleOption option, ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.color = option.color();
        this.landingY = option.landingY();
        this.landingScale = option.landingScale();
        this.landingLingerTicks = option.landingLingerTicks();
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.friction = 0.98F;
        this.lifetime = (int)(64.0D / (Math.random() * 0.8D + 0.2D));
        this.quadSize *= 0.8F;
        setColor(option.color());
        this.pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.onGround || hasReachedLandingY()) {
            double landY = Double.isNaN(landingY) ? this.y : landingY;
            this.level.addParticle(new ColoredDripLandParticleOption(color, landingLingerTicks, landingScale), this.x, landY, this.z, 0.0D, 0.0D, 0.0D);
            this.remove();
        }
    }

    private boolean hasReachedLandingY() {
        return !Double.isNaN(landingY) && this.y <= landingY;
    }

    private void setColor(int color) {
        int redRaw = (color >> 16) & 0xFF;
        int greenRaw = (color >> 8) & 0xFF;
        int blueRaw = color & 0xFF;
        float average = (redRaw + greenRaw + blueRaw) / 3.0F;
        float red = saturate(redRaw, average) / 255.0F;
        float green = saturate(greenRaw, average) / 255.0F;
        float blue = saturate(blueRaw, average) / 255.0F;
        this.setColor(red, green, blue);
    }

    private static float saturate(int channel, float average) {
        float saturated = average + (channel - average) * 3.5F;
        return Math.clamp(saturated, 0.0F, 255.0F);
    }

    public record Provider(SpriteSet spriteSet) implements ParticleProvider<ColoredDripParticleOption> {
        @Override
        public ColoredDripParticle createParticle(ColoredDripParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ColoredDripParticle particle = new ColoredDripParticle(option, level, x, y, z, spriteSet);
            particle.setParticleSpeed(xSpeed, ySpeed, zSpeed);
            return particle;
        }
    }

    public static class Land extends TextureSheetParticle {
        private Land(ColoredDripLandParticleOption option, ClientLevel level, double x, double y, double z, SpriteSet sprites) {
            super(level, x, y, z);
            this.setSize(0.01F, 0.01F);
            this.lifetime = (int)(Math.max(1, option.lingerTicks()) / (Math.random() * 0.8D + 0.2D));
            this.quadSize *= 0.9F * option.scale();
            setColor(option.color());
            this.pickSprite(sprites);
        }

        @Override
        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
        }

        @Override
        public void tick() {
            super.tick();
            this.xd = 0.0D;
            this.yd = 0.0D;
            this.zd = 0.0D;
        }

        private void setColor(int color) {
            float red = ((color >> 16) & 0xFF) / 255.0F;
            float green = ((color >> 8) & 0xFF) / 255.0F;
            float blue = (color & 0xFF) / 255.0F;
            this.setColor(red, green, blue);
        }

        public record Provider(SpriteSet spriteSet) implements ParticleProvider<ColoredDripLandParticleOption> {
            @Override
            public Land createParticle(ColoredDripLandParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new Land(option, level, x, y, z, spriteSet);
            }
        }
    }
}
