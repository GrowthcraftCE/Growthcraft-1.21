package growthcraft.lib.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

public final class MachineMenuOpener {
    private MachineMenuOpener() {
    }

    public static InteractionResult open(Level level, Player player, MenuType<? extends AbstractContainerMenu> menuType, Component title) {
        if (!level.isClientSide) {
            MenuProvider provider = new SimpleMenuProvider((containerId, inventory, openingPlayer) ->
                    menuType.create(containerId, inventory), title);
            player.openMenu(provider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
