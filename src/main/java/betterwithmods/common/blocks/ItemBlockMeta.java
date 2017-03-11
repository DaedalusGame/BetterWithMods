package betterwithmods.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class ItemBlockMeta extends ItemBlock {
    public ItemBlockMeta(Block block) {
        super(block);
        this.setMaxDamage(0).setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);

        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("Items")) {
                NBTTagList list = stack.getTagCompound().getTagList("Items", 10);
                List<String> items = new ArrayList<>();
                items.add(I18n.format("bwm.item.containment"));
                for (int i = 0; i < list.tagCount() && items.size() < 4; i++) {
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    ItemStack s = new ItemStack(tag);
                    if (!s.isEmpty())
                        items.add(s.getCount() + "x " + s.getDisplayName());
                    if (items.size() == 4 && list.tagCount() > 3)
                        items.add("...");
                }
                tooltip.addAll(items);
            }
        }
    }
}
