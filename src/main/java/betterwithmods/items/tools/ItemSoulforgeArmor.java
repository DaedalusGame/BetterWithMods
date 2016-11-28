package betterwithmods.items.tools;

import betterwithmods.BWMod;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/14/16
 */
public class ItemSoulforgeArmor extends ItemArmor {
    private static final ArmorMaterial SOULFORGED_STEEL = EnumHelper.addArmorMaterial("soulforged_steel", "betterwithmods:steel", 40, new int[]{3, 6, 8, 3}, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);

    public ItemSoulforgeArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(SOULFORGED_STEEL, 2, equipmentSlotIn);
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return BWMod.MODID + ":textures/models/armor/steel_layer_" + (this.armorType.getSlotIndex() == 2 ? "2" : "1") + ".png";
    }
}
