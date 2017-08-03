package betterwithmods.common.blocks.mechanical.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockWindmill;
import betterwithmods.util.DirUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//TODO almost need to completely rewrite these in terms of capabilities.
public class TileEntityWindmillHorizontal extends TileAxleGenerator implements IColor {
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
            IBlockState state = getBlockWorld().getBlockState(this.pos);
            this.getBlockWorld().notifyBlockUpdate(this.pos, state, state, 3);
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
    public void calculatePower() {
        byte power;
        if (isValid() && isOverworld() || isNether()) {

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
    public void verifyIntegrity() {
        boolean valid = true;
        if (getBlockWorld().getBlockState(pos).getBlock() != null && getBlockWorld().getBlockState(pos).getBlock() == BWMBlocks.WINDMILL) {
            EnumFacing.Axis axis = getBlockWorld().getBlockState(pos).getValue(DirUtils.AXIS);
            for (int vert = -6; vert <= 6; vert++) {
                for (int i = -6; i <= 6; i++) {
                    int xP = (axis == EnumFacing.Axis.Z ? i : 0);
                    int zP = (axis == EnumFacing.Axis.X ? i : 0);
                    BlockPos offset = pos.add(xP, vert, zP);
                    if (xP == 0 && vert == 0 && zP == 0)
                        continue;
                    else
                        valid = getBlockWorld().isAirBlock(offset);
                    if (!valid)
                        break;
                }
                if (!valid)
                    break;
            }
        }
        isValid = valid && this.getBlockWorld().canBlockSeeSky(pos);
    }

    //Extend the bounding box if the TESR is bigger than the occupying block.
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getBlockWorld().getBlockState(pos).getBlock() != null && getBlockWorld().getBlockState(pos).getBlock() instanceof BlockWindmill) {
            EnumFacing.Axis axis = getBlockWorld().getBlockState(pos).getValue(DirUtils.AXIS);
            int xP = axis == EnumFacing.Axis.Z ? radius : 0;
            int yP = radius;
            int zP = axis == EnumFacing.Axis.X ? radius : 0;
            return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
        } else {
            return super.getRenderBoundingBox();
        }
    }
}
