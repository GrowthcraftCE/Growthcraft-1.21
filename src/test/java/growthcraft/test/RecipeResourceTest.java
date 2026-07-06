package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeResourceTest {
    private static final List<Path> DATA_ROOTS = List.of(
            Path.of("src/main/resources/data"),
            Path.of("src/generated/resources/data")
    );
    private static final Pattern TYPE_FIELD = Pattern.compile("\"type\"\\s*:\\s*\"[^\"]+\"");
    private static final Pattern FORGE_TYPE_FIELD = Pattern.compile("\"type\"\\s*:\\s*\"forge:");
    private static final Pattern RECIPE_PATH = Pattern.compile("^[^/\\\\]+[/\\\\]recipes?[/\\\\].+\\.json$");

    @Test
    void recipeJsonFilesDeclareType() throws IOException {
        List<Path> missingType = new ArrayList<>();
        for (Path root : DATA_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                missingType.addAll(files
                        .filter(Files::isRegularFile)
                        .filter(path -> RECIPE_PATH.matcher(root.relativize(path).toString()).matches())
                        .filter(path -> !declaresType(path))
                        .toList());
            }
        }
        List<Path> sortedMissingType = missingType.stream().sorted().toList();

        assertTrue(
                sortedMissingType.isEmpty(),
                () -> "Recipe JSON files missing a type field:" + System.lineSeparator()
                        + String.join(System.lineSeparator(), sortedMissingType.stream().map(Path::toString).toList())
        );
    }

    @Test
    void recipeConditionsUseNeoForgeKey() throws IOException {
        List<Path> recipesWithLegacyConditions = new ArrayList<>();
        for (Path root : DATA_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                recipesWithLegacyConditions.addAll(files
                        .filter(Files::isRegularFile)
                        .filter(path -> RECIPE_PATH.matcher(root.relativize(path).toString()).matches())
                        .filter(RecipeResourceTest::declaresLegacyConditions)
                        .toList());
            }
        }
        List<Path> sortedRecipes = recipesWithLegacyConditions.stream().sorted().toList();

        assertTrue(
                sortedRecipes.isEmpty(),
                () -> "Recipe JSON files must use neoforge:conditions instead of conditions:" + System.lineSeparator()
                        + String.join(System.lineSeparator(), sortedRecipes.stream().map(Path::toString).toList())
        );
    }

    @Test
    void recipeConditionTypesUseNeoForgeNamespace() throws IOException {
        List<Path> recipesWithForgeConditions = new ArrayList<>();
        for (Path root : DATA_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                recipesWithForgeConditions.addAll(files
                        .filter(Files::isRegularFile)
                        .filter(path -> RECIPE_PATH.matcher(root.relativize(path).toString()).matches())
                        .filter(RecipeResourceTest::declaresForgeConditionType)
                        .toList());
            }
        }
        List<Path> sortedRecipes = recipesWithForgeConditions.stream().sorted().toList();

        assertTrue(
                sortedRecipes.isEmpty(),
                () -> "Recipe JSON condition types must use the neoforge namespace:" + System.lineSeparator()
                        + String.join(System.lineSeparator(), sortedRecipes.stream().map(Path::toString).toList())
        );
    }

    private static boolean declaresType(Path path) {
        try {
            return TYPE_FIELD.matcher(Files.readString(path)).find();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read recipe JSON: " + path, e);
        }
    }

    private static boolean declaresLegacyConditions(Path path) {
        try {
            return hasTopLevelProperty(Files.readString(path), "conditions");
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read recipe JSON: " + path, e);
        }
    }

    private static boolean declaresForgeConditionType(Path path) {
        try {
            return FORGE_TYPE_FIELD.matcher(Files.readString(path)).find();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read recipe JSON: " + path, e);
        }
    }

    private static boolean hasTopLevelProperty(String json, String propertyName) {
        int depth = 0;
        boolean inString = false;
        boolean escaping = false;

        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (inString) {
                if (escaping) {
                    escaping = false;
                } else if (ch == '\\') {
                    escaping = true;
                } else if (ch == '"') {
                    inString = false;
                }
                continue;
            }

            if (ch == '"') {
                int end = findStringEnd(json, i + 1);
                if (depth == 1 && propertyName.equals(json.substring(i + 1, end)) && isPropertyName(json, end + 1)) {
                    return true;
                }
                i = end;
                continue;
            }
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
            }
        }
        return false;
    }

    private static int findStringEnd(String json, int start) {
        boolean escaping = false;
        for (int i = start; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (escaping) {
                escaping = false;
            } else if (ch == '\\') {
                escaping = true;
            } else if (ch == '"') {
                return i;
            }
        }
        throw new IllegalArgumentException("Unterminated JSON string");
    }

    private static boolean isPropertyName(String json, int start) {
        for (int i = start; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            }
            return ch == ':';
        }
        return false;
    }
}
