package growthcraft.core.data.tags;

import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftItems;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftItemTags extends IntrinsicHolderTagsProvider<Item> {
    public static final TagKey<Item> C_WRENCHES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "tools/wrench"));

    public GrowthcraftItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add all crowbar variants to the common wrench tag so other mods recognize them
        this.tag(C_WRENCHES)
                .add(
                        GrowthcraftItems.CROWBAR_WHITE.get(),
                        GrowthcraftItems.CROWBAR_LIGHT_GRAY.get(),
                        GrowthcraftItems.CROWBAR_GRAY.get(),
                        GrowthcraftItems.CROWBAR_BLACK.get(),
                        GrowthcraftItems.CROWBAR_BROWN.get(),
                        GrowthcraftItems.CROWBAR_RED.get(),
                        GrowthcraftItems.CROWBAR_ORANGE.get(),
                        GrowthcraftItems.CROWBAR_YELLOW.get(),
                        GrowthcraftItems.CROWBAR_LIME.get(),
                        GrowthcraftItems.CROWBAR_GREEN.get(),
                        GrowthcraftItems.CROWBAR_CYAN.get(),
                        GrowthcraftItems.CROWBAR_LIGHT_BLUE.get(),
                        GrowthcraftItems.CROWBAR_BLUE.get(),
                        GrowthcraftItems.CROWBAR_PURPLE.get(),
                        GrowthcraftItems.CROWBAR_MAGENTA.get(),
                        GrowthcraftItems.CROWBAR_PINK.get()
                );

        this.tag(GrowthcraftMilkTags.Items.TAG_MILK_BUCKETS)
                .add(GrowthcraftMilkItems.MILK_BUCKET_IRON.get());
    }
}
