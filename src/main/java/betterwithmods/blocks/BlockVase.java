package betterwithmods.blocks;

import betterwithmods.BWMItems;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.blocks.tile.TileEntityVase;
import betterwithmods.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Christian on 24.09.2016.
 */
public class BlockVase extends BWMBlock implements IMultiVariants, ITileEntityProvider {
    public static final PropertyEnum<EnumDyeColor> Color = BlockColored.COLOR;

    public BlockVase() {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(Color, EnumDyeColor.WHITE));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return createTileEntity(world, this.getStateFromMeta(meta));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityVase();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.getActualState(world, pos);
        return new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 1.0D, 0.875D);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public String[] getVariants() {
        EnumDyeColor[] dyes = EnumDyeColor.values();
        String[] variants = new String[dyes.length];

        for(int i = 0; i < dyes.length; ++i) {
            EnumDyeColor dye = dyes[i];
            variants[i] = "color="+dye.getName();
        }

        return variants;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);

        if(te != null && playerIn != null && !playerIn.isSneaking() && heldItem != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            return ((TileEntityVase)te).onActivated(playerIn,hand,heldItem);
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity != null && entity instanceof EntityArrow) {
            world.playEvent(2001, pos, Block.getStateId(state));
            world.setBlockToAir(pos);
        }

        super.onEntityCollidedWithBlock(world, pos, state, entity);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                InvUtils.ejectInventoryContents(world, pos, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
                world.updateComparatorOutputLevel(pos, this);
            }

            if(tile instanceof TileEntityVase)
                ((TileEntityVase)tile).onBreak();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return (state.getValue(Color)).getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
    {
        for (int i = 0; i < EnumDyeColor.values().length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(Color, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(Color).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, Color);
    }
}
