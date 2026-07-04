package growthcraft.cellar.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.RoasterInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RoasterRecipe implements Recipe<RoasterInput> {
    private final int roastingLevel;
    private final ItemStack inputItem;
    private final ItemStack result;

    public RoasterRecipe(int roastingLevel, ItemStack inputItem, ItemStack result) {
        this.roastingLevel = roastingLevel;
        this.inputItem = inputItem;
        this.result = result;
    }

    public int getRoastingLevel() {
        return roastingLevel;
    }

    public ItemStack getInputItem() {
        return inputItem.copy();
    }

    public ItemStack getResult() {
        return result.copy();
    }

    @Override
    public boolean matches(RoasterInput input, Level level) {
        ItemStack stack = input.getItem(0);
        return ItemStack.isSameItemSameComponents(stack, inputItem) && stack.getCount() >= inputItem.getCount();
    }

    @Override
    public ItemStack assemble(RoasterInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrowthcraftCellarRecipes.ROASTER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftCellarRecipes.ROASTER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<RoasterRecipe> {
        public static final MapCodec<RoasterRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.intRange(1, 8).fieldOf("roasting_level").forGetter(RoasterRecipe::getRoastingLevel),
                ItemStack.STRICT_CODEC.fieldOf("input_item").forGetter(RoasterRecipe::getInputItem),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RoasterRecipe::getResult)
        ).apply(instance, RoasterRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public RoasterRecipe decode(RegistryFriendlyByteBuf buf) {
                int roastingLevel = buf.readVarInt();
                ItemStack inputItem = ItemStack.STREAM_CODEC.decode(buf);
                ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                return new RoasterRecipe(roastingLevel, inputItem, result);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, RoasterRecipe recipe) {
                buf.writeVarInt(recipe.roastingLevel);
                ItemStack.STREAM_CODEC.encode(buf, recipe.inputItem);
                ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            }
        };

        @Override
        public MapCodec<RoasterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RoasterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
