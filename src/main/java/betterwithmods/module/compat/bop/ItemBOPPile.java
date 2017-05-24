package betterwithmods.module.compat.bop;

import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemBOPPile extends Item implements IMultiLocations {
    public static ItemStack getMaterial(EnumMaterial material) {
        return getMaterial(material, 1);
    }

    public ItemBOPPile() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
    }

    public static ItemStack getMaterial(EnumMaterial material, int count) {
        return new ItemStack(BiomesOPlenty.PILES, count, material.getMetadata());
    }

    @Override
    public String[] getLocations() {
        List<String> names = new ArrayList<>();
        for (EnumMaterial material : EnumMaterial.values()) {
            names.add(material.getName());
        }
        return names.toArray(new String[EnumMaterial.values().length]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (EnumMaterial material : EnumMaterial.values()) {
            list.add(getMaterial(material));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + EnumMaterial.values()[stack.getMetadata()].getName();
    }


    public enum EnumMaterial {
        LOAMY_DIRT_PILE,
        SANDY_DIRT_PILE,
        SILTY_DIRT_PILE;

        int getMetadata() {
            return this.ordinal();
        }

        String getName() {
            return this.name().toLowerCase();
        }
    }
}
