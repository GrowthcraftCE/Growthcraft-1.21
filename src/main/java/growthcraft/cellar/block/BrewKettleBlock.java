package growthcraft.cellar.block;

import growthcraft.cellar.config.Reference;
import growthcraft.cellar.init.GrowthcraftCellarMenus;
import growthcraft.lib.block.MachineMenuOpener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BrewKettleBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty HAS_LID = BooleanProperty.create("has_lid");

    public BrewKettleBlock() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .strength(1.5F)
                .sound(SoundType.METAL)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(HAS_LID, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HAS_LID);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return MachineMenuOpener.open(level, player, GrowthcraftCellarMenus.BREW_KETTLE.get(),
                Component.translatable("block." + Reference.MODID + "." + Reference.UnlocalizedName.Block.BREW_KETTLE));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
