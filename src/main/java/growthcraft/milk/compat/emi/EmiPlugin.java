package growthcraft.milk.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.compat.jei.CheesePreppingRecipeCategory;
import growthcraft.milk.compat.jei.CheeseWaxingRecipeCategory;
import growthcraft.milk.config.Reference;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import growthcraft.milk.init.GrowthcraftMilkItems;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.CheesePressRecipe;
import growthcraft.milk.recipe.ChurnRecipe;
import growthcraft.milk.recipe.MixingVatRecipe;
import growthcraft.milk.recipe.PancheonRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class EmiPlugin implements dev.emi.emi.api.EmiPlugin {
    private static final ResourceLocation DUMMY_SPRITE_LOCATION = ResourceLocation.fromNamespaceAndPath("emi", "textures/gui/widgets.png");
    private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);

    private static final EmiStack PANCHEON_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.PANCHEON.get());
    private static final EmiStack CHURN_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.CHURN.get());
    private static final EmiStack CHEESE_PRESS_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.CHEESE_PRESS.get());
    private static final EmiStack MIXING_VAT_WORKSTATION = EmiStack.of(GrowthcraftMilkBlocks.MIXING_VAT.get());

    public static final EmiRecipeCategory PANCHEON = category("pancheon", PANCHEON_WORKSTATION);
    public static final EmiRecipeCategory CHURN = category("churn", CHURN_WORKSTATION);
    public static final EmiRecipeCategory CHEESE_PRESS = category("cheese_press", CHEESE_PRESS_WORKSTATION);
    public static final EmiRecipeCategory MIXING_VAT = category("mixing_vat_1", MIXING_VAT_WORKSTATION);
    public static final EmiRecipeCategory CURD_DRAINING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "drying"), EmiStack.of(GrowthcraftMilkBlocks.ASIAGO_CHEESE_CURDS.get()));
    public static final EmiRecipeCategory CHEESE_AGING = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "aging"), EmiStack.of(GrowthcraftMilkBlocks.GOUDA_CHEESE.get()));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(PANCHEON);
        registry.addWorkstation(PANCHEON, PANCHEON_WORKSTATION);
        for (RecipeHolder<PancheonRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.PANCHEON_TYPE.get())) {
            registry.addRecipe(new EmiPancheonRecipe(holder));
        }

        registry.addCategory(CHURN);
        registry.addWorkstation(CHURN, CHURN_WORKSTATION);
        for (RecipeHolder<ChurnRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHURN_TYPE.get())) {
            registry.addRecipe(new EmiChurnRecipe(holder));
        }

        registry.addCategory(CHEESE_PRESS);
        registry.addWorkstation(CHEESE_PRESS, CHEESE_PRESS_WORKSTATION);
        for (RecipeHolder<CheesePressRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get())) {
            registry.addRecipe(new EmiCheesePressRecipe(holder));
        }

        registry.addCategory(MIXING_VAT);
        registry.addWorkstation(MIXING_VAT, MIXING_VAT_WORKSTATION);
        for (RecipeHolder<MixingVatRecipe> holder : registry.getRecipeManager().getAllRecipesFor(GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get())) {
            registry.addRecipe(new EmiMixingVatRecipe(holder));
        }

        registry.addCategory(CHEESE_AGING);
        for (var cheese : GrowthcraftMilkItems.getCheeseRegistry()) {
            if (cheese.aged() != null) {  // condition to skip ricotta
                if (cheese.waxed() != null) {
                    registry.addRecipe(new EmiCheeseAgingRecipe(cheese.waxed(), cheese.aged()));
                }
                else {
                    registry.addRecipe(new EmiCheeseAgingRecipe(cheese.unprocessed(), cheese.aged()));
                }
            }
        }

        registry.addCategory(CURD_DRAINING);
        for (var cheese : GrowthcraftMilkItems.getCheeseRegistry()) {
            if (cheese.drainedCurds() != null) {
                registry.addRecipe(new EmiCurdsRecipe(cheese.curds(), cheese.drainedCurds()));
            }
        }

        // waxing
        int count = 0;
        for (var cheese : GrowthcraftMilkItems.getCheeseRegistry()) {
            if (cheese.waxed() != null && cheese.waxingItem() != null) { // second part unneeded
                registry.addRecipe(EmiWorldInteractionRecipe.builder()
                        .leftInput(EmiStack.of(cheese.unprocessed()))
                        .rightInput(EmiStack.of(cheese.waxingItem()), false)
                        .output(EmiStack.of(cheese.waxed()))
                        .id(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "/wa" + (++count)))
                        .build()
                );
            }
        }

        // slicing
        count = 0;
        for (var cheese : GrowthcraftMilkItems.getCheeseRegistry()) {
            if (cheese.slice() != null) {
                if (cheese.aged() != null) {
                    registry.addRecipe(EmiWorldInteractionRecipe.builder()
                            .leftInput(EmiStack.of(cheese.aged()))
                            .rightInput(EmiIngredient.of(GrowthcraftTags.Items.CHEESE_CUTTING_TOOLS), true)
                            .output(EmiStack.of(cheese.slice()))
                            .id(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "/sli" + (++count)))
                            .build()
                    );
                }
            }
        }
    }

    private static EmiRecipeCategory category(String path, EmiStack icon) {
        return new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(Reference.MODID, path), icon, DUMMY_SPRITE);
    }
}
