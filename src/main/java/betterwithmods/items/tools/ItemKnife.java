package betterwithmods.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.items.IBWMItem;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemKnife extends ItemTool implements IBWMItem
{
	private boolean repair = false;
	
	public ItemKnife(ToolMaterial material)
	{
		super(material, Sets.newHashSet(new Block[] {Blocks.CRAFTING_TABLE}));
		this.setHarvestLevel("axe", material.getHarvestLevel() - 1);
		this.setMaxDamage(material.getMaxUses() / 2);
		this.setCreativeTab(BWCreativeTabs.BWTAB);
		setUnlocalizedName("knife");
		setRegistryName("knife");
		GameRegistry.register(this);
		register();
        MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public String getLocation(int meta) {
		return "betterwithmods:iron_knife";
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return !repair;
	}
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent evt)
	{
		repair = this == evt.crafting.getItem();
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		if(!hasContainerItem(stack))
			return null;

		ItemStack newStack = new ItemStack(this);
		if(stack != null && stack.getItem() == this) {
			newStack.setItemDamage(stack.getItemDamage() + 1);
		}
		return newStack;
	}
}
