package growthcraft.test;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeRuntimeCatalogTest {
    private static final List<Path> DATA_ROOTS = List.of(
            Path.of("src/main/resources/data"),
            Path.of("src/generated/resources/data")
    );
    private static final Pattern RECIPE_PATH = Pattern.compile("^([^/\\\\]+)[/\\\\]recipes?[/\\\\](.+)\\.json$");
    private static final Pattern TOP_LEVEL_TYPE_FIELD = Pattern.compile("^\\s{0,4}\"type\"\\s*:\\s*\"([^\"]+)\"");

    private static final Set<String> SUPPORTED_CUSTOM_TYPES = Set.of(
            "growthcraft_cellar:culture_jar",
            "growthcraft_milk:cheese_press_recipe",
            "growthcraft_milk:churn_recipe",
            "growthcraft_milk:pancheon_recipe"
    );

    private static final Set<String> BLOCKED_CUSTOM_TYPES = Set.of(
            "growthcraft_apiary:brew_kettle_recipe",
            "growthcraft_apiary:fermentation_barrel_recipe",
            "growthcraft_apiary:fruit_press_recipe",
            "growthcraft_apples:fermentation_barrel_recipe",
            "growthcraft_apples:fruit_press_recipe",
            "growthcraft_cellar:brew_kettle_recipe",
            "growthcraft_cellar:culture_jar_recipe",
            "growthcraft_cellar:culture_jar_starter_recipe",
            "growthcraft_cellar:fermentation_barrel_recipe",
            "growthcraft_cellar:fruit_press_recipe",
            "growthcraft_cellar:roaster_recipe",
            "growthcraft_milk:brew_kettle_recipe",
            "growthcraft_milk:culture_jar_recipe",
            "growthcraft_milk:culture_jar_starter_recipe",
            "growthcraft_milk:mixing_vat_fluid_recipe",
            "growthcraft_milk:mixing_vat_item_recipe",
            "growthcraft_rice:brew_kettle_recipe",
            "growthcraft_rice:culture_jar_recipe",
            "growthcraft_rice:culture_jar_starter_recipe",
            "growthcraft_rice:fermentation_barrel_recipe"
    );

    @Test
    void customRecipeTypesAreCataloguedAsSupportedOrBlocked() throws IOException {
        Map<String, String> unknownCustomTypes = new TreeMap<>();

        for (RecipeJson recipe : scanRecipes()) {
            if (recipe.type().startsWith("minecraft:") || !recipe.type().startsWith("growthcraft")) {
                continue;
            }

            if (!SUPPORTED_CUSTOM_TYPES.contains(recipe.type()) && !BLOCKED_CUSTOM_TYPES.contains(recipe.type())) {
                unknownCustomTypes.put(recipe.id(), recipe.type());
            }
        }

        assertTrue(
                unknownCustomTypes.isEmpty(),
                () -> "Custom recipe types need migration catalogue entries:" + System.lineSeparator()
                        + formatUnknownTypes(unknownCustomTypes)
        );
    }

    private static Set<RecipeJson> scanRecipes() throws IOException {
        Set<RecipeJson> recipes = new TreeSet<>();
        for (Path root : DATA_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                for (Path path : files.filter(Files::isRegularFile).toList()) {
                    Matcher pathMatcher = RECIPE_PATH.matcher(root.relativize(path).toString());
                    if (!pathMatcher.matches()) {
                        continue;
                    }

                    String type = readType(path);
                    recipes.add(new RecipeJson(pathMatcher.group(1) + ":" + pathMatcher.group(2).replace('\\', '/'), type));
                }
            }
        }
        return recipes;
    }

    private static String readType(Path recipePath) throws IOException {
        for (String line : Files.readAllLines(recipePath)) {
            Matcher typeMatcher = TOP_LEVEL_TYPE_FIELD.matcher(line);
            if (typeMatcher.find()) {
                return typeMatcher.group(1);
            }
        }
        throw new IOException("Recipe JSON is missing a top-level type: " + recipePath);
    }

    private static String formatUnknownTypes(Map<String, String> unknownCustomTypes) {
        return String.join(System.lineSeparator(), unknownCustomTypes.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .toList());
    }

    private record RecipeJson(String id, String type) implements Comparable<RecipeJson> {
        @Override
        public int compareTo(RecipeJson other) {
            return id.compareTo(other.id);
        }
    }
}
