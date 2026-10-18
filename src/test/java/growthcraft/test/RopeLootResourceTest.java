package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RopeLootResourceTest {
    private static final List<Path> ROOTS = List.of(
            Path.of("src/main/resources/data"),
            Path.of("src/generated/resources/data"));

    @Test
    void obsoleteRopeLootIsNotPackaged() throws IOException {
        for (Path root : ROOTS) {
            assertFalse(Files.exists(root.resolve("growthcraft/loot_table/blocks/rope_linen.json")),
                    "The removed rope block must not retain a loot table in " + root);
            try (var files = Files.walk(root)) {
                for (Path path : files.filter(Files::isRegularFile)
                        .filter(p -> p.toString().contains("loot_table"))
                        .filter(p -> p.toString().endsWith(".json")).toList()) {
                    assertFalse(Files.readString(path).contains("\"growthcraft:rope_linen\""),
                            "Loot table references the removed rope item: " + path);
                }
            }
        }
    }

    @Test
    void currentRopeRetainsOneRegisteredRopeDrop() throws IOException {
        for (Path root : ROOTS) {
            Path path = root.resolve("growthcraft/loot_table/blocks/rope_linen2.json");
            String json = Files.readString(path);
            var names = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"")
                    .matcher(json).results().map(match -> match.group(1)).toList();
            assertEquals(List.of("growthcraft:rope_linen2"), names, path.toString());
            assertTrue(Pattern.compile("\"rolls\"\\s*:\\s*1(?:\\.0)?\\s*[,}]")
                    .matcher(json).find(), path.toString());
        }
    }
}
