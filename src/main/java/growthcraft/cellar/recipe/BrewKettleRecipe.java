package growthcraft.cellar.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.BrewKettleInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class BrewKettleRecipe implements Recipe<BrewKettleInput> {
    public record FluidAmount(ResourceLocation fluidId, int amount) {}
    public record CountedIngredient(Ingredient ingredient, int count) {
        public boolean test(ItemStack stack) {
            return stack.getCount() >= count && ingredient.test(stack);
        }
    }

    private final boolean requiresHeat;
    private final boolean requiresLid;
    private final int processingTime;
    private final int byProductChance;
    private final FluidAmount inputFluid;
    private final CountedIngredient inputItem;
    private final FluidAmount outputFluid;
    private final ItemStack byProduct;

    public BrewKettleRecipe(boolean requiresHeat, boolean requiresLid, int processingTime, int byProductChance,
                            FluidAmount inputFluid, CountedIngredient inputItem, FluidAmount outputFluid, ItemStack byProduct) {
        this.requiresHeat = requiresHeat;
        this.requiresLid = requiresLid;
        this.processingTime = processingTime <= 0 ? 600 : processingTime;
        this.byProductChance = Math.max(0, Math.min(100, byProductChance));
        this.inputFluid = inputFluid;
        this.inputItem = inputItem;
        this.outputFluid = outputFluid;
        this.byProduct = byProduct.copy();
    }

    public boolean requiresHeat() {
        return requiresHeat;
    }

    public boolean requiresLid() {
        return requiresLid;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getByProductChance() {
        return byProductChance;
    }

    public FluidAmount getInputFluid() {
        return inputFluid;
    }

    public CountedIngredient getInputItem() {
        return inputItem;
    }

    public FluidAmount getOutputFluid() {
        return outputFluid;
    }

    public ItemStack getByProduct() {
        return byProduct.copy();
    }

    @Override
    public boolean matches(BrewKettleInput input, Level level) {
        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(BrewKettleInput input, HolderLookup.Provider registries) {
        return byProduct.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return byProduct.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrowthcraftCellarRecipes.BREW_KETTLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftCellarRecipes.BREW_KETTLE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<BrewKettleRecipe> {
        private static final Codec<Boolean> LEGACY_BOOL_CODEC = Codec.withAlternative(Codec.BOOL, Codec.STRING.xmap(Boolean::parseBoolean, Object::toString));

        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        private static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("item").orElse(ResourceLocation.withDefaultNamespace("air")).forGetter(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(BuiltInRegistries.ITEM.get(id), count)));

        private static final Codec<CountedIngredient> COUNTED_INGREDIENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("item").forGetter(ingredient -> java.util.Optional.empty()),
                ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(ingredient -> java.util.Optional.empty()),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(CountedIngredient::count)
        ).apply(instance, (itemId, tagId, count) -> {
            Ingredient ingredient = itemId
                    .map(id -> Ingredient.of(BuiltInRegistries.ITEM.get(id)))
                    .orElseGet(() -> tagId
                            .map(id -> Ingredient.of(TagKey.create(Registries.ITEM, id)))
                            .orElse(Ingredient.EMPTY));
            return new CountedIngredient(ingredient, count);
        }));

        public static final MapCodec<BrewKettleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                LEGACY_BOOL_CODEC.optionalFieldOf("requires_heat", Boolean.TRUE).forGetter(BrewKettleRecipe::requiresHeat),
                LEGACY_BOOL_CODEC.optionalFieldOf("requires_lid", Boolean.FALSE).forGetter(BrewKettleRecipe::requiresLid),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 600).forGetter(BrewKettleRecipe::getProcessingTime),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("by_product_chance", 0).forGetter(BrewKettleRecipe::getByProductChance),
                FLUID_AMOUNT_CODEC.fieldOf("input_fluid").forGetter(BrewKettleRecipe::getInputFluid),
                COUNTED_INGREDIENT_CODEC.fieldOf("input_item").forGetter(BrewKettleRecipe::getInputItem),
                FLUID_AMOUNT_CODEC.fieldOf("output_fluid").forGetter(BrewKettleRecipe::getOutputFluid),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("by_product", ItemStack.EMPTY).forGetter(BrewKettleRecipe::getByProduct)
        ).apply(instance, BrewKettleRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, BrewKettleRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public BrewKettleRecipe decode(RegistryFriendlyByteBuf buf) {
                boolean requiresHeat = buf.readBoolean();
                boolean requiresLid = buf.readBoolean();
                int processingTime = buf.readVarInt();
                int byProductChance = buf.readVarInt();
                FluidAmount inputFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                int ingredientCount = buf.readVarInt();
                FluidAmount outputFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                ItemStack byProduct = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                return new BrewKettleRecipe(requiresHeat, requiresLid, processingTime, byProductChance,
                        inputFluid, new CountedIngredient(ingredient, ingredientCount), outputFluid, byProduct);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, BrewKettleRecipe recipe) {
                buf.writeBoolean(recipe.requiresHeat);
                buf.writeBoolean(recipe.requiresLid);
                buf.writeVarInt(recipe.processingTime);
                buf.writeVarInt(recipe.byProductChance);
                buf.writeResourceLocation(recipe.inputFluid.fluidId());
                buf.writeVarInt(recipe.inputFluid.amount());
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem.ingredient());
                buf.writeVarInt(recipe.inputItem.count());
                buf.writeResourceLocation(recipe.outputFluid.fluidId());
                buf.writeVarInt(recipe.outputFluid.amount());
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.byProduct);
            }
        };

        @Override
        public MapCodec<BrewKettleRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BrewKettleRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
