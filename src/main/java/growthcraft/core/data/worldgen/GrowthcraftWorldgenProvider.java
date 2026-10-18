package growthcraft.core.data.worldgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import growthcraft.core.config.Reference;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Salt ore resources; user settings are evaluated by the runtime feature and placement.
 * Generates:
 *  - data/growthcraft/worldgen/configured_feature/salt_ore.json
 *  - data/growthcraft/worldgen/placed_feature/salt_ore_placed.json
 *  - data/growthcraft/neoforge/biome_modifier/add_salt_ore_*.json (per dimension)
 */
public class GrowthcraftWorldgenProvider implements DataProvider {
    private final PackOutput packOutput;

    public GrowthcraftWorldgenProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        var writes = new java.util.ArrayList<CompletableFuture<?>>();
        try {
            // Configured Feature
            Path configuredPath = resolve("worldgen/configured_feature", Reference.UnlocalizedName.Block.SALT_ORE + ".json");
            JsonObject configured = buildConfiguredFeature();
            writes.add(DataProvider.saveStable(output, configured, configuredPath));

            // Placed Feature
            Path placedPath = resolve("worldgen/placed_feature", Reference.UnlocalizedName.Block.SALT_ORE_PLACED + ".json");
            JsonObject placed = buildPlacedFeature();
            writes.add(DataProvider.saveStable(output, placed, placedPath));

            // Keep every dimension registered; runtime switches decide whether to place ore.
            for (String dimension : java.util.List.of("overworld", "nether", "end")) {
                writes.add(DataProvider.saveStable(output, buildBiomeModifier("minecraft:is_" + dimension),
                        resolveNeoForge("add_salt_ore_" + dimension + ".json")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
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
        root.addProperty("type", "growthcraft:salt_ore");

        JsonObject config = new JsonObject();
        config.addProperty("discard_chance_on_air_exposure", 0.0);
        config.addProperty("size", 5);

        JsonArray targets = new JsonArray();

        // All targets remain available; runtime config controls placement.
        targets.add(oreTarget(tagPredicate("minecraft:stone_ore_replaceables"), state(Reference.UnlocalizedName.Block.SALT_ORE)));

        targets.add(oreTarget(tagPredicate("minecraft:deepslate_ore_replaceables"), state("salt_ore_deepslate")));
        targets.add(oreTarget(tagPredicate("minecraft:base_stone_nether"), state("salt_ore_nether")));
        JsonObject endPredicate = new JsonObject();
        endPredicate.addProperty("predicate_type", "minecraft:block_match");
        endPredicate.addProperty("block", "minecraft:end_stone");
        targets.add(oreTarget(endPredicate, state("salt_ore_end")));
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
        state.addProperty("Name", Reference.MODID + ":" + blockPath);
        return state;
    }

    private static JsonObject buildPlacedFeature() {
        JsonObject root = new JsonObject();
        root.addProperty("feature", Reference.MODID + ":" + Reference.UnlocalizedName.Block.SALT_ORE);

        JsonArray placement = new JsonArray();

        // Count, horizontal spread, height and switches are evaluated during generation.
        JsonObject runtimePlacement = new JsonObject();
        runtimePlacement.addProperty("type", "growthcraft:salt_ore");
        placement.add(runtimePlacement);

        // Biome filter
        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");
        placement.add(biome);

        root.add("placement", placement);
        return root;
    }

    private static JsonObject buildBiomeModifier(String biomeTag) {
        JsonObject root = new JsonObject();
        root.addProperty("type", "neoforge:add_features");

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
