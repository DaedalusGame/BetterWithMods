package betterwithmods.common.blocks;

import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.tile.TileHandCrank;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.Gameplay;
import betterwithmods.module.gameplay.MillRecipes;
import betterwithmods.module.hardcore.HCHunger;
import betterwithmods.util.InvUtils;
import betterwithmods.util.player.PlayerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCrank extends BWMBlock implements IMechanicalBlock, IMultiVariants {
    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 8);
    public static final float BASE_HEIGHT = 0.25F;
    private static final AxisAlignedBB CRANK_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, BASE_HEIGHT, 1.0F);

    public BlockCrank() {
        super(Material.ROCK);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(getDefaultState().withProperty(STAGE, 0));
        this.setHarvestLevel("pickaxe", 0);

    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileHandCrank();
    }

    public TileHandCrank getTile(IBlockAccess world, BlockPos pos) {
        return (TileHandCrank) world.getTileEntity(pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileHandCrank tile = getTile(worldIn, pos);
        if(tile != null) {
            return getDefaultState().withProperty(STAGE, tile.getStage());
        }
        return getDefaultState();
    }

    @Override
    public String[] getVariants() {
        return new String[]{"stage=0"};
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return CRANK_AABB;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        EnumActionResult result = toggleSwitch(player, world, pos);
        if (result == EnumActionResult.PASS) {
            if (world.isRemote) {
                if (hand == EnumHand.MAIN_HAND)
                    player.sendStatusMessage(new TextComponentTranslation("bwm.message.exhaustion"), true);
            }
        }
        return result == EnumActionResult.SUCCESS;
    }

    private boolean canCrank(EntityPlayer player) {
        return PlayerHelper.getHungerPenalty(player).ordinal() < 2 && player.getFoodStats().getFoodLevel() > 6;
    }

    private EnumActionResult toggleSwitch(EntityPlayer player, World world, BlockPos pos) {
        TileHandCrank tile = getTile(world, pos);
        if (tile != null) {
            if(tile.getStage() > 0)
                return EnumActionResult.FAIL;
            if (canCrank(player)) {
                if(Gameplay.getCrankExhaustion() > 0)
                    player.addExhaustion((float) Gameplay.getCrankExhaustion());
                tile.setStage(1);
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.PASS;
            }
        }
        return EnumActionResult.FAIL;
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
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    public boolean checkForOverpower(World world, BlockPos pos) {
        int potentialDevices = 0;
        for (int i = 0; i < 6; i++) {
            BlockPos offset = pos.offset(EnumFacing.getFront(i));
            if (i != 0) {
                Block block = world.getBlockState(offset).getBlock();
                if (block != null && block instanceof IMechanicalBlock) {
                    IMechanicalBlock mech = (IMechanicalBlock) block;
                    if (mech.canInputMechanicalPower())
                        potentialDevices++;
                }
            }
        }
        return potentialDevices > 1;
    }

    public void breakCrank(World world, BlockPos pos) {
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.STICK));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Blocks.COBBLESTONE, 2, 0));
        InvUtils.ejectStackWithOffset(world, pos, new ItemStack(BWMItems.MATERIAL, 1, 0));
        //world.playAuxSFX(2235, x, y, z, 0);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return true;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return false;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return getActualState(world.getBlockState(pos),world,pos).getValue(STAGE) > 0;
    }


    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        return false;
    }

    @Override
    public void overpower(World world, BlockPos pos) {
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        BlockPos down = pos.down();
        if (!world.isSideSolid(down, EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, STAGE);
    }
}
