package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
import growthcraft.lib.client.ClientFluidTypeExtensions;
import growthcraft.lib.fluid.FluidRegistryContainer;
import growthcraft.lib.utils.ColorUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Consumer;

public final class GrowthcraftApiaryFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Reference.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Reference.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Reference.MODID);
    public static final DeferredRegister<Item> ITEMS = GrowthcraftApiaryItems.ITEMS;

    public static final FluidRegistryContainer HONEY = register(Reference.UnlocalizedName.HONEY, Reference.FluidColor.HONEY);
    public static final FluidRegistryContainer HONEY_MEAD = register(Reference.UnlocalizedName.HONEY_MEAD, Reference.FluidColor.HONEY_MEAD);
    public static final FluidRegistryContainer HONEY_MEAD_MUST = register(Reference.UnlocalizedName.HONEY_MEAD_MUST, Reference.FluidColor.HONEY_MEAD_MUST);
    public static final WaxFluid WAX_BLACK = registerWax(Reference.UnlocalizedName.WAX_BLACK, Reference.FluidColor.WAX_BLACK);
    public static final WaxFluid WAX_BLUE = registerWax(Reference.UnlocalizedName.WAX_BLUE, Reference.FluidColor.WAX_BLUE);
    public static final WaxFluid WAX_BROWN = registerWax(Reference.UnlocalizedName.WAX_BROWN, Reference.FluidColor.WAX_BROWN);
    public static final WaxFluid WAX_CYAN = registerWax(Reference.UnlocalizedName.WAX_CYAN, Reference.FluidColor.WAX_CYAN);
    public static final WaxFluid WAX_GRAY = registerWax(Reference.UnlocalizedName.WAX_GRAY, Reference.FluidColor.WAX_GRAY);
    public static final WaxFluid WAX_GREEN = registerWax(Reference.UnlocalizedName.WAX_GREEN, Reference.FluidColor.WAX_GREEN);
    public static final WaxFluid WAX_LIGHT_BLUE = registerWax(Reference.UnlocalizedName.WAX_LIGHT_BLUE, Reference.FluidColor.WAX_LIGHT_BLUE);
    public static final WaxFluid WAX_LIGHT_GRAY = registerWax(Reference.UnlocalizedName.WAX_LIGHT_GRAY, Reference.FluidColor.WAX_LIGHT_GRAY);
    public static final WaxFluid WAX_LIME = registerWax(Reference.UnlocalizedName.WAX_LIME, Reference.FluidColor.WAX_LIME);
    public static final WaxFluid WAX_MAGENTA = registerWax(Reference.UnlocalizedName.WAX_MAGENTA, Reference.FluidColor.WAX_MAGENTA);
    public static final WaxFluid WAX_ORANGE = registerWax(Reference.UnlocalizedName.WAX_ORANGE, Reference.FluidColor.WAX_ORANGE);
    public static final WaxFluid WAX_PINK = registerWax(Reference.UnlocalizedName.WAX_PINK, Reference.FluidColor.WAX_PINK);
    public static final WaxFluid WAX_PURPLE = registerWax(Reference.UnlocalizedName.WAX_PURPLE, Reference.FluidColor.WAX_PURPLE);
    public static final WaxFluid WAX_RED = registerWax(Reference.UnlocalizedName.WAX_RED, Reference.FluidColor.WAX_RED);
    public static final WaxFluid WAX_WHITE = registerWax(Reference.UnlocalizedName.WAX_WHITE, Reference.FluidColor.WAX_WHITE);
    public static final WaxFluid WAX_YELLOW = registerWax(Reference.UnlocalizedName.WAX_YELLOW, Reference.FluidColor.WAX_YELLOW);

    public static final FluidRegistryContainer[] ALL = new FluidRegistryContainer[] {
            HONEY,
            HONEY_MEAD,
            HONEY_MEAD_MUST
    };

    public static final WaxFluid[] WAXES = new WaxFluid[] {
            WAX_BLACK,
            WAX_BLUE,
            WAX_BROWN,
            WAX_CYAN,
            WAX_GRAY,
            WAX_GREEN,
            WAX_LIGHT_BLUE,
            WAX_LIGHT_GRAY,
            WAX_LIME,
            WAX_MAGENTA,
            WAX_ORANGE,
            WAX_PINK,
            WAX_PURPLE,
            WAX_RED,
            WAX_WHITE,
            WAX_YELLOW
    };

    private GrowthcraftApiaryFluids() {
    }

    private static FluidRegistryContainer register(String name, ColorUtils.GrowthcraftColor color) {
        FluidType.Properties typeProperties = FluidType.Properties.create()
                .canSwim(true)
                .canDrown(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name)
                .tint(color)
                .fogColor(
                        color.toFloatValues().get("red"),
                        color.toFloatValues().get("green"),
                        color.toFloatValues().get("blue")
                );
        if (Reference.UnlocalizedName.HONEY_MEAD_MUST.equals(name)) {
            client.useTexturesFrom(Reference.MODID, Reference.UnlocalizedName.HONEY_MEAD);
        }

        BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY);

        FluidRegistryContainer.AdditionalProperties additionalProperties =
                new FluidRegistryContainer.AdditionalProperties()
                        .tickRate(5)
                        .slopeFindDistance(4)
                        .levelDecreasePerBlock(1)
                        .explosionResistance(100.0F);

        return new FluidRegistryContainer(
                name,
                typeProperties,
                () -> FluidRegistryContainer.createExtension(client),
                additionalProperties,
                blockProperties,
                new Item.Properties().stacksTo(1),
                FLUIDS,
                FLUID_TYPES,
                BLOCKS,
                ITEMS
        );
    }

    private static WaxFluid registerWax(String name, ColorUtils.GrowthcraftColor color) {
        FluidType.Properties typeProperties = FluidType.Properties.create()
                .canSwim(true)
                .canDrown(true)
                .canPushEntity(true)
                .supportsBoating(true)
                .lightLevel(0);

        ClientFluidTypeExtensions client = new ClientFluidTypeExtensions(Reference.MODID, name);
        client.still = ResourceLocation.fromNamespaceAndPath("growthcraft", "block/fluid/fluid_still");
        client.flowing = ResourceLocation.fromNamespaceAndPath("growthcraft", "block/fluid/fluid_flowing");
        client.overlay = ResourceLocation.fromNamespaceAndPath("growthcraft", "block/fluid/fluid_overlay");
        client.renderOverlay = ResourceLocation.fromNamespaceAndPath("growthcraft", "textures/block/fluid/fluid_overlay.png");
        client.tint(color)
                .fogColor(
                        color.toFloatValues().get("red"),
                        color.toFloatValues().get("green"),
                        color.toFloatValues().get("blue")
                );

        DeferredHolder<FluidType, FluidType> type = FLUID_TYPES.register(name + "_fluid", () -> new FluidType(
                typeProperties
                        .sound(SoundActions.BUCKET_FILL, net.minecraft.sounds.SoundEvents.BUCKET_FILL)
                        .sound(SoundActions.BUCKET_EMPTY, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY)
        ) {
            @Override
            public void initializeClient(Consumer<net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions> consumer) {
                consumer.accept(FluidRegistryContainer.createExtension(client));
            }
        });

        WaxFluid fluid = new WaxFluid(type);
        fluid.source = FLUIDS.register(name + "_fluid_source", () -> new BaseFlowingFluid.Source(fluid.properties));
        fluid.flowing = FLUIDS.register(name + "_fluid_flowing", () -> new BaseFlowingFluid.Flowing(fluid.properties));
        fluid.properties = new BaseFlowingFluid.Properties(type, fluid.source, fluid.flowing)
                .explosionResistance(100.0F)
                .levelDecreasePerBlock(1)
                .slopeFindDistance(4)
                .tickRate(5);
        fluid.block = BLOCKS.register(name, () -> new LiquidBlock(fluid.source.get(), waxBlockProperties()));
        fluid.properties.block(() -> fluid.block.get());
        fluid.bucket = ITEMS.register(name + "_bucket", () -> new BucketItem(fluid.source.get(), new Item.Properties().stacksTo(1)));
        fluid.properties.bucket(() -> fluid.bucket.get());
        return fluid;
    }

    private static BlockBehaviour.Properties waxBlockProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.WATER)
                .replaceable()
                .noCollission()
                .strength(100.0F)
                .pushReaction(PushReaction.DESTROY)
                .noLootTable()
                .liquid()
                .sound(SoundType.EMPTY);
    }

    public static final class WaxFluid {
        public final DeferredHolder<FluidType, FluidType> type;
        public DeferredHolder<Fluid, BaseFlowingFluid.Source> source;
        public DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing;
        public DeferredHolder<Block, LiquidBlock> block;
        public DeferredHolder<Item, BucketItem> bucket;
        private BaseFlowingFluid.Properties properties;

        private WaxFluid(DeferredHolder<FluidType, FluidType> type) {
            this.type = type;
        }
    }
}
