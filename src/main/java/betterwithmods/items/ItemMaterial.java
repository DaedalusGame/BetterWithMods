package betterwithmods.items;

import betterwithmods.BWMItems;
import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ItemMaterial extends Item implements IMultiLocations {
    private static final String[] names = {"gear", "nethercoal", "hemp", "hemp_fibers", "hemp_cloth", "dung", "tanned_leather", "scoured_leather", "leather_strap", "leather_belt", "wood_blade",
            "windmill_blade", "glue", "tallow", "ingot_steel", "ground_netherrack", "hellfire_dust", "concentrated_hellfire", "coal_dust", "filament", "polished_lapis",
            "potash", "sawdust", "soul_dust", "screw", "brimstone", "niter", "element", "fuse", "blasting_oil", "nugget_iron", "nugget_steel", "leather_cut",
            "tanned_leather_cut", "scoured_leather_cut", "redstone_latch", "nether_sludge", "flour", "haft", "charcoal_dust", "sharpening_stone", "knife_blade", "soul_flux", "ender_slag", "ender_ocular",
            "padding","armor_plate","broadhead", "cocoa_powder"};

    public ItemMaterial() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
    }

    public static ItemStack getMaterial(String material) {
        return getMaterial(material, 1);
    }

    public static ItemStack getMaterial(String material, int count) {
        OptionalInt meta = IntStream.range(0, names.length).filter(n -> material.equals(names[n].toLowerCase())).findFirst();
        return new ItemStack(BWMItems.MATERIAL, count, meta.isPresent() ? meta.getAsInt() : 0);
    }

    @Override
    public String[] getLocations() {
        return names;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + names[stack.getItemDamage()];
    }
}
