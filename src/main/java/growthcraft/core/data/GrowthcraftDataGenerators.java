package growthcraft.core.data;

import growthcraft.core.config.Reference;
import growthcraft.core.data.loot.GrowthcraftBlockLoot;
import growthcraft.core.data.recipe.GrowthcraftRecipeProvider;
import growthcraft.core.data.worldgen.GrowthcraftWorldgenProvider;
import growthcraft.core.data.tags.GrowthcraftBlockTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Reference.MODID)
public class GrowthcraftDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        LootTableProvider.SubProviderEntry blocks = new LootTableProvider.SubProviderEntry(GrowthcraftBlockLoot::new, LootContextParamSets.BLOCK);

        LootTableProvider lootTables = new LootTableProvider(output, Set.of(), List.of(blocks), lookupProvider);
        generator.addProvider(event.includeServer(), lootTables);

        // Block tags (mineable, needs_* tool level, etc.)
        generator.addProvider(event.includeServer(), new GrowthcraftBlockTags(output, lookupProvider, existingFileHelper));

        // Recipes
        generator.addProvider(event.includeServer(), new GrowthcraftRecipeProvider(output, lookupProvider));

        // Worldgen (configured/placed features and biome modifiers)
        generator.addProvider(event.includeServer(), new GrowthcraftWorldgenProvider(output));

        // Cellar: client-side item models
        generator.addProvider(event.includeClient(), new growthcraft.cellar.data.CellarItemModels(output, existingFileHelper));
    }
}
