package growthcraft.cellar.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Arrays;
import java.util.List;

final class EmiRecipeUtil {
    private EmiRecipeUtil() {
    }

    static EmiIngredient ingredient(Ingredient ingredient, int count) {
        List<EmiStack> stacks = Arrays.stream(ingredient.getItems())
                .map(stack -> EmiStack.of(stack.copyWithCount(count)))
                .toList();
        return EmiIngredient.of(stacks);
    }

    static EmiStack item(ItemStack stack) {
        return EmiStack.of(stack);
    }

    static EmiStack fluid(ResourceLocation fluidId, int amount) {
        Fluid fluid = BuiltInRegistries.FLUID.get(fluidId);
        return fluid == Fluids.EMPTY || amount <= 0 ? EmiStack.EMPTY : EmiStack.of(fluid, amount);
    }
}
