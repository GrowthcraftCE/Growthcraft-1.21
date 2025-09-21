package growthcraft.cellar.init;

import growthcraft.cellar.block.CultureJarBlock;
import growthcraft.cellar.block.entity.CultureJarBlockEntity;
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

    private GrowthcraftCellarBlockEntities() {}
}
