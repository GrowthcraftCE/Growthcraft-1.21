package growthcraft.core.data;

import growthcraft.core.config.Reference;
import growthcraft.core.data.loot.GrowthcraftBlockLoot;
import growthcraft.core.data.recipe.GrowthcraftRecipeProvider;
import growthcraft.core.data.worldgen.GrowthcraftWorldgenProvider;
import growthcraft.core.data.tags.GrowthcraftBlockTags;
import growthcraft.core.data.tags.GrowthcraftItemTags;
import growthcraft.apiary.data.loot.ApiaryBlockLoot;
import growthcraft.apples.data.loot.ApplesBlockLoot;
import growthcraft.bamboo.data.loot.BambooBlockLoot;
import growthcraft.cellar.data.loot.CellarBlockLoot;
import growthcraft.milk.data.loot.MilkBlockLoot;
import growthcraft.rice.data.loot.RiceBlockLoot;
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
        LootTableProvider.SubProviderEntry apiaryBlocks = new LootTableProvider.SubProviderEntry(ApiaryBlockLoot::new, LootContextParamSets.BLOCK);
        LootTableProvider.SubProviderEntry applesBlocks = new LootTableProvider.SubProviderEntry(ApplesBlockLoot::new, LootContextParamSets.BLOCK);
        LootTableProvider.SubProviderEntry bambooBlocks = new LootTableProvider.SubProviderEntry(BambooBlockLoot::new, LootContextParamSets.BLOCK);
        LootTableProvider.SubProviderEntry cellarBlocks = new LootTableProvider.SubProviderEntry(CellarBlockLoot::new, LootContextParamSets.BLOCK);
        LootTableProvider.SubProviderEntry milkBlocks = new LootTableProvider.SubProviderEntry(MilkBlockLoot::new, LootContextParamSets.BLOCK);
        LootTableProvider.SubProviderEntry riceBlocks = new LootTableProvider.SubProviderEntry(RiceBlockLoot::new, LootContextParamSets.BLOCK);

        LootTableProvider lootTables = new LootTableProvider(output, Set.of(), List.of(blocks, apiaryBlocks, applesBlocks, bambooBlocks, cellarBlocks, milkBlocks, riceBlocks), lookupProvider);
        generator.addProvider(event.includeServer(), lootTables);

        // Block tags (mineable, needs_* tool level, etc.)
        generator.addProvider(event.includeServer(), new GrowthcraftBlockTags(output, lookupProvider, existingFileHelper));

        // Item tags (crowbars in common wrench tag)
        generator.addProvider(event.includeServer(), new GrowthcraftItemTags(output, lookupProvider));

        // Recipes
        generator.addProvider(event.includeServer(), new GrowthcraftRecipeProvider(output, lookupProvider));

        // Worldgen (configured/placed features and biome modifiers)
        generator.addProvider(event.includeServer(), new GrowthcraftWorldgenProvider(output));

        // Cellar: client-side item models
        generator.addProvider(event.includeClient(), new growthcraft.cellar.data.CellarItemModels(output, existingFileHelper));

        // Milk: client-side item models
        generator.addProvider(event.includeClient(), new growthcraft.milk.data.MilkItemModels(output, existingFileHelper));

    }
}
