package betterwithmods.blocks.tile.gen;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.blocks.BlockAxle;
import betterwithmods.blocks.BlockWindmill;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWindmillHorizontal extends TileEntityMillGenerator implements IColor {
    public int[] bladeMeta = {0, 0, 0, 0};

    public TileEntityWindmillHorizontal() {
        super();
        this.radius = 7;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    public int getBladeColor(int blade) {
        return bladeMeta[blade];
    }

    @Override
    public int getColorFromBlade(int blade) {
        return bladeMeta[blade];
    }

    @Override
    public boolean dyeBlade(int dyeColor) {
        boolean dyed = false;
        if (bladeMeta[dyeIndex] != dyeColor) {
            bladeMeta[dyeIndex] = dyeColor;
            dyed = true;
            IBlockState state = getWorld().getBlockState(this.pos);
            this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
            this.markDirty();
        }
        dyeIndex++;
        if (dyeIndex > 3)
            dyeIndex = 0;
        return dyed;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < 4; i++) {
            if (tag.hasKey("Color_" + i))
                bladeMeta[i] = tag.getInteger("Color_" + i);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        for (int i = 0; i < 4; i++) {
            t.setInteger("Color_" + i, bladeMeta[i]);
        }
        t.setByte("DyeIndex", this.dyeIndex);
        return t;
    }

    @Override
    public void updateSpeed() {
        byte speed = 0;
        if (this.isValid() && !isGalacticraftDimension() && isNotOtherDimension()) {
            if (this.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && (this.getWorld().isRaining() || this.getWorld().isThundering()))
                speed = 2;
            else
                speed = 1;
        }
        if (speed != this.runningState || (speed == 0 && this.getWorld().getBlockState(pos).getValue(BlockWindmill.ISACTIVE))) {
            this.setRunningState(speed);
            this.getWorld().setBlockState(pos, this.getWorld().getBlockState(pos).withProperty(BlockWindmill.ISACTIVE, speed > 0));
            getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);//this.getWorld().markBlockForUpdate(pos);
        }
    }

    public boolean isNotOtherDimension() {
        return this.getWorld().provider.getDimensionType() != DimensionType.NETHER && this.getWorld().provider.getDimensionType() != DimensionType.THE_END;
    }

    public boolean isGalacticraftDimension() {
        boolean isDimension = false;
        if (Loader.isModLoaded("GalacticraftCore")) {
            isDimension = false;//GalacticraftCompat.isGalacticraftDimension(this.getWorld());
        }
        return isDimension;
    }

    @Override
    public void overpower() {
        if (this.getBlockType() instanceof BlockWindmill) {
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWindmill.AXIS);
            for (EnumFacing dir : EnumFacing.VALUES) {
                if (dir.getAxis() == axis) {
                    BlockPos offset = pos.offset(dir);
                    Block axle = this.getWorld().getBlockState(offset).getBlock();
                    if (axle instanceof BlockAxle)
                        ((BlockAxle) axle).overpower(this.getWorld(), offset);
                    else if (axle instanceof IMechanicalBlock && ((IMechanicalBlock) axle).canInputPowerToSide(getWorld(), offset, dir.getOpposite()))
                        ((IMechanicalBlock) axle).overpower(this.getWorld(), offset);
                }
            }
        }
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        if (getWorld().getBlockState(pos).getBlock() != null && getWorld().getBlockState(pos).getBlock() == BWMBlocks.WINDMILL_BLOCK) {
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWindmill.AXIS);
            for (int vert = -6; vert <= 6; vert++) {
                for (int i = -6; i <= 6; i++) {
                    int xP = (axis == EnumFacing.Axis.Z ? i : 0);
                    int zP = (axis == EnumFacing.Axis.X ? i : 0);
                    BlockPos offset = pos.add(xP, vert, zP);
                    if (xP == 0 && vert == 0 && zP == 0)
                        continue;
                    else
                        valid = getWorld().isAirBlock(offset);
                    if (!valid)
                        break;
                }
                if (!valid)
                    break;
            }
        }
        return valid && this.getWorld().canBlockSeeSky(pos);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = this.getUpdateTag();
        return new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
        IBlockState state = getWorld().getBlockState(this.pos);
        this.getWorld().notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    //Extend the bounding box if the TESR is bigger than the occupying block.
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getWorld().getBlockState(pos).getBlock() != null && getWorld().getBlockState(pos).getBlock() instanceof BlockWindmill) {
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWindmill.AXIS);
            int xP = axis == EnumFacing.Axis.Z ? radius : 0;
            int yP = radius;
            int zP = axis == EnumFacing.Axis.X ? radius : 0;
            return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
        } else
            return super.getRenderBoundingBox();
    }
}
