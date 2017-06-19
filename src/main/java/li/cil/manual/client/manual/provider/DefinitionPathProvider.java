package li.cil.manual.client.manual.provider;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockCookingPot;
import com.google.common.collect.Sets;
import li.cil.manual.api.manual.PathProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Created by primetoxinz on 6/18/17.
 */
public class DefinitionPathProvider implements PathProvider {
    public static Set<ItemStack> blacklist = Sets.newHashSet(ItemStack.EMPTY);

    BiFunction<World, IBlockState, String> customPath = (world, state) -> {
        Block block = state.getBlock();


        return null;
    };

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
        if (block == BWMBlocks.PLANTER)
            return "%LANGUAGE%/blocks/planter.md";
        if (block == BWMBlocks.BROKEN_GEARBOX)
            return "%LANGUAGE%/blocks/gearbox.md";
        if (block instanceof BlockCookingPot) {
            return "%LANGUAGE%/blocks/" + state.getValue(BlockCookingPot.TYPE).getName() + ".md";
        }
        String path = stack.getUnlocalizedName().replace("tile.bwm:", "");
        return "%LANGUAGE%/blocks/" + path + ".md";
    }
}
