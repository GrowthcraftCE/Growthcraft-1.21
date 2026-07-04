package growthcraft.milk.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.input.MixingVatInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MixingVatRecipe implements Recipe<MixingVatInput> {
    public enum ResultType {
        FLUID,
        ITEM;

        static ResultType parse(String name) {
            return ResultType.valueOf(name.toUpperCase(Locale.ROOT));
        }
    }

    public record FluidAmount(ResourceLocation fluidId, int amount) {}

    public record IngredientStack(Ingredient ingredient, int count) {
        boolean matches(ItemStack stack) {
            return stack.getCount() >= count && ingredient.test(stack);
        }
    }

    private final int processingTime;
    private final boolean requiresHeat;
    private final ResultType resultType;
    private final FluidAmount inputFluid;
    private final Optional<FluidAmount> reagentFluid;
    private final List<IngredientStack> ingredients;
    private final Optional<FluidAmount> resultFluid;
    private final Optional<FluidAmount> resultFluidWaste;
    private final ItemStack resultItem;
    private final ItemStack activationTool;
    private final ItemStack resultActivationTool;

    public MixingVatRecipe(int processingTime, boolean requiresHeat, String resultType, FluidAmount inputFluid,
                           Optional<FluidAmount> reagentFluid, List<IngredientStack> ingredients,
                           Optional<FluidAmount> resultFluid, Optional<FluidAmount> resultFluidWaste,
                           ItemStack resultItem, ItemStack activationTool, ItemStack resultActivationTool) {
        this(processingTime, requiresHeat, ResultType.parse(resultType), inputFluid, reagentFluid, ingredients,
                resultFluid, resultFluidWaste, resultItem, activationTool, resultActivationTool);
    }

    public MixingVatRecipe(int processingTime, boolean requiresHeat, ResultType resultType, FluidAmount inputFluid,
                           Optional<FluidAmount> reagentFluid, List<IngredientStack> ingredients,
                           Optional<FluidAmount> resultFluid, Optional<FluidAmount> resultFluidWaste,
                           ItemStack resultItem, ItemStack activationTool, ItemStack resultActivationTool) {
        this.processingTime = Math.max(1, processingTime);
        this.requiresHeat = requiresHeat;
        this.resultType = resultType;
        this.inputFluid = inputFluid;
        this.reagentFluid = reagentFluid;
        this.ingredients = List.copyOf(ingredients);
        this.resultFluid = resultFluid;
        this.resultFluidWaste = resultFluidWaste;
        this.resultItem = resultItem.copy();
        this.activationTool = activationTool.copy();
        this.resultActivationTool = resultActivationTool.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public boolean requiresHeat() {
        return requiresHeat;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String getResultTypeName() {
        return resultType.name().toLowerCase(Locale.ROOT);
    }

    public FluidAmount getInputFluid() {
        return inputFluid;
    }

    public Optional<FluidAmount> getReagentFluid() {
        return reagentFluid;
    }

    public List<IngredientStack> getIngredientStacks() {
        return ingredients;
    }

    public Optional<FluidAmount> getResultFluid() {
        return resultFluid;
    }

    public Optional<FluidAmount> getResultFluidWaste() {
        return resultFluidWaste;
    }

    public ItemStack getResultItemStack() {
        return resultItem.copy();
    }

    public ItemStack getActivationTool() {
        return activationTool.copy();
    }

    public ItemStack getResultActivationTool() {
        return resultActivationTool.copy();
    }

    public FluidStack getResultFluidStack() {
        return toFluidStack(resultFluid);
    }

    public FluidStack getWasteFluidStack() {
        return toFluidStack(resultFluidWaste);
    }

    @Override
    public boolean matches(MixingVatInput input, Level level) {
        if (requiresHeat != input.heated()) {
            return false;
        }
        if (!matchesFluid(input.inputFluid(), inputFluid)) {
            return false;
        }
        if (reagentFluid.isPresent() && !matchesFluid(input.reagentFluid(), reagentFluid.get())) {
            return false;
        }
        if (reagentFluid.isEmpty() && !input.reagentFluid().isEmpty()) {
            return false;
        }
        return matchesIngredients(input);
    }

    private boolean matchesIngredients(MixingVatInput input) {
        boolean[] used = new boolean[input.size()];
        for (IngredientStack required : ingredients) {
            boolean matched = false;
            for (int i = 0; i < input.size(); i++) {
                if (!used[i] && required.matches(input.getItem(i))) {
                    used[i] = true;
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return false;
            }
        }
        for (int i = 0; i < input.size(); i++) {
            if (!used[i] && !input.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchesFluid(FluidStack stack, FluidAmount required) {
        if (stack.isEmpty() || stack.getAmount() < required.amount()) {
            return false;
        }
        var fluid = BuiltInRegistries.FLUID.get(required.fluidId());
        return fluid != Fluids.EMPTY && fluid.getFluidType() == stack.getFluid().getFluidType();
    }

    private static FluidStack toFluidStack(Optional<FluidAmount> amount) {
        if (amount.isEmpty()) {
            return FluidStack.EMPTY;
        }
        FluidAmount fluidAmount = amount.get();
        var fluid = BuiltInRegistries.FLUID.get(fluidAmount.fluidId());
        if (fluid == Fluids.EMPTY || fluidAmount.amount() <= 0) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, fluidAmount.amount());
    }

    @Override
    public ItemStack assemble(MixingVatInput input, HolderLookup.Provider registries) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return resultItem.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrowthcraftMilkRecipes.MIXING_VAT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftMilkRecipes.MIXING_VAT_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<MixingVatRecipe> {
        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        private static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("item").orElse(ResourceLocation.withDefaultNamespace("air")).forGetter(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(BuiltInRegistries.ITEM.get(id), count)));

        private static final Codec<IngredientStack> INGREDIENT_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(IngredientStack::ingredient),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(IngredientStack::count)
        ).apply(instance, IngredientStack::new));

        public static final MapCodec<MixingVatRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 1200).forGetter(MixingVatRecipe::getProcessingTime),
                Codec.BOOL.optionalFieldOf("requires_heat", true).forGetter(MixingVatRecipe::requiresHeat),
                Codec.STRING.fieldOf("result_type").forGetter(MixingVatRecipe::getResultTypeName),
                FLUID_AMOUNT_CODEC.fieldOf("input_fluid").forGetter(MixingVatRecipe::getInputFluid),
                FLUID_AMOUNT_CODEC.optionalFieldOf("reagent_fluid").forGetter(MixingVatRecipe::getReagentFluid),
                INGREDIENT_STACK_CODEC.listOf(0, 3).optionalFieldOf("ingredients", List.of()).forGetter(MixingVatRecipe::getIngredientStacks),
                FLUID_AMOUNT_CODEC.optionalFieldOf("result_fluid").forGetter(MixingVatRecipe::getResultFluid),
                FLUID_AMOUNT_CODEC.optionalFieldOf("result_fluid_waste").forGetter(MixingVatRecipe::getResultFluidWaste),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("result_item", ItemStack.EMPTY).forGetter(MixingVatRecipe::getResultItemStack),
                LEGACY_ITEM_STACK_CODEC.fieldOf("activation_tool").forGetter(MixingVatRecipe::getActivationTool),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("result_activation_tool", ItemStack.EMPTY).forGetter(MixingVatRecipe::getResultActivationTool)
        ).apply(instance, MixingVatRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MixingVatRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public MixingVatRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                boolean requiresHeat = buf.readBoolean();
                ResultType resultType = buf.readEnum(ResultType.class);
                FluidAmount inputFluid = decodeFluidAmount(buf);
                Optional<FluidAmount> reagentFluid = decodeOptionalFluidAmount(buf);
                int ingredientCount = buf.readVarInt();
                NonNullList<IngredientStack> ingredients = NonNullList.create();
                for (int i = 0; i < ingredientCount; i++) {
                    ingredients.add(new IngredientStack(Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readVarInt()));
                }
                Optional<FluidAmount> resultFluid = decodeOptionalFluidAmount(buf);
                Optional<FluidAmount> wasteFluid = decodeOptionalFluidAmount(buf);
                ItemStack resultItem = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                ItemStack activationTool = ItemStack.STREAM_CODEC.decode(buf);
                ItemStack resultActivationTool = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                return new MixingVatRecipe(processingTime, requiresHeat, resultType, inputFluid, reagentFluid, ingredients,
                        resultFluid, wasteFluid, resultItem, activationTool, resultActivationTool);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, MixingVatRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                buf.writeBoolean(recipe.requiresHeat);
                buf.writeEnum(recipe.resultType);
                encodeFluidAmount(buf, recipe.inputFluid);
                encodeOptionalFluidAmount(buf, recipe.reagentFluid);
                buf.writeVarInt(recipe.ingredients.size());
                for (IngredientStack ingredient : recipe.ingredients) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient.ingredient());
                    buf.writeVarInt(ingredient.count());
                }
                encodeOptionalFluidAmount(buf, recipe.resultFluid);
                encodeOptionalFluidAmount(buf, recipe.resultFluidWaste);
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultItem);
                ItemStack.STREAM_CODEC.encode(buf, recipe.activationTool);
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.resultActivationTool);
            }

            private FluidAmount decodeFluidAmount(RegistryFriendlyByteBuf buf) {
                return new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
            }

            private Optional<FluidAmount> decodeOptionalFluidAmount(RegistryFriendlyByteBuf buf) {
                return buf.readBoolean() ? Optional.of(decodeFluidAmount(buf)) : Optional.empty();
            }

            private void encodeFluidAmount(RegistryFriendlyByteBuf buf, FluidAmount amount) {
                buf.writeResourceLocation(amount.fluidId());
                buf.writeVarInt(amount.amount());
            }

            private void encodeOptionalFluidAmount(RegistryFriendlyByteBuf buf, Optional<FluidAmount> amount) {
                buf.writeBoolean(amount.isPresent());
                amount.ifPresent(fluidAmount -> encodeFluidAmount(buf, fluidAmount));
            }
        };

        @Override
        public MapCodec<MixingVatRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MixingVatRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
