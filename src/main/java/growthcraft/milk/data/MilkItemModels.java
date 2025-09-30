package growthcraft.milk.data;

import growthcraft.milk.config.Reference;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates item models for Growthcraft Milk items.
 * For now, only the milking bucket tools (iron/copper) are needed.
 */
public class MilkItemModels extends ItemModelProvider {
    public MilkItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Layered model using existing textures in growthcraft_milk assets
        milkingBucketTool(Reference.UnlocalizedName.MILKING_BUCKET_IRON);
    }

    private void milkingBucketTool(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/milking_bucket_contents_default"))
                .texture("layer1", modLoc("item/milking_bucket_base"));
    }
}
