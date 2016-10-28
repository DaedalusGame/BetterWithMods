package betterwithmods.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IDebarkable {
    ItemStack getBark(IBlockState state);
}
