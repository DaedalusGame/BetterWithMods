package betterwithmods.entity;

import java.nio.charset.Charset;

import javax.annotation.Nullable;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.tile.TileEntityPulley;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mrebhan
 */

public class EntityMovingPlatform extends Entity implements IEntityAdditionalSpawnData {

	private int targetY;
	private boolean up;
	private IBlockState blockState;
	private IBlockState onTop;

	public EntityMovingPlatform(World worldIn) {
		this(worldIn, null, 0, null, null);
	}

	public EntityMovingPlatform(World worldIn, BlockPos source, int targetY, IBlockState blockState,
			IBlockState onTop) {
		super(worldIn);
		this.setSizeAccordingToBlockState(blockState);
		this.targetY = targetY;
		if (source != null) {
			this.up = source.getY() < targetY;
			this.setPositionAndUpdate(source.getX() + 0.5, source.getY(), source.getZ() + 0.5);
		}
		this.blockState = blockState;
		this.onTop = onTop;
	}

	private void setSizeAccordingToBlockState(IBlockState blockState) {
		this.setSize(1, (blockState == null ? 1 : (blockState.getBlock() == BWRegistry.anchor ? 0.4F : 1F)));
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		targetY = compound.getInteger("targetY");
		up = compound.getBoolean("up");
		int i = compound.getByte("Data") & 255;
		this.blockState = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
		int j = compound.getByte("TopData") & 255;
		this.onTop = Block.getBlockFromName(compound.getString("TopBlock")).getStateFromMeta(j);
		setSizeAccordingToBlockState(blockState);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setInteger("targetY", targetY);
		compound.setBoolean("up", up);
		Block block = this.blockState != null ? this.blockState.getBlock() : Blocks.AIR;
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(block);
		compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
		compound.setByte("Data", (byte) block.getMetaFromState(this.blockState));
		Block top = this.onTop != null ? this.onTop.getBlock() : Blocks.AIR;
		ResourceLocation resourcelocation2 = (ResourceLocation) Block.REGISTRY.getNameForObject(top);
		compound.setString("TopBlock", resourcelocation2 == null ? "" : resourcelocation2.toString());
		compound.setByte("TopData", (byte) top.getMetaFromState(this.onTop));
	}

	@Override
	public void onUpdate() {

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

		worldObj.getEntitiesWithinAABBExcludingEntity(this,
				getEntityBoundingBox().offset(0, 0.25, 0).expand(0, 0.25, 0)).forEach(e -> {
					if (!(e instanceof EntityMovingPlatform) && !(e instanceof EntityExtendingRope)) {
						e.motionY = this.motionY;
					}
				});

		worldObj.getEntitiesWithinAABBExcludingEntity(this,
				getEntityBoundingBox().offset(0, -0.25, 0).expand(0, -0.25, 0)).forEach(e -> {
					if (!(e instanceof EntityMovingPlatform) && !(e instanceof EntityExtendingRope)) {
						e.setPosition(e.posX, getEntityBoundingBox().maxY + 0.01 + (up ? 0.24 : 0), e.posZ);
					}
				});

	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	private void done() {
		for (Entity e : worldObj.getEntitiesWithinAABBExcludingEntity(this,
				getEntityBoundingBox().offset(0, 0.5, 0).expand(0, 0.25, 0))) {
			if (!(e instanceof EntityMovingPlatform) && !(e instanceof EntityExtendingRope)) {
				e.setPosition(e.posX, getEntityBoundingBox().maxY + 0.02, e.posZ);
			}
		}

		BlockPos target = new BlockPos(posX - 0.5, targetY, posZ - 0.5);
		worldObj.setBlockState(target, blockState);
		if (onTop != null && onTop.getBlock() != Blocks.AIR)
			worldObj.setBlockState(target.up(), onTop);
		this.setDead();
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(targetY);
		buffer.writeBoolean(up);
		Block block = this.blockState != null ? this.blockState.getBlock() : Blocks.AIR;
		ResourceLocation resourcelocation = (ResourceLocation) Block.REGISTRY.getNameForObject(block);
		String data = resourcelocation == null ? "" : resourcelocation.toString();
		buffer.writeInt(data.length());
		buffer.writeBytes(data.getBytes(Charset.forName("UTF-8")));
		buffer.writeByte((byte) block.getMetaFromState(this.blockState));
		Block top = this.onTop != null ? this.onTop.getBlock() : Blocks.AIR;
		ResourceLocation resourcelocation2 = (ResourceLocation) Block.REGISTRY.getNameForObject(top);
		String topdata = resourcelocation2 == null ? "" : resourcelocation2.toString();
		buffer.writeInt(topdata.length());
		buffer.writeBytes(topdata.getBytes(Charset.forName("UTF-8")));
		buffer.writeByte((byte) top.getMetaFromState(this.onTop));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {
		targetY = additionalData.readInt();
		up = additionalData.readBoolean();
		int len = additionalData.readInt();
		byte[] bytes = new byte[len];
		additionalData.readBytes(bytes);
		String name = new String(bytes, Charset.forName("UTF-8"));
		int meta = additionalData.readByte();
		this.blockState = Block.getBlockFromName(name).getStateFromMeta(meta);
		int len2 = additionalData.readInt();
		byte[] bytes2 = new byte[len2];
		additionalData.readBytes(bytes2);
		String name2 = new String(bytes2, Charset.forName("UTF-8"));
		int meta2 = additionalData.readByte();
		this.onTop = Block.getBlockFromName(name2).getStateFromMeta(meta2);
		setSizeAccordingToBlockState(blockState);
	}

	public int getTargetY() {
		return this.targetY;
	}

	public IBlockState getBlockState() {
		return blockState;
	}

	public IBlockState getOnTop() {
		return onTop;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead && (blockState == null || blockState.getBlock() == BWRegistry.anchor
				|| blockState.getBlock() == TileEntityPulley.PLATFORM);
	}

	/**
	 * Returns the collision bounding box for this entity
	 */
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return canBeCollidedWith() ? this.getEntityBoundingBox() : null;
	}

}
