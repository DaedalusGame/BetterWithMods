package betterwithmods.common.blocks;

import betterwithmods.common.BWSounds;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.items.tools.ItemHacksaw;
import betterwithmods.common.items.tools.ItemSoulforgeArmor;
import betterwithmods.util.InvUtils;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSteel extends BWMBlock {

    public static final PropertyInteger HEIGHT = PropertyInteger.create("height", 0, 15);

    public BlockSteel() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(100F);
        setResistance(4000F);
        setDefaultState(getDefaultState().withProperty(HEIGHT, 0));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1, (16-state.getValue(HEIGHT))/16d,1);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return state.getValue(HEIGHT) == 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(HEIGHT) == 0;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(HEIGHT) == 0;
    }


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HEIGHT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HEIGHT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HEIGHT, meta);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.getItem() instanceof ItemHacksaw) {
            int height = state.getValue(HEIGHT);
            if (height < 15)
                worldIn.setBlockState(pos, state.withProperty(HEIGHT, height + 1));
            else
                worldIn.setBlockToAir(pos);
            if(!PlayerHelper.hasPart(playerIn, EntityEquipmentSlot.HEAD, ItemSoulforgeArmor.class)) {
                worldIn.playSound(null,pos,BWSounds.METAL_HACKSAW, SoundCategory.BLOCKS,1.0f,0.80f);
                worldIn.playSound(null,pos, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS,0.5f,worldIn.rand.nextFloat() * 0.5f + worldIn.rand.nextFloat()*0.5f);
            }
            stack.damageItem(1,playerIn);
            InvUtils.ejectStackWithOffset(worldIn, pos, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PLATE_STEEL));
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return state.getValue(HEIGHT) != 0 || entity instanceof EntityPlayer;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (world.getBlockState(pos).getValue(HEIGHT) != 0) {
            super.onBlockExploded(world, pos, explosion);
        }
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 4;
    }
}
