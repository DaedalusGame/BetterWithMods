package betterwithmods.items;

import betterwithmods.BWMBlocks;
import betterwithmods.blocks.BlockPlanter;
import betterwithmods.blocks.BlockPlanter.EnumPlanterType;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class ItemFertilizer extends Item
{
	public ItemFertilizer() {
		super();
		this.setCreativeTab(BWCreativeTabs.BWTAB);
	}
    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlockState(pos).getBlock();
        if(block != null && block instanceof IPlantable) {
            Block below = world.getBlockState(pos.down()).getBlock();
            if(processBlock(below, world, pos.down())) {
                if(!player.capabilities.isCreativeMode)
                    stack.stackSize--;
                return EnumActionResult.SUCCESS;
            }
        }
        else if(processBlock(block, world, pos)) {
            if(!player.capabilities.isCreativeMode)
                stack.stackSize--;
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    private boolean processBlock(Block block, World world, BlockPos pos)
    {
        if(block == Blocks.FARMLAND) {
            world.setBlockState(pos, BWMBlocks.FERTILE_FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)));
            world.playEvent(2005, pos.up(), 0);
            return true;
        }
        else if(block == BWMBlocks.PLANTER && block.getMetaFromState(world.getBlockState(pos)) == 1) {
            world.setBlockState(pos, BWMBlocks.PLANTER.getDefaultState().withProperty(BlockPlanter.TYPE, EnumPlanterType.FERTILE));
            world.playEvent(2005, pos.up(), 0);
            return true;
        }
        return false;
    }
}
