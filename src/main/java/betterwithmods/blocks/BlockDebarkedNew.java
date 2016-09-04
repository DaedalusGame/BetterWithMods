package betterwithmods.blocks;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IDebarkable;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockDebarkedNew extends BlockNewLog implements IDebarkable
{
    public BlockDebarkedNew()
    {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setUnlocalizedName("bwm:debarkedNew");
    }

    @Override
    public ItemStack getBark(IBlockState state)
    {
        return new ItemStack(BWRegistry.bark, 1, 4 + this.damageDropped(state));
    }
}
