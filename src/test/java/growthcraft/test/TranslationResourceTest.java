package growthcraft.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TranslationResourceTest {
    private static final List<Path> RESOURCE_ROOTS = List.of(
            Path.of("src/main/resources"),
            Path.of("src/generated/resources")
    );
    private static final Path JAVA_SOURCE_ROOT = Path.of("src/main/java");
    private static final Pattern BLOCKSTATE_PATH = Pattern.compile("^assets[/\\\\]([^/\\\\]+)[/\\\\]blockstates[/\\\\](.+)\\.json$");
    private static final Pattern LANG_PATH = Pattern.compile("^assets[/\\\\]([^/\\\\]+)[/\\\\]lang[/\\\\]en_us\\.json$");
    private static final Pattern TAG_PATH = Pattern.compile("^data[/\\\\]([^/\\\\]+)[/\\\\]tags[/\\\\](block|item)[/\\\\](.+)\\.json$");
    private static final Pattern JSON_KEY = Pattern.compile("\"((?:\\\\.|[^\"\\\\])+)\"\\s*:");
    private static final Pattern TRANSLATABLE_LITERAL = Pattern.compile("\\btranslatable\\(\\s*\"([^\"]+)\"");
    private static final Pattern REFERENCE_CONSTANT = Pattern.compile("public\\s+static\\s+final\\s+String\\s+([A-Z0-9_]+)\\s*=\\s*\"([^\"]+)\"");
    private static final Pattern ITEM_REGISTER = Pattern.compile("\\bITEMS\\.register\\(\\s*([^,]+?)\\s*,");
    private static final Pattern ONE_ARG_REGISTER = Pattern.compile("\\bregister\\(\\s*([^,)]+?)\\s*\\)");
    private static final Pattern TWO_ARG_REGISTER = Pattern.compile("\\bregister(?:BlockItem|BowlFood)?\\(\\s*([^,]+?)\\s*,");
    private static final Pattern BLOCK_ITEM_REGISTER = Pattern.compile("\\bblockItem\\(\\s*([^,]+?)\\s*,");
    private static final Pattern CHEESE_REGISTER = Pattern.compile("\\bregisterCheese\\(\\s*([^,]+?)\\s*,");
    private static final Pattern AGED_CHEESE_REGISTER = Pattern.compile("\\bregisterAgedCheese\\(\\s*([^,]+?)\\s*,");
    private static final Pattern WAXED_CHEESE_REGISTER = Pattern.compile("\\bregisterWaxedCheese\\(\\s*([^,]+?)\\s*,");
    private static final Pattern CHEESE_CURDS_REGISTER = Pattern.compile("\\bregisterCheeseCurds\\(\\s*([^,]+?)\\s*,");
    private static final Pattern DRAINED_CHEESE_CURDS_REGISTER = Pattern.compile("\\bregisterDrainedCheeseCurds\\(\\s*([^,)]+?)\\s*\\)");
    private static final Pattern CHEESE_SLICE_REGISTER = Pattern.compile("\\bregisterCheeseSlice\\(\\s*([^,)]+?)\\s*\\)");
    private static final Pattern FLUID_REGISTER = Pattern.compile("\\bregister(?:Fluid)?\\(\\s*([^,]+?)\\s*,");
    private static final Pattern WAX_FLUID_REGISTER = Pattern.compile("\\bregisterWax\\(\\s*([^,]+?)\\s*,");
    private static final Set<String> GROWTHCRAFT_NAMESPACES = Set.of(
            "growthcraft",
            "growthcraft_apiary",
            "growthcraft_apples",
            "growthcraft_bamboo",
            "growthcraft_cellar",
            "growthcraft_milk",
            "growthcraft_rice"
    );

    @Test
    void visibleBlocksHaveEnglishTranslations() throws IOException {
        Set<String> missingKeys = new TreeSet<>();
        Set<String> translations = readEnglishTranslationKeys();

        for (String id : scanResourceIds(BLOCKSTATE_PATH)) {
            if (!hasBlockTranslation(id, translations)) {
                missingKeys.add(toTranslationKey("block", id));
            }
        }

        assertNoMissingTranslations("Visible block resources missing English translations", missingKeys);
    }

    @Test
    void visibleItemsHaveEnglishTranslations() throws IOException {
        Set<String> missingKeys = new TreeSet<>();
        Set<String> translations = readEnglishTranslationKeys();

        for (String id : scanRegisteredItemIds()) {
            String itemKey = toTranslationKey("item", id);
            String blockKey = toTranslationKey("block", id);
            if (!translations.contains(itemKey) && !translations.contains(blockKey)) {
                missingKeys.add(itemKey + " or " + blockKey);
            }
        }

        assertNoMissingTranslations("Visible item resources missing English translations", missingKeys);
    }

    @Test
    void javaTranslatableLiteralsHaveEnglishTranslations() throws IOException {
        Set<String> missingKeys = new TreeSet<>();
        Set<String> translations = readEnglishTranslationKeys();

        for (Path path : javaFiles()) {
            String source = Files.readString(path);
            Matcher matcher = TRANSLATABLE_LITERAL.matcher(source);
            while (matcher.find()) {
                String key = matcher.group(1);
                if (isGrowthcraftTranslationKey(key) && !translations.contains(key)) {
                    missingKeys.add(key + " in " + path);
                }
            }
        }

        assertNoMissingTranslations("Java Component.translatable literals missing English translations", missingKeys);
    }

    @Test
    void growthcraftTagsHaveEnglishTranslations() throws IOException {
        Set<String> missingKeys = new TreeSet<>();
        Set<String> translations = readEnglishTranslationKeys();

        for (Path root : RESOURCE_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                for (Path path : files.filter(Files::isRegularFile).toList()) {
                    Matcher matcher = TAG_PATH.matcher(root.relativize(path).toString());
                    if (!matcher.matches()) {
                        continue;
                    }

                    String namespace = matcher.group(1);
                    if (!GROWTHCRAFT_NAMESPACES.contains(namespace)) {
                        continue;
                    }

                    String key = "tag." + matcher.group(2) + "." + namespace + "." + matcher.group(3).replace('\\', '.').replace('/', '.');
                    if (!translations.contains(key)) {
                        missingKeys.add(key);
                    }
                }
            }
        }

        assertNoMissingTranslations("Growthcraft tags missing English translations", missingKeys);
    }

    private static Set<String> readEnglishTranslationKeys() throws IOException {
        Set<String> keys = new TreeSet<>();
        for (Path root : RESOURCE_ROOTS) {
            if (!Files.exists(root)) {
                continue;
            }

            try (Stream<Path> files = Files.walk(root)) {
                for (Path path : files.filter(Files::isRegularFile).toList()) {
                    Matcher langMatcher = LANG_PATH.matcher(root.relativize(path).toString());
                    if (!langMatcher.matches()) {
                        continue;
                    }

                    Matcher keyMatcher = JSON_KEY.matcher(Files.readString(path));
                    while (keyMatcher.find()) {
                        keys.add(keyMatcher.group(1));
                    }
                }
            }
        }
        return keys;
    }

    private static List<Path> javaFiles() throws IOException {
        try (Stream<Path> files = Files.walk(JAVA_SOURCE_ROOT)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    private static Set<String> scanResourceIds(Pattern pathPattern) throws IOException {
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
                        .map(TranslationResourceTest::toResourceId)
                        .filter(TranslationResourceTest::isGrowthcraftResourceId)
                        .collect(Collectors.toCollection(TreeSet::new)));
            }
        }
        return ids;
    }

    private static Set<String> scanRegisteredItemIds() throws IOException {
        Map<String, Map<String, String>> constantsByNamespace = readReferenceConstants();
        Set<String> ids = new TreeSet<>();

        for (Path path : javaFiles()) {
            String fileName = path.getFileName().toString();
            if (!fileName.endsWith("Items.java") && !fileName.endsWith("Fluids.java")) {
                continue;
            }

            String namespace = namespaceFor(path);
            if (namespace == null) {
                continue;
            }

            String source = Files.readString(path);
            if (fileName.endsWith("Items.java")) {
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, ITEM_REGISTER, "");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, ONE_ARG_REGISTER, "");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, TWO_ARG_REGISTER, "");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, BLOCK_ITEM_REGISTER, "");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, CHEESE_REGISTER, "_cheese");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, AGED_CHEESE_REGISTER, "_cheese_aged");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, WAXED_CHEESE_REGISTER, "_cheese_waxed");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, CHEESE_CURDS_REGISTER, "_cheese_curds");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, DRAINED_CHEESE_CURDS_REGISTER, "_cheese_curds_drained");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, CHEESE_SLICE_REGISTER, "_cheese_slice");
            } else {
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, FLUID_REGISTER, "_fluid_bucket");
                collectRegisteredItems(ids, namespace, constantsByNamespace, source, WAX_FLUID_REGISTER, "_bucket");
            }
        }

        return ids;
    }

    private static Map<String, Map<String, String>> readReferenceConstants() throws IOException {
        Map<String, Map<String, String>> constantsByNamespace = new HashMap<>();
        try (Stream<Path> files = Files.walk(JAVA_SOURCE_ROOT)) {
            for (Path path : files
                    .filter(Files::isRegularFile)
                    .filter(file -> file.endsWith("Reference.java"))
                    .toList()) {
                String namespace = namespaceFor(path);
                if (namespace == null) {
                    continue;
                }

                Map<String, String> constants = constantsByNamespace.computeIfAbsent(namespace, ignored -> new HashMap<>());
                Matcher matcher = REFERENCE_CONSTANT.matcher(Files.readString(path));
                while (matcher.find()) {
                    constants.put(matcher.group(1), matcher.group(2));
                }
            }
        }
        return constantsByNamespace;
    }

    private static void collectRegisteredItems(
            Set<String> ids,
            String namespace,
            Map<String, Map<String, String>> constantsByNamespace,
            String source,
            Pattern pattern,
            String suffix
    ) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String name = resolveNameExpression(matcher.group(1), constantsByNamespace.get(namespace));
            if (name != null && !name.isBlank()) {
                ids.add(namespace + ":" + name + suffix);
            }
        }
    }

    private static String resolveNameExpression(String expression, Map<String, String> constants) {
        if (constants == null) {
            return null;
        }

        StringBuilder resolved = new StringBuilder();
        for (String rawPart : expression.split("\\+")) {
            String part = rawPart.trim();
            if (part.startsWith("\"") && part.endsWith("\"")) {
                resolved.append(part, 1, part.length() - 1);
                continue;
            }

            int lastDot = part.lastIndexOf('.');
            String constantName = lastDot >= 0 ? part.substring(lastDot + 1) : part;
            String constant = constants.get(constantName);
            if (constant == null) {
                return null;
            }
            resolved.append(constant);
        }
        return resolved.toString();
    }

    private static boolean hasBlockTranslation(String resourceId, Set<String> translations) {
        if (translations.contains(toTranslationKey("block", resourceId))) {
            return true;
        }

        String[] split = resourceId.split(":", 2);
        if (split.length != 2) {
            return false;
        }

        String namespace = split[0];
        String path = split[1].replace('/', '.');
        return translations.contains("fluid_type." + namespace + "." + path)
                || translations.contains("fluid_type." + namespace + "." + path + "_fluid")
                || (path.endsWith("_fluid")
                && translations.contains("fluid_type." + namespace + "." + path.substring(0, path.length() - "_fluid".length())));
    }

    private static String toResourceId(Matcher matcher) {
        return matcher.group(1) + ":" + matcher.group(2).replace('\\', '/');
    }

    private static String toTranslationKey(String prefix, String resourceId) {
        String[] split = resourceId.split(":", 2);
        return prefix + "." + split[0] + "." + split[1].replace('/', '.');
    }

    private static boolean isGrowthcraftResourceId(String id) {
        int separator = id.indexOf(':');
        return separator > 0 && GROWTHCRAFT_NAMESPACES.contains(id.substring(0, separator));
    }

    private static boolean isGrowthcraftTranslationKey(String key) {
        return GROWTHCRAFT_NAMESPACES.stream().anyMatch(namespace -> key.contains(namespace));
    }

    private static String namespaceFor(Path path) {
        String normalized = path.toString().replace('\\', '/');
        if (normalized.contains("/growthcraft/core/")) {
            return "growthcraft";
        }
        if (normalized.contains("/growthcraft/apiary/")) {
            return "growthcraft_apiary";
        }
        if (normalized.contains("/growthcraft/apples/")) {
            return "growthcraft_apples";
        }
        if (normalized.contains("/growthcraft/bamboo/")) {
            return "growthcraft_bamboo";
        }
        if (normalized.contains("/growthcraft/cellar/")) {
            return "growthcraft_cellar";
        }
        if (normalized.contains("/growthcraft/milk/")) {
            return "growthcraft_milk";
        }
        if (normalized.contains("/growthcraft/rice/")) {
            return "growthcraft_rice";
        }
        return null;
    }

    private static void assertNoMissingTranslations(String message, Set<String> missingKeys) {
        assertTrue(
                missingKeys.isEmpty(),
                () -> message + ":" + System.lineSeparator()
                        + String.join(System.lineSeparator(), missingKeys)
        );
    }
}
