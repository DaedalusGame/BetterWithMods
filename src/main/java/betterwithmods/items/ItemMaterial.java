package betterwithmods.items;

import java.util.List;

import betterwithmods.client.BWCreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMaterial extends Item implements ITannin
{
	public String[] names = {"Gear", "Nethercoal", "Hemp", "HempFibers", "HempCloth", "Dung", "TannedLeather", "ScouredLeather", "LeatherStrap", "LeatherBelt", "WoodBlade",
			"WindmillBlade", "Glue", "Tallow", "IngotSteel", "GroundNetherrack", "HellfireDust", "ConcentratedHellfire", "CoalDust", "Filament", "PolishedLapis",
			"Potash", "Sawdust", "SoulDust", "Screw", "Brimstone", "Niter", "Element", "Fuse", "BlastingOil", "NuggetIron", "NuggetSteel", "LeatherCut",
			"TannedLeatherCut", "ScouredLeatherCut", "RedstoneLatch", "NetherSludge", "Flour", "Haft", "CharcoalDust", "SharpeningStone", "KnifeBlade"};
	
	public ItemMaterial()
	{
		this.setHasSubtypes(true);
		this.setUnlocalizedName("bwm:material");
		this.setCreativeTab(BWCreativeTabs.BWTAB);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(int i = 0; i < 42; i++)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + names[stack.getItemDamage()];
	}

	@Override
	public int getStackSizeForTanning(int meta) 
	{
		if(meta == 5) return 1;
		return 0;
	}
}
