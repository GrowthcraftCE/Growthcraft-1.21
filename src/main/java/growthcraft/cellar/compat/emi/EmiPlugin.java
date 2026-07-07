package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.BrewKettleRecipe;
import growthcraft.cellar.recipe.CultureJarRecipe;
import growthcraft.cellar.recipe.FermentationBarrelRecipe;
import growthcraft.cellar.recipe.FruitPressRecipe;
import growthcraft.cellar.recipe.RoasterRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {
    private static final ResourceLocation DUMMY_SPRITE_LOCATION = ResourceLocation.fromNamespaceAndPath("emi", "textures/gui/widgets.png");
    private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);

    private static final EmiStack PRESS_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.FRUIT_PRESS.get());
    private static final EmiStack BARREL_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get());
    private static final EmiStack KETTLE_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.BREW_KETTLE.get());
    private static final EmiStack JAR_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.CULTURE_JAR.get());
    private static final EmiStack ROASTER_WORKSTATION = EmiStack.of(GrowthcraftCellarBlocks.ROASTER.get());

    public static final EmiRecipeCategory PRESS = category("press", PRESS_WORKSTATION);
    public static final EmiRecipeCategory BARREL = category("barrel", BARREL_WORKSTATION);
    public static final EmiRecipeCategory KETTLE = category("kettle", KETTLE_WORKSTATION);
    public static final EmiRecipeCategory JAR = category("jar2", JAR_WORKSTATION);
    public static final EmiRecipeCategory ROASTER = category("roaster", ROASTER_WORKSTATION);

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(PRESS);
        registry.addWorkstation(PRESS, PRESS_WORKSTATION);
        for (RecipeHolder<FruitPressRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get())) {
            registry.addRecipe(new EmiPressRecipe(holder));
        }

        registry.addCategory(BARREL);
        registry.addWorkstation(BARREL, BARREL_WORKSTATION);
        for (RecipeHolder<FermentationBarrelRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get())) {
            registry.addRecipe(new EmiBarrelRecipe(holder));
        }

        registry.addCategory(KETTLE);
        registry.addWorkstation(KETTLE, KETTLE_WORKSTATION);
        for (RecipeHolder<BrewKettleRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get())) {
            registry.addRecipe(new EmiKettleRecipe(holder));
        }

        registry.addCategory(ROASTER);
        registry.addWorkstation(ROASTER, ROASTER_WORKSTATION);
        for (RecipeHolder<RoasterRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.ROASTER_TYPE.get())) {
            registry.addRecipe(new EmiRoasterRecipe(holder));
        }

        registry.addCategory(JAR);
        registry.addWorkstation(JAR, JAR_WORKSTATION);
        for (RecipeHolder<CultureJarRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftCellarRecipes.CULTURE_JAR_TYPE.get())) {
            registry.addRecipe(new EmiCultureJarRecipe(holder));
        }
    }

    private static EmiRecipeCategory category(String path, EmiStack icon) {
        return new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Reference.MODID, path), icon, DUMMY_SPRITE);
    }
}
