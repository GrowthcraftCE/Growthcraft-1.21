package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.BrewKettleMenu;
import growthcraft.cellar.menu.CultureJarMenu;
import growthcraft.cellar.menu.FermentationBarrelMenu;
import growthcraft.cellar.menu.RoasterMenu;
import growthcraft.lib.menu.MachineMenu;
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
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> FRUIT_PRESS = registerMachine(Reference.UnlocalizedName.Block.FRUIT_PRESS);
    public static final DeferredHolder<MenuType<?>, MenuType<RoasterMenu>> ROASTER = MENUS.register(
            Reference.UnlocalizedName.Block.ROASTER,
            () -> new MenuType<>(RoasterMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private static DeferredHolder<MenuType<?>, MenuType<MachineMenu>> registerMachine(String name) {
        return MENUS.register(name, () -> new MenuType<>((containerId, playerInventory) ->
                new MachineMenu(GrowthcraftCellarMenus.machineMenuType(name), containerId, playerInventory), FeatureFlags.DEFAULT_FLAGS));
    }

    private static MenuType<MachineMenu> machineMenuType(String name) {
        if (Reference.UnlocalizedName.Block.FRUIT_PRESS.equals(name)) return FRUIT_PRESS.get();
        throw new IllegalArgumentException("Unknown cellar menu: " + name);
    }

    private GrowthcraftCellarMenus() {}
}
