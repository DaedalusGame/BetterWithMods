package betterwithmods.blocks.tile.gen;

import betterwithmods.BWSounds;
import betterwithmods.blocks.BlockGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TileEntityMechGenerator extends TileEntity implements ITickable
{
	//Every generator will take up a single block with no extended bounding box
	public int radius;
	public byte runningState = 0;
	public float speed = 0.0F;
	public float runningSpeed = 0.4F;
	public int overpowerTime = 30;
	
	public byte dyeIndex = 0;
	
	protected byte waterMod = 1;
	
	public float currentRotation = 0.0F;
	public float previousRotation = 0.0F;
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		if(tag.hasKey("RunningState"))
			runningState = tag.getByte("RunningState");
		if(tag.hasKey("CurrentRotation"))
			currentRotation = tag.getFloat("CurrentRotation");
		if(tag.hasKey("RotationSpeed"))
			previousRotation = tag.getFloat("RotationSpeed");
		if(tag.hasKey("DyeIndex"))
			dyeIndex = tag.getByte("DyeIndex");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		
		t.setByte("RunningState", runningState);
		t.setFloat("CurrentRotation", currentRotation);
		t.setFloat("RotationSpeed", previousRotation);

		return t;
	}
	
	public float getCurrentRotation()
	{
		return this.currentRotation;
	}
	
	public float getPrevRotation()
	{
		return this.previousRotation;
	}
	
	@Override
	public void update()
	{
		if(this.runningState != 0)
		{
			this.previousRotation = (this.runningState > 1 ?  this.runningSpeed * 5 : this.runningSpeed) * this.waterMod;//this.currentRotation;
			this.currentRotation += (this.runningState > 1 ? 5 : this.runningState) * this.runningSpeed * this.waterMod;
			if(this.worldObj.rand.nextInt(100) == 0)
				this.worldObj.playSound(null, pos, BWSounds.WOODCREAK, SoundCategory.BLOCKS, 0.75F, worldObj.rand.nextFloat() * 0.25F + 0.25F);
		}
		if(this.currentRotation >= 360.0F)
			this.currentRotation -= 360.0F;
		if(this.worldObj.getWorldTime() % 20L == 0L && worldObj.getBlockState(pos).getBlock() instanceof BlockGen)
		{
			verifyIntegrity();
			updateSpeed();
			if(this.runningState == 2 && overpowerTime < 1)
			{
				overpower();
			}
			else if(this.runningState != 2 && overpowerTime != 30)
				overpowerTime = 30;
			else if(this.runningState == 2 && overpowerTime > 0)
				overpowerTime--;
		}
	}
	
	public abstract void updateSpeed();
	
	public abstract void overpower();
	
	public abstract boolean isValid();
	
	public byte getRunningState()
	{
		return this.runningState;
	}
	
	public boolean verifyIntegrity()
	{
		return true;
	}

	public void setRunningState(int i)
	{
		boolean oldRun = this.worldObj.getBlockState(this.pos).getValue(BlockGen.ISACTIVE);
		boolean newRun = oldRun;
		this.runningState = (byte)i;
		if(runningState > 0)
			newRun = true;
		else if(runningState == 0)
			newRun = false;
		if(newRun != oldRun)
		{
			this.worldObj.setBlockState(this.pos, this.worldObj.getBlockState(pos).withProperty(BlockGen.ISACTIVE, newRun));
			worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.worldObj.markBlockForUpdate(pos);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	//Unless you increase this, expect to see the TESR to pop in as you approach.
	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return super.getMaxRenderDistanceSquared() * 3.0D;
	}
}
