package betterwithmods.common.blocks.tile;

import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.BlockMechMachines;
import betterwithmods.common.registry.bulk.CraftingManagerMill;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMill extends TileBasicInventory implements ITickable, IMechanicalPower {
    public int grindCounter;
    private int grindType = 0;
    private boolean validateContents;
    private boolean containsIngredientsToGrind;
    private int powerLevel;
    private int counter;

    public TileEntityMill() {
        this.grindCounter = 0;
        this.validateContents = true;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (this.getWorld().isRemote)
            return;

        Block block = this.getWorld().getBlockState(this.pos).getBlock();

        if (block == null || !(block instanceof BlockMechMachines))
            return;

        BlockMechMachines mill = (BlockMechMachines) block;

        if (counter == 20) {
            powerLevel = getMechanicalInput(EnumFacing.DOWN);
            powerLevel = Math.min(powerLevel + getMechanicalInput(EnumFacing.UP), getMaximumInput(EnumFacing.UP));
            counter = 0;
        } else
            counter++;

        if (this.validateContents)
            validateContents();

        if (mill.isMechanicalOn(getWorld(), pos))
            if (getWorld().rand.nextInt(20) == 0)
                getWorld().playSound(null, pos, BWSounds.STONEGRIND, SoundCategory.BLOCKS, 0.5F + getWorld().rand.nextFloat() * 0.1F, 0.5F + getWorld().rand.nextFloat() * 0.1F);

        if (this.containsIngredientsToGrind && mill.isMechanicalOn(getWorld(), pos)) {
            if (!this.getWorld().isRemote) {
                if (grindType == 2) {
                    if (this.getWorld().rand.nextInt(25) < 2) {
                        getWorld().playSound(null, pos, SoundEvents.ENTITY_GHAST_HURT, SoundCategory.BLOCKS, 0.75F, getWorld().rand.nextFloat() * 0.4F + 0.8F);
                    }
                } else if (grindType == 3) {
                    if (this.getWorld().rand.nextInt(20) < 2)
                        getWorld().playSound(null, pos, SoundEvents.ENTITY_WOLF_HURT, SoundCategory.BLOCKS, 2.0F, (getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
            this.grindCounter += 1 + getGrindingBonus();
            if (this.grindCounter > 199) {
                grindContents();
                this.grindCounter = 0;
                this.validateContents = true;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("GrindCounter"))
            this.grindCounter = tag.getInteger("GrindCounter");
    }

    @Override
    public ItemStackHandler createItemStackHandler() {
        return new MillInventory(this);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("GrindCounter", this.grindCounter);
        return tag;
    }

    public int getGrindType() {
        return this.grindType;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            if (isCompanionCubeInInventory())
                this.getWorld().playSound(null, this.pos, SoundEvents.ENTITY_WOLF_WHINE, SoundCategory.BLOCKS, 0.5F, 1.0F);
            this.validateContents = true;
        }
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }


    private void ejectStack(ItemStack stack) {
        List<EnumFacing> validDirections = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState check = getWorld().getBlockState(pos.offset(facing));
            if (check.getBlock().isReplaceable(getWorld(), pos.offset(facing)) || getWorld().isAirBlock(pos.offset(facing)))
                validDirections.add(facing);
        }

        if (validDirections.isEmpty()) {
            IBlockState down = getWorld().getBlockState(pos.offset(EnumFacing.DOWN));
            if (down.getBlock().isReplaceable(getWorld(), pos.offset(EnumFacing.DOWN)) || getWorld().isAirBlock(pos.offset(EnumFacing.DOWN)))
                validDirections.add(EnumFacing.DOWN);
        }

        BlockPos offset;
        if (validDirections.size() > 1)
            offset = pos.offset(validDirections.get(getWorld().rand.nextInt(validDirections.size())));
        else if (validDirections.isEmpty())
            offset = pos.offset(EnumFacing.UP);
        else
            offset = pos.offset(validDirections.get(0));

        InvUtils.ejectStackWithOffset(getWorld(), offset, stack);
    }

    public int getGrindProgressScaled(int scale) {
        return this.grindCounter * scale / 200;
    }

    public boolean isGrinding() {
        return this.grindCounter > 0;
    }

    public boolean isCompanionCubeInInventory() {
        for (int i = 0; i < 3; i++) {
            if (this.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                Item item = this.inventory.getStackInSlot(i).getItem();
                if (item != null) {
                    if (item == Item.getItemFromBlock(BWMBlocks.WOLF))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean grindContents() {
        CraftingManagerMill mill = CraftingManagerMill.getInstance();
        List<Object> ingredients = mill.getValidCraftingIngredients(inventory);

        if (ingredients != null) {
            for (Object ingredient : ingredients) {
                if (ingredient instanceof ItemStack) {
                    ItemStack stack = ((ItemStack) ingredient).copy();
                    if (stack != ItemStack.EMPTY) {
                        Item item = stack.getItem();
                        if (item == Item.getItemFromBlock(BWMBlocks.WOLF)) {
                            this.getWorld().playSound(null, pos, SoundEvents.ENTITY_WOLF_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            break;
                        }
                    }
                }
            }

            ItemStack[] output = mill.craftItem(inventory);

            assert (output != null && output.length > 0);

            for (ItemStack anOutput : output) {
                ItemStack stack = anOutput.copy();
                if (stack != ItemStack.EMPTY)
                    ejectStack(stack);
            }
            return true;
        }
        return false;
    }

    private void validateContents() {
        int oldGrindType = getGrindType();
        int newGrindType = 0;
        List<Object> ingredients = CraftingManagerMill.getInstance().getValidCraftingIngredients(inventory);

        if (ingredients != null) {
            this.containsIngredientsToGrind = true;
            newGrindType = 1;
            for (Object ingredient : ingredients) {
                if (ingredient instanceof ItemStack) {
                    ItemStack stack = ((ItemStack) ingredient).copy();
                    if (stack != ItemStack.EMPTY) {
                        Item item = stack.getItem();
                        if (item == Item.getItemFromBlock(BWMBlocks.WOLF)) {
                            newGrindType = 3;
                            break;
                        }
                        if (item == Item.getItemFromBlock(Blocks.NETHERRACK)) {
                            newGrindType = 2;
                            break;
                        }
                    }
                }
            }
        } else {
            this.grindCounter = 0;
            this.containsIngredientsToGrind = false;
        }
        this.validateContents = false;
        if (oldGrindType != newGrindType) {
            this.grindType = newGrindType;
        }
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        int power = 0;
        if (getBlockType() instanceof IMechanicalBlock) {
            if (((IMechanicalBlock) getBlockType()).canInputPowerToSide(getWorld(), getPos(), facing)) {
                power = Math.min(MechanicalUtil.searchForAdvMechanical(getWorld(), getPos(), facing), getMaximumInput(facing));
            }
        }
        return power;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 10;
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {
        powerLevel = tag.getInteger("Power");
    }

    @Override
    public NBTTagCompound writeToTag(NBTTagCompound tag) {
        tag.setInteger("Power", powerLevel);
        return tag;
    }

    private int getGrindingBonus() {
        return powerLevel / 3;
    }

    private static class MillInventory extends ItemStackHandler {
        private TileEntity tile;
        public MillInventory(TileEntity tile) {
            super(3);
            this.tile = tile;
        }

        @Override
        public void onContentsChanged(int slot) {
            tile.markDirty();
        }
    }
}
