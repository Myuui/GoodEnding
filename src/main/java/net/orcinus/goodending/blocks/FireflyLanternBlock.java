package net.orcinus.goodending.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.orcinus.goodending.init.GoodEndingParticleTypes;
import net.orcinus.goodending.init.GoodEndingSoundEvents;

import java.util.stream.Stream;

public class FireflyLanternBlock extends LanternBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    protected static final VoxelShape STANDING_SHAPE = Stream.of(
        box(3, 11, 3, 13, 12, 13),
        box(3, 0, 3, 13, 10, 13),
        box(4, 10, 4, 12, 11, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public FireflyLanternBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return STANDING_SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        world.setBlock(pos, state.cycle(OPEN), 2);
        world.playSound(null, pos, state.getValue(OPEN) ? GoodEndingSoundEvents.FIREFLY_LANTERN_CLOSE : GoodEndingSoundEvents.FIREFLY_LANTERN_OPEN, SoundSource.BLOCKS, 1, 1);
        world.gameEvent(player, state.getValue(OPEN) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int step = random.nextBoolean() ? -1 : 1;
        boolean flag = state.getValue(OPEN) && (!world.canSeeSky(pos) || world.getDayTime() >= 13000 && world.getDayTime() < 24000);
        if (flag) {
            if (!state.getValue(HANGING)) {
                world.addParticle(GoodEndingParticleTypes.FIREFLY, pos.getX() + 0.5D, pos.getY() + 0.7F, pos.getZ() + 0.5D, 0.1F * random.nextFloat() * step, random.nextFloat() / random.nextInt(2, 10), 0.1F * random.nextFloat() * step);
            }
            this.addExteriorFireflies(state, world, pos, random);
        } else {
            this.addInteriorFireflies(state, world, pos, random);
        }
        if (random.nextDouble() < 0.1D) {
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), GoodEndingSoundEvents.ENTITY_FIREFLY_SWARM_IDLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    private void addExteriorFireflies(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int i = pos.getX();
        int j = pos.getY() + (state.getValue(HANGING) ? -1 : 6);
        int k = pos.getZ();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int l = 0; l < 10; ++l) {
            mutable.set(i + Mth.nextInt(random, -6, 6), j - random.nextInt(6), k + Mth.nextInt(random, -6, 6));
            BlockState blockState = world.getBlockState(mutable);
            if (blockState.isCollisionShapeFullBlock(world, mutable)) {
                continue;
            }
            double velx = random.nextGaussian() * 0.025;
            double vely = random.nextGaussian() * 0.025;
            double velz = random.nextGaussian() * 0.025;
            world.addParticle(GoodEndingParticleTypes.FIREFLY, (double) mutable.getX() + random.nextDouble(), (double) mutable.getY() + random.nextDouble(), (double) mutable.getZ() + random.nextDouble(), velx, vely, velz);
        }
    }

    private void addInteriorFireflies(BlockState state, Level world, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY();
        double z = pos.getZ() + 0.5D;
        double xVelocity = random.nextFloat() / 45.0F * (random.nextBoolean() ? -1 : 1);
        double yVelocity = random.nextFloat() / 100.0F * (state.getValue(HANGING) ? 2 : -1);
        double zVelocity = random.nextFloat() / 45.0F * (random.nextBoolean() ? -1 : 1);
        world.addParticle(GoodEndingParticleTypes.FIREFLY, x, y + 0.4, z, xVelocity, yVelocity, zVelocity);
    }
}
