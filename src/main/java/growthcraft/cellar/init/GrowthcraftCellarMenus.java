package growthcraft.cellar.init;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.menu.CultureJarMenu;
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

    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> BREW_KETTLE = registerMachine(Reference.UnlocalizedName.Block.BREW_KETTLE);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> FERMENTATION_BARREL = registerMachine(Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> FRUIT_PRESS = registerMachine(Reference.UnlocalizedName.Block.FRUIT_PRESS);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> ROASTER = registerMachine(Reference.UnlocalizedName.Block.ROASTER);

    private static DeferredHolder<MenuType<?>, MenuType<MachineMenu>> registerMachine(String name) {
        return MENUS.register(name, () -> new MenuType<>((containerId, playerInventory) ->
                new MachineMenu(GrowthcraftCellarMenus.machineMenuType(name), containerId, playerInventory), FeatureFlags.DEFAULT_FLAGS));
    }

    private static MenuType<MachineMenu> machineMenuType(String name) {
        if (Reference.UnlocalizedName.Block.BREW_KETTLE.equals(name)) return BREW_KETTLE.get();
        if (Reference.UnlocalizedName.Block.FERMENT_BARREL_OAK.equals(name)) return FERMENTATION_BARREL.get();
        if (Reference.UnlocalizedName.Block.FRUIT_PRESS.equals(name)) return FRUIT_PRESS.get();
        if (Reference.UnlocalizedName.Block.ROASTER.equals(name)) return ROASTER.get();
        throw new IllegalArgumentException("Unknown cellar menu: " + name);
    }

    private GrowthcraftCellarMenus() {}
}
