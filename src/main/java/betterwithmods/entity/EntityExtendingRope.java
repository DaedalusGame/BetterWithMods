package betterwithmods.entity;

import betterwithmods.BWMBlocks;
import betterwithmods.blocks.tile.TileEntityPulley;
import betterwithmods.config.BWConfig;
import betterwithmods.util.AABBArray;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author mrebhan
 */

public class EntityExtendingRope extends Entity implements IEntityAdditionalSpawnData {

    private BlockPos pulley;
    private int targetY;
    private boolean up;

    private Map<Vec3i, IBlockState> blocks;
    private AABBArray blockBB;

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
        this.blocks = new HashMap<>();
        this.blockBB = null;
        this.setSize(0.1F, 1F);
        this.ignoreFrustumCheck = true;
    }

    private static AxisAlignedBB createAABB(Vec3d part1, Vec3d part2) {
        return new AxisAlignedBB(part1.xCoord, part1.yCoord, part1.zCoord, part2.xCoord, part2.yCoord, part2.zCoord);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public float getEyeHeight() {
        return -1;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        if (blocks != null)
            updatePassengers(posY, y, false);
        super.setPosition(x, y, z);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        pulley = new BlockPos(compound.getInteger("PulleyX"), compound.getInteger("PulleyY"),
                compound.getInteger("PulleyZ"));
        targetY = compound.getInteger("TargetY");
        up = compound.getBoolean("Up");
        if (compound.hasKey("BlockData")) {
            byte[] bytes = compound.getByteArray("BlockData");
            ByteBuf buf = Unpooled.buffer(bytes.length);
            buf.writeBytes(bytes);
            blocks = deserializeBlockmap(buf);
        }
        rebuildBlockBoundingBox();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("PulleyX", pulley.getX());
        compound.setInteger("PulleyY", pulley.getY());
        compound.setInteger("PulleyZ", pulley.getZ());
        compound.setInteger("TargetY", targetY);
        compound.setBoolean("Up", up);
        ByteBuf buf = Unpooled.buffer();
        serializeBlockmap(buf, blocks);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        compound.setByteArray("BlockData", bytes);

        if (BWConfig.dumpBlockData) {
            for (int i = 0; i < bytes.length; i++) {
                if (i % 16 == 0) {
                    String text = Integer.toHexString(i);
                    while (text.length() < 8) {
                        text = "0" + text;
                    }
                    System.out.print("\n" + text + ": ");
                }
                String b = Integer.toHexString(Byte.toUnsignedInt(bytes[i]));
                while (b.length() < 2) {
                    b = "0" + b;
                }
                System.out.print(b);
                if (i % 2 == 1) {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }
    }

    private void serializeBlockmap(ByteBuf buf, Map<Vec3i, IBlockState> blocks) {
        // TODO: Maybe add TileEntity support? It would be cool if blocks
        // directly on top of the platform would be transported with it
        buf.writeInt(blocks.size());
        blocks.forEach((vec, state) -> {
            buf.writeInt(vec.getX());
            buf.writeInt(vec.getY());
            buf.writeInt(vec.getZ());
            Block block = state != null ? state.getBlock() : Blocks.AIR;
            ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
            String blockName = resourcelocation == null ? "" : resourcelocation.toString();
            buf.writeInt(blockName.length());
            buf.writeBytes(blockName.getBytes(Charset.forName("UTF-8")));
            buf.writeByte((byte) block.getMetaFromState(state));
        });
    }

    private Map<Vec3i, IBlockState> deserializeBlockmap(ByteBuf buf) {
        Map<Vec3i, IBlockState> map = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            Vec3i vec = new Vec3i(buf.readInt(), buf.readInt(), buf.readInt());
            int len = buf.readInt();
            byte[] bytes = new byte[len];
            buf.readBytes(bytes);
            String name = new String(bytes, Charset.forName("UTF-8"));
            int meta = buf.readByte();
            @SuppressWarnings("deprecation")
            IBlockState state = Block.getBlockFromName(name).getStateFromMeta(meta);
            map.put(vec, state);
        }
        return map;
    }

    private void rebuildBlockBoundingBox() {
        if (blocks == null || blocks.isEmpty()) {
            this.blockBB = null;
        } else {
            List<AxisAlignedBB> bbs = new ArrayList<>();
            bbs.add(new AxisAlignedBB(0.45, 0, 0.45, 0.55, 1, 0.55)); // rope
            // bounding
            // box
            for (Vec3i vec : blocks.keySet()) {
                bbs.add(new AxisAlignedBB(vec.getX(), vec.getY(), vec.getZ(), vec.getX() + 1,
                        vec.getY() + getBlockStateHeight(blocks.get(vec)), vec.getZ() + 1));
            }
            this.blockBB = new AABBArray(bbs.toArray(new AxisAlignedBB[0])).offset(-0.5, 0, -0.5);
        }
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

    public void updatePassengers(double posY, double newPosY, boolean b) {
        HashSet<Entity> entitiesInBlocks = new HashSet<>();
        HashSet<Entity> passengers = new HashSet<>();
        HashMap<Entity, Double> entMaxY = new HashMap<>();

        blocks.forEach((vec, state) -> {
            if (getBlockStateHeight(state) > 0) {
                Vec3d pos = new Vec3d(pulley.getX(), posY, pulley.getZ()).addVector(vec.getX(), vec.getY(), vec.getZ());
                getEntityWorld().getEntitiesWithinAABBExcludingEntity(this,
                        createAABB(pos, pos.addVector(1, getBlockStateHeight(state), 1))).forEach(e -> {
                    if (!(e instanceof EntityExtendingRope)) {
                        double targetY = pos.yCoord + getBlockStateHeight(state) - 0.01;
                        if (!entMaxY.containsKey(e) || entMaxY.get(e) < targetY) {
                            if ((!getEntityWorld().isRemote ^ e instanceof EntityPlayer) || b) {
                                entitiesInBlocks.add(e);
                                entMaxY.put(e, targetY);
                            }
                        }
                    }
                });
            }
        });

        getEntityWorld().getEntitiesWithinAABBExcludingEntity(this,
                AABBArray.toAABB(this.getEntityBoundingBox()).expand(0, 0.5, 0).offset(0, 0.5, 0)).forEach(e -> {
            if (!(e instanceof EntityExtendingRope)
                    && (!(e instanceof EntityPlayer) || !((EntityPlayer) e).capabilities.isFlying))
                passengers.add(e);
        });

        passengers.forEach(e -> e.moveEntity(null, 0, newPosY - posY, 0));
        passengers.forEach(e -> e.fallDistance = 0);
        entitiesInBlocks.forEach(e -> e.setPosition(e.posX, Math.max(e.posY, entMaxY.get(e) + newPosY - posY), e.posZ));
        entitiesInBlocks.forEach(e -> e.isAirBorne = false);
        entitiesInBlocks.forEach(e -> e.onGround = true);
        entitiesInBlocks.forEach(e -> e.motionY = Math.max(up ? 0 : -BWConfig.downSpeed, e.motionY));

    }

    private double getBlockStateHeight(IBlockState blockState) {
        return (blockState == null ? 1
                : (blockState.getBlock() == BWMBlocks.ANCHOR ? 0.375F
                : (blockState.getBlock() instanceof BlockRailBase || blockState.getBlock() instanceof BlockRedstoneWire ? 0 : 1)));
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    private boolean done() {
        if (!getEntityWorld().isRemote) {
            TileEntity te = getEntityWorld().getTileEntity(pulley);
            if (te instanceof TileEntityPulley) {
                TileEntityPulley pulley = (TileEntityPulley) te;
                if (!pulley.onJobCompleted(up, targetY, this)) {
                    BlockPos pos = this.pulley.down(this.pulley.getY() - targetY);
                    // rails need to be placed after all the other blocks
                    // blocks.forEach((vec, state) -> {
                    // if (!(state.getBlock() instanceof BlockRailBase)) {
                    // if (state.getBlock().)
                    // getEntityWorld().setBlockState(pos.add(vec), state, 3);
                    // }
                    // });
                    // blocks.forEach((vec, state) -> {
                    // if (state.getBlock() instanceof BlockRailBase) {
                    // getEntityWorld().setBlockState(pos.add(vec), state, 3);
                    // }
                    // });

                    int retries = 0;
                    while (!blocks.isEmpty() && retries < 10) {
                        retries++;
                        int skipped = 0;
                        for (Entry<Vec3i, IBlockState> entry : blocks.entrySet()) {
                            BlockPos blockPos = pos.add(entry.getKey());
                            if (entry.getValue().getBlock().canPlaceBlockAt(getEntityWorld(), blockPos)) {
                                getEntityWorld().setBlockState(blockPos, entry.getValue(), 3);
                                blocks.remove(entry.getKey());
                                skipped = 0;
                                break;
                            }
                            skipped++;
                        }
                        if (skipped == 0) {
                            retries = 0;
                        }
                    }

                    if (retries > 0) {
                        blocks.forEach((vec, state) -> state.getBlock().getDrops(getEntityWorld(), pos, state, 0).forEach(stack -> getEntityWorld()
                                .spawnEntityInWorld(new EntityItem(getEntityWorld(), posX, posY, posZ, stack))));
                    }

                    updatePassengers(posY, targetY + 0.25, true);
                    return true;
                }
            }
        }
        return false;
    }

    public void addBlock(Vec3i offset, IBlockState state) {
        blocks.put(offset, state);
        rebuildBlockBoundingBox();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(pulley.getX());
        buffer.writeInt(pulley.getY());
        buffer.writeInt(pulley.getZ());
        buffer.writeInt(targetY);
        buffer.writeBoolean(up);
        serializeBlockmap(buffer, blocks);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        pulley = new BlockPos(additionalData.readInt(), additionalData.readInt(), additionalData.readInt());
        targetY = additionalData.readInt();
        up = additionalData.readBoolean();
        blocks = deserializeBlockmap(additionalData);
    }

    public int getTargetY() {
        return this.targetY;
    }

    public void setTargetY(int i) {
        this.targetY = i;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public boolean getUp() {
        return up;
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return false;
    }

    public boolean isPathBlocked() {
        HashSet<BlockPos> blocked = new HashSet<>();
        blocks.forEach((vec, state) -> {
            if (blocked.isEmpty() && !up || state.getBlock() != BWMBlocks.ANCHOR) {
                BlockPos pos = this.pulley.down(this.pulley.getY() - targetY).add(vec);
                if (up)
                    pos = pos.up();
                else
                    pos = pos.down();

                Block b = getEntityWorld().getBlockState(pos).getBlock();

                if (!(b == Blocks.AIR || b.isReplaceable(getEntityWorld(), pos))) {
                    blocked.add(pos);
                }
            }
        });
        return !blocked.isEmpty();
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    public BlockPos getPulleyPosition() {
        return this.pulley;
    }

    public Map<Vec3i, IBlockState> getBlocks() {
        return blocks;
    }

    @Override
    protected void setSize(float width, float height) {
        if (blockBB == null)
            super.setSize(width, height);
    }

    @Override
    public void setEntityBoundingBox(AxisAlignedBB bb) {
        rebuildBlockBoundingBox();
        super.setEntityBoundingBox(blockBB != null ? blockBB.offset(this.posX, this.posY, this.posZ) : bb);
    }

    public AxisAlignedBB getBlockBoundingBox(Vec3i block, IBlockState state) {
        Vec3d pos = new Vec3d(pulley.getX(), posY, pulley.getZ()).addVector(block.getX(), block.getY(), block.getZ());
        return new AxisAlignedBB(pos, pos.addVector(1, getBlockStateHeight(state), 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getCollisionBoundingBox() {
        return canBeCollidedWith() ? (this.getEntityBoundingBox() instanceof AABBArray
                ? ((AABBArray) this.getEntityBoundingBox()).forEach(i -> i.setMaxY(i.maxY - 0.125))
                : this.getEntityBoundingBox()) : null;
    }

}
