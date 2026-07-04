package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentBaselineTest {
    private static final List<Path> RESOURCE_ROOTS = List.of(
            Path.of("src/main/resources"),
            Path.of("src/generated/resources")
    );
    private static final Pattern BLOCKSTATE_PATH = Pattern.compile("^assets[/\\\\]([^/\\\\]+)[/\\\\]blockstates[/\\\\](.+)\\.json$");
    private static final Pattern ITEM_MODEL_PATH = Pattern.compile("^assets[/\\\\]([^/\\\\]+)[/\\\\]models[/\\\\]item[/\\\\](.+)\\.json$");
    private static final Pattern RECIPE_PATH = Pattern.compile("^data[/\\\\]([^/\\\\]+)[/\\\\]recipes?[/\\\\](.+)\\.json$");

    @Test
    void baselineBlocksStillExist() throws IOException {
        assertBaselinePresent("content-baseline/blocks.txt", scanIds(BLOCKSTATE_PATH));
    }

    @Test
    void baselineItemsStillExist() throws IOException {
        assertBaselinePresent("content-baseline/items.txt", scanIds(ITEM_MODEL_PATH));
    }

    @Test
    void baselineRecipesStillExist() throws IOException {
        assertBaselinePresent("content-baseline/recipes.txt", scanIds(RECIPE_PATH));
    }

    private static void assertBaselinePresent(String baselineResource, Set<String> actualIds) throws IOException {
        Set<String> expectedIds = readBaseline(baselineResource);
        Set<String> missingIds = new TreeSet<>(expectedIds);
        missingIds.removeAll(actualIds);

        assertTrue(
                missingIds.isEmpty(),
                () -> "Missing baseline content IDs from " + baselineResource + ":" + System.lineSeparator()
                        + String.join(System.lineSeparator(), missingIds)
        );
    }

    private static Set<String> readBaseline(String resourceName) throws IOException {
        try (var stream = ContentBaselineTest.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new IOException("Missing test baseline resource: " + resourceName);
            }

            return new String(stream.readAllBytes(), StandardCharsets.UTF_8)
                    .lines()
                    .map(String::trim)
                    .map(line -> line.replace("\uFEFF", ""))
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toCollection(TreeSet::new));
        }
    }

    private static Set<String> scanIds(Pattern pathPattern) throws IOException {
        Set<String> ids = new TreeSet<>();
        for (Path root : RESOURCE_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                ids.addAll(files
                        .filter(Files::isRegularFile)
                        .map(root::relativize)
                        .map(Path::toString)
                        .map(pathPattern::matcher)
                        .filter(Matcher::find)
                        .map(ContentBaselineTest::toResourceId)
                        .collect(Collectors.toCollection(TreeSet::new)));
            }
        }
        return ids;
    }

    private static String toResourceId(Matcher matcher) {
        return matcher.group(1) + ":" + matcher.group(2).replace('\\', '/');
    }
}
