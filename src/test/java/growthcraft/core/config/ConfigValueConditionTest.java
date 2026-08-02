package growthcraft.core.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigValueConditionTest {
    private static final Path RESOLVER = Path.of("src/main/java/growthcraft/core/config/ConfigValueConditionResolver.java");
    private static final Path CONDITION = Path.of("src/main/java/growthcraft/core/config/ConfigValueCondition.java");
    private static final Path ADJUNCT_BASIC_TAG = Path.of("src/main/resources/data/growthcraft_cellar/tags/item/adjunct_grains_basic.json");
    private static final Path ADJUNCT_EXTENDED_TAG = Path.of("src/main/resources/data/growthcraft_cellar/tags/item/adjunct_grains_extended.json");
    private static final Path ADJUNCT_EXTENDED_MINUS_WHEAT_TAG = Path.of("src/main/resources/data/growthcraft_cellar/tags/item/adjunct_grains_extended_minus_wheat.json");

    @Test
    void resolverIncludesCurrentRegisteredModuleSpecsAndAliases() throws IOException {
        String source = Files.readString(RESOLVER);

        assertTrue(source.contains("\"core\", GrowthcraftConfig.SPEC"));
        assertTrue(source.contains("\"growthcraft\", GrowthcraftConfig.SPEC"));
        assertTrue(source.contains("\"cellar\", GrowthcraftCellarConfig.SPEC"));
        assertTrue(source.contains("\"growthcraft_cellar\", GrowthcraftCellarConfig.SPEC"));
        assertTrue(source.contains("\"milk\", GrowthcraftMilkConfig.SPEC"));
        assertTrue(source.contains("\"growthcraft_milk\", GrowthcraftMilkConfig.SPEC"));
    }

    @Test
    void registeredConfigsExposeRepresentativeBooleanConditionPaths() throws IOException {
        assertTrue(Files.readString(Path.of("src/main/java/growthcraft/core/config/GrowthcraftConfig.java"))
                .contains("\"saltOreGenEnabled\""));
        assertTrue(Files.readString(Path.of("src/main/java/growthcraft/cellar/config/GrowthcraftCellarConfig.java"))
                .contains("\"allow_additional_adjunct_grains\""));
        assertTrue(Files.readString(Path.of("src/main/java/growthcraft/milk/config/GrowthcraftMilkConfig.java"))
                .contains("\"module_enabled\""));
    }

    @Test
    void conditionFailsClosedForUnknownAndNonBooleanConfigValues() throws IOException {
        String resolverSource = Files.readString(RESOLVER);
        String conditionSource = Files.readString(CONDITION);

        assertTrue(resolverSource.contains("unknown config module"));
        assertTrue(resolverSource.contains("invalid config value"));
        assertTrue(resolverSource.contains("instanceof ModConfigSpec.BooleanValue"));
        assertTrue(resolverSource.contains("is not boolean"));
        assertTrue(conditionSource.contains("configValue.isEmpty()"));
        assertTrue(conditionSource.contains("return false;"));
    }

    @Test
    void optionalAdjunctTagsDoNotBlockRiceAdjunctRecipes() throws IOException {
        String basicTag = Files.readString(ADJUNCT_BASIC_TAG);
        String extendedTag = Files.readString(ADJUNCT_EXTENDED_TAG);
        String extendedMinusWheatTag = Files.readString(ADJUNCT_EXTENDED_MINUS_WHEAT_TAG);

        assertTrue(basicTag.contains("\"id\": \"#c:grains/oats\""));
        assertTrue(basicTag.contains("\"required\": false"));
        assertTrue(extendedTag.contains("\"id\": \"#c:crops/corn\""));
        assertTrue(extendedTag.contains("\"required\": false"));
        assertTrue(extendedMinusWheatTag.contains("\"id\": \"#c:grains/oats\""));
        assertTrue(extendedMinusWheatTag.contains("\"id\": \"#c:crops/corn\""));
        assertTrue(extendedMinusWheatTag.contains("\"required\": false"));
        assertTrue(extendedTag.contains("#c:crops/rice"));
        assertTrue(extendedMinusWheatTag.contains("#c:crops/rice"));
    }
}
