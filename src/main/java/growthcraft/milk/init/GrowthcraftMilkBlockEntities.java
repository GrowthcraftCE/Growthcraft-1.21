package growthcraft.milk.init;

import growthcraft.milk.block.entity.CheesePressBlockEntity;
import growthcraft.milk.block.entity.ChurnBlockEntity;
import growthcraft.milk.block.entity.MixingVatBlockEntity;
import growthcraft.milk.block.entity.PancheonBlockEntity;
import growthcraft.milk.block.entity.ShopSignBlockEntity;
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MixingVatBlockEntity>> MIXING_VAT = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.MIXING_VAT,
            () -> BlockEntityType.Builder.of(MixingVatBlockEntity::new, GrowthcraftMilkBlocks.MIXING_VAT.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShopSignBlockEntity>> SHOP_SIGN = BLOCK_ENTITY_TYPES.register(
            Reference.UnlocalizedName.SHOP_SIGN,
            () -> BlockEntityType.Builder.of(ShopSignBlockEntity::new,
                    GrowthcraftMilkBlocks.HANGING_SIGN_1_OAK.get(),
                    GrowthcraftMilkBlocks.HANGING_SIGN_1_SPRUCE.get(),
                    GrowthcraftMilkBlocks.HANGING_SIGN_2_OAK.get(),
                    GrowthcraftMilkBlocks.HANGING_SIGN_2_SPRUCE.get()).build(null)
    );

    private GrowthcraftMilkBlockEntities() {
    }
}
