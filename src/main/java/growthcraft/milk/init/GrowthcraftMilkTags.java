package growthcraft.milk.init;

import growthcraft.milk.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * Minimal tags for the Milk module needed by early porting.
 */
public final class GrowthcraftMilkTags {
    private GrowthcraftMilkTags() {}

    public static final class Items {
        private Items() {}
        // Used by recipes later; kept here for compatibility with previous code. Not yet consumed.
        public static final TagKey<Item> TAG_MILK_BUCKETS = tag("milk_buckets");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Reference.MODID, name));
        }
    }

    public static final class EntityTypes {
        private EntityTypes() {}
        // Entities that can be milked (e.g., cows, goats with conditions, etc.)
        public static final TagKey<EntityType<?>> MILKABLE = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Reference.MODID, "milkable"));
    }
}
