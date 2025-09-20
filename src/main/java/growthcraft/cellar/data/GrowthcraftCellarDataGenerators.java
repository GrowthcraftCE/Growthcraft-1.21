package growthcraft.cellar.data;

import growthcraft.cellar.config.Reference;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * Datagen bootstrap for Growthcraft Cellar.
 * Adds client-side providers (item models) for resources that were previously hand-authored.
 */
@EventBusSubscriber(modid = Reference.MODID)
public class GrowthcraftCellarDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existing = event.getExistingFileHelper();

        // Client-side providers
        generator.addProvider(event.includeClient(), new CellarItemModels(output, existing));

        // Note: Server-side providers (loot tables, tags, recipes, etc.) can be added later as needed.
    }
}
