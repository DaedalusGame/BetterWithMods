package betterwithmods.common.blocks.tile.gen;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockWaterwheel;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWaterwheel extends TileEntityMillGenerator {
    public TileEntityWaterwheel() {
        super();
        this.radius = 3;
        this.waterMod = 0;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    public boolean isWater(BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.WATER;
    }
    @Override
    public boolean isValid() {
        boolean isAir = true;
        boolean hasWater = true;
        if (getWorld().getBlockState(pos) != null && getWorld().getBlockState(pos).getBlock() == BWMBlocks.WATERWHEEL) {
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWaterwheel.AXIS);
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    int xPos = (axis == EnumFacing.Axis.Z ? i : 0);
                    int zPos = (axis == EnumFacing.Axis.X ? i : 0);
                    BlockPos offset = pos.add(xPos, j, zPos);
                    if (j == -2)
                        hasWater = isWater(offset);
                    if (!hasWater) {
                        hasWater = sidesHaveWater();
                        if (!hasWater)
                            break;
                    }
                    if (i == 0 && j == 0)
                        continue;
                    else if (j > -2) {
                        if (i == -2 || i == 2) {
                            isAir = getWorld().isAirBlock(offset) || isWater(offset);
                        } else
                            isAir = getWorld().isAirBlock(offset);
                    }
                    if (!isAir)
                        break;
                }
                if (!isAir || !hasWater)
                    break;
            }
        }
        return isAir && hasWater;
    }

    public boolean sidesHaveWater() {
        EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWaterwheel.AXIS);
        int leftWater = 0;
        int rightWater = 0;
        boolean bottomIsUnobstructed = true;
        for (int i = -2; i < 3; i++) {
            int xP1 = axis == EnumFacing.Axis.Z ? -2 : 0;
            int xP2 = axis == EnumFacing.Axis.Z ? 2 : 0;
            int zP1 = axis == EnumFacing.Axis.X ? -2 : 0;
            int zP2 = axis == EnumFacing.Axis.X ? 2 : 0;
            BlockPos leftPos = pos.add(xP1, i, zP1);
            BlockPos rightPos = pos.add(xP2, i, zP2);
            if (isWater(leftPos))
                leftWater++;
            else if (isWater(rightPos))
                rightWater++;

            int xP = axis == EnumFacing.Axis.Z ? i : 0;
            int yP = -2;
            int zP = axis == EnumFacing.Axis.X ? i : 0;
            BlockPos bPos = pos.add(xP, yP, zP);
            bottomIsUnobstructed = getWorld().isAirBlock(bPos) || isWater(bPos);
            if (!bottomIsUnobstructed)
                break;
        }
        return bottomIsUnobstructed && (leftWater != 0 || rightWater != 0) && (leftWater < rightWater || leftWater > rightWater);
    }

    @Override
    public void updateSpeed() {
        int speed = 0;
        if (isValid()) {
            //speed = 1;
            float[] waterMeta = {0, 0, 0};
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWaterwheel.AXIS);
            int leftWater = 0;
            int rightWater = 0;
            for (int i = 0; i < 3; i++) {
                int metaPos = i - 1;
                int xP = axis == EnumFacing.Axis.Z ? metaPos : 0;
                int zP = axis == EnumFacing.Axis.X ? metaPos : 0;
                BlockPos lowPos = pos.add(xP, -2, zP);
                if (isWater(lowPos)) {
                    int meta = getWorld().getBlockState(lowPos).getBlock().getMetaFromState(getWorld().getBlockState(lowPos));
                    waterMeta[i] = BlockLiquid.getLiquidHeightPercent(meta);
                } /*else if (getWorld().getBlockState(lowPos).getBlock() instanceof IFluidBlock) {
                    int[] opp = {2, 1, 0};
                    IFluidBlock fluid = (IFluidBlock) getWorld().getBlockState(lowPos).getBlock();
                    waterMeta[opp[i]] = fluid.getFilledPercentage(getWorld(), lowPos);
                }*/
            }
            for (int i = -1; i < 3; i++) {
                int xP1 = axis == EnumFacing.Axis.Z ? -2 : 0;
                int xP2 = axis == EnumFacing.Axis.Z ? 2 : 0;
                int zP1 = axis == EnumFacing.Axis.X ? -2 : 0;
                int zP2 = axis == EnumFacing.Axis.X ? 2 : 0;
                BlockPos leftPos = pos.add(xP1, i, zP1);
                BlockPos rightPos = pos.add(xP2, i, zP2);
                if (isWater(leftPos))
                    leftWater++;
                else if (isWater(rightPos))
                    rightWater++;
            }
            if (leftWater > rightWater || (waterMeta[0] < waterMeta[1] && waterMeta[1] < waterMeta[2] && leftWater >= rightWater))
                waterMod = -1;
            else if (rightWater > leftWater || (waterMeta[0] > waterMeta[1] && waterMeta[1] > waterMeta[2] && rightWater >= leftWater))
                waterMod = 1;
            else {
                waterMod = 0;
            }
            if (waterMod != 0) {
                speed = 1;
            }
        }
        if (speed != this.runningState) {
            this.setRunningState(speed);
            this.getWorld().setBlockState(pos, this.getWorld().getBlockState(pos));
            getWorld().scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(getWorld()), 5);//this.getWorld().markBlockForUpdate(pos);
        }
    }

    @Override
    public void overpower() {
    }

    //Extend the bounding box if the TESR is bigger than the occupying block.
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (getWorld().getBlockState(pos).getBlock() != null && getWorld().getBlockState(pos).getBlock() instanceof BlockWaterwheel) {
            EnumFacing.Axis axis = getWorld().getBlockState(pos).getValue(BlockWaterwheel.AXIS);
            int xP = axis == EnumFacing.Axis.Z ? radius : 0;
            int yP = radius;
            int zP = axis == EnumFacing.Axis.X ? radius : 0;
            return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
        } else
            return super.getRenderBoundingBox();
    }
}
