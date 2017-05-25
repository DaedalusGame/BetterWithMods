package betterwithmods.common.blocks;

import betterwithmods.BWMod;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.api.block.ITurnable;
import betterwithmods.api.tile.IBehaviorCollect;
import betterwithmods.api.tile.IBehaviorEntity;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.behaviors.BehaviorBreakBlock;
import betterwithmods.common.blocks.behaviors.BehaviorDefaultDispenseBlock;
import betterwithmods.common.blocks.behaviors.BehaviorEntity;
import betterwithmods.common.blocks.tile.TileEntityBlockDispenser;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBDispenser extends BlockDispenser implements ITurnable, IMultiVariants {
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> BLOCK_DISPENSER_REGISTRY = new RegistryDefaulted<>(new BehaviorDefaultDispenseBlock());
    public static final RegistryDefaulted<Block, IBehaviorCollect> BLOCK_COLLECT_REGISTRY = new RegistryDefaulted<>(new BehaviorBreakBlock());
    public static final RegistryDefaulted<Class<? extends Entity>, IBehaviorEntity> ENTITY_COLLECT_REGISTRY = new RegistryDefaulted<>(new BehaviorEntity());

    public BlockBDispenser() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHardness(3.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public String[] getVariants() {
        return new String[]{"facing=north,triggered=false"};
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;
        else {
            if (world.getTileEntity(pos) != null) {
                player.openGui(BWMod.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
    }

    private boolean isRedstonePowered(IBlockState state, World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing != state.getValue(BlockDirectional.FACING)) {
                BlockPos check = pos.offset(facing);
                if (world.getRedstonePower(check, facing) > 0)
                    return true;
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos other) {
        boolean flag = isRedstonePowered(state, world, pos);
        boolean flag1 = state.getValue(TRIGGERED);
        if (flag && !flag1) {
            world.scheduleUpdate(pos, this, this.tickRate(world));
            world.setBlockState(pos, state.withProperty(TRIGGERED, true), 5);
        } else if (!flag && flag1) {
            world.scheduleUpdate(pos, this, this.tickRate(world));
            world.setBlockState(pos, state.withProperty(TRIGGERED, false), 5);
        }
    }


    @Override
    protected void dispense(World world, BlockPos pos) {
        BlockSourceImpl impl = new BlockSourceImpl(world, pos);
        TileEntityBlockDispenser tile = impl.getBlockTileEntity();
        if (tile != null) {
            if (!world.getBlockState(pos).getValue(TRIGGERED)) {
                BlockPos check = pos.offset(impl.getBlockState().getValue(FACING));
                Block block = world.getBlockState(check).getBlock();
                if (world.getBlockState(check).getBlockHardness(world, check) < 0)
                    return;
                IBehaviorCollect behavior = BLOCK_COLLECT_REGISTRY.getObject(block);
                if (behavior != null) {
                    if(!world.isAirBlock(check) || !block.isReplaceable(world,check)) {
                        NonNullList<ItemStack> stacks = behavior.collect(new BlockSourceImpl(world, check));
                        InvUtils.insert(tile.inventory, stacks, false);
                    }
                }
                List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(check, check.add(1, 1, 1)), entity -> !entity.isDead);
                if (!entities.isEmpty()) {
                    IBehaviorEntity behaviorEntity = ENTITY_COLLECT_REGISTRY.getObject(entities.get(0).getClass());
                    NonNullList<ItemStack> stacks = behaviorEntity.collect(world, check, entities.get(0),tile.getCurrentSlot());
                    InvUtils.insert(tile.inventory, stacks, false);
                }
            } else {
                int index = tile.nextIndex;
                ItemStack stack = tile.getNextStackFromInv();
                if (index == -1 || stack.isEmpty())
                    world.playEvent(1001, pos, 0);
                else {
                    IBehaviorDispenseItem item = this.getBehavior(stack);
                    if (item != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
                        ItemStack stack1 = item.dispense(impl, stack);
                        tile.inventory.setStackInSlot(index, stack1);
                    }
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityBlockDispenser) {
            InvUtils.ejectInventoryContents(world, pos, ((TileEntityBlockDispenser) te).inventory);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    protected IBehaviorDispenseItem getBehavior(@Nullable ItemStack stack) {
        return BLOCK_DISPENSER_REGISTRY.getObject(stack == null ? null : stack.getItem());
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockDispenser();
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return null;
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return null;
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
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {

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
}


