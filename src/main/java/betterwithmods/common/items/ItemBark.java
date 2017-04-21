package betterwithmods.common.items;

import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class ItemBark extends Item implements IMultiLocations {
    public ItemBark() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    private final static int[] sizes = new int[]{5,3,2,4,2,8};
    public static int getTanningStackSize(int meta) {
        if(meta > sizes.length || meta < 0)
            return 8;
        return sizes[meta];
    }

    @Override
    public String[] getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values()) {
            locations.add("bark_" + enumType.getName());
        }
        return locations.toArray(new String[locations.size()]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values()) {
            list.add(new ItemStack(item, 1, enumType.getMetadata()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
