package growthcraft.milk.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.input.CheesePressInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CheesePressRecipe implements Recipe<CheesePressInput> {
    private final int processingTime;
    private final ItemStack inputItem;
    private final ItemStack resultItem;
    private final ItemStack sliceItem;

    public CheesePressRecipe(int processingTime, ItemStack inputItem, ItemStack resultItem, ItemStack sliceItem) {
        this.processingTime = Math.max(1, processingTime);
        this.inputItem = inputItem.copy();
        this.resultItem = resultItem.copy();
        this.sliceItem = sliceItem.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public ItemStack getInputItem() {
        return inputItem.copy();
    }

    public ItemStack getResultItem() {
        return resultItem.copy();
    }

    public ItemStack getSliceItem() {
        return sliceItem.copy();
    }

    @Override
    public boolean matches(CheesePressInput input, Level level) {
        ItemStack stack = input.getItem(0);
        return ItemStack.isSameItemSameComponents(stack, inputItem) && stack.getCount() >= inputItem.getCount();
    }

    @Override
    public ItemStack assemble(CheesePressInput input, HolderLookup.Provider registries) {
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
        return GrowthcraftMilkRecipes.CHEESE_PRESS_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftMilkRecipes.CHEESE_PRESS_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CheesePressRecipe> {
        private static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("item").orElse(ResourceLocation.withDefaultNamespace("air")).forGetter(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(BuiltInRegistries.ITEM.get(id), count)));

        public static final MapCodec<CheesePressRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("processing_time", 6000).forGetter(CheesePressRecipe::getProcessingTime),
                LEGACY_ITEM_STACK_CODEC.fieldOf("ingredient").forGetter(CheesePressRecipe::getInputItem),
                LEGACY_ITEM_STACK_CODEC.fieldOf("result_item").forGetter(CheesePressRecipe::getResultItem),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("slice", ItemStack.EMPTY).forGetter(CheesePressRecipe::getSliceItem)
        ).apply(instance, CheesePressRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CheesePressRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public CheesePressRecipe decode(RegistryFriendlyByteBuf buf) {
                int processingTime = buf.readVarInt();
                ItemStack input = ItemStack.STREAM_CODEC.decode(buf);
                ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                ItemStack slice = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                return new CheesePressRecipe(processingTime, input, result, slice);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, CheesePressRecipe recipe) {
                buf.writeVarInt(recipe.processingTime);
                ItemStack.STREAM_CODEC.encode(buf, recipe.inputItem);
                ItemStack.STREAM_CODEC.encode(buf, recipe.resultItem);
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.sliceItem);
            }
        };

        @Override
        public MapCodec<CheesePressRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CheesePressRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
