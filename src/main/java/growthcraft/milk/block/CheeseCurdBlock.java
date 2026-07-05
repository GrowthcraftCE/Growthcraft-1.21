package growthcraft.milk.block;

import com.mojang.serialization.MapCodec;
import growthcraft.milk.config.Reference;
import growthcraft.milk.particle.ColoredDripParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CheeseCurdBlock extends Block {
    public static final MapCodec<CheeseCurdBlock> CODEC = simpleCodec(properties -> new CheeseCurdBlock());
    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    private static final VoxelShape SHAPE = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 8.0D, 12.0D);
    private static final int DRIP_COLOR = Reference.FluidColor.WHEY.toIntValue();

    public CheeseCurdBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)
                .noOcclusion()
                .randomTicks()
                .isValidSpawn((state, getter, pos, type) -> false)
                .isRedstoneConductor((state, getter, pos) -> false)
                .isViewBlocking((state, getter, pos) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !level.getBlockState(pos.above()).isAir();
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || isMaxAge(state)) {
            return;
        }

        level.setBlock(pos, getStateForAge(getAge(state) + 1), Block.UPDATE_ALL);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isMaxAge(state) || random.nextInt(16) > 4) {
            return;
        }

        double x = pos.getX() + 0.25D + random.nextDouble() * 0.5D;
        double y = pos.getY() - 0.05D;
        double z = pos.getZ() + 0.25D + random.nextDouble() * 0.5D;
        level.addParticle(new ColoredDripParticleOption(DRIP_COLOR), x, y, z, 0.0D, 0.0D, 0.0D);
    }

    protected int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    public boolean isMaxAge(BlockState state) {
        return getAge(state) >= MAX_AGE;
    }

    public BlockState getStateForAge(int age) {
        return defaultBlockState().setValue(AGE, Math.clamp(age, 0, MAX_AGE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
