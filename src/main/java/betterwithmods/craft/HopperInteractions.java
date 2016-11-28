package betterwithmods.craft;

import betterwithmods.BWMBlocks;
import betterwithmods.blocks.tile.SimpleItemStackHandler;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.items.ItemMaterial;
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
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/13/16
 */
public class HopperInteractions {
    public static final ArrayList<HopperRecipe> recipes = new ArrayList<>();

    static {
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("ground_netherrack"), ItemMaterial.getMaterial("hellfire_dust")));
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("soul_dust"), ItemMaterial.getMaterial("sawdust")));
        recipes.add(new HopperRecipe(5, new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND, 1, 1)) {
            @Override
            public void craft(EntityItem inputStack, World world, BlockPos pos) {
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output);
                TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
                SimpleItemStackHandler inventory = tile.inventory;
                ItemStack sand = secondaryOutput.get(world.rand.nextInt(secondaryOutput.size())).copy();
                if (!InvUtils.addItemStackToInv(inventory, sand)) {
                    InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), sand);
                }
                onCraft(world, pos, inputStack);
            }
        });
        recipes.add(new HopperRecipe(6, new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE), ItemStack.EMPTY) {
            @Override
            public void onCraft(World world, BlockPos pos, EntityItem item) {
                TileEntityFilteredHopper hopper = (TileEntityFilteredHopper) world.getTileEntity(pos);
                int stackSize = hopper.soulsRetained;
                if (stackSize > item.getEntityItem().getCount())
                    stackSize = item.getEntityItem().getCount();
                hopper.soulsRetained -= stackSize;
                item.getEntityItem().shrink(stackSize);
                EntityItem soul = new EntityItem(world, item.lastTickPosX, item.lastTickPosY, item.lastTickPosZ, new ItemStack(Blocks.SOUL_SAND, stackSize));
                if (!InvUtils.addItemStackToInv(hopper.inventory, soul.getEntityItem())) {
                    soul.setDefaultPickupDelay();
                    world.spawnEntity(soul);
                }
                if (item.getEntityItem().getCount() < 1)
                    item.setDead();
            }
        });
    }

    public static boolean attemptToCraft(int filterType, World world, BlockPos pos, EntityItem input) {
        for (HopperRecipe recipe : recipes) {
            if (recipe.isRecipe(filterType, input)) {
                recipe.craft(input, world, pos);
                return true;
            }
        }
        return false;
    }

    public static class SoulUrn extends HopperRecipe {
        public SoulUrn(ItemStack input, ItemStack output) {
            super(6, input, output, new ItemStack(BWMBlocks.URN, 1, 8));
        }

        @Override
        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output);
            onCraft(world, pos, inputStack);
        }

        @Override
        public void onCraft(World world, BlockPos pos, EntityItem item) {
            ((TileEntityFilteredHopper) world.getTileEntity(pos)).increaseSoulCount(item.getEntityItem().getCount());
            if (!world.isRemote) {
                world.playSound(null, pos, SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
            }
            super.onCraft(world, pos, item);
        }
    }

    public static abstract class HopperRecipe {
        final ItemStack input;
        final ItemStack output;
        final List<ItemStack> secondaryOutput;
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
                    ItemStack i = inputStack.getEntityItem();
                    return i.getItem().equals(input.getItem()) && i.getMetadata() == input.getMetadata() && i.getCount() >= input.getCount();
                }
                return false;
            }
            return false;
        }

        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output);
            for (int i = 0; i < inputStack.getEntityItem().getCount(); i++)
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), secondaryOutput);
            onCraft(world, pos, inputStack);
        }

        public void onCraft(World world, BlockPos pos, EntityItem item) {
            item.getEntityItem().shrink(1);
            if (item.getEntityItem().getCount() <= 0)
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
    }
}
