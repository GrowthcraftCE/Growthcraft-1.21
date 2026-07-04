package growthcraft.milk.init;

import growthcraft.milk.block.entity.CheesePressBlockEntity;
import growthcraft.milk.block.entity.ChurnBlockEntity;
import growthcraft.milk.block.entity.PancheonBlockEntity;
import growthcraft.milk.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Reference.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CheesePressBlockEntity>> CHEESE_PRESS = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.CHEESE_PRESS,
            () -> BlockEntityType.Builder.of(CheesePressBlockEntity::new, GrowthcraftMilkBlocks.CHEESE_PRESS.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ChurnBlockEntity>> CHURN = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.CHURN,
            () -> BlockEntityType.Builder.of(ChurnBlockEntity::new, GrowthcraftMilkBlocks.CHURN.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PancheonBlockEntity>> PANCHEON = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.PANCHEON,
            () -> BlockEntityType.Builder.of(PancheonBlockEntity::new, GrowthcraftMilkBlocks.PANCHEON.get()).build(null)
    );

    private GrowthcraftMilkBlockEntities() {
    }
}
