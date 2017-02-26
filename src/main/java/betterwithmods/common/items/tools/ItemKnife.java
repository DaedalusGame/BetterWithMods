package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import com.google.common.collect.Sets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemKnife extends ItemTool {
    private boolean repair = false;

    public ItemKnife(ToolMaterial material) {
        super(material, Sets.newHashSet(Blocks.CRAFTING_TABLE));
        this.setHarvestLevel("axe", material.getHarvestLevel() - 1);
        this.setMaxDamage(material.getMaxUses() / 2);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return !repair;
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent evt) {
        repair = this == evt.crafting.getItem();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type == EnumEnchantmentType.BREAKABLE;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (!hasContainerItem(stack))
            return null;

        ItemStack newStack = new ItemStack(this);
        if (stack != ItemStack.EMPTY && stack.getItem() == this) {
            newStack.setItemDamage(stack.getItemDamage() + 1);
        }
        return newStack;
    }
}
