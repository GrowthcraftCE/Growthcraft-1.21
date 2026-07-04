package growthcraft.milk.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import growthcraft.milk.init.GrowthcraftMilkRecipes;
import growthcraft.milk.recipe.input.ChurnInput;
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
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class ChurnRecipe implements Recipe<ChurnInput> {
    public record FluidAmount(ResourceLocation fluidId, int amount) {}

    private final int plungesNeeded;
    private final FluidAmount inputFluid;
    private final FluidAmount outputFluid;
    private final ItemStack byProduct;
    private final int byProductChance;

    public ChurnRecipe(int plungesNeeded, FluidAmount inputFluid, FluidAmount outputFluid, ItemStack byProduct, int byProductChance) {
        this.plungesNeeded = Math.max(1, plungesNeeded);
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.byProduct = byProduct.copy();
        this.byProductChance = Math.clamp(byProductChance, 0, 100);
    }

    public int getPlungesNeeded() {
        return plungesNeeded;
    }

    public FluidAmount getInputFluid() {
        return inputFluid;
    }

    public FluidAmount getOutputFluid() {
        return outputFluid;
    }

    public ItemStack getByProduct() {
        return byProduct.copy();
    }

    public int getByProductChance() {
        return byProductChance;
    }

    public FluidStack getOutputFluidStack() {
        var fluid = BuiltInRegistries.FLUID.get(outputFluid.fluidId());
        if (fluid == Fluids.EMPTY || outputFluid.amount() <= 0) {
            return FluidStack.EMPTY;
        }
        return new FluidStack(fluid, outputFluid.amount());
    }

    @Override
    public boolean matches(ChurnInput input, Level level) {
        FluidStack stack = input.fluid();
        if (stack.isEmpty() || stack.getAmount() < inputFluid.amount()) {
            return false;
        }
        var fluid = BuiltInRegistries.FLUID.get(inputFluid.fluidId());
        return fluid != Fluids.EMPTY && fluid.getFluidType() == stack.getFluid().getFluidType();
    }

    @Override
    public ItemStack assemble(ChurnInput input, HolderLookup.Provider registries) {
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
        return GrowthcraftMilkRecipes.CHURN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return GrowthcraftMilkRecipes.CHURN_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ChurnRecipe> {
        private static final Codec<FluidAmount> FLUID_AMOUNT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidAmount::fluidId),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(FluidAmount::amount)
        ).apply(instance, FluidAmount::new));

        private static final Codec<ItemStack> LEGACY_ITEM_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("item").orElse(ResourceLocation.withDefaultNamespace("air")).forGetter(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
        ).apply(instance, (id, count) -> new ItemStack(BuiltInRegistries.ITEM.get(id), count)));

        public static final MapCodec<ChurnRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("plunges", 7).forGetter(ChurnRecipe::getPlungesNeeded),
                FLUID_AMOUNT_CODEC.fieldOf("input_fluid").forGetter(ChurnRecipe::getInputFluid),
                FLUID_AMOUNT_CODEC.fieldOf("output_fluid").forGetter(ChurnRecipe::getOutputFluid),
                LEGACY_ITEM_STACK_CODEC.optionalFieldOf("by_product", ItemStack.EMPTY).forGetter(ChurnRecipe::getByProduct),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("by_product_chance", 100).forGetter(ChurnRecipe::getByProductChance)
        ).apply(instance, ChurnRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ChurnRecipe> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ChurnRecipe decode(RegistryFriendlyByteBuf buf) {
                int plunges = buf.readVarInt();
                FluidAmount inputFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                FluidAmount outputFluid = new FluidAmount(buf.readResourceLocation(), buf.readVarInt());
                ItemStack byProduct = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
                int byProductChance = buf.readVarInt();
                return new ChurnRecipe(plunges, inputFluid, outputFluid, byProduct, byProductChance);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buf, ChurnRecipe recipe) {
                buf.writeVarInt(recipe.plungesNeeded);
                buf.writeResourceLocation(recipe.inputFluid.fluidId());
                buf.writeVarInt(recipe.inputFluid.amount());
                buf.writeResourceLocation(recipe.outputFluid.fluidId());
                buf.writeVarInt(recipe.outputFluid.amount());
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.byProduct);
                buf.writeVarInt(recipe.byProductChance);
            }
        };

        @Override
        public MapCodec<ChurnRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChurnRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
