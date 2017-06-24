package betterwithmods.common.registry;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

import static betterwithmods.common.items.ItemMaterial.EnumMaterial;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/13/16
 */
public class HopperInteractions {
    public static final ArrayList<HopperRecipe> RECIPES = new ArrayList<>();

    public static void addHopperRecipe(HopperRecipe recipe) {
        RECIPES.add(recipe);
    }
    static {

    }

    public static boolean attemptToCraft(int filterType, World world, BlockPos pos, EntityItem input) {
        for (HopperRecipe recipe : RECIPES) {
            if (recipe.isRecipe(filterType, input)) {
                if (recipe.canCraft(world, pos)) {
                    recipe.craft(input, world, pos);
                    return true;
                }
            }
        }
        return false;
    }

    public static class SoulUrnRecipe extends HopperRecipe {
        public SoulUrnRecipe(ItemStack input, ItemStack output) {
            super(6, input, output, new ItemStack(BWMBlocks.URN, 1, 8));
        }

        @Override
        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output.copy());
            onCraft(world, pos, inputStack);
        }

        @Override
        public void onCraft(World world, BlockPos pos, EntityItem item) {
            ((TileEntityFilteredHopper) world.getTileEntity(pos)).increaseSoulCount(1);
            if (!world.isRemote) {
                world.playSound(null, pos, SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
            }
            super.onCraft(world, pos, item);
        }

        @Override
        public boolean canCraft(World world, BlockPos pos) {
            return true;
        }
    }

    public static abstract class HopperRecipe {
        public final ItemStack input;
        public final ItemStack output;
        public final List<ItemStack> secondaryOutput;
        private final int filterType;

        public HopperRecipe(int filterType, ItemStack input, ItemStack output, ItemStack... secondaryOutput) {
            this.filterType = filterType;
            this.input = input;
            this.output = output;
            this.secondaryOutput = Lists.newArrayList(secondaryOutput);
        }

        public boolean isRecipe(int filterType, EntityItem inputStack) {
            if (filterType == this.filterType) {
                if (inputStack != null) {
                    ItemStack i = inputStack.getItem();
                    return InvUtils.matches(i,input);
                }
                return false;
            }
            return false;
        }

        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output.copy());
            for (int i = 0; i < inputStack.getItem().getCount(); i++)
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), secondaryOutput);
            onCraft(world, pos, inputStack);
        }

        public void onCraft(World world, BlockPos pos, EntityItem item) {
            item.getItem().shrink(1);
            if (item.getItem().getCount() <= 0)
                item.setDead();
        }

        public int getFilterType() {
            return filterType;
        }

        public ItemStack getInput() {
            return input;
        }

        public ItemStack getOutput() {
            return output;
        }

        public List<ItemStack> getSecondaryOutput() {
            return secondaryOutput;
        }

        public boolean canCraft(World world, BlockPos pos) {
            TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
            ItemStackHandler inventory = tile.inventory;
            boolean flag = true;
            if (!secondaryOutput.isEmpty()) {
                for (ItemStack stack : secondaryOutput) {
                    if (!InvUtils.insert(inventory, stack, true).isEmpty()) {
                        flag = false;
                        break;
                    }
                }
            }
            return flag;
        }
    }
}
