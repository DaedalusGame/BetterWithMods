package li.cil.manual.client.manual.provider;

import betterwithmods.BWMod;
import com.google.common.collect.Sets;
import li.cil.manual.api.manual.PathProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by primetoxinz on 6/18/17.
 */
public class DefinitionPathProvider implements PathProvider {
    public static Set<ItemStack> blacklist = Sets.newHashSet(ItemStack.EMPTY);

    public static boolean isBlacklisted(ItemStack stack) {
        return blacklist.stream().anyMatch(s -> stack.isItemEqual(s));
    }

    @Nullable
    @Override
    public String pathFor(ItemStack stack) {
        if (isBlacklisted(stack) || !stack.getItem().getRegistryName().getResourceDomain().equals(BWMod.MODID))
            return null;
        String path = stack.getUnlocalizedName().replace("item.bwm:", "");
        return "%LANGUAGE/items/" + path + ".md";
    }

    @Nullable
    @Override
    public String pathFor(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int meta = block.damageDropped(state);
        ItemStack stack = new ItemStack(block, 1, meta);
        if (isBlacklisted(stack) || !stack.getItem().getRegistryName().getResourceDomain().equals(BWMod.MODID))
            return null;
        String path = stack.getUnlocalizedName().replace("tile.bwm:", "");

        return "%LANGUAGE%/blocks/" + path + ".md";
    }
}
