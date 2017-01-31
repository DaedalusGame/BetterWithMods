package betterwithmods.craft;

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Random;

/**
 * Created by blueyu2 on 12/12/16.
 */
public class ChoppingRecipe extends ShapelessOreRecipe {
    private final ItemStack log, bark, sawdust;

    public ChoppingRecipe(ItemStack planks, ItemStack bark, ItemStack sawdust, ItemStack log) {
        super(planks, new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE), log);
        this.log = log;
        this.bark = bark;
        this.sawdust = sawdust;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return isMatch(inv);
    }

    public boolean isMatch(IInventory inventory) {
        boolean hasAxe = false;
        boolean hasLog = false;

        for (int x = 0; x < inventory.getSizeInventory(); x++)
        {
            boolean inRecipe = false;
            ItemStack slot = inventory.getStackInSlot(x);

            if (slot != null) {
                if (isAxe(slot)) {
                    if(!hasAxe) {
                        hasAxe = true;
                        inRecipe = true;
                    }
                    else
                        return false;
                }
                else if (OreDictionary.itemMatches(slot, log, true)) {
                    if(!hasLog) {
                        hasLog = true;
                        inRecipe = true;
                    }
                    else
                        return false;
                }
                if(!inRecipe)
                    return false;
            }
        }
        return hasAxe && hasLog;
    }

    private boolean isAxe(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem().getToolClasses(stack).contains("axe")) {
                if(stack.getItem().getRegistryName().getResourceDomain().equals("tconstruct")) {
                    if (stack.getItemDamage() >= stack.getMaxDamage())
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        ItemStack[] ret = new ItemStack[inv.getSizeInventory()];
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (int i = 0; i < ret.length; i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack != ItemStack.EMPTY && isAxe(stack)) {
                ItemStack copy = stack.copy();
                if(copy.getItem().getHarvestLevel(copy, "axe", null, null) > 1) {
                    ret[i] = copy;
                }
                else if (!copy.attemptDamageItem(1, new Random())) {
                    ret[i] = copy;
                }
                else if (copy.getItem().getRegistryName().getResourceDomain().equals("tconstruct")) {
                    ret[i] = copy;
                }
            }
            stacks.add(ret[i].copy());
        }

        return stacks;
    }

    @SubscribeEvent
    public void dropExtra(PlayerEvent.ItemCraftedEvent event) {
        if(event.player == null)
            return;

        if(isMatch(event.craftMatrix))
        {
            if(!event.player.getEntityWorld().isRemote) {
                if (sawdust != null)
                    event.player.entityDropItem(sawdust, 0);
                if (bark != null)
                    event.player.entityDropItem(bark, 0);
            }
            else
                event.player.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 0.25F, 2.5F);
        }
    }
}
