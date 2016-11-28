package betterwithmods.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by tyler on 9/11/16.
 */
public class ItemArcaneScroll extends Item {

    public ItemArcaneScroll() {
        super();
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            ItemStack stack = new ItemStack(this);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("enchant", Enchantment.getEnchantmentID(enchantment));
            stack.setTagCompound(tag);
            subItems.add(stack);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        int id = stack.getTagCompound() != null ? stack.getTagCompound().getInteger("enchant") : 0;
        tooltip.add(Enchantment.getEnchantmentByID(id).getTranslatedName(-1));
    }
}
