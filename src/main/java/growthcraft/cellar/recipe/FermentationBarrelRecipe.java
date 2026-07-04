package growthcraft.cellar.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.FermentationBarrelInput;
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

import java.util.List;
import java.util.Optional;

public class FermentationBarrelRecipe implements Recipe<FermentationBarrelInput> {
    public record FluidAmount(ResourceLocation fluidId, int amount) {}
    public record EffectSpec(ResourceLocation effectId, int duration, int amplifier) {}
    public record CountedIngredient(Ingredient ingredient, int count) {
        public boolean accepts(ItemStack stack) {
            return ingredient.test(stack);
        }

        public boolean test(ItemStack stack) {
            return stack.getCount() >= count && ingredient.test(stack);
        }
    }

    private final int processingTime;
    private final CountedIngredient ingredientItem;
    private final FluidAmount ingredientFluid;
    private final FluidAmount result;
    private final List<EffectSpec> effects;
    private final ItemStack bottle;
    private final int color;

    public FermentationBarrelRecipe(int processingTime, CountedIngredient ingredientItem, FluidAmount ingredientFluid,
                                    FluidAmount result, List<EffectSpec> effects, ItemStack bottle, int color) {
        this.processingTime = processingTime <= 0 ? 1200 : processingTime;
        this.ingredientItem = ingredientItem;
        this.ingredientFluid = ingredientFluid;
        this.result = result;
        this.effects = List.copyOf(effects);
        this.bottle = bottle.copy();
        this.color = color;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public CountedIngredient getIngredientItem() {
        return ingredientItem;
    }

    public FluidAmount getIngredientFluid() {
        return ingredientFluid;
    }

    public FluidAmount getResult() {
        return result;
    }

    public List<EffectSpec> getEffects() {
        return effects;
    }

    public ItemStack getBottle() {
        return bottle.copy();
    }

    public int getColor() {
        return color;
    }

    public int getOutputMultiplier(FermentationBarrelInput input) {
        int unit = ingredientFluid.amount();
        int amount = input.fluid().getAmount();
        if (unit <= 0 || amount < unit || amount % unit != 0) return 0;
        return amount / unit;
    }

    @Override
    public boolean matches(FermentationBarrelInput input, Level level) {
        int multiplier = getOutputMultiplier(input);
        return multiplier > 0
                && input.fluid().getFluid() == BuiltInRegistries.FLUID.get(ingredientFluid.fluidId())
                && input.getItem(0).getCount() >= ingredientItem.count() * multiplier
                && ingredientItem.accepts(input.getItem(0));
    }

    @Override
    public ItemStack assemble(FermentationBarrelInput input, HolderLookup.Provider registries) {
        return bottle.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return bottle.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrowthcraftCellarRecipes.FERMENTATION_BARREL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftCellarRecipes.FERMENTATION_BARREL_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FermentationBarrelRecipe> {
        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        private static final Codec<CountedIngredient> COUNTED_INGREDIENT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.optionalFieldOf("item").forGetter(ingredient -> Optional.empty()),
                ResourceLocation.CODEC.optionalFieldOf("tag").forGetter(ingredient -> Optional.empty()),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("count", 1).forGetter(CountedIngredient::count)
        ).apply(instance, (itemId, tagId, count) -> {
            Ingredient ingredient = itemId
                    .map(id -> Ingredient.of(BuiltInRegistries.ITEM.get(id)))
                    .orElseGet(() -> tagId
                            .map(id -> Ingredient.of(TagKey.create(Registries.ITEM, id)))
                            .orElse(Ingredient.EMPTY));
            return new CountedIngredient(ingredient, count);
        }));

        private static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("item").orElse(ResourceLocation.withDefaultNamespace("air")).forGetter(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(BuiltInRegistries.ITEM.get(id), count)));

        private static final Codec<EffectSpec> EFFECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("effect").forGetter(EffectSpec::effectId),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("duration", 200).forGetter(EffectSpec::duration),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("amplifier", 0).forGetter(EffectSpec::amplifier)
        ).apply(instance, EffectSpec::new));

        private static final Codec<Integer> COLOR_CODEC = Codec.withAlternative(
                ExtraCodecs.NON_NEGATIVE_INT,
                Codec.STRING.xmap(Integer::decode, color -> "0x" + Integer.toHexString(color).toUpperCase())
        );

        public static final MapCodec<FermentationBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 1200).forGetter(FermentationBarrelRecipe::getProcessingTime),
                COUNTED_INGREDIENT_CODEC.fieldOf("ingredient_item").forGetter(FermentationBarrelRecipe::getIngredientItem),
                FLUID_AMOUNT_CODEC.fieldOf("ingredient_fluid").forGetter(FermentationBarrelRecipe::getIngredientFluid),
                FLUID_AMOUNT_CODEC.fieldOf("result").forGetter(FermentationBarrelRecipe::getResult),
                EFFECT_CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(FermentationBarrelRecipe::getEffects),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("bottle", ItemStack.EMPTY).forGetter(FermentationBarrelRecipe::getBottle),
                COLOR_CODEC.optionalFieldOf("color", 0xFFFFFF).forGetter(FermentationBarrelRecipe::getColor)
        ).apply(instance, FermentationBarrelRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public FermentationBarrelRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                int ingredientCount = buf.readVarInt();
                FluidAmount ingredientFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                FluidAmount result = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                int effectCount = buf.readVarInt();
                var effects = new java.util.ArrayList<EffectSpec>(effectCount);
                for (int i = 0; i < effectCount; i++) {
                    effects.add(new EffectSpec(buf.readResourceLocation(), buf.readVarInt(), buf.readVarInt()));
                }
                ItemStack bottle = ItemStack.STREAM_CODEC.decode(buf);
                int color = buf.readVarInt();
                return new FermentationBarrelRecipe(processingTime, new CountedIngredient(ingredient, ingredientCount),
                        ingredientFluid, result, effects, bottle, color);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, FermentationBarrelRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredientItem.ingredient());
                buf.writeVarInt(recipe.ingredientItem.count());
                buf.writeResourceLocation(recipe.ingredientFluid.fluidId());
                buf.writeVarInt(recipe.ingredientFluid.amount());
                buf.writeResourceLocation(recipe.result.fluidId());
                buf.writeVarInt(recipe.result.amount());
                buf.writeVarInt(recipe.effects.size());
                for (EffectSpec effect : recipe.effects) {
                    buf.writeResourceLocation(effect.effectId());
                    buf.writeVarInt(effect.duration());
                    buf.writeVarInt(effect.amplifier());
                }
                ItemStack.STREAM_CODEC.encode(buf, recipe.bottle);
                buf.writeVarInt(recipe.color);
            }
        };

        @Override
        public MapCodec<FermentationBarrelRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FermentationBarrelRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
