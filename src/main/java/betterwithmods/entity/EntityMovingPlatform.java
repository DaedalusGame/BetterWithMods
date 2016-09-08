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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author mrebhan
 */

public class EntityMovingPlatform extends Entity implements IEntityAdditionalSpawnData {

	private boolean up;
	private IBlockState blockState;
	private IBlockState onTop;
	private Vec3i offset;

	public EntityMovingPlatform(World worldIn) {
		this(worldIn, null, null, null, null);
	}

	public EntityMovingPlatform(World worldIn, Vec3i offset, EntityExtendingRope rope, IBlockState blockState,
			IBlockState onTop) {
		super(worldIn);
		this.setSizeAccordingToBlockState(blockState);
		if (rope != null) {
			this.up = rope.getUp();
		}
		this.blockState = blockState;
		this.onTop = onTop;
		this.offset = offset;
	}

	private void setSizeAccordingToBlockState(IBlockState blockState) {
		this.setSize(1, (blockState == null ? 1 : (blockState.getBlock() == BWRegistry.anchor ? 0.4F : 1F)));
	}

	@Override
	protected void entityInit() {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		int[] o = compound.getIntArray("Offset");
		offset = new Vec3i(o[0], o[1], o[2]);
		up = compound.getBoolean("Up");
		int i = compound.getByte("Data") & 255;
		this.blockState = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
		int j = compound.getByte("TopData") & 255;
		this.onTop = Block.getBlockFromName(compound.getString("TopBlock")).getStateFromMeta(j);
		setSizeAccordingToBlockState(blockState);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setIntArray("Offset", new int[] { offset.getX(), offset.getY(), offset.getZ() });
		compound.setBoolean("Up", up);
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

		if (getRidingEntity() == null) {
			this.setDead();
		}

		this.noClip = true;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().offset(0, 0.5, 0).expand(0, 0.5, 0))
				.forEach(e -> {
					if (!(e instanceof EntityMovingPlatform) && !(e instanceof EntityExtendingRope)) {
						e.setPosition(e.posX, getEntityBoundingBox().maxY + 0.01 + (up ? 0.24 : 0), e.posZ);
					}
				});

	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	public void done(BlockPos target) {
		for (Entity e : worldObj.getEntitiesWithinAABBExcludingEntity(this,
				getEntityBoundingBox().offset(0, 0.5, 0).expand(0, 0.25, 0))) {
			if (!(e instanceof EntityMovingPlatform) && !(e instanceof EntityExtendingRope)) {
				e.setPosition(e.posX, getEntityBoundingBox().maxY + 0.02, e.posZ);
			}
		}

		worldObj.setBlockState(target, blockState);
		if (onTop != null && onTop.getBlock() != Blocks.AIR)
			worldObj.setBlockState(target.up(), onTop);
		this.setDead();
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(offset.getX());
		buffer.writeInt(offset.getY());
		buffer.writeInt(offset.getZ());
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

	@SuppressWarnings("deprecation")
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		offset = new Vec3i(additionalData.readInt(), additionalData.readInt(), additionalData.readInt());
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

	public Vec3i getOffset() {
		return this.offset;
	}

}
