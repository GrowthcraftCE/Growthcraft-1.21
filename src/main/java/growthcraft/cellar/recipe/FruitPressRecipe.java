package growthcraft.cellar.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.FruitPressInput;
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

public class FruitPressRecipe implements Recipe<FruitPressInput> {
    public record FluidAmount(ResourceLocation fluidId, int amount) {}
    public record CountedIngredient(Ingredient ingredient, int count) {
        public boolean test(ItemStack stack) {
            return stack.getCount() >= count && ingredient.test(stack);
        }
    }

    private final int processingTime;
    private final CountedIngredient inputItem;
    private final FluidAmount outputFluid;
    private final ItemStack byProduct;

    public FruitPressRecipe(int processingTime, CountedIngredient inputItem, FluidAmount outputFluid, ItemStack byProduct) {
        this.processingTime = processingTime <= 0 ? 600 : processingTime;
        this.inputItem = inputItem;
        this.outputFluid = outputFluid;
        this.byProduct = byProduct.copy();
    }

    public int getProcessingTime() {
        return processingTime;
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
    public boolean matches(FruitPressInput input, Level level) {
        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(FruitPressInput input, HolderLookup.Provider registries) {
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
        return GrowthcraftCellarRecipes.FRUIT_PRESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftCellarRecipes.FRUIT_PRESS_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FruitPressRecipe> {
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

        public static final MapCodec<FruitPressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 600).forGetter(FruitPressRecipe::getProcessingTime),
                COUNTED_INGREDIENT_CODEC.fieldOf("input_item").forGetter(FruitPressRecipe::getInputItem),
                FLUID_AMOUNT_CODEC.fieldOf("output_fluid").forGetter(FruitPressRecipe::getOutputFluid),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("by_product", ItemStack.EMPTY).forGetter(FruitPressRecipe::getByProduct)
        ).apply(instance, FruitPressRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FruitPressRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public FruitPressRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                int ingredientCount = buf.readVarInt();
                FluidAmount outputFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                ItemStack byProduct = ItemStack.STREAM_CODEC.decode(buf);
                return new FruitPressRecipe(processingTime, new CountedIngredient(ingredient, ingredientCount), outputFluid, byProduct);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, FruitPressRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.inputItem.ingredient());
                buf.writeVarInt(recipe.inputItem.count());
                buf.writeResourceLocation(recipe.outputFluid.fluidId());
                buf.writeVarInt(recipe.outputFluid.amount());
                ItemStack.STREAM_CODEC.encode(buf, recipe.byProduct);
            }
        };

        @Override
        public MapCodec<FruitPressRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FruitPressRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
