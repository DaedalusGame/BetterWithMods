package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockWindmill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO almost need to completely rewrite these in terms of capabilities.
public class TileEntityWindmillVertical extends TileAxleGenerator implements IColor {
    public int[] bladeMeta = {0, 0, 0, 0, 0, 0, 0, 0};

    public int getBladeColor(int blade) {
        return bladeMeta[blade];
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
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
        if (dyeIndex > 7)
            dyeIndex = 0;
        return dyed;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        for (int i = 0; i < 8; i++) {
            if (tag.hasKey("Color_" + i))
                bladeMeta[i] = tag.getInteger("Color_" + i);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagCompound t = super.writeToNBT(tag);
        for (int i = 0; i < 8; i++) {
            t.setInteger("Color_" + i, bladeMeta[i]);
        }
        t.setByte("DyeIndex", this.dyeIndex);
        return t;
    }

    public boolean isSlaveValid(int offset) {
        int airCounter = 0;
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                BlockPos offPos = pos.add(x, offset, z);
                if (x == 0 && z == 0)
                    continue;
                if (getWorld().isAirBlock(offPos)) {
                    airCounter++;
                } else {
                    return false;
                }

            }
        }
        return airCounter > 25;
    }

    @Override
    public void verifyIntegrity() {
        boolean valid = false;
        if (getWorld().getBlockState(pos).getBlock() == BWMBlocks.WINDMILL) {
            for (int i = -3; i <= 3; i++) {
                if (i != 0) {
                    if (isSlaveValid(i)) {
                        valid = true;
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
        }

        isValid = valid && this.getWorld().canBlockSeeSky(pos);
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

    @Override
    public void calculatePower() {
        byte power;
        if (isValid() && (isOverworld() || isNether())) {
            if (world.isRaining()) {
                power = 2;
            } else if (world.isThundering()) {
                power = 3;
            } else {
                power = 1;
            }
        } else {
            power = 0;
        }
        if (power != this.power) {
            setPower(power);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getWorld().getBlockState(pos).getBlock() != null && getWorld().getBlockState(pos).getBlock() instanceof BlockWindmill)
            return new AxisAlignedBB(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4);
        else
            return super.getRenderBoundingBox();
    }
}
