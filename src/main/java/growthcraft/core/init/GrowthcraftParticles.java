package growthcraft.core.init;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.core.config.Reference;
import growthcraft.lib.particle.ColoredDripLandParticleOption;
import growthcraft.lib.particle.ColoredDripParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Reference.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDripParticleOption>> COLORED_DRIP = PARTICLE_TYPES.register(
            "colored_drip",
            () -> new ParticleType<>(false) {
                private final MapCodec<ColoredDripParticleOption> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        com.mojang.serialization.Codec.INT.fieldOf("color").forGetter(ColoredDripParticleOption::color),
                        com.mojang.serialization.Codec.DOUBLE.optionalFieldOf("landing_y", Double.NaN).forGetter(ColoredDripParticleOption::landingY),
                        com.mojang.serialization.Codec.FLOAT.optionalFieldOf("landing_scale", 1.0F).forGetter(ColoredDripParticleOption::landingScale)
                ).apply(instance, ColoredDripParticleOption::new));

                private final StreamCodec<? super RegistryFriendlyByteBuf, ColoredDripParticleOption> streamCodec = StreamCodec.composite(
                        ByteBufCodecs.INT, ColoredDripParticleOption::color,
                        ByteBufCodecs.DOUBLE, ColoredDripParticleOption::landingY,
                        ByteBufCodecs.FLOAT, ColoredDripParticleOption::landingScale,
                        ColoredDripParticleOption::new
                );

                @Override
                public MapCodec<ColoredDripParticleOption> codec() {
                    return codec;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, ColoredDripParticleOption> streamCodec() {
                    return streamCodec;
                }
            }
    );

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColoredDripLandParticleOption>> COLORED_DRIP_LAND = PARTICLE_TYPES.register(
            "colored_drip_land",
            () -> new ParticleType<>(false) {
                private final MapCodec<ColoredDripLandParticleOption> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        com.mojang.serialization.Codec.INT.fieldOf("color").forGetter(ColoredDripLandParticleOption::color),
                        com.mojang.serialization.Codec.INT.optionalFieldOf("linger_ticks", 24).forGetter(ColoredDripLandParticleOption::lingerTicks),
                        com.mojang.serialization.Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(ColoredDripLandParticleOption::scale)
                ).apply(instance, ColoredDripLandParticleOption::new));

                private final StreamCodec<? super RegistryFriendlyByteBuf, ColoredDripLandParticleOption> streamCodec = StreamCodec.composite(
                        ByteBufCodecs.INT, ColoredDripLandParticleOption::color,
                        ByteBufCodecs.INT, ColoredDripLandParticleOption::lingerTicks,
                        ByteBufCodecs.FLOAT, ColoredDripLandParticleOption::scale,
                        ColoredDripLandParticleOption::new
                );

                @Override
                public MapCodec<ColoredDripLandParticleOption> codec() {
                    return codec;
                }

                @Override
                public StreamCodec<? super RegistryFriendlyByteBuf, ColoredDripLandParticleOption> streamCodec() {
                    return streamCodec;
                }
            }
    );

    private GrowthcraftParticles() {
    }
}
