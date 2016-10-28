package betterwithmods.blocks.tile.gen;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.blocks.BlockAxle;
import betterwithmods.blocks.BlockWindmill;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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

public class TileEntityWindmillVertical extends TileEntityMechGenerator implements IColor {
    public int[] bladeMeta = {0, 0, 0, 0, 0, 0, 0, 0};

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
            IBlockState state = worldObj.getBlockState(this.pos);
            this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
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

    @Override
    public boolean isValid() {
        return isMasterValid();
    }

    public boolean isSlaveValid(int offset) {
        boolean notBlocked = true;
        int airCounter = 0;
        for (int x = -4; x < 5; x++) {
            for (int z = -4; z < 5; z++) {
                BlockPos offPos = pos.add(x, offset, z);
                if (x == 0 && z == 0) {
                    if (worldObj.getBlockState(offPos).getBlock() instanceof IAxle && ((IAxle) worldObj.getBlockState(offPos).getBlock()).getAxisAlignment(worldObj, offPos) == 0)
                        continue;
                }
                if (worldObj.provider.getDimensionType() == DimensionType.NETHER)
                    notBlocked = this.worldObj.isAirBlock(offPos);
                else if (worldObj.provider.getDimensionType() != DimensionType.NETHER) {
                    notBlocked = this.worldObj.isAirBlock(offPos);
                    if (worldObj.canBlockSeeSky(offPos))
                        airCounter++;
                }
                if (!notBlocked)
                    break;
            }
            if (!notBlocked)
                break;
        }
        if (worldObj.provider.getDimensionType() != DimensionType.NETHER)
            return notBlocked && airCounter > 25;
        return notBlocked;
    }

    public boolean isMasterValid() {
        boolean valid = true;
        if (worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() == BWMBlocks.WINDMILL_BLOCK) {
            for (int i = -3; i < 4; i++) {
                if (i == 0)
                    continue;
                valid = isSlaveValid(i);
                if (!valid)
                    break;
            }
        }
        return valid;
    }

    @Override
    public boolean verifyIntegrity() {
        return masterHasIntegrity();
    }

    public boolean masterHasIntegrity() {
        boolean integrity = true;
        {
            for (int offY = -3; offY < 4; offY++) {
                BlockPos offset = pos.add(0, offY, 0);
                if (offY == 0)
                    continue;
                integrity = worldObj.getBlockState(offset).getBlock() instanceof IAxle && ((IAxle) worldObj.getBlockState(offset).getBlock()).getAxisAlignment(worldObj, offset) == 0;
                if (!integrity) {
                    invalidateWindmill();
                    break;
                }
            }
        }
        return integrity;
    }

    private void invalidateWindmill() {
        this.worldObj.setBlockState(this.pos, BWMBlocks.WINDMILL_BLOCK.getDefaultState());
        for (int i = -3; i < 4; i++) {
            BlockPos pos = this.pos.add(0, i, 0);
            if (worldObj.getBlockState(pos).getBlock() instanceof BlockAxle)
                this.worldObj.setBlockState(pos, BWMBlocks.AXLE.getDefaultState());
        }
        if (!this.worldObj.isRemote)
            InvUtils.ejectStackWithOffset(worldObj, pos, new ItemStack(BWMItems.WINDMILL, 1, 2));
        this.worldObj.setBlockState(this.pos, BWMBlocks.AXLE.getDefaultState());
    }

    @Override
    public void overpower() {
        if (this.getBlockType() instanceof BlockWindmill) {
            BlockWindmill block = (BlockWindmill) this.worldObj.getBlockState(pos).getBlock();
            {
                int align = block.getAxisAlignment(this.worldObj, pos);
                EnumFacing[] dir = BlockAxle.facings[align];
                for (int i = 0; i < 2; i++) {
                    BlockPos offset = pos.offset(dir[i]);
                    Block axle = this.worldObj.getBlockState(offset).getBlock();
                    if (axle instanceof BlockAxle)
                        ((BlockAxle) axle).overpower(this.worldObj, offset);
                    else if (axle instanceof IMechanicalBlock && ((IMechanicalBlock) axle).canInputPowerToSide(worldObj, offset, dir[i].getOpposite()))
                        ((IMechanicalBlock) axle).overpower(this.worldObj, offset);
                }
            }
        }
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

    @Override
    public void updateSpeed() {
        byte speed = 0;
        if (this.isValid() && !isGalacticraftDimension() && isNotOtherDimension()) {
            if (this.worldObj.isRaining() || this.worldObj.isThundering())
                speed = 2;
            else
                speed = 1;
        }
        if (speed != this.runningState && worldObj.getBlockState(pos).getBlock() instanceof BlockWindmill) {
            this.setRunningState(speed);
            this.worldObj.setBlockState(pos, this.worldObj.getBlockState(pos).withProperty(BlockWindmill.ISACTIVE, speed > 0));
            worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.worldObj.markBlockForUpdate(pos);
        }
    }

    public boolean isNotOtherDimension() {
        return this.worldObj.provider.getDimensionType() != DimensionType.THE_END;
    }

    public boolean isGalacticraftDimension() {
        boolean isDimension = false;
        if (Loader.isModLoaded("GalacticraftCore")) {
            isDimension = false;//GalacticraftCompat.isGalacticraftDimension(this.worldObj);
        }
        return isDimension;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() instanceof BlockWindmill)
            return new AxisAlignedBB(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4);
        else
            return super.getRenderBoundingBox();
    }
}
