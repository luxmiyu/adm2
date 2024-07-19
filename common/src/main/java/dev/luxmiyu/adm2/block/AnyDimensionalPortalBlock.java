package dev.luxmiyu.adm2.block;

import dev.luxmiyu.adm2.portal.Portal;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class AnyDimensionalPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);

    public AnyDimensionalPortalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X));
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(AXIS) == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!world.isClient) return;

        if (random.nextInt(100) == 0) {
            double soundX = (double) pos.getX() + 0.5d;
            double soundY = (double) pos.getY() + 0.5d;
            double soundZ = (double) pos.getZ() + 0.5d;
            float volume = 0.5f;
            float pitch = random.nextFloat() * 0.4f + 0.8f;
            boolean useDistance = false;
            world.playSound(soundX, soundY, soundZ, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, volume, pitch, useDistance);
        }

        for (int i = 0; i < 4; ++i) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() + random.nextDouble();
            double f = (double) pos.getZ() + random.nextDouble();
            double g = ((double) random.nextFloat() - 0.5) * 0.5;
            double h = ((double) random.nextFloat() - 0.5) * 0.5 + 0.5;
            double j = ((double) random.nextFloat() - 0.5) * 0.5;
            int k = random.nextInt(2) * 2 - 1;

            if (world.getBlockState(pos.west()).isOf(this) || world.getBlockState(pos.east()).isOf(this)) {
                f = (double) pos.getZ() + 0.5 + 0.25 * (double) k;
                j = random.nextFloat() * 2.0f * (float) k;
            } else {
                d = (double) pos.getX() + 0.5 + 0.25 * (double) k;
                g = random.nextFloat() * 2.0f * (float) k;
            }

            world.addParticle(new BlockStateParticleEffect(ParticleTypes.FALLING_DUST, this.getDefaultState()), d, e, f, g, h, j);
        }
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (world.isClient()) return state;

        int diffX = Math.abs(pos.getX() - neighborPos.getX());
        int diffZ = Math.abs(pos.getZ() - neighborPos.getZ());

        Direction.Axis axis = state.get(AXIS);

        boolean invalidX = (axis == Direction.Axis.X) && diffX > 0;
        boolean invalidZ = (axis == Direction.Axis.Z) && diffZ > 0;

        if (invalidX || invalidZ) {
            return state; // unrelated update
        }

        boolean isValidFrameBlock = Portal.isDimensionLoaded(world.getServer(), neighborState.getBlock());
        boolean isPortalBlock = neighborState.isOf(this);

        if (isValidFrameBlock || isPortalBlock) {
            return state;
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }
}
