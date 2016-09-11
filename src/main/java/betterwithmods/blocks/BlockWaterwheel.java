package betterwithmods.blocks;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

public class BlockWaterwheel extends BlockGen implements IMechanical, IAxle
{
    public static final PropertyInteger AXLEDIR = PropertyInteger.create("dir", 1, 2);

    public BlockWaterwheel()
    {
        super(Material.WOOD, "waterwheel",null);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXLEDIR, 1).withProperty(ISACTIVE, false));
        GameRegistry.registerTileEntity(TileEntityWaterwheel.class, "bwm.waterwheel");
    }

    public IBlockState getWaterwheelState(int axis)
    {
        return this.getDefaultState().withProperty(AXLEDIR, axis);
    }

    @Override
    public int tickRate(World world)
    {
        return 20;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        return new ItemStack(BWRegistry.windmill, 1, 1);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityWaterwheel)
        {
            boolean active = ((TileEntityWaterwheel)world.getTileEntity(pos)).isValid();
            world.setBlockState(pos, state.withProperty(ISACTIVE, active));
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        int ori = state.getValue(AXLEDIR);
        AxisAlignedBB axis = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
        switch(ori)
        {
            case 0: axis = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F); break;
            case 1: axis = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F); break;
            case 2: axis = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F); break;
        }
        return axis;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return BWRegistry.windmill;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 1;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityWaterwheel();
    }

    public int getPowerLevel(IBlockAccess world, BlockPos pos)
    {
        return getPowerLevelFromState(world.getBlockState(pos));
    }

    public int getPowerLevelFromState(IBlockState state)
    {
        return state.getValue(ISACTIVE) ? 4 : 0;
    }

    @Override
    public int getMechPowerLevelToFacing(World world, BlockPos pos,
                                         EnumFacing dir)
    {
        if(world.getBlockState(pos).getValue(ISACTIVE))
        {
            int axis = getAxisAlignment(world, pos);
            if(dir == BlockAxle.facings[axis][0] || dir == BlockAxle.facings[axis][1])
                return 4;
        }
        return 0;
    }

    @Override
    public EnumFacing getAxleDirection(World world, BlockPos pos)
    {
        int meta = this.getAxisAlignment(world, pos);
        if(meta == 0)
            return EnumFacing.UP;
        else if(meta == 1)
            return EnumFacing.SOUTH;
        else if(meta == 2)
            return EnumFacing.EAST;
        return EnumFacing.UP;
    }

    @Override
    public int getAxisAlignment(IBlockAccess world, BlockPos pos)
    {
        return getAxisAlignmentFromState(world.getBlockState(pos));
    }

    private int getAxisAlignmentFromState(IBlockState state)
    {
        if(state.getBlock() == this)
            return state.getValue(AXLEDIR);
        return 0;
    }

    @Override
    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing dir)
    {
        int axis = this.getAxisAlignment(world, pos);
        return dir == BlockAxle.facings[axis][0] || dir == BlockAxle.facings[axis][1];
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AXLEDIR, ISACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(AXLEDIR);
        if(state.getValue(ISACTIVE))
            meta += 8;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        boolean active = meta > 7;
        if(active)
            meta -= 8;
        return this.getWaterwheelState(meta).withProperty(ISACTIVE, active);
    }

    @Override
    public ItemStack getGenStack(IBlockState state)
    {
        return new ItemStack(BWRegistry.windmill, 1, 1);
    }
}
