package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.CultureJarMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftCellarMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Reference.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CultureJarMenu>> CULTURE_JAR = MENUS.register(
            Reference.UnlocalizedName.Block.CULTURE_JAR,
            () -> new MenuType<>(CultureJarMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private GrowthcraftCellarMenus() {}
}
