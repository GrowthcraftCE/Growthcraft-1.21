package growthcraft.bamboo.init;

import growthcraft.bamboo.config.Reference;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftBambooItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<BlockItem> BAMBOO_POST_VERTICAL = ITEMS.register(
            Reference.UnlocalizedName.Block.BAMBOO_POST_VERTICAL,
            () -> new BlockItem(GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get(), new Item.Properties()));

    private GrowthcraftBambooItems() {}
}
