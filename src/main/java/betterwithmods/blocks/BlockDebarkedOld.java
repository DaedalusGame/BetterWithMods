package betterwithmods.blocks;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IDebarkable;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockDebarkedOld extends BlockOldLog implements IDebarkable
{
    public BlockDebarkedOld()
    {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setUnlocalizedName("bwm:debarkedOld");
    }

    @Override
    public ItemStack getBark(IBlockState state)
    {
        return new ItemStack(BWRegistry.bark, 1, this.damageDropped(state));
    }
}
