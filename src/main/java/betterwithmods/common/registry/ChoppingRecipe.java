package betterwithmods.common.registry;

import betterwithmods.common.BWOreDictionary;
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
    private final Object log;
    private final ItemStack bark, sawdust;

    public ChoppingRecipe(ItemStack planks, ItemStack bark, ItemStack sawdust, Object log) {
        super(planks, new ItemStack(Items.IRON_AXE, 1, OreDictionary.WILDCARD_VALUE), log);
        this.log = log;
        this.bark = bark;
        this.sawdust = sawdust;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public ChoppingRecipe(ItemStack planks, ItemStack sawdust, Object log) {
        this(planks, ItemStack.EMPTY, sawdust, log);
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

            if (!slot.isEmpty()) {
                if (isAxe(slot)) {
                    if(!hasAxe) {
                        hasAxe = true;
                        inRecipe = true;
                    }
                    else
                        return false;
                }
                else {
                    if (log instanceof ItemStack) {
                        if (OreDictionary.itemMatches(slot, (ItemStack)log, true)) {
                            if (!hasLog) {
                                hasLog = true;
                                inRecipe = true;
                            } else
                                return false;
                        }
                    }
                    else if (log instanceof String) {
                        if (BWOreDictionary.listContains(slot, OreDictionary.getOres((String)log))) {
                            if (!hasLog) {
                                hasLog = true;
                                inRecipe = true;
                            } else
                                return false;
                        }
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
        NonNullList<ItemStack> stacks = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < stacks.size(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty() && isAxe(stack)) {
                ItemStack copy = stack.copy();
                if(copy.getItem().getHarvestLevel(copy, "axe", null, null) > 1) {
                    stacks.set(i, copy.copy());
                }
                else if (!copy.attemptDamageItem(1, new Random())) {
                    stacks.set(i, copy.copy());
                }
                else if (copy.getItem().getRegistryName().getResourceDomain().equals("tconstruct")) {
                    stacks.set(i, copy.copy());
                }
            }
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
                if (!sawdust.isEmpty())
                    event.player.entityDropItem(sawdust.copy(), 0);
                if (!bark.isEmpty())
                    event.player.entityDropItem(bark.copy(), 0);
            }
            else
                event.player.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 0.25F, 2.5F);
        }
    }
}
