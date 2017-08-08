package betterwithmods.common.blocks;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.world.gen.feature.WorldGenBloodTree;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockBloodLog extends BlockLog {
    public static final PropertyBool EXPANDABLE = PropertyBool.create("expandable");

    public BlockBloodLog() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, EnumAxis.Y).withProperty(EXPANDABLE, false));
        this.setTickRandomly(true);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote && world.provider.isNether() && state.getValue(EXPANDABLE)) {
            List<EnumFacing> values = Lists.newArrayList(EnumFacing.values()).stream().filter(f -> f != EnumFacing.DOWN).collect(Collectors.toList());
            for (EnumFacing face : values) {
                if (rand.nextInt(20) == 0)
                    expandTree(world, pos, face);
            }
        }
    }

    private void expandTree(World world, BlockPos pos, EnumFacing facing) {
        WorldGenBloodTree tree = new WorldGenBloodTree();
        tree.generateBranch(world, pos, facing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(EXPANDABLE, (meta & 3) % 4 == 1);

        switch (meta & 12)
        {
            case 0:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(EXPANDABLE) ? 1 : 0;

        switch (state.getValue(LOG_AXIS))
        {
            case X:
                meta |= 4;
                break;
            case Z:
                meta |= 8;
                break;
            case NONE:
                meta |= 12;
        }
        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EXPANDABLE, LOG_AXIS);
    }
}
