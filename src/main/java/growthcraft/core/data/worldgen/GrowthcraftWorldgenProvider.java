package growthcraft.core.data.worldgen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import growthcraft.core.config.GrowthcraftConfig;
import growthcraft.core.config.Reference;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Minimal worldgen datagen for Salt Ore using config values.
 * Generates:
 *  - data/growthcraft/worldgen/configured_feature/salt_ore.json
 *  - data/growthcraft/worldgen/placed_feature/salt_ore_placed.json
 *  - data/growthcraft/neoforge/biome_modifier/add_salt_ore_*.json (per dimension)
 */
public class GrowthcraftWorldgenProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput packOutput;

    // Defaults used during data generation if configs are not yet loaded
    private static final boolean DEFAULT_SALT_ORE_GEN_ENABLED = true;
    private static final boolean DEFAULT_SALT_ORE_GEN_DEEPSLATE_ENABLED = true;
    private static final boolean DEFAULT_SALT_ORE_GEN_NETHER_ENABLED = true;
    private static final boolean DEFAULT_SALT_ORE_GEN_END_ENABLED = true;
    private static final int DEFAULT_SALT_ORE_VEIN_SIZE = 5;
    private static final int DEFAULT_SALT_ORE_HEIGHT_MIN = -32;
    private static final int DEFAULT_SALT_ORE_HEIGHT_MAX = 64;
    private static final int DEFAULT_SALT_ORE_SPREAD_AMOUNT = 10;

    private static boolean cfgBoolOrDefault(java.util.function.Supplier<Boolean> supplier, boolean def) {
        try { return supplier.get(); } catch (IllegalStateException ex) { return def; }
    }
    private static int cfgIntOrDefault(java.util.function.Supplier<Integer> supplier, int def) {
        try { return supplier.get(); } catch (IllegalStateException ex) { return def; }
    }

    public GrowthcraftWorldgenProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        if (!cfgBoolOrDefault(GrowthcraftConfig::saltOreGenEnabled, DEFAULT_SALT_ORE_GEN_ENABLED)) {
            // Nothing to generate when disabled
            return CompletableFuture.completedFuture(null);
        }

        try {
            // Configured Feature
            Path configuredPath = resolve("worldgen/configured_feature", Reference.UnlocalizedName.Block.SALT_ORE + ".json");
            JsonObject configured = buildConfiguredFeature();
            DataProvider.saveStable(output, configured, configuredPath);

            // Placed Feature
            Path placedPath = resolve("worldgen/placed_feature", Reference.UnlocalizedName.Block.SALT_ORE_PLACED + ".json");
            JsonObject placed = buildPlacedFeature();
            DataProvider.saveStable(output, placed, placedPath);

            // Biome Modifiers (per-dimension based on flags)
            // Overworld
            if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenDeepslateEnabled, DEFAULT_SALT_ORE_GEN_DEEPSLATE_ENABLED)) {
                Path bmOverworld = resolveNeoForge("add_salt_ore_overworld.json");
                JsonObject bm = buildBiomeModifier("minecraft:is_overworld");
                DataProvider.saveStable(output, bm, bmOverworld);
            }
            // Nether
            if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenNetherEnabled, DEFAULT_SALT_ORE_GEN_NETHER_ENABLED)) {
                Path bmNether = resolveNeoForge("add_salt_ore_nether.json");
                JsonObject bm = buildBiomeModifier("minecraft:is_nether");
                DataProvider.saveStable(output, bm, bmNether);
            }
            // End
            if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenEndEnabled, DEFAULT_SALT_ORE_GEN_END_ENABLED)) {
                Path bmEnd = resolveNeoForge("add_salt_ore_end.json");
                JsonObject bm = buildBiomeModifier("minecraft:is_end");
                DataProvider.saveStable(output, bm, bmEnd);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private Path resolve(String folder, String filename) throws IOException {
        Path base = this.packOutput.getOutputFolder().resolve("data").resolve(Reference.MODID).resolve(folder);
        Files.createDirectories(base);
        return base.resolve(filename);
    }

    private Path resolveNeoForge(String filename) throws IOException {
        Path base = this.packOutput.getOutputFolder().resolve("data").resolve(Reference.MODID).resolve("neoforge").resolve("biome_modifier");
        Files.createDirectories(base);
        return base.resolve(filename);
    }

    private static JsonObject buildConfiguredFeature() {
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:ore");

        JsonObject config = new JsonObject();
        config.addProperty("discard_chance_on_air_exposure", 0.0);
        config.addProperty("size", cfgIntOrDefault(GrowthcraftConfig::saltOreVeinSize, DEFAULT_SALT_ORE_VEIN_SIZE));

        JsonArray targets = new JsonArray();

        // Overworld stone target (always include if master enabled)
        targets.add(oreTarget(tagPredicate("minecraft:stone_ore_replaceables"), state(Reference.UnlocalizedName.Block.SALT_ORE)));

        // Deepslate target
        if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenDeepslateEnabled, DEFAULT_SALT_ORE_GEN_DEEPSLATE_ENABLED)) {
            targets.add(oreTarget(tagPredicate("minecraft:deepslate_ore_replaceables"), state("salt_ore_deepslate")));
        }
        // Nether target
        if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenNetherEnabled, DEFAULT_SALT_ORE_GEN_NETHER_ENABLED)) {
            targets.add(oreTarget(tagPredicate("minecraft:nether_ore_replaceables"), state("salt_ore_nether")));
        }
        // End target uses end_stone block match
        if (cfgBoolOrDefault(GrowthcraftConfig::saltOreGenEndEnabled, DEFAULT_SALT_ORE_GEN_END_ENABLED)) {
            JsonObject endPredicate = new JsonObject();
            endPredicate.addProperty("predicate_type", "minecraft:block_match");
            endPredicate.addProperty("block", "minecraft:end_stone");
            targets.add(oreTarget(endPredicate, state("salt_ore_end")));
        }
        config.add("targets", targets);
        root.add("config", config);
        return root;
    }

    private static JsonObject oreTarget(JsonObject targetPredicate, JsonObject state) {
        JsonObject t = new JsonObject();
        t.add("target", targetPredicate);
        t.add("state", state);
        return t;
    }

    private static JsonObject tagPredicate(String tag) {
        JsonObject predicate = new JsonObject();
        predicate.addProperty("predicate_type", "minecraft:tag_match");
        predicate.addProperty("tag", tag);
        return predicate;
    }

    private static JsonObject state(String blockPath) {
        JsonObject state = new JsonObject();
        JsonObject name = new JsonObject();
        state.addProperty("Name", Reference.MODID + ":" + blockPath);
        return state;
    }

    private static JsonObject buildPlacedFeature() {
        JsonObject root = new JsonObject();
        root.addProperty("feature", Reference.MODID + ":" + Reference.UnlocalizedName.Block.SALT_ORE);

        JsonArray placement = new JsonArray();

        // Count per chunk
        JsonObject count = new JsonObject();
        count.addProperty("type", "minecraft:count");
        count.addProperty("count", cfgIntOrDefault(GrowthcraftConfig::saltOreSpreadAmount, DEFAULT_SALT_ORE_SPREAD_AMOUNT));
        placement.add(count);

        // In square
        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");
        placement.add(inSquare);

        // Height range (uniform)
        JsonObject heightRange = new JsonObject();
        heightRange.addProperty("type", "minecraft:height_range");
        JsonObject height = new JsonObject();
        height.addProperty("type", "minecraft:uniform");
        JsonObject min = new JsonObject();
        min.addProperty("absolute", cfgIntOrDefault(GrowthcraftConfig::saltOreHeightMin, DEFAULT_SALT_ORE_HEIGHT_MIN));
        JsonObject max = new JsonObject();
        max.addProperty("absolute", cfgIntOrDefault(GrowthcraftConfig::saltOreHeightMax, DEFAULT_SALT_ORE_HEIGHT_MAX));
        height.add("min_inclusive", min);
        height.add("max_inclusive", max);
        heightRange.add("height", height);
        placement.add(heightRange);

        // Biome filter
        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");
        placement.add(biome);

        root.add("placement", placement);
        return root;
    }

    private static JsonObject buildBiomeModifier(String biomeTag) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "neoforge:add_features_by_tag");

        // Biome tag to apply to (single tag reference string with leading '#')
        root.addProperty("biomes", "#" + biomeTag);

        // Feature step and list
        root.addProperty("step", "underground_ores");
        JsonArray features = new JsonArray();
        features.add(Reference.MODID + ":" + Reference.UnlocalizedName.Block.SALT_ORE_PLACED);
        root.add("features", features);
        return root;
    }

    @Override
    public String getName() {
        return Reference.NAME + " Worldgen";
    }
}
