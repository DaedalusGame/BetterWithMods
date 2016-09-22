package betterwithmods.blocks;

import betterwithmods.BWMItems;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanical;
import betterwithmods.blocks.tile.gen.*;
import betterwithmods.util.ColorUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.Random;

public class BlockWindmill extends BlockGen implements IMechanical, IAxle
{
    public static final PropertyInteger AXLEDIR = PropertyInteger.create("dir", 0, 2);

    public BlockWindmill()
    {
        super(Material.WOOD, "windmill_block",null);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXLEDIR, 0).withProperty(ISACTIVE, false));

    }

    public IBlockState getWindmillState(int axis)
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
        return new ItemStack(BWMItems.WINDMILL, 1, state.getValue(AXLEDIR) == 0 ? 2 : 0);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMechGenerator)
        {
            boolean active;
            if(state.getValue(AXLEDIR) == 0)
                active = ((TileEntityWindmillVertical)world.getTileEntity(pos)).isValid();
            else
                active = ((TileEntityWindmillHorizontal)world.getTileEntity(pos)).isValid();
            world.setBlockState(pos, state.withProperty(ISACTIVE, active));
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);

        if(world.isRemote && stack != null && ColorUtils.contains(stack.getItem(), stack.getItemDamage()))
            return true;

        if(!world.isRemote && tile != null && tile instanceof IColor && stack != null && ColorUtils.contains(stack.getItem(), stack.getItemDamage()))
        {
            IColor color = (IColor)tile;
            int meta = ColorUtils.get(stack.getItem(), stack.getItemDamage()); //reverseMeta[stack.getItemDamage()];
            if(color.dyeBlade(meta))
            {
                if(!player.capabilities.isCreativeMode)
                    stack.stackSize--;
                return true;
            }
        }
        return false;
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
        return BWMItems.WINDMILL;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(AXLEDIR) == 0 ? 2 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if(state.getValue(AXLEDIR) == 0)
            return new TileEntityWindmillVertical();
        else
            return new TileEntityWindmillHorizontal();
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
        return this.getWindmillState(meta).withProperty(ISACTIVE, active);
    }

    @Override
    public ItemStack getGenStack(IBlockState state)
    {
        return state.getValue(AXLEDIR) == 0 ? new ItemStack(BWMItems.WINDMILL, 1, 2) : new ItemStack(BWMItems.WINDMILL, 1, 0);
    }
}
