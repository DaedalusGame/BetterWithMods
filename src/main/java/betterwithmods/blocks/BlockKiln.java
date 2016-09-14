package betterwithmods.blocks;

import betterwithmods.BWRegistry;
import betterwithmods.craft.KilnInteraction;
import betterwithmods.craft.heat.BWMHeatRegistry;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Random;

public class BlockKiln extends BTWBlock
{
	public static final PropertyInteger COOK = PropertyInteger.create("cook", 0, 8);
	public BlockKiln()
	{
		super(Material.ROCK, "kiln", null);
		this.setTickRandomly(true);
		this.setHardness(2.0F);
		this.setResistance(10.0F);
	}
	
	@Override
	public int tickRate(World world)
	{
		return 20;
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		world.scheduleBlockUpdate(pos, this, 20, 5);
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		int oldCookTime = getCookCounter(world, pos);
		BlockPos down = pos.down();
		BlockPos up = pos.up();
		if(world.getBlockState(down).getBlock() != BWRegistry.stokedFlame)
		{
			Block block = world.getBlockState(down).getBlock();
			int meta = block.damageDropped(world.getBlockState(down));
		
			int auxFlames = 0;
			boolean intenseFlame = BWMHeatRegistry.contains(block, meta) && BWMHeatRegistry.get(block, meta).value > 4;
			
			if(!intenseFlame)
			{
				world.setBlockState(pos, world.getBlockState(pos).withProperty(COOK, 0));
			
				if(oldCookTime > 0)
					world.sendBlockBreakProgress(0, up, -1);
			
				return;
			}
		}
		
		int currentTickRate = 20;
		
		boolean canCook = false;
		Block above = world.getBlockState(up).getBlock();
		int aboveMeta = world.getBlockState(up).getBlock().damageDropped(world.getBlockState(up));
		if(!world.isAirBlock(up) && KilnInteraction.contains(above, aboveMeta))
		{
			if(checkKilnIntegrity(world, pos))
				canCook = true;
		}
		
		if(canCook)
		{
			int newCookTime = oldCookTime + 1;
			
			if(newCookTime > 7)
			{
				newCookTime = 0;
				cookBlock(world, pos.up());
				setCookCounter(world, pos, 0);
			}
			else
			{
				if(newCookTime > 0)
				{
					world.sendBlockBreakProgress(0, up, newCookTime + 2);
				}
				
				currentTickRate = calculateTickRate(world, pos);
			}
			
			setCookCounter(world, pos, newCookTime);
			
			if(newCookTime == 0)
			{
				world.sendBlockBreakProgress(0, up, -1);
				setCookCounter(world, pos, 0);
				world.scheduleBlockUpdate(pos, this, tickRate(world), 5);//world.markBlockForUpdate(pos);
			}
		}
		else if(oldCookTime != 0)
		{
			world.sendBlockBreakProgress(0, up, -1);
			setCookCounter(world, pos, 0);
			world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
		}
		
		world.scheduleBlockUpdate(pos, this, currentTickRate, 5);
	}
	
	private int calculateTickRate(IBlockAccess world, BlockPos pos)
	{
		int secondaryFire = 0;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for(int xP = -1; xP < 2; xP++)
		{
			for(int zP = -1; zP < 2; zP++)
			{
				if(xP != 0 || zP != 0)
				{
					BlockPos bPos = pos.add(xP, -1, zP);
					if(BWMHeatRegistry.contains(world.getBlockState(bPos).getBlock(), world.getBlockState(bPos).getBlock().damageDropped(world.getBlockState(bPos))))
					{
						if(BWMHeatRegistry.get(world.getBlockState(bPos).getBlock(), world.getBlockState(bPos).getBlock().damageDropped(world.getBlockState(bPos))).value > 4)
							secondaryFire++;
					}
				}
			}
		}
		int tickRate = 60 * (8 - secondaryFire) / 8 + 20;
		return tickRate;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if(getCookCounterFromMeta(world.getBlockState(pos).getValue(COOK)) > 0)
			world.sendBlockBreakProgress(0, pos.up(), -1);
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if(Loader.isModLoaded("terrafirmacraft"))
			return Item.getItemFromBlock(GameRegistry.findBlock("terrafirmacraft", "FireBrick"));
		return Item.getItemFromBlock(Blocks.BRICK_BLOCK);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block)
	{
		int cookTime = getCookCounter(world, pos);
		BlockPos above = pos.up();
		BlockPos below = pos.down();
		IBlockState aboveBlock = world.getBlockState(above);
		IBlockState belowBlock = world.getBlockState(below);
		if(cookTime > 0)
		{
			if(!KilnInteraction.contains(aboveBlock.getBlock(), aboveBlock.getBlock().damageDropped(aboveBlock)))
			{
				if(!BWMHeatRegistry.contains(belowBlock.getBlock(), belowBlock.getBlock().damageDropped(belowBlock)) || (BWMHeatRegistry.contains(belowBlock.getBlock(), belowBlock.getBlock().damageDropped(state)) && BWMHeatRegistry.get(belowBlock.getBlock(), belowBlock.getBlock().damageDropped(belowBlock)).value < 5))
				{
					if(getCookCounter(world, pos) > 0)
					{
						world.sendBlockBreakProgress(0, above, -1);
						setCookCounter(world, pos, 0);
					}
				}
			}
		}
		
		world.scheduleBlockUpdate(pos, this, 20, 5);
	}
	
	@Override
	public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public boolean canRotateVertically(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	public int getCookCounterFromMeta(int meta)
	{
		return meta & 0x7;
	}
	
	public int getCookCounter(IBlockAccess world, BlockPos pos)
	{
		return getCookCounterFromMeta(world.getBlockState(pos).getValue(COOK));
	}
	
	public void setCookCounter(World world, BlockPos pos, int cookCounter)
	{
		world.setBlockState(pos, world.getBlockState(pos).withProperty(COOK, cookCounter));
	}
	
	private void cookBlock(World world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		int meta = world.getBlockState(pos).getBlock().damageDropped(world.getBlockState(pos));
		if(block != null)
		{
			if(KilnInteraction.contains(block, meta))
			{
				List<ItemStack> result = KilnInteraction.getProduct(block, meta);
				InvUtils.ejectStackWithOffset(world, pos, result);
				world.setBlockToAir(pos);
			}
		}
	}
	
	private boolean checkKilnIntegrity(IBlockAccess world, BlockPos pos)
	{
		int brickCount = 0;
		for(int i = 1; i < 6; i++)
		{
			Block block = world.getBlockState(pos).getBlock();
			
			if(block == Blocks.BRICK_BLOCK || block == BWRegistry.kiln)
				brickCount++;
			else if(Loader.isModLoaded("terrafirmacraft") && block == GameRegistry.findBlock("terrafirmacraft", "FireBrick"))
				brickCount++;
		}
		return brickCount > 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COOK, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COOK);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, COOK);
	}
}
