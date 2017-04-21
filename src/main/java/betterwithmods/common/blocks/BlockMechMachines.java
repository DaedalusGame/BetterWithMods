package betterwithmods.common.blocks;

import betterwithmods.BWMod;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.common.blocks.tile.TileEntityMill;
import betterwithmods.common.blocks.tile.TileEntityPulley;
import betterwithmods.common.blocks.tile.TileEntityTurntable;
import betterwithmods.module.gameplay.MechanicalBreakage;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockMechMachines extends BWMBlock implements IMechanicalBlock, ITileEntityProvider, IMultiVariants {

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(BWMBlocks.SINGLE_MACHINES,1, type.getMeta());
    }

    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");
    public static final PropertyEnum<BlockMechMachines.EnumType> MACHINETYPE = PropertyEnum.create("machinetype", BlockMechMachines.EnumType.class);

    public BlockMechMachines() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(MACHINETYPE, BlockMechMachines.EnumType.MILL)
                .withProperty(ISACTIVE, false)
        );
        this.useNeighborBrightness = true;
    }

    @Override
    public String[] getVariants() {
        return new String[]{
                "ison=true,machinetype=mill",
                "ison=false,machinetype=mill",
                "ison=true,machinetype=pulley",
                "ison=false,machinetype=pulley",
                "ison=true,machinetype=hopper",
                "ison=false,machinetype=hopper",
                "ison=true,machinetype=turntable",
                "ison=false,machinetype=turntable",};
    }

    @SuppressWarnings("deprecation")
    @Override
    public Material getMaterial(IBlockState state) {
        switch (state.getValue(MACHINETYPE)) {
            case HOPPER:
            case PULLEY:
                setHarvestLevel("axe", 0, state);
                return Material.WOOD;
            default:
                setHarvestLevel("pickaxe", 0, state);
                return super.getMaterial(state);
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        switch (state.getValue(MACHINETYPE)) {
            case HOPPER:
            case PULLEY:
                return SoundType.WOOD;
            default:
                return super.getSoundType(state, world, pos, entity);
        }
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        return type == EnumType.MILL || type == EnumType.PULLEY || type == EnumType.TURNTABLE;
    }

    public int tickRateForMeta(int meta) {
        if(meta == 1)
            return 1;
        return 10;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        world.scheduleBlockUpdate(pos, this, tickRateForMeta(type.getMeta()), 5);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        BlockMechMachines.EnumType type = state.getValue(MACHINETYPE);
        return type.getMeta();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(MACHINETYPE).getSolidity();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(MACHINETYPE).getSolidity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                player.openGui(BWMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            } else {
                if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityTurntable && hand == EnumHand.MAIN_HAND) {
                    return ((TileEntityTurntable) world.getTileEntity(pos)).processRightClick(player);
                }
            }
            return true;
        }
    }

    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                return InvUtils.calculateComparatorLevel(tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            }
        }
        return 0;
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                InvUtils.ejectInventoryContents(world, pos, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
                world.updateComparatorOutputLevel(pos, this);
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean gettingPower = this.isInputtingMechPower(world, pos);
        boolean isOn = isMechanicalOn(world, pos);

        if (world.getTileEntity(pos) instanceof TileEntityTurntable) {
            if (!world.getGameRules().getBoolean("doDaylightCycle"))
                ((TileEntityTurntable) world.getTileEntity(pos)).toggleAsynchronous(null);
        }

        if (isOn != gettingPower)
            setMechanicalOn(world, pos, gettingPower);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        if (!isCurrentStateValid(world, pos)) {
            world.scheduleBlockUpdate(pos, this, tickRateForMeta(type.getMeta()), 5);
        }
        if (type == BlockMechMachines.EnumType.HOPPER) {
            ((TileEntityFilteredHopper) world.getTileEntity(pos)).outputBlocked = false;
        }
    }

    public boolean isCurrentStateValid(World world, BlockPos pos) {
        boolean gettingPower = isInputtingMechPower(world, pos);
        boolean isOn = isMechanicalOn(world, pos);
        return isOn == gettingPower;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return createTileEntity(world, this.getStateFromMeta(meta));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch (state.getValue(MACHINETYPE)) {
            case MILL:
                return new TileEntityMill();
            case PULLEY:
                return new TileEntityPulley();
            case HOPPER:
                return new TileEntityFilteredHopper();
            case TURNTABLE:
                return new TileEntityTurntable();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 6));
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        if (type != EnumType.TURNTABLE)
            return MechanicalUtil.isBlockPoweredByAxle(world, pos, this) || MechanicalUtil.isPoweredByCrank(world, pos);
        else
            return MechanicalUtil.isBlockPoweredByAxle(world, pos, this);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos,
                                       EnumFacing dir) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        switch (type) {
            case MILL:
                return dir == EnumFacing.UP || dir == EnumFacing.DOWN;
            case PULLEY:
                return dir != EnumFacing.DOWN && dir != EnumFacing.UP;
            case HOPPER:
                return dir != EnumFacing.UP && dir != EnumFacing.DOWN;
            case TURNTABLE:
                return dir == EnumFacing.DOWN;
        }
        return false;
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        if (!world.isRemote) {
            switch (type) {
                case MILL:
                    breakMill(world, pos);
                    break;
                case PULLEY:
                    breakPulley(world, pos);
                    break;
                case HOPPER:
                    breakHopper(world, pos);
                    break;
                case TURNTABLE:
                    breakTurntable(world, pos);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote) {
            BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
            if (type == BlockMechMachines.EnumType.HOPPER) {
                if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityFilteredHopper) {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb)
                        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);//world.markBlockForUpdate(pos);
                }
            }
        }
    }

    private void breakMill(World world, BlockPos pos) {
        if (MechanicalBreakage.millstone)
            InvUtils.ejectBrokenItems(world, pos, new ResourceLocation(BWMod.MODID, "block/mill"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    private void breakPulley(World world, BlockPos pos) {
        if (MechanicalBreakage.pulley)
            InvUtils.ejectBrokenItems(world, pos, new ResourceLocation(BWMod.MODID, "block/pulley"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    public void breakHopper(World world, BlockPos pos) {
        if (MechanicalBreakage.hopper)
            InvUtils.ejectBrokenItems(world, pos, new ResourceLocation(BWMod.MODID, "block/hopper"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    private void breakTurntable(World world, BlockPos pos) {
        if (MechanicalBreakage.turntable)
            InvUtils.ejectBrokenItems(world, pos, new ResourceLocation(BWMod.MODID, "block/turntable"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        boolean isOn = world.getBlockState(pos).getValue(ISACTIVE);
        if (type == BlockMechMachines.EnumType.MILL && isOn)
            updateMill(world, pos, rand);
    }

    public void updateMill(World world, BlockPos pos, Random rand) {
        if (isMechanicalOn(world, pos)) {
            emitSmoke(world, pos, rand, 5);
        }
    }

    private void emitSmoke(World world, BlockPos pos, Random rand, int heat) {
        for (int i = 0; i < heat; i++) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float fX = x + rand.nextFloat();
            float fY = y + rand.nextFloat() * 0.5F + 1.0F;
            float fZ = z + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fX, fY, fZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return null;
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isMechanicalOnFromState(world.getBlockState(pos));
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        if (isOn != world.getBlockState(pos).getValue(ISACTIVE)) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, isOn));
        }
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ISACTIVE);
    }

    public boolean isRedstonePowered(World world, BlockPos pos) {
        return world.isBlockIndirectlyGettingPowered(pos) > 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE, MACHINETYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(MACHINETYPE, EnumType.byMeta(meta >> 1)).withProperty(ISACTIVE,(meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(MACHINETYPE).getMeta() << 1 | (state.getValue(ISACTIVE) ? 1 : 0);
    }

    public enum EnumType implements IStringSerializable {
        MILL(0, "mill", true),
        PULLEY(2, "pulley", true),
        HOPPER(4, "hopper"),
        TURNTABLE(6, "turntable", true);

        private static final BlockMechMachines.EnumType[] META_LOOKUP = values();

        private int meta;
        private String name;
        private boolean solidity;

        EnumType(int meta, String name) {
            this(meta, name, false);
        }

        EnumType(int meta, String name, boolean solid) {
            this.meta = meta;
            this.name = name;
            this.solidity = solid;
        }

        public static BlockMechMachines.EnumType byMeta(int meta) {
            return META_LOOKUP[meta%META_LOOKUP.length];
        }

        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public boolean getSolidity() {
            return solidity;
        }
    }
}
