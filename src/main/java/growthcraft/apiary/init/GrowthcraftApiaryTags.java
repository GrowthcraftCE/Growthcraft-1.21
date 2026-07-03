package growthcraft.apiary.init;

import growthcraft.apiary.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class GrowthcraftApiaryTags {
    public static class Items {
        public static final TagKey<Item> HONEY_COMB = tag("honey_comb");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Reference.MODID, name));
        }
    }
}
