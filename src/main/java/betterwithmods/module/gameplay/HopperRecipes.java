package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.blocks.BlockRope;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.HopperFilters;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.block.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by primetoxinz on 6/23/17.
 */
public class HopperRecipes extends Feature {
    public HopperRecipes() {
        canDisable = false;
    }

    private static boolean isItem(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            return block instanceof BlockRope || block instanceof BlockBush || block instanceof BlockTorch || block instanceof BlockSand || block instanceof BlockGravel || BWOreDictionary.isOre(stack, "treeSapling");
        }
        return true;
    }

    private static boolean isDust(ItemStack stack) {
        return BWOreDictionary.listContains(stack, OreDictionary.getOres("sand")) ||
                BWOreDictionary.listContains(stack, OreDictionary.getOres("listAllseeds")) ||
                BWOreDictionary.listContains(stack, OreDictionary.getOres("foodFlour")) ||
                BWOreDictionary.listContains(stack, BWOreDictionary.dustNames) ||
                BWOreDictionary.listContains(stack, OreDictionary.getOres("pile"));
    }

    private static boolean isFlat(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        if (item == BWMItems.MATERIAL) {
            return meta == 1 || meta == 4 || (meta > 5 && meta < 10) || (meta > 31 && meta < 35);
        }
        return item == Item.getItemFromBlock(Blocks.WOOL) || item == Item.getItemFromBlock(Blocks.CARPET) || item == Items.LEATHER || item == Items.MAP || item == Items.FILLED_MAP || BWOreDictionary.listContains(stack, OreDictionary.getOres("string")) || BWOreDictionary.listContains(stack, OreDictionary.getOres("paper"));
    }

    @Override
    public void init(FMLInitializationEvent event) {



        HopperFilters.addFilter(1, Blocks.LADDER, 0, HopperRecipes::isItem);
        HopperFilters.addFilter(2, Blocks.TRAPDOOR, 0, stack -> !isItem(stack));

        HopperFilters.addFilter(4, BWMBlocks.SLATS, OreDictionary.WILDCARD_VALUE, HopperRecipes::isFlat);
        HopperFilters.addFilter(5, new ItemStack(BWMBlocks.WICKER, 1), HopperRecipes::isDust);

        HopperFilters.addFilter(3, BWMBlocks.GRATE, OreDictionary.WILDCARD_VALUE, stack -> stack.getMaxStackSize() == 1);
        HopperFilters.addFilter(7, Blocks.IRON_BARS, 0, stack -> stack.getMaxStackSize() > 1);

        HopperFilters.addFilter(6, Blocks.SOUL_SAND, 0, stack -> stack.isItemEqual(new ItemStack(Blocks.SOUL_SAND)));

        HopperInteractions.addHopperRecipe(new HopperInteractions.SoulUrnRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GROUND_NETHERRACK), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST)));
        HopperInteractions.addHopperRecipe(new HopperInteractions.SoulUrnRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST)));
        HopperInteractions.addHopperRecipe(new HopperInteractions.HopperRecipe(5, new ItemStack(Blocks.GRAVEL), new ItemStack(Items.FLINT), new ItemStack(Blocks.SAND), new ItemStack(Blocks.SAND, 1, 1)) {
            @Override
            public void craft(EntityItem inputStack, World world, BlockPos pos) {
                InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), output.copy());
                TileEntityFilteredHopper tile = (TileEntityFilteredHopper) world.getTileEntity(pos);
                ItemStackHandler inventory = tile.inventory;
                ItemStack sand = secondaryOutput.get(world.rand.nextInt(secondaryOutput.size())).copy();
                if (!InvUtils.insert(inventory, sand, false).isEmpty()) {
                    InvUtils.ejectStackWithOffset(world, inputStack.getPosition(), sand);
                }
                onCraft(world, pos, inputStack);
            }
        });
        HopperInteractions.addHopperRecipe(new HopperInteractions.HopperRecipe(6, new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE), ItemStack.EMPTY) {
            @Override
            public boolean canCraft(World world, BlockPos pos) {
                TileEntityFilteredHopper hopper = (TileEntityFilteredHopper) world.getTileEntity(pos);
                return super.canCraft(world, pos) && hopper.soulsRetained > 0;
            }

            @Override
            public ItemStack getOutput() {
                return new ItemStack(Blocks.SOUL_SAND);
            }

            @Override
            public void craft(EntityItem inputStack, World world, BlockPos pos) {
                onCraft(world,pos,inputStack);
            }

            @Override
            public void onCraft(World world, BlockPos pos, EntityItem item) {
                TileEntityFilteredHopper hopper = (TileEntityFilteredHopper) world.getTileEntity(pos);
                int stackSize = hopper.soulsRetained;
                if (stackSize > item.getItem().getCount())
                    stackSize = item.getItem().getCount();
                hopper.soulsRetained -= stackSize;
                item.getItem().shrink(stackSize);
                EntityItem soul = new EntityItem(world, item.lastTickPosX, item.lastTickPosY, item.lastTickPosZ, new ItemStack(Blocks.SOUL_SAND, stackSize));
                if (!InvUtils.insert(hopper.inventory, soul.getItem(), false).isEmpty()) {
                    soul.setDefaultPickupDelay();
                    world.spawnEntity(soul);
                }
                if (item.getItem().getCount() < 1)
                    item.setDead();
            }
        });
    }
}


