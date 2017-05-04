package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.BlockStump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Koward
 */
public class ItemStumpRemover extends Item {
    public ItemStumpRemover() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
        this.maxStackSize = 16;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn != null) {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (!playerIn.canPlayerEdit(pos, facing, stack)) {
                return EnumActionResult.FAIL;
            } else if (stack.getCount() == 0) {
                return EnumActionResult.FAIL;
            } else {
                IBlockState state = worldIn.getBlockState(pos);
                if (state.getBlock() instanceof BlockStump) {
                    if (!worldIn.isRemote) {
                        worldIn.playSound(null, pos, SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1, 1);
                        worldIn.destroyBlock(pos, false);
                    }

                    stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.FAIL;
                }
            }
        }
        return EnumActionResult.FAIL;
    }
}
