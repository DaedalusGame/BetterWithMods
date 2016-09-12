package betterwithmods.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Optional;

import betterwithmods.util.item.ItemExt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityItemBuoy extends EntityItem {
	/**
	 * Wrapper around EntityItem.
	 * 
	 * @author Koward
	 * 
	 * @param orig
	 *            Original EntityItem object.
	 */
	public EntityItemBuoy(EntityItem orig) {
		super(orig.worldObj, orig.posX, orig.posY, orig.posZ, orig.getEntityItem());
		NBTTagCompound originalData = new NBTTagCompound();
		orig.writeEntityToNBT(originalData);
		this.readEntityFromNBT(originalData);

		String thrower = orig.getThrower();
		Entity entity = thrower == null ? null : orig.worldObj.getPlayerEntityByName(thrower);
		double tossSpeed = entity != null && entity.isSprinting() ? 2D : 1D;

		this.motionX = orig.motionX * tossSpeed;
		this.motionY = orig.motionY * tossSpeed;
		this.motionZ = orig.motionZ * tossSpeed;
	}

	public EntityItemBuoy(World world) {
		super(world);
	}

	/**
	 * Used to access {@link EntityItem.searchForOtherItemsNearby} as it's
	 * private.
	 */
	private void superSearchForOtherItemsNearby() {
		try {
			ReflectionHelper.findMethod(EntityItem.class, this,
					new String[] { "func_85054_d", "searchForOtherItemsNearby" }, new Class<?>[] {})
					.invoke(this, (Object[]) null);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void setAge(int age) {
		ReflectionHelper.setPrivateValue(EntityItem.class, this, age, new String[] { "field_70292_b", "age" });
	}

	private int getAge0() {
		return ReflectionHelper.getPrivateValue(EntityItem.class, this, new String[] { "field_70292_b", "age" });
	}

	private DataParameter<Optional<ItemStack>> getITEM() {
		return ReflectionHelper.getPrivateValue(EntityItem.class, this, new String[] { "field_184533_c", "ITEM" });
	}

	private int getPickupDelay() {
		return ReflectionHelper.getPrivateValue(EntityItem.class, this,
				new String[] { "field_145804_b", "delayBeforeCanPickup" });
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		ItemStack stack = this.getDataManager().get(getITEM()).orNull();
		if (stack != null && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this))
			return;
		if (this.getEntityItem() == null) {
			this.setDead();
		} else {
			// super.super.onUpdate() START
			if (!this.worldObj.isRemote) {
				this.setFlag(6, this.isGlowing());
			}
			this.onEntityUpdate();
			// super.super.onUpdate() END

			if (getPickupDelay() > 0 && getPickupDelay() != 32767) {
				setPickupDelay(getPickupDelay() - 1);
			}

			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;

			if (!this.func_189652_ae()) {
				this.motionY -= 0.03999999910593033D;
			}
			updateBuoy();

			this.noClip = this.pushOutOfBlocks(this.posX,
					(this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY
					|| (int) this.prevPosZ != (int) this.posZ;

			if (flag || this.ticksExisted % 25 == 0) {
				if (this.worldObj.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
					this.motionY = 0.20000000298023224D;
					this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
					this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
				}

				if (!this.worldObj.isRemote) {
					superSearchForOtherItemsNearby();
				}
			}

			float f = 0.98F;

			if (this.onGround) {
				f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX),
						MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1,
						MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;
			}

			this.motionX *= (double) f;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= (double) f;

			if (this.onGround) {
				this.motionY *= -0.5D;
			}

			if (this.getAge0() != -32768) {
				setAge(getAge0() + 1);
			}

			// this.handleWaterMovement();

			ItemStack item = this.getDataManager().get(getITEM()).orNull();

			if (!this.worldObj.isRemote && this.getAge0() >= lifespan) {
				int hook = net.minecraftforge.event.ForgeEventFactory.onItemExpire(this, item);
				if (hook < 0)
					this.setDead();
				else
					this.lifespan += hook;
			}
			if (item != null && item.stackSize <= 0) {
				this.setDead();
			}
		}
	}

	private void updateBuoy() {
		final byte maxIterations = 10;
		double waterAccumulator = 0.0D;
		final double offset = 0.1D;

		for (int i = 0; i < maxIterations; ++i) {
			double low = getEntityBoundingBox().minY
					+ (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (double) (i + 0) * 0.375D + offset;
			double high = getEntityBoundingBox().minY
					+ (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (double) (i + 1) * 0.375D + offset;
			AxisAlignedBB boundingBox = new AxisAlignedBB(getEntityBoundingBox().minX, low, getEntityBoundingBox().minZ,
					getEntityBoundingBox().maxX, high, getEntityBoundingBox().maxZ);

			if (!worldObj.isAABBInMaterial(boundingBox, Material.WATER)) {
				break;
			}

			waterAccumulator += 1.0D / (double) maxIterations;
		}

		if (waterAccumulator > 0.001D) {
			if (!isDrifted()) {
				float buoyancy = ItemExt.getBuoyancy(getEntityItem()) + 1.0F;
				motionY += 0.04D * (double) buoyancy * waterAccumulator;
			}

			motionX *= 0.9;
			motionY *= 0.9;
			motionZ *= 0.9;
		}

	}

	/**
	 * Check the non visible current between two water blocks for all blocks
	 * nearby entity.
	 * 
	 * @return
	 */
	private boolean isDrifted() {
		int minX = MathHelper.floor_double(getEntityBoundingBox().minX);
		int maxX = MathHelper.floor_double(getEntityBoundingBox().maxX + 1.0D);
		int minY = MathHelper.floor_double(getEntityBoundingBox().minY);
		int maxY = MathHelper.floor_double(getEntityBoundingBox().maxY + 1.0D);
		int minZ = MathHelper.floor_double(getEntityBoundingBox().minZ);
		int maxZ = MathHelper.floor_double(getEntityBoundingBox().maxZ + 1.0D);

		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z) {
					if (checkBlockDrifting(x, y, z))
						return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check the non visible current between two water blocks.
	 */
	private boolean checkBlockDrifting(int x, int y, int z) {
		for (int height = y - 1; height <= y + 1; height++) {
			IBlockState blockState = worldObj.getBlockState(new BlockPos(x, height, z));
			if (blockState.getBlock() == Blocks.FLOWING_WATER || blockState.getBlock() == Blocks.WATER) {
				int meta = blockState.getBlock().getMetaFromState(blockState);
				if (meta >= 8)
					return true;
			}
		}
		return false;
	}
}