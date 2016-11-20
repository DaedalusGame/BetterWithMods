package betterwithmods.craft;

import betterwithmods.blocks.tile.SimpleItemStackHandler;
import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.items.ItemMaterial;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

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
    public static boolean attemptToCraft(int filterType,World world, BlockPos pos, EntityItem input) {
        for (HopperRecipe recipe : recipes) {
            if(recipe.isRecipe(filterType,input)) {
                recipe.craft(input,world,pos);
                return true;
            }
        }
        return false;
    }
    static {
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("ground_netherrack"),ItemMaterial.getMaterial("hellfire_dust")));
        recipes.add(new SoulUrn(ItemMaterial.getMaterial("soul_dust"),ItemMaterial.getMaterial("sawdust")));
        recipes.add(new HopperRecipe(5,new ItemStack(Blocks.GRAVEL),new ItemStack(Items.FLINT)) {
            @Override
            public void onCraft(World world, BlockPos pos, EntityItem item) {
                TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
                SimpleItemStackHandler inventory = tile.inventory;
                ItemStack stack = item.getEntityItem();
                int separate = world.rand.nextInt(stack.stackSize + 1);
                int redStack = stack.stackSize - separate;
                ItemStack redSand = new ItemStack(Blocks.SAND, redStack, 1);
                if (redStack != 0) {
                    EntityItem red = new EntityItem(world, item.lastTickPosX, item.lastTickPosY, item.lastTickPosZ, redSand);
                    if (!InvUtils.addItemStackToInv(inventory, red.getEntityItem())) {
                        red.setDefaultPickupDelay();
                        world.spawnEntityInWorld(red);
                    }
                }
                if (separate != 0) {
                    ItemStack sand = new ItemStack(Blocks.SAND, separate, 0);
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
    public static class SoulUrn extends HopperRecipe {
        public SoulUrn(ItemStack input, ItemStack output) {
            super(6,input,output);
        }
        @Override
        public void onCraft(World world, BlockPos pos, EntityItem item) {
            ((TileEntityFilteredHopper) world.getTileEntity(pos)).increaseSoulCount(item.getEntityItem().stackSize);
            if (!world.isRemote) {
                world.playSound(null, pos, SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
            }
            item.setDead();
        }
    }
    public static abstract class HopperRecipe {
        private int filterType;
        private ItemStack input;
        private List<ItemStack> output;
        public HopperRecipe(int filterType, ItemStack input, ItemStack... output) {
            this.filterType = filterType;
            this.input = input;
            this.output = Arrays.asList(output);
        }
        public boolean isRecipe(int filterType, EntityItem inputStack) {
            if(filterType == this.filterType) {
                if (inputStack != null) {
                    ItemStack i = inputStack.getEntityItem();

                    return i.getItem().equals(input.getItem()) && i.getMetadata() == input.getMetadata() && i.stackSize >= input.stackSize;
                }
                return false;
            }
            return false;
        }
        public void craft(EntityItem inputStack, World world, BlockPos pos) {
            for(int i = 0; i < inputStack.getEntityItem().stackSize; i++)
                InvUtils.ejectStackWithOffset(world,inputStack.getPosition(),output);
            onCraft(world,pos, inputStack);
        }
        public abstract void onCraft(World world, BlockPos pos, EntityItem item);
    }
}
