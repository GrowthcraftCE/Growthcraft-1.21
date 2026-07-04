package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.cellar.menu.CultureJarMenu;
import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.cellar.menu.FruitPressMenu;
import growthcraft.cellar.menu.RoasterMenu;
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

    public static final DeferredHolder<MenuType<?>, MenuType<BrewKettleMenu>> BREW_KETTLE = MENUS.register(
            Reference.UnlocalizedName.Block.BREW_KETTLE,
            () -> new MenuType<>(BrewKettleMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final DeferredHolder<MenuType<?>, MenuType<FermentationBarrelMenu>> FERMENTATION_BARREL = MENUS.register(
            Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK,
            () -> new MenuType<>(FermentationBarrelMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final DeferredHolder<MenuType<?>, MenuType<FruitPressMenu>> FRUIT_PRESS = MENUS.register(
            Reference.UnlocalizedName.Block.FRUIT_PRESS,
            () -> new MenuType<>(FruitPressMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
    public static final DeferredHolder<MenuType<?>, MenuType<RoasterMenu>> ROASTER = MENUS.register(
            Reference.UnlocalizedName.Block.ROASTER,
            () -> new MenuType<>(RoasterMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private GrowthcraftCellarMenus() {}
}
