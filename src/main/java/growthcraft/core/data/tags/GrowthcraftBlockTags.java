package growthcraft.core.data.tags;

import growthcraft.bamboo.init.GrowthcraftBambooBlocks;
import growthcraft.core.config.Reference;
import growthcraft.core.init.GrowthcraftBlocks;
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
                        GrowthcraftBlocks.SALT_ORE_END.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(
                        GrowthcraftBambooBlocks.BAMBOO_POST_VERTICAL.get(),
                        GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get()
                );

        this.tag(BlockTags.CLIMBABLE)
                .add(GrowthcraftBambooBlocks.BAMBOO_POST_HORIZONTAL.get());
    }
}
