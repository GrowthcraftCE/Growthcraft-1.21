package growthcraft.cellar;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GrowthcraftCellarClientTest {
    @Test
    void nonClientPackDiscoveryReturnsBeforeReadingConfig() throws IOException {
        // Unit tests run without the transformed Minecraft runtime. Check the
        // startup ordering contract here, including custom pack types from mods.
        String source = Files.readString(Path.of(
                "src/main/java/growthcraft/cellar/GrowthcraftCellarClient.java"));
        String handler = source.substring(source.indexOf("static void addPacks("));
        int guard = handler.indexOf("if (event.getPackType() != PackType.CLIENT_RESOURCES)");
        int earlyReturn = handler.indexOf("return;", guard);
        int configRead = handler.indexOf("GrowthcraftCellarConfig.shouldUseThinGrapeModels()");

        assertTrue(guard >= 0, "Only client resources may read the grape model config");
        assertTrue(earlyReturn > guard && earlyReturn < configRead,
                "Non-client discovery must return before accessing an unloaded config");
    }
}
