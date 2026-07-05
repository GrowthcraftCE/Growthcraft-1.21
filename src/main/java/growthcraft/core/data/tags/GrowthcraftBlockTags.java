package growthcraft.core.data.tags;

import growthcraft.apiary.init.GrowthcraftApiaryBlocks;
import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.apples.init.GrowthcraftApplesBlocks;
import growthcraft.cellar.init.GrowthcraftCellarBlocks;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
import growthcraft.core.init.GrowthcraftTags;
import growthcraft.milk.init.GrowthcraftMilkBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class GrowthcraftBlockTags extends BlockTagsProvider {
    public GrowthcraftBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Ensure salt ores are mineable with a pickaxe (including wooden pickaxe)
        // Do NOT put them into needs_stone_tool/needs_iron_tool/etc. tags so wooden works.
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        GrowthcraftBlocks.SALT_ORE.get(),
                        GrowthcraftBlocks.SALT_ORE_DEEPSLATE.get(),
                        GrowthcraftBlocks.SALT_ORE_NETHER.get(),
                        GrowthcraftBlocks.SALT_ORE_END.get(),
                        GrowthcraftCellarBlocks.BREW_KETTLE.get(),
                        GrowthcraftCellarBlocks.ROASTER.get(),
                        GrowthcraftMilkBlocks.MIXING_VAT.get(),
                        GrowthcraftMilkBlocks.PANCHEON.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        GrowthcraftCellarBlocks.FERMENTATION_BARREL_OAK.get(),
                        GrowthcraftCellarBlocks.FRUIT_PRESS.get(),
                        GrowthcraftMilkBlocks.CHEESE_PRESS.get(),
                        GrowthcraftMilkBlocks.CHURN.get(),
                        GrowthcraftMilkBlocks.HANGING_SIGN_1_OAK.get(),
                        GrowthcraftMilkBlocks.HANGING_SIGN_1_SPRUCE.get(),
                        GrowthcraftMilkBlocks.HANGING_SIGN_2_OAK.get(),
                        GrowthcraftMilkBlocks.HANGING_SIGN_2_SPRUCE.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_BUTTON.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_DOOR.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_FENCE.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_GATE.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_ROPE_LINEN.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_PRESSURE_PLATE.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_SLAB.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_STAIRS.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_TRAPDOOR.get(),
                        GrowthcraftApplesBlocks.APPLE_TREE_FRUIT.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get(),
                        GrowthcraftApplesBlocks.BEE_BOX_APPLE.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_ACACIA.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_BAMBOO.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_BIRCH.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_CHERRY.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_CRIMSON.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_DARK_OAK.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_JUNGLE.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_MANGROVE.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_OAK.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_SPRUCE.get(),
                        GrowthcraftApiaryBlocks.BEE_BOX_WARPED.get(),
                        GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get(),
                        GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get()
                );

        this.tag(BlockTags.LEAVES)
                .add(GrowthcraftApplesBlocks.APPLE_TREE_LEAVES.get());

        this.tag(BlockTags.LOGS)
                .add(
                        GrowthcraftApplesBlocks.APPLE_WOOD.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_LOG.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_LOG_STRIPPED.get(),
                        GrowthcraftApplesBlocks.APPLE_WOOD_STRIPPED.get()
                );

        this.tag(BlockTags.SAPLINGS)
                .add(GrowthcraftApplesBlocks.APPLE_TREE_SAPLING.get());

        this.tag(BlockTags.CLIMBABLE)
                .add(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get());

        this.tag(GrowthcraftTags.Blocks.ROPE)
                .add(
                        GrowthcraftBlocks.ROPE_LINEN.get(),
                        GrowthcraftBlocks.ROPE_LINEN_ACACIA_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_BAMBOO_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_BIRCH_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_CHERRY_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_CRIMSON_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_DARK_OAK_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_JUNGLE_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_MANGROVE_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_NETHER_BRICK_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_OAK_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_SPRUCE_FENCE.get(),
                        GrowthcraftBlocks.ROPE_LINEN_WARPED_FENCE.get(),
                        GrowthcraftApplesBlocks.APPLE_PLANK_FENCE_ROPE_LINEN.get(),
                        GrowthcraftCellarBlocks.HOPS_VINE.get(),
                        GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE.get(),
                        GrowthcraftCellarBlocks.RED_GRAPE_VINE.get(),
                        GrowthcraftCellarBlocks.WHITE_GRAPE_VINE.get(),
                        GrowthcraftCellarBlocks.PURPLE_GRAPE_VINE_LEAVES.get(),
                        GrowthcraftCellarBlocks.RED_GRAPE_VINE_LEAVES.get(),
                        GrowthcraftCellarBlocks.WHITE_GRAPE_VINE_LEAVES.get()
                );
    }
}
