package growthcraft.lib.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Vector3f;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidRegistryContainer {
    public final net.neoforged.neoforge.registries.DeferredHolder<FluidType, FluidType> type;
    public final FluidType.Properties typeProperties;
    public final net.neoforged.neoforge.registries.DeferredHolder<Block, LiquidBlock> block;
    public final net.neoforged.neoforge.registries.DeferredHolder<Item, BucketItem> bucket;
    public final net.neoforged.neoforge.registries.DeferredHolder<Fluid, net.neoforged.neoforge.fluids.BaseFlowingFluid.Source> source;
    public final net.neoforged.neoforge.registries.DeferredHolder<Fluid, net.neoforged.neoforge.fluids.BaseFlowingFluid.Flowing> flowing;
    private net.neoforged.neoforge.fluids.BaseFlowingFluid.Properties properties;

    public DeferredRegister<Fluid> FLUID_REGISTRY;
    public DeferredRegister<FluidType> FLUID_TYPE_REGISTRY;

    public DeferredRegister<Block> BLOCK_REGISTRY;
    public DeferredRegister<Item> ITEM_REGISTRY;

    public FluidRegistryContainer(String name,
                                  FluidType.Properties typeProperties,
                                  Supplier<IClientFluidTypeExtensions> clientExtensions,
                                  @Nullable AdditionalProperties additionalProperties,
                                  BlockBehaviour.Properties blockProperties,
                                  Item.Properties itemProperties,
                                  DeferredRegister<Fluid>  FLUID_REGISTRY,
                                  DeferredRegister<FluidType> FLUID_TYPE_REGISTRY,
                                  DeferredRegister<Block> BLOCK_REGISTRY,
                                  DeferredRegister<Item> ITEM_REGISTRY) {

        this.FLUID_REGISTRY = FLUID_REGISTRY;
        this.FLUID_TYPE_REGISTRY = FLUID_TYPE_REGISTRY;
        this.BLOCK_REGISTRY = BLOCK_REGISTRY;
        this.ITEM_REGISTRY = ITEM_REGISTRY;

        this.typeProperties = typeProperties.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);

        this.type = FLUID_TYPE_REGISTRY.register(name, () -> new FluidType(this.typeProperties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(clientExtensions.get());
            }
        });

        this.source = FLUID_REGISTRY.register(name + "_source", () -> new BaseFlowingFluid.Source(this.properties));
        this.flowing = FLUID_REGISTRY.register(name + "_flowing",
                () -> new BaseFlowingFluid.Flowing(this.properties));

        this.properties = new BaseFlowingFluid.Properties(this.type, this.source, this.flowing);
        if (additionalProperties != null) {
            this.properties.explosionResistance(additionalProperties.explosionResistance)
                    .levelDecreasePerBlock(additionalProperties.levelDecreasePerBlock)
                    .slopeFindDistance(additionalProperties.slopeFindDistance).tickRate(additionalProperties.tickRate);
        }

        this.block = BLOCK_REGISTRY.register(name + "_fluid", () -> {
            BaseFlowingFluid.Source src = this.source.get();
            if (additionalProperties != null && additionalProperties.blockFactory != null) {
                return additionalProperties.blockFactory.apply(src, blockProperties);
            }
            return new LiquidBlock(src, blockProperties);
        });
        this.properties.block(() -> this.block.get());

        this.bucket = ITEM_REGISTRY.register(name + "_fluid_bucket", () -> new BucketItem(this.source.get(), itemProperties));
        this.properties.bucket(() -> this.bucket.get());
    }

    public FluidRegistryContainer(String name, FluidType.Properties typeProperties,
                                  Supplier<IClientFluidTypeExtensions> clientExtensions, BlockBehaviour.Properties blockProperties,
                                  Item.Properties itemProperties,
                                  DeferredRegister<Fluid>  FLUID_REGISTRY,
                                  DeferredRegister<FluidType> FLUID_TYPE_REGISTRY,
                                  DeferredRegister<Block> BLOCK_REGISTRY,
                                  DeferredRegister<Item> ITEM_REGISTRY) {
        this(name, typeProperties, clientExtensions, null, blockProperties, itemProperties,
                FLUID_REGISTRY, FLUID_TYPE_REGISTRY, BLOCK_REGISTRY, ITEM_REGISTRY);
    }

    public static IClientFluidTypeExtensions createExtension(ClientFluidTypeExtensions extensions) {
        return new IClientFluidTypeExtensions() {
            private static final ResourceLocation UNDERWATER_LOCATION = ResourceLocation.parse("textures/misc/underwater.png");
            private static final ResourceLocation WATER_STILL = ResourceLocation.parse("block/water_still");
            private static final ResourceLocation WATER_FLOW = ResourceLocation.parse("block/water_flow");
            private static final ResourceLocation WATER_OVERLAY = ResourceLocation.parse("block/water_overlay");

            @Override
            public ResourceLocation getFlowingTexture() {
                return extensions.flowing != null ? extensions.flowing : WATER_FLOW;
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture() {
                return extensions.overlay != null ? extensions.overlay : WATER_OVERLAY;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return extensions.renderOverlay != null ? extensions.renderOverlay : UNDERWATER_LOCATION;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return extensions.still != null ? extensions.still : WATER_STILL;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                if (extensions.tintFunction != null) {
                    Integer c = extensions.tintFunction.apply(state, getter, pos);
                    if (c != null) return c;
                }
                return this.getTintColor();
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return this.getTintColor();
            }

            @Override
            public int getTintColor() {
                return extensions.tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return extensions.fogColor == null
                        ? IClientFluidTypeExtensions.super.modifyFogColor(camera, partialTick, level, renderDistance,
                        darkenWorldAmount, fluidFogColor)
                        : extensions.fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        };
    }

    /**
     * Exposes the NeoForge BaseFlowingFluid.Properties for this fluid pair (source/flowing).
     * Mods integrating with Growthcraft can use this to tweak runtime attributes such as
     * levelDecreasePerBlock, slopeFindDistance, tickRate, or explosionResistance if needed.
     *
     * Note: Prefer configuring through AdditionalProperties at registration time when possible.
     */
    public BaseFlowingFluid.Properties getProperties() {
        return this.properties;
    }

    public static class AdditionalProperties {
        private int levelDecreasePerBlock = 1;
        private float explosionResistance = 1;
        private int slopeFindDistance = 4;
        private int tickRate = 5;
        // Optional factory allowing callers to provide a custom LiquidBlock implementation for the source fluid
        private java.util.function.BiFunction<BaseFlowingFluid.Source, BlockBehaviour.Properties, LiquidBlock> blockFactory;

        public AdditionalProperties explosionResistance(float resistance) {
            this.explosionResistance = resistance;
            return this;
        }

        public AdditionalProperties levelDecreasePerBlock(int decrease) {
            this.levelDecreasePerBlock = decrease;
            return this;
        }

        public AdditionalProperties slopeFindDistance(int distance) {
            this.slopeFindDistance = distance;
            return this;
        }

        public AdditionalProperties tickRate(int rate) {
            this.tickRate = rate;
            return this;
        }

        public AdditionalProperties customBlock(java.util.function.BiFunction<BaseFlowingFluid.Source, BlockBehaviour.Properties, LiquidBlock> factory) {
            this.blockFactory = factory;
            return this;
        }
    }

}
