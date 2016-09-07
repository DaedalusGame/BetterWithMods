package betterwithmods.entity;

import betterwithmods.blocks.tile.TileEntityPulley;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mrebhan
 */

public class EntityExtendingRope extends Entity implements IEntityAdditionalSpawnData {

	private BlockPos pulley;
	private int targetY;
	private boolean up;

	public EntityExtendingRope(World worldIn) {
		this(worldIn, null, null, 0);
	}

	public EntityExtendingRope(World worldIn, BlockPos pulley, BlockPos source, int targetY) {
		super(worldIn);
		this.pulley = pulley;
		this.targetY = targetY;
		if (source != null) {
			this.up = source.getY() < targetY;
			this.setPositionAndUpdate(source.getX() + 0.5, source.getY(), source.getZ() + 0.5);
		}
		this.setSize(0.1F, 1F);
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		pulley = new BlockPos(compound.getInteger("pulleyX"), compound.getInteger("pulleyY"),
				compound.getInteger("pulleyZ"));
		targetY = compound.getInteger("targetY");
		up = compound.getBoolean("up");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("pulleyX", pulley.getX());
		compound.setInteger("pulleyY", pulley.getY());
		compound.setInteger("pulleyZ", pulley.getZ());
		compound.setInteger("targetY", targetY);
		compound.setBoolean("up", up);
	}

	@Override
	public void onUpdate() {
		// if (pulley == null && worldObj.isRemote)
		// return;

		this.noClip = true;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		this.moveEntity(0, this.motionY, 0);

		this.motionY = 0;
		if (up) {
			if (posY < targetY) {
				this.motionY = 0.1;
			} else {
				done();
			}
		} else {
			if (posY > targetY) {
				this.motionY = -0.1;
			} else {
				done();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	private void done() {
		if (!worldObj.isRemote) {
			TileEntity te = worldObj.getTileEntity(pulley);
			if (te instanceof TileEntityPulley) {
				TileEntityPulley pulley = (TileEntityPulley) te;
				pulley.onJobCompleted(up, targetY);
				this.setDead();
				return;
			}
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(pulley.getX());
		buffer.writeInt(pulley.getY());
		buffer.writeInt(pulley.getZ());
		buffer.writeInt(targetY);
		buffer.writeBoolean(up);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		pulley = new BlockPos(additionalData.readInt(), additionalData.readInt(), additionalData.readInt());
		targetY = additionalData.readInt();
		up = additionalData.readBoolean();
	}

	public int getTargetY() {
		return this.targetY;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

}
