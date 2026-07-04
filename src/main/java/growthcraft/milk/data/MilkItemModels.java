package growthcraft.milk.data;

import growthcraft.milk.config.Reference;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MilkItemModels extends ItemModelProvider {
    private static final String[] CHEESE_NAMES = {
            Reference.UnlocalizedName.APPENZELLER,
            Reference.UnlocalizedName.ASIAGO,
            Reference.UnlocalizedName.CASU_MARZU,
            Reference.UnlocalizedName.CHEDDAR,
            Reference.UnlocalizedName.EMMENTALER,
            Reference.UnlocalizedName.GORGONZOLA,
            Reference.UnlocalizedName.GOUDA,
            Reference.UnlocalizedName.MONTEREY,
            Reference.UnlocalizedName.PARMESAN,
            Reference.UnlocalizedName.PROVOLONE
    };

    public MilkItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        milkingBucketTool(Reference.UnlocalizedName.MILKING_BUCKET_IRON);

        for (String cheeseName : CHEESE_NAMES) {
            cheeseCut(cheeseName);
        }
    }

    private void milkingBucketTool(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/milking_bucket_contents_default"))
                .texture("layer1", modLoc("item/milking_bucket_base"));
    }

    private void cheeseCut(String name) {
        withExistingParent(name + "_cut", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/cheese/" + name + "_cut"));
    }
}
