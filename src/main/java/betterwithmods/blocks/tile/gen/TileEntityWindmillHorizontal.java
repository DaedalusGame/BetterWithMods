package betterwithmods.blocks.tile.gen;

import betterwithmods.blocks.BlockWindmill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import betterwithmods.BWRegistry;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.blocks.BlockAxle;

public class TileEntityWindmillHorizontal extends TileEntityMechGenerator implements IColor
{
	public int[] bladeMeta = {0, 0, 0, 0};
	
	public TileEntityWindmillHorizontal()
	{
		super();
		this.radius = 7;
	}
	
	public int getBladeColor(int blade)
	{
		return bladeMeta[blade];
	}
	
	@Override
	public int getColorFromBlade(int blade)
	{
		return bladeMeta[blade];
	}

	@Override
	public boolean dyeBlade(int dyeColor)
	{
		boolean dyed = false;
		if(bladeMeta[dyeIndex] != dyeColor)
		{
			bladeMeta[dyeIndex] = dyeColor;
			dyed = true;
			IBlockState state = worldObj.getBlockState(this.pos);
			this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
			this.markDirty();
		}
		dyeIndex++;
		if(dyeIndex > 3)
			dyeIndex = 0;
		return dyed;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		for(int i = 0; i < 4; i++)
		{
			if(tag.hasKey("Color_" + i))
				bladeMeta[i] = tag.getInteger("Color_" + i);
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		NBTTagCompound t = super.writeToNBT(tag);
		for(int i = 0; i < 4; i++)
		{
			t.setInteger("Color_" + i, bladeMeta[i]);
		}
		t.setByte("DyeIndex", this.dyeIndex);
		return t;
	}

	@Override
	public void updateSpeed() 
	{
		byte speed = 0;
		if(this.isValid() && !isGalacticraftDimension() && isNotOtherDimension())
		{
			if(this.worldObj.provider.getDimensionType() == DimensionType.OVERWORLD && (this.worldObj.isRaining() || this.worldObj.isThundering()))
				speed = 2;
			else
				speed = 1;
		}
		if(speed != this.runningState)
		{
			this.setRunningState(speed);
			this.worldObj.setBlockState(pos, this.worldObj.getBlockState(pos).withProperty(BlockWindmill.ISACTIVE, speed > 0));
			worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);//this.worldObj.markBlockForUpdate(pos);
		}
	}

	public boolean isNotOtherDimension()
	{
		return this.worldObj.provider.getDimensionType() != DimensionType.NETHER && this.worldObj.provider.getDimensionType() != DimensionType.THE_END;
	}
	
	public boolean isGalacticraftDimension()
	{
		boolean isDimension = false;
		if(Loader.isModLoaded("GalacticraftCore"))
		{
			isDimension = false;//GalacticraftCompat.isGalacticraftDimension(this.worldObj);
		}
		return isDimension;
	}

	@Override
	public void overpower() 
	{
		BlockWindmill block = (BlockWindmill)this.worldObj.getBlockState(pos).getBlock();
		{
			int align = block.getAxisAlignment(this.worldObj, pos);
			EnumFacing[] dir = BlockAxle.facings[align];
			for(int i = 0; i < 2; i++)
			{
				BlockPos offset = pos.offset(dir[i]);
				Block axle = this.worldObj.getBlockState(offset).getBlock();
				if(axle instanceof BlockAxle)
					((BlockAxle)axle).overpower(this.worldObj, offset);
				else if(axle instanceof IMechanicalBlock && ((IMechanicalBlock)axle).canInputPowerToSide(worldObj, offset, dir[i].getOpposite()))
					((IMechanicalBlock)axle).overpower(this.worldObj, offset);
			}
		}
	}

	@Override
	public boolean isValid() 
	{
		boolean valid = true;
		if(worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() == BWRegistry.windmillBlock)
		{
			BlockWindmill axle = (BlockWindmill)this.worldObj.getBlockState(pos).getBlock();
			int axis = axle.getAxisAlignment(this.worldObj, pos);
			for(int vert = -6; vert <= 6; vert++)
			{
				for(int i = -6; i <= 6; i++)
				{
					int xP = axis == 1 ? i : 0;
					int zP = axis == 2 ? i : 0;
					int xPos = xP;
					int yPos = vert;
					int zPos = zP;
					BlockPos offset = pos.add(xPos, yPos, zPos);
					if(xP == 0 && yPos == 0 && zP == 0)
						continue;
					else
						valid = worldObj.isAirBlock(offset);
					if(!valid)
						break;
				}
				if(!valid)
					break;
			}
		}
		return valid && this.worldObj.canBlockSeeSky(pos);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = this.getUpdateTag();
		return new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), tag);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
		IBlockState state = worldObj.getBlockState(this.pos);
		this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	//Extend the bounding box if the TESR is bigger than the occupying block.
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		int x = pos.getX(); int y = pos.getY(); int z = pos.getZ();
		if(worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() instanceof BlockWindmill)
		{
			int xP = ((BlockWindmill)this.worldObj.getBlockState(pos).getBlock()).getAxisAlignment(worldObj, pos) == 1 ? radius : 0;
			int yP = radius;
			int zP = ((BlockWindmill)this.worldObj.getBlockState(pos).getBlock()).getAxisAlignment(worldObj, pos) == 2 ? radius : 0;
			return new AxisAlignedBB(x - xP - 1, y - yP - 1, z - zP - 1, x + xP + 1, y + yP + 1, z + zP + 1);
		}
		else
			return super.getRenderBoundingBox();
	}
}
