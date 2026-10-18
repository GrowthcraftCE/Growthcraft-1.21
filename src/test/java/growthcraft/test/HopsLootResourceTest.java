package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HopsLootResourceTest {
    @Test
    void hopsProduceRequiresMaturity() throws IOException {
        Path path = Path.of("src/generated/resources/data/growthcraft_cellar/loot_table/blocks/hops_vine.json");
        String json = Files.readString(path);
        assertTrue(json.contains("\"condition\": \"minecraft:block_state_property\""));
        assertTrue(json.contains("\"block\": \"growthcraft_cellar:hops_vine\""));
        assertTrue(Pattern.compile("\"age\"\\s*:\\s*\"7\"").matcher(json).find());
        var items = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"")
                .matcher(json).results().map(match -> match.group(1)).toList();
        assertEquals(List.of("growthcraft_cellar:hops"), items);
    }
}
