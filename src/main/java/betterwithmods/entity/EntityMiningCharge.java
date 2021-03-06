package betterwithmods.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

import static net.minecraft.util.EnumFacing.*;

/**
 * Created by tyler on 9/5/16.
 */
public class EntityMiningCharge extends Entity {
    private static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(EntityMiningCharge.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FACING = EntityDataManager.<Integer>createKey(EntityMiningCharge.class, DataSerializers.VARINT);
    private EntityLivingBase igniter;
    /**
     * How long the fuse is
     */
    private int fuse;
    private EnumFacing facing;

    public EntityMiningCharge(World worldIn) {
        super(worldIn);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityMiningCharge(World worldIn, double x, double y, double z, EntityLivingBase igniter, EnumFacing facing) {
        this(worldIn);
        setFacing(facing);
        this.setPosition(x, y, z);
        this.setFuse(80);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.igniter = igniter;
        func_189654_d(true);
    }

    protected void entityInit() {
        this.dataManager.register(FUSE, Integer.valueOf(80));
        this.dataManager.register(FACING, Integer.valueOf(EnumFacing.NORTH.getIndex()));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.func_189652_ae())
        {
            this.motionY -= 0.03999999910593033D;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);


        --this.fuse;

        if (this.fuse <= 0) {
            this.setDead();

            if (!this.worldObj.isRemote) {
                this.explode();
            }
        } else {
            this.handleWaterMovement();
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    private void explode() {
        this.worldObj.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D, new int[0]);
        BlockPos pos = getPosition();
        EnumFacing facing = this.facing.getOpposite();
        for(int k = 0; k<=3;k++) {
            int dir = facing.getAxisDirection() == AxisDirection.POSITIVE ? 1 : -1;
            if(k < 3) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (facing == UP || facing == DOWN)
                            explodeBlock(worldObj, pos.add(i, dir*k, j));
                        else if (facing == NORTH || facing == SOUTH)
                            explodeBlock(worldObj, pos.add(i, j, dir*k));
                        else if (facing == EAST || facing == WEST)
                            explodeBlock(worldObj, pos.add(dir*k, i, j));
                    }
                }
            } else {
                if (facing == UP || facing == DOWN)
                    explodeBlock(worldObj, pos.add(0,dir*k,0));
                else if (facing == NORTH || facing == SOUTH)
                    explodeBlock(worldObj, pos.add(0,0, dir*k));
                else if (facing == EAST || facing == WEST)
                    explodeBlock(worldObj, pos.add(dir*k, 0, 0));
            }
        }
        List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class,new AxisAlignedBB(pos).expand(5,5,5));
        entities.stream().forEach(entity -> entity.attackEntityFrom(DamageSource.causeExplosionDamage(igniter),45f));
    }

    private void explodeBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        float resistance = state.getBlock().getExplosionResistance(world,pos,null,null);
        if(resistance < 100) {
            state.getBlock().dropBlockAsItem(world, pos, state, 0);
            state.getBlock().onBlockExploded(world, pos, new Explosion(world,igniter,posX,posY,posZ,0, Collections.emptyList()));
            world.setBlockToAir(pos);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Fuse", (short) this.getFuse());
        compound.setByte("Facing", (byte) this.getFacing().getIndex());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.setFuse(compound.getShort("Fuse"));
        this.setFacing(compound.getByte("Facing"));
    }

    /**
     * returns null or the entityliving it was placed or ignited by
     */
    public EntityLivingBase getTntPlacedBy() {
        return this.igniter;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public void setFuse(int fuseIn) {
        this.dataManager.set(FUSE, Integer.valueOf(fuseIn));
        this.fuse = fuseIn;
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        if (FUSE.equals(key)) {
            this.fuse = this.getFuseDM();
        }
        if (FACING.equals(key)) {
            this.facing = EnumFacing.getFront(getFacingDM());
        }
    }

    public void setFacing(EnumFacing facing) {
        this.dataManager.set(FACING, Integer.valueOf(facing.getIndex()));
        this.facing = facing;
    }

    public void setFacing(int facing ) {
        setFacing(EnumFacing.getFront(facing));
    }
    public int getFuseDM() {
        return ((Integer) this.dataManager.get(FUSE)).intValue();
    }

    public int getFacingDM() {
        return ((Integer) this.dataManager.get(FACING)).intValue();
    }

    public EnumFacing getFacing() {
        return facing;
    }
    public int getFuse() {
        return this.fuse;
    }
}