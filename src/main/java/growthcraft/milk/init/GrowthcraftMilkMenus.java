package growthcraft.milk.init;

import growthcraft.lib.menu.MachineMenu;
import growthcraft.milk.config.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GrowthcraftMilkMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Reference.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> CHEESE_PRESS = register(Reference.UnlocalizedName.CHEESE_PRESS);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> CHURN = register(Reference.UnlocalizedName.CHURN);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> MIXING_VAT = register(Reference.UnlocalizedName.MIXING_VAT);
    public static final DeferredHolder<MenuType<?>, MenuType<MachineMenu>> PANCHEON = register(Reference.UnlocalizedName.PANCHEON);

    private static DeferredHolder<MenuType<?>, MenuType<MachineMenu>> register(String name) {
        return MENUS.register(name, () -> new MenuType<>((containerId, playerInventory) ->
                new MachineMenu(GrowthcraftMilkMenus.menuType(name), containerId, playerInventory), FeatureFlags.DEFAULT_FLAGS));
    }

    private static MenuType<MachineMenu> menuType(String name) {
        if (Reference.UnlocalizedName.CHEESE_PRESS.equals(name)) return CHEESE_PRESS.get();
        if (Reference.UnlocalizedName.CHURN.equals(name)) return CHURN.get();
        if (Reference.UnlocalizedName.MIXING_VAT.equals(name)) return MIXING_VAT.get();
        if (Reference.UnlocalizedName.PANCHEON.equals(name)) return PANCHEON.get();
        throw new IllegalArgumentException("Unknown milk menu: " + name);
    }

    private GrowthcraftMilkMenus() {
    }
}
