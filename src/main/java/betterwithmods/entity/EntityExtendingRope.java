package betterwithmods.entity;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.tile.TileEntityPulley;
import betterwithmods.config.BWConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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
		pulley = new BlockPos(compound.getInteger("PulleyX"), compound.getInteger("PulleyY"),
				compound.getInteger("PulleyZ"));
		targetY = compound.getInteger("TargetY");
		up = compound.getBoolean("Up");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("PulleyX", pulley.getX());
		compound.setInteger("PulleyY", pulley.getY());
		compound.setInteger("PulleyZ", pulley.getZ());
		compound.setInteger("TargetY", targetY);
		compound.setBoolean("Up", up);
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (up) {
			if (posY > targetY) {
				if (done())
					return;
			}
		} else {
			if (posY < targetY) {
				if (done())
					return;
			}
		}

		this.setPosition(pulley.getX() + 0.5, this.posY + (up ? BWConfig.upSpeed : -BWConfig.downSpeed),
				pulley.getZ() + 0.5);
	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	private boolean done() {
		if (!worldObj.isRemote) {
			TileEntity te = worldObj.getTileEntity(pulley);
			if (te instanceof TileEntityPulley) {
				TileEntityPulley pulley = (TileEntityPulley) te;
				if (!pulley.onJobCompleted(up, targetY, this)) {
					getPassengers().forEach(p -> ((EntityMovingPlatform) p).done(this.pulley
							.down(this.pulley.getY() - targetY).add(((EntityMovingPlatform) p).getOffset())));
					return true;
				}
			}
		}
		return false;
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

	public boolean getUp() {
		return up;
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return passenger instanceof EntityMovingPlatform;
	}

	@Override
	public double getMountedYOffset() {
		return (double) this.height * 0.75D;
	}

	@Override
	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger) && passenger instanceof EntityMovingPlatform) {
			EntityMovingPlatform platform = (EntityMovingPlatform) passenger;
			Vec3i offset = platform.getOffset();
			platform.setPosition(this.posX + offset.getX(), this.posY + offset.getY(), this.posZ + offset.getZ());
		}
	}

	public void setTargetY(int i) {
		this.targetY = i;
	}

	public boolean isPathBlocked() {
		for (Entity e : getPassengers()) {
			if (e instanceof EntityMovingPlatform) {
				EntityMovingPlatform platform = (EntityMovingPlatform) e;
				if (platform.getBlockState().getBlock() == BWRegistry.anchor && up)
					continue;
				BlockPos pos = this.pulley.down(this.pulley.getY() - targetY)
						.add(((EntityMovingPlatform) platform).getOffset());
				if (up)
					pos = pos.up();
				else
					pos = pos.down();

				Block b = worldObj.getBlockState(pos).getBlock();

				if (!(b == Blocks.AIR || b.isReplaceable(worldObj, pos))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean writeToNBTOptional(NBTTagCompound compound) {
		return false;
	}

	public BlockPos getPulleyPosition() {
		return this.pulley;
	}
	
}
