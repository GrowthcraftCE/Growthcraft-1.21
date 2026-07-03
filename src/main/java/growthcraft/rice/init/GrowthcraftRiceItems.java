package growthcraft.rice.init;

import growthcraft.rice.config.Reference;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftRiceItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MODID);

    public static final DeferredItem<Item> YEAST_SEISHU = ITEMS.register(
            Reference.UnlocalizedName.Item.YEAST_SEISHU,
            () -> new Item(new Item.Properties())
    );

    private GrowthcraftRiceItems() {}
}
