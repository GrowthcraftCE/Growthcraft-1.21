package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.entity.BrewKettleBlockEntity;
import growthcraft.cellar.block.entity.CorkCoasterBlockEntity;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
import growthcraft.cellar.block.entity.FermentationBarrelBlockEntity;
import growthcraft.cellar.block.entity.FruitPressBlockEntity;
import growthcraft.cellar.block.entity.RoasterBlockEntity;
import growthcraft.cellar.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftCellarBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CultureJarBlockEntity>> CULTURE_JAR = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.CULTURE_JAR,
            () -> BlockEntityType.Builder.of(CultureJarBlockEntity::new, GrowthcraftCellarBlocks.CULTURE_JAR.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BrewKettleBlockEntity>> BREW_KETTLE = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.BREW_KETTLE,
            () -> BlockEntityType.Builder.of(BrewKettleBlockEntity::new, GrowthcraftCellarBlocks.BREW_KETTLE.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FermentationBarrelBlockEntity>> FERMENTATION_BARREL = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK,
            () -> BlockEntityType.Builder.of(FermentationBarrelBlockEntity::new, GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FruitPressBlockEntity>> FRUIT_PRESS = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.FRUIT_PRESS,
            () -> BlockEntityType.Builder.of(FruitPressBlockEntity::new, GrowthcraftCellarBlocks.FRUIT_PRESS.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RoasterBlockEntity>> ROASTER = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Block.ROASTER,
            () -> BlockEntityType.Builder.of(RoasterBlockEntity::new, GrowthcraftCellarBlocks.ROASTER.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CorkCoasterBlockEntity>> CORK_COASTER = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.Item.CORK_COASTER,
            () -> BlockEntityType.Builder.of(CorkCoasterBlockEntity::new, GrowthcraftCellarBlocks.CORK_COASTER.get()).build(null)
    );

    private GrowthcraftCellarBlockEntities() {}
}
