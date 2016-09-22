package betterwithmods.blocks.mini;

import betterwithmods.blocks.BWMBlock;
import betterwithmods.util.InvUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class BlockMini extends BWMBlock
{
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 6);
    public static final PropertyInteger ORIENTATION = createOrientation();

    public static PropertyInteger createOrientation()
    {
        return PropertyInteger.create("orientation", 0, 5);
    }
    public BlockMini(Material material, String name)
    {
        super(material);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0).withProperty(ORIENTATION, 0));
        this.setSoundType(material == Material.WOOD ? SoundType.WOOD : SoundType.STONE);
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
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float flX, float flY, float flZ, int meta, EntityLivingBase placer)
    {
        return metaBlockPlace(facing, flX, flY, flZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
    {
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType)
        {/*
            if(stack.hasTagCompound()) {
                ((TileEntityMultiType) world.getTileEntity(pos)).setCosmeticType(stack.getTagCompound().getInteger("type"));
                world.setBlockState(pos, state.withProperty(TYPE, stack.getTagCompound().getInteger("type")));
            }
            else */{
                int meta = stack.getItemDamage();
                ((TileEntityMultiType) world.getTileEntity(pos)).setCosmeticType(meta);
                world.setBlockState(pos, state.withProperty(TYPE, meta));
            }
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType)
        {
            return new ItemStack(this, 1, ((TileEntityMultiType)world.getTileEntity(pos)).getCosmeticType());
        }
        return new ItemStack(this, 1, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
    {
        for(int i = 0; i < 6; i++)
        {
            ItemStack stack = new ItemStack(this, 1, i);/*
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("type", i);
            stack.setTagCompound(tag);*/
            list.add(stack);
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);

        stack = new ItemStack(this, 1, state.getValue(TYPE));
        InvUtils.ejectStackWithOffset(world, pos, stack);
    }

    public abstract IBlockState metaBlockPlace(EnumFacing facing, float flX, float flY, float flZ);

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityMultiType();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        int type = 0;
        if(world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityMultiType)
            type = ((TileEntityMultiType)world.getTileEntity(pos)).getCosmeticType();
        return state.withProperty(TYPE, type);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ORIENTATION, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ORIENTATION);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE, ORIENTATION);
    }
}
