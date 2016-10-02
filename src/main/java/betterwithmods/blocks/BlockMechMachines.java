package betterwithmods.blocks;

import betterwithmods.BWMod;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.blocks.tile.*;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
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
import java.util.List;
import java.util.Random;

public class BlockMechMachines extends BWMBlock implements IMechanicalBlock, ITileEntityProvider, IMultiVariants {
    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");
    public static final PropertyEnum<BlockMechMachines.EnumType> MACHINETYPE = PropertyEnum.create("machinetype", BlockMechMachines.EnumType.class);
    public static final PropertyInteger SUBTYPE = PropertyInteger.create("subtype", 0, 11);
    public static final PropertyInteger FILLEDSLOTS = PropertyInteger.create("filledslots", 0, 3);
    //Mill, Pulley, Crucible, Cauldron, Hopper, Turntable
    private static boolean keepInv;

    public BlockMechMachines() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState()
                .withProperty(MACHINETYPE, BlockMechMachines.EnumType.MILL)
                .withProperty(SUBTYPE, 0)
                .withProperty(FILLEDSLOTS, 0)
                .withProperty(ISACTIVE, false)
                .withProperty(DirUtils.FACING, EnumFacing.UP)
        );
    }

    @Override
    public String[] getVariants() {
        return new String[]{"facing=up,filledslots=0,ison=false,machinetype=mill,subtype=0",
                "facing=up,filledslots=0,ison=false,machinetype=pulley,subtype=0",
                "facing=up,filledslots=0,ison=false,machinetype=crucible,subtype=0",
                "facing=up,filledslots=0,ison=false,machinetype=cauldron,subtype=0",
                "facing=up,filledslots=0,ison=false,machinetype=hopper,subtype=0",
                "facing=up,filledslots=0,ison=false,machinetype=turntable,subtype=0",};
    }

    @SuppressWarnings("deprecation")
	@Override
    public Material getMaterial(IBlockState state) {
        switch (state.getValue(MACHINETYPE)) {
            case HOPPER:
            case PULLEY:
                return Material.WOOD;
            default:
            	return super.getMaterial(state);
        }
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        switch (state.getValue(MACHINETYPE)) {
            case HOPPER:
            case PULLEY:
                return SoundType.WOOD;
            case CRUCIBLE:
                return SoundType.GLASS;
            case CAULDRON:
                return SoundType.METAL;
            default:
                return super.getSoundType(state, world, pos, entity);
        }
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockMechMachines.EnumType type = world.getBlockState(pos).getValue(MACHINETYPE);
        if (type == BlockMechMachines.EnumType.MILL || type == BlockMechMachines.EnumType.PULLEY || type == BlockMechMachines.EnumType.TURNTABLE) {
            return true;
        }
        return false;
    }

    public int tickRateForMeta(int meta) {
        if (meta > 7)
            meta -= 8;
        switch (meta) {
            case 1:
            case 2:
            case 3:
                return 1;
            default:
                return 10;
        }
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            if (world.getTileEntity(pos) != null && world.getTileEntity(pos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                player.openGui(BWMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            } else {
                if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityTurntable) {
                    return ((TileEntityTurntable) world.getTileEntity(pos)).processRightClick(player);
                }
            }
            return true;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (!keepInv) {
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

        if (world.getTileEntity(pos) instanceof IMechSubtype) {
            IMechSubtype sub = (IMechSubtype) world.getTileEntity(pos);
            if (sub.getSubtype() != state.getValue(SUBTYPE))
                world.setBlockState(pos, state.withProperty(SUBTYPE, sub.getSubtype()));
        }
        if (world.getTileEntity(pos) instanceof TileEntityTurntable) {
            if (!world.getGameRules().getBoolean("doDaylightCycle"))
                ((TileEntityTurntable) world.getTileEntity(pos)).toggleAsynchronous(null);
        }

        if (isOn != gettingPower)
            setMechanicalOn(world, pos, gettingPower);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
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
            case CRUCIBLE:
                return new TileEntityCrucible();
            case CAULDRON:
                return new TileEntityCauldron();
            case HOPPER:
                return new TileEntityFilteredHopper();
            case TURNTABLE:
                return new TileEntityTurntable();
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 6; i++) {
            list.add(new ItemStack(item, 1, i));
        }
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
            case CRUCIBLE:
            case CAULDRON:
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
        InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/mill"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    private void breakPulley(World world, BlockPos pos) {
        InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/pulley"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    public void breakHopper(World world, BlockPos pos) {
        InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/hopper"));
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.3F, world.rand.nextFloat() * 0.1F + 0.45F);
        world.setBlockToAir(pos);
    }

    private void breakTurntable(World world, BlockPos pos) {
        InvUtils.ejectBrokenItems(world, pos, new ResourceLocation("betterwithmods", "block/turntable"));
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
        else if (!isOn && (type == BlockMechMachines.EnumType.CAULDRON || type == BlockMechMachines.EnumType.CRUCIBLE))
            updateCookingPot(world, pos, rand);
    }

    public void updateMill(World world, BlockPos pos, Random rand) {
        if (isMechanicalOn(world, pos)) {
            emitSmoke(world, pos, rand);
        }
    }

    private void emitSmoke(World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 5; i++) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            float fX = x + rand.nextFloat();
            float fY = y + rand.nextFloat() * 0.5F + 1.0F;
            float fZ = z + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, fX, fY, fZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void updateCookingPot(World world, BlockPos pos, Random rand) {
        if (!isMechanicalOn(world, pos)) {
            TileEntityCookingPot tile = (TileEntityCookingPot) world.getTileEntity(pos);
            int heat = tile.fireIntensity;
            if (heat > 4) {
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
        }
    }

    @Override
    public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public void setFacing(World world, BlockPos pos, EnumFacing facing) {

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
    public void rotateAroundYAxis(World world, BlockPos pos,
                                  boolean reverse) {

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
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        int subType = state.getValue(MACHINETYPE).getSubTypeCount();
        int actualType = 0;
        int filledSlots = 0;
        int facing = 1;
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            if (subType > 0 && tile instanceof IMechSubtype) {
                actualType = ((IMechSubtype) tile).getSubtype();
            }
            if (tile instanceof TileEntityVisibleInventory) {
                filledSlots = ((TileEntityVisibleInventory) tile).filledSlots();
            }
            if (tile instanceof TileEntityCookingPot) {
                facing = ((TileEntityCookingPot) tile).facing;
            }
        }
        return state.withProperty(SUBTYPE, actualType).withProperty(FILLEDSLOTS, filledSlots).withProperty(DirUtils.FACING, EnumFacing.getFront(facing));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE, MACHINETYPE, SUBTYPE, FILLEDSLOTS, DirUtils.FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = false;
        if (meta > 7) {
            isActive = true;
            meta -= 8;
        }
        return this.getDefaultState().withProperty(MACHINETYPE, BlockMechMachines.EnumType.byMeta(meta)).withProperty(ISACTIVE, isActive);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(MACHINETYPE).getMeta();
        meta += state.getValue(ISACTIVE) ? 8 : 0;
        return meta;
    }

    public static enum EnumType implements IStringSerializable {
        MILL(0, "mill", true),
        PULLEY(1, "pulley", true),
        CRUCIBLE(2, "crucible"),
        CAULDRON(3, "cauldron"),
        HOPPER(4, "hopper", 7, false),
        TURNTABLE(5, "turntable", 3, true);

        private static final BlockMechMachines.EnumType[] META_LOOKUP = new BlockMechMachines.EnumType[values().length];
        private int meta;
        private String name;
        private int subTypes;
        private boolean solidity;

        private EnumType(int meta, String name) {
            this(meta, name, 0, false);
        }

        private EnumType(int meta, String name, boolean solid) {
            this(meta, name, 0, solid);
        }

        private EnumType(int meta, String name, int numSubTypes, boolean solid) {
            this.meta = meta;
            this.name = name;
            this.subTypes = numSubTypes;
            this.solidity = solid;
        }

        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public int getSubTypeCount() {
            return this.subTypes;
        }

        public boolean getSolidity() {
            return solidity;
        }

        public static BlockMechMachines.EnumType byMeta(int meta) {
            if (meta > 7)
                meta -= 8;
            if (meta < 0 || meta >= META_LOOKUP.length)
                meta = 0;
            return META_LOOKUP[meta];
        }

        static {
            for (BlockMechMachines.EnumType machineTypes : values()) {
                META_LOOKUP[machineTypes.getMeta()] = machineTypes;
            }
        }
    }
}
