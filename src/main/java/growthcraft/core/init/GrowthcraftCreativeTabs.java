package growthcraft.core.init;

import growthcraft.core.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register(
            Reference.UnlocalizedName.CreativeTab.TAB,
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(GrowthcraftItems.CROWBAR_WHITE.get()))
                    .title(Component.literal("Growthcraft"))
                    .displayItems((params, output) -> {
                        // Materials
                        output.accept(GrowthcraftItems.SALT.get());
                        output.accept(GrowthcraftItems.ROPE_LINEN.get());

                        // Blocks
                        output.accept(GrowthcraftItems.SALT_BLOCK.get());
                        output.accept(GrowthcraftItems.SALT_ORE.get());
                        output.accept(GrowthcraftItems.SALT_ORE_DEEPSLATE.get());
                        output.accept(GrowthcraftItems.SALT_ORE_NETHER.get());
                        output.accept(GrowthcraftItems.SALT_ORE_END.get());

                        // Tools
                        output.accept(GrowthcraftItems.CROWBAR_WHITE.get());
                        output.accept(GrowthcraftItems.CROWBAR_LIGHT_GRAY.get());
                        output.accept(GrowthcraftItems.CROWBAR_GRAY.get());
                        output.accept(GrowthcraftItems.CROWBAR_BLACK.get());
                        output.accept(GrowthcraftItems.CROWBAR_BROWN.get());
                        output.accept(GrowthcraftItems.CROWBAR_RED.get());
                        output.accept(GrowthcraftItems.CROWBAR_ORANGE.get());
                        output.accept(GrowthcraftItems.CROWBAR_YELLOW.get());
                        output.accept(GrowthcraftItems.CROWBAR_LIME.get());
                        output.accept(GrowthcraftItems.CROWBAR_GREEN.get());
                        output.accept(GrowthcraftItems.CROWBAR_CYAN.get());
                        output.accept(GrowthcraftItems.CROWBAR_LIGHT_BLUE.get());
                        output.accept(GrowthcraftItems.CROWBAR_BLUE.get());
                        output.accept(GrowthcraftItems.CROWBAR_PURPLE.get());
                        output.accept(GrowthcraftItems.CROWBAR_MAGENTA.get());
                        output.accept(GrowthcraftItems.CROWBAR_PINK.get());
                        output.accept(GrowthcraftItems.WRENCH.get());
                    })
                    .build()
    );
}
