package betterwithmods.items;

import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemBark extends Item implements IMultiLocations {
    public ItemBark() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public static int getTanningStackSize(int meta) {
        //TODO fix values for faithful mode
        if (meta < BlockPlanks.EnumType.values().length) {
            switch (BlockPlanks.EnumType.byMetadata(meta)) {
                case OAK:
                    return 5;
                case SPRUCE:
                    return 3;
                case BIRCH:
                    return 2;
                case JUNGLE:
                    return 4;
                case ACACIA:
                    return 2;
                case DARK_OAK:
                    return 8;
                default:
                    return 8;
            }
        } else {
            return 8;
        }
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
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values()) {
            list.add(new ItemStack(item, 1, enumType.getMetadata()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
