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

import java.util.ArrayList;
import java.util.List;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/13/16
 */
public class HopperInteractions {
    public static ArrayList<HopperRecipe> recipes = new ArrayList<>();

    static {
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("ground_netherrack"), ItemMaterial.getMaterial("hellfire_dust")));
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("soul_dust"), ItemMaterial.getMaterial("sawdust")));
        recipes.add(new HopperRecipe(5, new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND, 1, 1)) {
            @Override
            public void onCraft(World world, BlockPos pos, EntityItem item) {

                TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
                SimpleItemStackHandler inventory = tile.inventory;
                ItemStack stack = item.getEntityItem();
                int separate = world.rand.nextInt(stack.func_190916_E() + 1);
                int redStack = stack.func_190916_E() - separate;
                ItemStack redSand = getSecondaryOutput().get(1).copy();
                redSand.func_190920_e(separate);
                if (redStack != 0) {
                    EntityItem red = new EntityItem(world, item.lastTickPosX, item.lastTickPosY, item.lastTickPosZ, redSand);
                    if (!InvUtils.addItemStackToInv(inventory, red.getEntityItem())) {
                        red.setDefaultPickupDelay();
                        world.spawnEntityInWorld(red);
                    }
                }
                if (separate != 0) {
                    ItemStack sand = getSecondaryOutput().get(0).copy();
                    sand.func_190920_e(separate);
                    EntityItem reg = new EntityItem(world, item.lastTickPosX, item.lastTickPosY, item.lastTickPosZ, sand);
                    if (!InvUtils.addItemStackToInv(inventory, reg.getEntityItem())) {
                        reg.setDefaultPickupDelay();
                        world.spawnEntityInWorld(reg);
                    }
                }
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
        public void onCraft(World world, BlockPos pos, EntityItem item) {
            ((TileEntityFilteredHopper) world.getTileEntity(pos)).increaseSoulCount(item.getEntityItem().func_190916_E());
            if (!world.isRemote) {
                world.playSound(null, pos, SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
            }
            item.setDead();
        }
    }

    public static abstract class HopperRecipe {
        private int filterType;
        private ItemStack input;
        private ItemStack output;
        private List<ItemStack> secondaryOutput;

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
                    return i.getItem().equals(input.getItem()) && i.getMetadata() == input.getMetadata() && i.func_190916_E() >= input.func_190916_E();
                }
                return false;
            }
            return false;
        }

        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            for (int i = 0; i < inputStack.getEntityItem().func_190916_E(); i++)
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), secondaryOutput);
            onCraft(world, pos, inputStack);
        }

        public abstract void onCraft(World world, BlockPos pos, EntityItem item);

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
