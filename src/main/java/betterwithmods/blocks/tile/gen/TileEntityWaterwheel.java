package betterwithmods.blocks.tile.gen;

import betterwithmods.BWMBlocks;
import betterwithmods.api.block.IAxle;
import betterwithmods.blocks.BlockWaterwheel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityWaterwheel extends TileEntityMechGenerator {
    public TileEntityWaterwheel() {
        super();
        this.radius = 3;
        this.waterMod = 0;
    }

    public float getRotationState(float rotationTime) {
        if (this.runningState != 0)
            return this.currentRotation + this.runningState * this.speed * rotationTime * waterMod;
        return this.currentRotation;
    }

    @Override
    public boolean isValid() {
        boolean isAir = true;
        boolean hasWater = true;
        if (worldObj.getBlockState(pos) != null && worldObj.getBlockState(pos).getBlock() == BWMBlocks.WATERWHEEL) {
            IAxle axle = (IAxle) worldObj.getBlockState(pos).getBlock();
            int axis = axle.getAxisAlignment(worldObj, pos);
            for (int i = -2; i < 3; i++) {
                for (int j = -2; j < 3; j++) {
                    int xPos = (axis == 1 ? i : 0);
                    int yPos = j;
                    int zPos = (axis == 2 ? i : 0);
                    BlockPos offset = pos.add(xPos, yPos, zPos);
                    if (j == -2)
                        hasWater = (worldObj.getBlockState(offset).getBlock() instanceof BlockLiquid && worldObj.getBlockState(offset).getMaterial() == Material.WATER) || (worldObj.getBlockState(offset).getBlock() instanceof IFluidBlock && worldObj.getBlockState(offset).getMaterial() == Material.WATER);// == Blocks.water || worldObj.getBlock(xPos, yPos, zPos) == Blocks.flowing_water;//worldObj.isMaterialInBB(AxisAlignedBB.getBoundingBox(xPos, yPos, zPos, xPos + 1, yPos + 1, zPos + 1), Material.water);
                    if (!hasWater) {
                        hasWater = sidesHaveWater();
                        if (!hasWater)
                            break;
                    }
                    if (i == 0 && j == 0)
                        continue;
                    else if (j > -2) {
                        if (j != -2 && (i == -2 || i == 2)) {
                            isAir = worldObj.isAirBlock(offset) || ((worldObj.getBlockState(offset).getBlock() instanceof BlockLiquid && worldObj.getBlockState(offset).getMaterial() == Material.WATER) || (worldObj.getBlockState(offset).getBlock() instanceof IFluidBlock && worldObj.getBlockState(offset).getMaterial() == Material.WATER));
                            //if(!hasWater)
                            //hasWater = (worldObj.getBlock(xPos, yPos, zPos) instanceof BlockLiquid && worldObj.getBlock(xPos, yPos, zPos).getMaterial() == Material.water) || (worldObj.getBlock(xPos, yPos, zPos) instanceof IFluidBlock && worldObj.getBlock(xPos, yPos, zPos).getMaterial() == Material.water);
                        } else
                            isAir = worldObj.isAirBlock(offset);
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
        IAxle axle = (IAxle) worldObj.getBlockState(pos).getBlock();
        int axis = axle.getAxisAlignment(worldObj, pos);
        int leftWater = 0;
        int rightWater = 0;
        boolean bottomIsUnobstructed = true;
        for (int i = -2; i < 3; i++) {
            int xP1 = axis == 1 ? -2 : 0;
            int xP2 = axis == 1 ? 2 : 0;
            int zP1 = axis == 2 ? -2 : 0;
            int zP2 = axis == 2 ? 2 : 0;
            int xPos1 = xP1;
            int xPos2 = xP2;
            int yPos = i;
            int zPos1 = zP1;
            int zPos2 = zP2;
            BlockPos leftPos = pos.add(xPos1, yPos, zPos1);
            Block leftBlock = worldObj.getBlockState(leftPos).getBlock();
            BlockPos rightPos = pos.add(xPos2, yPos, zPos2);
            Block rightBlock = worldObj.getBlockState(rightPos).getBlock();
            if (leftBlock instanceof BlockLiquid || leftBlock instanceof IFluidBlock)
                leftWater++;
            else if (rightBlock instanceof BlockLiquid || rightBlock instanceof IFluidBlock)
                rightWater++;

            int xP = axis == 1 ? i : 0;
            int yP = -2;
            int zP = axis == 2 ? i : 0;
            BlockPos bPos = pos.add(xP, yP, zP);
            bottomIsUnobstructed = worldObj.isAirBlock(bPos) || ((worldObj.getBlockState(bPos).getBlock() instanceof BlockLiquid && worldObj.getBlockState(bPos).getMaterial() == Material.WATER) || (worldObj.getBlockState(bPos).getBlock() instanceof IFluidBlock && worldObj.getBlockState(bPos).getMaterial() == Material.WATER));
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
            IAxle axle = (IAxle) worldObj.getBlockState(pos).getBlock();
            int axis = axle.getAxisAlignment(worldObj, pos);
            int leftWater = 0;
            int rightWater = 0;
            for (int i = 0; i < 3; i++) {
                int metaPos = i - 1;
                int xP = axis == 1 ? metaPos : 0;
                int zP = axis == 2 ? metaPos : 0;
                BlockPos lowPos = pos.add(xP, -2, zP);
                //int meta = worldObj.getBlockState(lowPos).getBlock();
                if (worldObj.getBlockState(lowPos).getBlock() instanceof BlockLiquid) {
                    int meta = ((BlockLiquid) worldObj.getBlockState(lowPos).getBlock()).getMetaFromState(worldObj.getBlockState(lowPos));
                    waterMeta[i] = BlockLiquid.getLiquidHeightPercent(meta);
                } else if (worldObj.getBlockState(lowPos).getBlock() instanceof IFluidBlock) {
                    int[] opp = {2, 1, 0};
                    IFluidBlock fluid = (IFluidBlock) worldObj.getBlockState(lowPos).getBlock();
                    waterMeta[opp[i]] = fluid.getFilledPercentage(worldObj, lowPos);
                }
            }
            for (int i = -1; i < 3; i++) {
                int xP1 = axis == 1 ? -2 : 0;
                int xP2 = axis == 1 ? 2 : 0;
                int zP1 = axis == 2 ? -2 : 0;
                int zP2 = axis == 2 ? 2 : 0;
                BlockPos leftPos = pos.add(xP1, i, zP1);
                BlockPos rightPos = pos.add(xP2, i, zP2);
                Block leftBlock = worldObj.getBlockState(leftPos).getBlock();
                Block rightBlock = worldObj.getBlockState(rightPos).getBlock();
                if (leftBlock instanceof BlockLiquid || leftBlock instanceof IFluidBlock)
                    leftWater++;
                else if (rightBlock instanceof BlockLiquid || rightBlock instanceof IFluidBlock)
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
            this.worldObj.setBlockState(pos, this.worldObj.getBlockState(pos).withProperty(BlockWaterwheel.ISACTIVE, speed > 0));
            worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.worldObj.markBlockForUpdate(pos);
        }
    }

    @Override
    public void overpower() {
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
        IBlockState state = worldObj.getBlockState(this.pos);
        this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
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
        if (worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() instanceof BlockWaterwheel) {
            int xP = ((BlockWaterwheel) this.worldObj.getBlockState(pos).getBlock()).getAxisAlignment(worldObj, pos) == 1 ? radius : 0;
            int yP = radius;
            int zP = ((BlockWaterwheel) this.worldObj.getBlockState(pos).getBlock()).getAxisAlignment(worldObj, pos) == 2 ? radius : 0;
            return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
        } else
            return super.getRenderBoundingBox();
    }
}
