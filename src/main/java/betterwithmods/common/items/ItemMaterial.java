package betterwithmods.common.items;

import betterwithmods.common.BWMItems;
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

public class ItemMaterial extends Item implements IMultiLocations {
    public static ItemStack getMaterial(EnumMaterial material) {
        return getMaterial(material, 1);
    }

    public ItemMaterial() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
    }

    public static ItemStack getMaterial(EnumMaterial material, int count) {
        return new ItemStack(BWMItems.MATERIAL, count, material.getMetadata());
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
        GEAR,
        NETHERCOAL,
        HEMP,
        HEMP_FIBERS,
        HEMP_CLOTH,
        DUNG,
        TANNED_LEATHER,
        SCOURED_LEATHER,
        LEATHER_STRAP,
        LEATHER_BELT,
        WOOD_BLADE,
        WINDMILL_BLADE,
        GLUE,
        TALLOW,
        INGOT_STEEL,
        GROUND_NETHERRACK,
        HELLFIRE_DUST,
        CONCENTRATED_HELLFIRE,
        COAL_DUST,
        FILAMENT,
        POLISHED_LAPIS,
        POTASH,
        SAWDUST,
        SOUL_DUST,
        SCREW,
        BRIMSTONE,
        NITER,
        ELEMENT,
        FUSE,
        BLASTING_OIL,
        NUGGET_IRON,
        NUGGET_STEEL,
        LEATHER_CUT,
        TANNED_LEATHER_CUT,
        SCOURED_LEATHER_CUT,
        REDSTONE_LATCH,
        NETHER_SLUDGE,
        FLOUR,
        HAFT,
        CHARCOAL_DUST,
        SHARPENING_STONE,
        KNIFE_BLADE,
        SOUL_FLUX,
        ENDER_SLAG,
        ENDER_OCULAR,
        PADDING,
        ARMOR_PLATE,
        BROADHEAD,
        COCOA_POWDER,
        DIAMOND_INGOT,
        CHAIN_MAIL,
        STEEL_GEAR,
        STEEL_SPRING;

        int getMetadata() {
            return this.ordinal();
        }

        String getName() {
            return this.name().toLowerCase();
        }
    }
}
