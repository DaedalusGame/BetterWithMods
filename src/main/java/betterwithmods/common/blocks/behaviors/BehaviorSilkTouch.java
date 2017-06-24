package betterwithmods.common.blocks.behaviors;

import betterwithmods.api.tile.IBehaviorCollect;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tyler on 5/25/17.
 */
public class BehaviorSilkTouch implements IBehaviorCollect {

    private static final Method method = ReflectionHelper.findMethod(Block.class, "getSilkTouchDrop", "func_180643_i", IBlockState.class);
    public static ItemStack getBlockSilkTouchDrop(IBlockState state) {
        try {
            return (ItemStack) method.invoke(state.getBlock(), state);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public NonNullList<ItemStack> collect(IBlockSource source) {
        NonNullList<ItemStack> list = InvUtils.asNonnullList(getBlockSilkTouchDrop(source.getBlockState()));
        breakBlock(source.getWorld(),source.getBlockState(),source.getBlockPos());
        return list;
    }
}
