package growthcraft.cellar.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.cellar.init.GrowthcraftCellarRecipes;
import growthcraft.cellar.recipe.input.CultureJarInput;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.material.Fluid;

/**
 * Culture Jar recipe: item + fluid => output item, with processing time and optional heat requirement (default true).
 */
public class CultureJarRecipe implements Recipe<CultureJarInput> {
    public record FluidRequirement(ResourceLocation fluidId, int amount) {}

    private final Ingredient ingredient;
    private final FluidRequirement fluid;
    private final ItemStack result;
    private final int time;
    private final boolean requiresHeatSource;

    public CultureJarRecipe(Ingredient ingredient, FluidRequirement fluid, ItemStack result, int time, boolean requiresHeatSource) {
        this.ingredient = ingredient;
        this.fluid = fluid;
        this.result = result;
        this.time = time <= 0 ? 300 : time;
        this.requiresHeatSource = requiresHeatSource;
    }

    public Ingredient getIngredient() { return ingredient; }
    public FluidRequirement getFluid() { return fluid; }
    public ItemStack getResult() { return result.copy(); }
    public int getTime() { return time; }
    public boolean requiresHeatSource() { return requiresHeatSource; }

    @Override
    public boolean matches(CultureJarInput input, Level level) {
        // Only check the item here; fluid and heat are enforced in the block entity
        return ingredient.test(input.getItem());
    }

    @Override
    public ItemStack assemble(CultureJarInput input, HolderLookup.Provider registries) { return result.copy(); }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) { return result.copy(); }

    @Override
    public RecipeSerializer<?> getSerializer() { return GrowthcraftCellarRecipes.CULTURE_JAR_SERIALIZER.get(); }

    @Override
    public RecipeType<?> getType() { return GrowthcraftCellarRecipes.CULTURE_JAR_TYPE.get(); }

    // Codecs/Serializers
    public static class Serializer implements RecipeSerializer<CultureJarRecipe> {
        private static final Codec<FluidRequirement> FLUID_REQ_CODEC = RecordCodecBuilder.create(b -> b.group(
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidRequirement::fluidId),
                ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(FluidRequirement::amount)
        ).apply(b, FluidRequirement::new));

        public static final MapCodec<CultureJarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(CultureJarRecipe::getIngredient),
                FLUID_REQ_CODEC.fieldOf("fluid").forGetter(CultureJarRecipe::getFluid),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(CultureJarRecipe::getResult),
                Codec.INT.optionalFieldOf("time", 300).forGetter(CultureJarRecipe::getTime),
                Codec.BOOL.optionalFieldOf("requires_heat_source", Boolean.TRUE).forGetter(CultureJarRecipe::requiresHeatSource)
        ).apply(instance, (ing, fluidReq, result, time, heat) -> new CultureJarRecipe(ing, fluidReq, result, time, heat)));

        public static final StreamCodec<RegistryFriendlyByteBuf, CultureJarRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public CultureJarRecipe decode(RegistryFriendlyByteBuf buf) {
                Ingredient ing = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ResourceLocation fluidId = buf.readResourceLocation();
                int amount = buf.readVarInt();
                ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                int time = buf.readVarInt();
                boolean heat = buf.readBoolean();
                return new CultureJarRecipe(ing, new FluidRequirement(fluidId, amount), result, time, heat);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, CultureJarRecipe recipe) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredient);
                buf.writeResourceLocation(recipe.fluid.fluidId());
                buf.writeVarInt(recipe.fluid.amount());
                ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                buf.writeVarInt(recipe.time);
                buf.writeBoolean(recipe.requiresHeatSource);
            }
        };

        @Override
        public MapCodec<CultureJarRecipe> codec() { return CODEC; }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CultureJarRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
