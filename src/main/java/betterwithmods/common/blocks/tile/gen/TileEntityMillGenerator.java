package betterwithmods.common.blocks.tile.gen;

import betterwithmods.common.BWSounds;
import betterwithmods.api.block.IMechanical;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.BlockMillGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityMillGenerator extends TileEntity implements ITickable, IMechanicalPower {
    //Every generator will take up a single block with no extended bounding box
    public int radius;
    public byte runningState = 0;
    public float speed = 0.0F;
    public float runningSpeed = 0.4F;
    public int overpowerTime = 30;

    public byte dyeIndex = 0;
    public float currentRotation = 0.0F;
    public float previousRotation = 0.0F;
    protected byte waterMod = 1;
    protected EnumFacing facing = null;

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("RunningState"))
            runningState = tag.getByte("RunningState");
        if (tag.hasKey("CurrentRotation"))
            currentRotation = tag.getFloat("CurrentRotation");
        if (tag.hasKey("RotationSpeed"))
            previousRotation = tag.getFloat("RotationSpeed");
        if (tag.hasKey("DyeIndex"))
            dyeIndex = tag.getByte("DyeIndex");
        if (tag.hasKey("Facing"))
            facing = EnumFacing.getFront(tag.getByte("Facing"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);

        t.setByte("RunningState", runningState);
        t.setFloat("CurrentRotation", currentRotation);
        t.setFloat("RotationSpeed", previousRotation);
        if (facing != null)
            t.setByte("Facing", (byte) facing.ordinal());
        return t;
    }

    public float getCurrentRotation() {
        return this.currentRotation;
    }

    public float getPrevRotation() {
        return this.previousRotation;
    }

    @Override
    public void update() {
        if (facing == null) {
            facing = ((BlockMillGenerator) getWorld().getBlockState(pos).getBlock()).getAxleDirectionFromState(getWorld().getBlockState(pos));
        }
        if (this.runningState != 0) {
            this.previousRotation = (this.runningState > 1 ? this.runningSpeed * 5 : this.runningSpeed) * this.waterMod;//this.currentRotation;
            this.currentRotation += (this.runningState > 1 ? 5 : this.runningState) * this.runningSpeed * this.waterMod;
            if (this.getWorld().rand.nextInt(100) == 0)
                this.getWorld().playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.75F, getWorld().rand.nextFloat() * 0.25F + 0.25F);
        }
        if (this.currentRotation >= 360.0F)
            this.currentRotation -= 360.0F;
        if (this.getWorld().getTotalWorldTime() % 20L == 0L && getWorld().getBlockState(pos).getBlock() instanceof BlockMillGenerator) {
            verifyIntegrity();
            updateSpeed();
            if (this.runningState == 2 && overpowerTime < 1) {
                overpower();
            } else if (this.runningState != 2 && overpowerTime != 30)
                overpowerTime = 30;
            else if (this.runningState == 2 && overpowerTime > 0)
                overpowerTime--;
        }
    }

    public EnumFacing getOrientation() {
        return facing;
    }

    public void setOrientation(EnumFacing facing) {
        if (facing != null) {
            if (facing.getAxisDirection() != EnumFacing.AxisDirection.POSITIVE)
                this.facing = facing.getOpposite();
            else
                this.facing = facing;
        }
    }

    public abstract void updateSpeed();

    public abstract void overpower();

    public abstract boolean isValid();

    public byte getRunningState() {
        return this.runningState;
    }

    public void setRunningState(int i) {
        boolean oldRun = this.getWorld().getBlockState(this.pos).getValue(BlockMillGenerator.ISACTIVE);
        boolean newRun = oldRun;
        this.runningState = (byte) i;
        if (runningState > 0)
            newRun = true;
        else if (runningState == 0)
            newRun = false;
        if (newRun != oldRun) {
            this.getWorld().setBlockState(this.pos, this.getWorld().getBlockState(pos).withProperty(BlockMillGenerator.ISACTIVE, newRun));
            getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);//this.getWorld().markBlockForUpdate(pos);
        }
    }

    public boolean verifyIntegrity() {
        return true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    //Unless you increase this, expect to see the TESR to pop in as you approach.
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return super.getMaxRenderDistanceSquared() * 3.0D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == MechanicalCapability.MECHANICAL_POWER || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == MechanicalCapability.MECHANICAL_POWER) {
            return MechanicalCapability.MECHANICAL_POWER.cast(this);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (getBlockType() instanceof IMechanical) {
            if (((IMechanical) getBlockType()).getMechPowerLevelToFacing(getWorld(), pos, facing) > 0)
                return 20;
        }
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public void readFromTag(NBTTagCompound tag) {

    }

    @Override
    public NBTTagCompound writeToTag(NBTTagCompound tag) {
        return null;
    }
}
