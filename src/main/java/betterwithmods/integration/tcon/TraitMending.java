package betterwithmods.integration.tcon;

import net.minecraft.init.Enchantments;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ToolBuilder;

public class TraitMending extends AbstractTrait
{
    public TraitMending()
    {
        super("mending", TextFormatting.DARK_GRAY);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag)
    {
        super.applyEffect(rootCompound, modifierTag);
        ToolBuilder.addEnchantment(rootCompound, Enchantments.MENDING);
    }
}
