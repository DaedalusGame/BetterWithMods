package betterwithmods.blocks.tile;

import betterwithmods.craft.TurntableCraft;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import betterwithmods.BWRegistry;
import betterwithmods.api.block.IBTWBlock;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.craft.TurntableInteraction;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTurntable extends TileEntity implements IMechSubtype, ITickable
{
	public byte timerPos = 0;
	private int potteryRotation = 0;
	private boolean potteryRotated = false;
	private static int[] ticksToRotate = {10, 20, 40, 80};
	private double[] offsets = {0.25D, 0.375D, 0.5D, 0.625D};
	private boolean asynchronous = false;
	private int rotationTime = 0;
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		if(tag.hasKey("SwitchSetting"))
		{
			this.timerPos = tag.getByte("SwitchSetting");
			if(this.timerPos > 3)
				this.timerPos = 3;
		}
		if(tag.hasKey("PotteryRotation"))
			this.potteryRotation = tag.getInteger("PotteryRotation");
		if(tag.hasKey("Asynchronous"))
			this.asynchronous = tag.getBoolean("Asynchronous");
		if(tag.hasKey("RotationTime"))
			this.rotationTime = tag.getInteger("RotationTime");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("PotteryRotation", this.potteryRotation);
		tag.setByte("SwitchSetting", this.timerPos);
		tag.setBoolean("Asynchronous", this.asynchronous);
		if(this.asynchronous || this.rotationTime != 0)
			tag.setInteger("RotationTime", this.rotationTime);
		return tag;
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		return oldState.getBlock() != newState.getBlock();
	}
	
	public void update()
	{
		if(this.worldObj.isRemote)
			return;
		else
		{
			if(worldObj.getBlockState(pos).getBlock() != null && worldObj.getBlockState(pos).getBlock() instanceof BlockMechMachines && ((BlockMechMachines)worldObj.getBlockState(pos).getBlock()).isMechanicalOn(worldObj, pos))
			{
				if(worldObj.getBlockState(pos).getValue(BlockMechMachines.SUBTYPE) != this.getSubtype())
					worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockMechMachines.SUBTYPE, this.getSubtype()));
				if(!asynchronous && worldObj.getWorldTime() % (long)ticksToRotate[timerPos] == 0)
				{
					this.worldObj.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
					rotateTurntable();
				}
				else if(asynchronous)
				{
					rotationTime++;
					if(rotationTime >= ticksToRotate[timerPos])
					{
						rotationTime = 0;
						this.worldObj.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
						rotateTurntable();
					}
				}
			}
		}
	}

	public boolean processRightClick(EntityPlayer player)
	{
		if(player.getHeldItem(EnumHand.MAIN_HAND) != null)
		{
			if(player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.CLOCK) {
				toggleAsynchronous(player);
				return true;
			}
		}
		else if(player.getHeldItemMainhand() == null && player.getHeldItemOffhand() == null)
		{
			advanceTimerPos();
			worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);
			worldObj.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
			return true;
		}
		return false;
	}

	public void toggleAsynchronous(EntityPlayer player)
	{
		if(!this.worldObj.getGameRules().getBoolean("doDaylightCycle"))
		{
			if(!asynchronous) {
				this.asynchronous = true;
			}
			else if(player != null)
			{
				player.addChatComponentMessage(new TextComponentTranslation("message.bwm:async.unavailable"));
			}
		}
		else {
			boolean isSneaking = player.isSneaking();
			String isOn = "enabled";
			boolean async = !this.asynchronous;
			if ((!async && !isSneaking) || (async && isSneaking))
				isOn = "disabled";
			player.addChatComponentMessage(new TextComponentTranslation("message.bwm:async." + isOn));
			if (!isSneaking) {
				this.worldObj.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.05F, 1.0F);
				this.asynchronous = async;
			}
		}
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
	
	public void rotateTurntable()
	{
		boolean reverse = ((BlockMechMachines)worldObj.getBlockState(pos).getBlock()).isRedstonePowered(worldObj, pos);
		
		this.potteryRotated = false;
		
		for(int tempY = 1; tempY < 3; tempY++)
		{
			BlockPos searchPos = pos.add(0, tempY, 0);
			boolean canTransmitHorizontally = canBlockTransmitRotationHorizontally(searchPos);
			boolean canTransmitVertically = canBlockTransmitRotationVertically(searchPos);
			rotateBlock(searchPos, reverse);
			
			if(canTransmitHorizontally)
				rotateBlocksAttached(searchPos, reverse);
			
			if(!canTransmitVertically)
				break;
		}
		
		if(!potteryRotated)
			potteryRotation = 0;
		
		worldObj.notifyBlockOfStateChange(pos, BWRegistry.singleMachines);
	}
	
	public byte getTimerPos()
	{
		return timerPos;
	}
	
	public double getOffset()
	{
		return offsets[this.timerPos];
	}
	
	public void advanceTimerPos()
	{
		timerPos++;
		if(timerPos > 3)
			timerPos = 0;
	}
	
	private boolean canBlockTransmitRotationHorizontally(BlockPos pos)
	{
		Block target = worldObj.getBlockState(pos).getBlock();
		
		if(target instanceof IBTWBlock)
		{
			return ((IBTWBlock)target).canRotateHorizontally(worldObj, pos);
		}
		if(target == Blocks.GLASS || target == Blocks.STAINED_GLASS)
			return true;
		if(target instanceof BlockPistonBase)
		{
			IBlockState state = worldObj.getBlockState(pos);

			if(!state.getValue(BlockPistonBase.EXTENDED))
				return true;
			return false;
		}
		if(target == Blocks.PISTON_EXTENSION || target == Blocks.PISTON_HEAD)
			return false;
		
		return this.worldObj.isBlockNormalCube(pos, false);
	}
	
	private boolean canBlockTransmitRotationVertically(BlockPos pos)
	{
		Block target = worldObj.getBlockState(pos).getBlock();
		
		if(target instanceof IBTWBlock)
		{
			return ((IBTWBlock)target).canRotateVertically(worldObj, pos);
		}
		if(target == Blocks.GLASS)
			return true;
		if(target == Blocks.CLAY)
			return false;
		if(target instanceof BlockPistonBase)
		{
			IBlockState state = worldObj.getBlockState(pos);

			if(!state.getValue(BlockPistonBase.EXTENDED))
				return true;
			return false;
		}
		if(target == Blocks.PISTON_EXTENSION || target == Blocks.PISTON_HEAD)
			return false;
		
		return this.worldObj.isBlockNormalCube(pos, false);
	}
	
	private void rotateBlocksAttached(BlockPos pos, boolean reverse)
	{
		Block[] newBlocks = new Block[4];
		for(int i = 0; i < 4; i++)
			newBlocks[i] = null;
		
		for(int i = 2; i < 6; i++)
		{
			BlockPos offset = pos.offset(EnumFacing.getFront(i));
			
			Block block = worldObj.getBlockState(offset).getBlock();
			IBlockState state = worldObj.getBlockState(offset);
			boolean attached = false;

			if(block instanceof BlockTorch)
			{
				EnumFacing facing = state.getValue(BlockTorch.FACING);
				if((facing == EnumFacing.getFront(i)))
				{
					attached = true;
					
					if(block == Blocks.UNLIT_REDSTONE_TORCH)
						block = Blocks.REDSTONE_TORCH;
				}
			}
			else if(block instanceof BlockLadder)
			{
				int meta = state.getValue(BlockLadder.FACING).getIndex();
				if(meta == i)
					attached = true;
			}
			else if(block == Blocks.WALL_SIGN)
			{
				int meta = state.getValue(BlockWallSign.FACING).getIndex();
				if(meta == i)
				{
					block.dropBlockAsItem(worldObj, offset, state, 0);
					this.worldObj.setBlockToAir(offset);
				}
			}
			else if(block instanceof BlockButton)
			{
				EnumFacing facing = state.getValue(BlockButton.FACING);
				if((facing == EnumFacing.getFront(i)))
				{
					block.dropBlockAsItem(worldObj, offset, state, 0);
					this.worldObj.setBlockToAir(offset);
				}
			}
			else if(block == Blocks.LEVER)
			{
				BlockLever.EnumOrientation facing = state.getValue(BlockLever.FACING);
				if(facing.getFacing() == EnumFacing.getFront(i))
				{
					block.dropBlockAsItem(worldObj, offset, state, 0);
					this.worldObj.setBlockToAir(offset);
				}
			}
			
			if(attached)
			{
				EnumFacing destFacing = DirUtils.rotateFacingAroundY(EnumFacing.getFront(i), reverse);
				newBlocks[destFacing.ordinal() - 2] = block;
				worldObj.setBlockToAir(offset);
			}
		}
		
		for(int i = 0; i < 4; i++)
		{
			Block block = newBlocks[i];
			
			if(block != null)
			{
				int facing = i + 2;
				int meta = 0;
				
				BlockPos offset = pos.offset(EnumFacing.getFront(facing));
				IBlockState state = worldObj.getBlockState(offset);
				if(block instanceof BlockTorch)
				{
					int targetFacing = 0;
					
					if(facing == 2)
						targetFacing = 4;
					else if(facing == 3)
						targetFacing = 3;
					else if(facing == 4)
						targetFacing = 2;
					else if(facing == 5)
						targetFacing = 1;
					meta = targetFacing;
					state = ((BlockTorch)block).getStateFromMeta(meta);
				}
				else if(block instanceof BlockLadder)
				{
					meta = facing;
					state = ((BlockLadder)block).getStateFromMeta(meta);
				}
				
				if(worldObj.getBlockState(offset).getBlock().isReplaceable(worldObj, offset))
					worldObj.setBlockState(offset, state);
				else
				{
					EnumFacing oldFacing = DirUtils.rotateFacingAroundY(EnumFacing.getFront(facing), !reverse);
					BlockPos oldPos = pos.offset(oldFacing);
					block.dropBlockAsItem(worldObj, oldPos, state, 0);
				}
			}
		}
	}
	
	private void rotateBlock(BlockPos pos, boolean reverse)
	{
		if(worldObj.isAirBlock(pos))
			return;
		
		IBlockState state = worldObj.getBlockState(pos);
		Block target = state.getBlock();
		int meta = target.getMetaFromState(state);
		Rotation rot = reverse ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;

		if(TurntableInteraction.contains(state) && TurntableInteraction.getProduct(state) != null)
		{
			if(target instanceof IBTWBlock) {
				IBTWBlock block = (IBTWBlock)target;
				if(block.canRotateOnTurntable(worldObj, pos))
					block.rotateAroundYAxis(worldObj, pos, reverse);
			}
			else if(state != state.withRotation(rot))
				worldObj.setBlockState(pos, state.withRotation(rot));
			rotateCraftable(state, TurntableInteraction.getProduct(state), pos, reverse);
			this.potteryRotated = true;
		}
		else if(target instanceof IBTWBlock)
		{
			IBTWBlock block = (IBTWBlock)target;
			
			if(block.canRotateOnTurntable(worldObj, pos))
				block.rotateAroundYAxis(worldObj, pos, reverse);
		}
		else
		{
			IBlockState newState = state.withRotation(rot);
			if(!(target instanceof BlockRailBase) && state != newState)
				worldObj.setBlockState(pos, newState);
			else if(target instanceof BlockRailBase)
			{
				BlockRailBase rail = (BlockRailBase)target;
				BlockRailBase.EnumRailDirection dir = state.getValue(rail.getShapeProperty());
				if(dir != BlockRailBase.EnumRailDirection.ASCENDING_NORTH && dir != BlockRailBase.EnumRailDirection.ASCENDING_EAST && dir != BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && dir != BlockRailBase.EnumRailDirection.ASCENDING_WEST)
				{
					if(state != newState)
						worldObj.setBlockState(pos, newState);
				}
			}
		}
	}

	private void rotateCraftable(IBlockState input, TurntableCraft craft, BlockPos pos, boolean reverse)
	{
		Block block = input.getBlock();
		this.potteryRotation++;
		if(this.potteryRotation > 7)
		{
			this.worldObj.playSound(null, pos, block.getSoundType(input, this.worldObj, pos, null).getPlaceSound(), SoundCategory.BLOCKS, 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.8F);
			if(!craft.getScrap().isEmpty() && craft.getScrap().size() > 0) {
				for(ItemStack scrap : craft.getScrap()) {
					InvUtils.ejectStackWithOffset(worldObj, pos.up(), scrap.copy());
				}
			}
			worldObj.setBlockState(pos, craft.getOutputState());
			this.potteryRotation = 0;
		}
	}

	@Override
	public int getSubtype() 
	{
		return this.timerPos + 8;
	}

	@Override
	public void setSubtype(int type)
	{
		this.timerPos = (byte)Math.min(type, 3);
	}
}
