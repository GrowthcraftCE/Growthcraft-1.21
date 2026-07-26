package growthcraft.cellar.recipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FermentationBarrelRecipeNameTest {
    private static final Path RECIPE_SOURCE =
            Path.of("src/main/java/growthcraft/cellar/recipe/FermentationBarrelRecipe.java");

    @Test
    void bottleUsesFluidItemNameWithoutCreatingCustomName() throws IOException {
        String source = Files.readString(RECIPE_SOURCE);
        int methodStart = source.indexOf("private static ItemStack createBottleStack");
        int nextMethod = source.indexOf("private static Optional<MobEffectInstance>", methodStart);
        String createBottleStack = source.substring(methodStart, nextMethod);

        assertTrue(createBottleStack.contains("DataComponents.ITEM_NAME"));
        assertFalse(createBottleStack.contains("DataComponents.CUSTOM_NAME"));
        assertTrue(createBottleStack.contains("DataComponents.POTION_CONTENTS"));
    }
}
