package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.api.block.ITurnable;
import betterwithmods.blocks.tile.TileEntityBlockDispenser;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.event.MobDropEvent;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryDefaulted;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBDispenser extends BlockDispenser implements ITurnable, IMultiVariants {
    public static final RegistryDefaulted<Item, IBehaviorDispenseItem> BLOCK_DISPENSER_REGISTRY = new RegistryDefaulted<>(new BehaviorDefaultDispenseBlock());

    public BlockBDispenser() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHardness(3.5F);
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
                if (!world.isAirBlock(check) && !world.getBlockState(check).getBlock().isReplaceable(world, check) && world.getBlockState(check).getBlockHardness(world, check) != -1.0F) {
                    IBlockState state = world.getBlockState(check);
                    List<ItemStack> stacks = state.getBlock().getDrops(world, check, state, 0);
                    if (stacks.isEmpty() && state.getBlock().canSilkHarvest(world, check, state, null))
                        stacks.add(state.getBlock().getPickBlock(state, null, world, check, MobDropEvent.player));
                    else if (!stacks.isEmpty()) {
                        for (ItemStack stack : stacks) {
                            if (ItemStack.areItemsEqual(stack, state.getBlock().getPickBlock(state, null, world, check, MobDropEvent.player))) {
                                stacks.remove(stack);
                                stacks.add(state.getBlock().getPickBlock(state, null, world, check, MobDropEvent.player));
                                break;
                            }
                        }
                    }
                    if (!stacks.isEmpty()) {
                        for (ItemStack stack : stacks)
                            tile.addStackToInventory(stack, check);
                    }
                    world.playSound(null, check, state.getBlock().getSoundType(state, world, pos, null).getPlaceSound(), SoundCategory.BLOCKS, 0.7F, 1.0F);
                    state.getBlock().breakBlock(world, check, state);
                    world.setBlockToAir(check);
                    return;
                }
                List<EntityLivingBase> creatures = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(check, check.add(1, 1, 1)), entity -> !entity.isDead && (entity instanceof EntityWolf || entity instanceof EntitySheep || entity instanceof EntityChicken));
                if (!creatures.isEmpty() && creatures.size() > 0) {
                    for (EntityLivingBase creature : creatures) {
                        if (creature instanceof EntityWolf) {
                            tile.addStackToInventory(new ItemStack(BWMBlocks.WOLF), check);
                            InvUtils.ejectStackWithOffset(world, check, new ItemStack(Items.STRING, 1 + rand.nextInt(3)));
                            world.playSound(null, check, SoundEvents.ENTITY_WOLF_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
                            creature.setDead();
                        } else if (creature instanceof EntitySheep) {
                            EntitySheep sheep = (EntitySheep) creature;
                            if (!sheep.getSheared() && sheep.getGrowingAge() > -1) {
                                List<ItemStack> shear = sheep.onSheared(new ItemStack(Items.SHEARS), world, check, 0);
                                for (ItemStack wool : shear) {
                                    tile.addStackToInventory(wool, check);
                                }
                                InvUtils.ejectStackWithOffset(world, check, new ItemStack(Items.STRING, 1 + rand.nextInt(2)));
                            }
                        } else if (creature instanceof EntityChicken) {
                            tile.addStackToInventory(new ItemStack(Items.EGG), check);
                            InvUtils.ejectStackWithOffset(world, check, new ItemStack(Items.FEATHER, 1 + rand.nextInt(2)));
                            world.playSound(null, check, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
                            creature.setDead();
                        }
                    }
                }
            } else {
                int index = tile.nextIndex;
                ItemStack stack = tile.getNextStackFromInv();
                if (index == -1 || stack == null)
                    world.playEvent(1001, pos, 0);
                else {
                    IBehaviorDispenseItem item = this.getBehavior(stack);
                    if (item != IBehaviorDispenseItem.DEFAULT_BEHAVIOR) {
                        ItemStack stack1 = item.dispense(impl, stack);
                        tile.inventory.setStackInSlot(index, stack1.func_190916_E() <= 0 ? ItemStack.field_190927_a : stack1);
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
}
