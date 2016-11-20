package betterwithmods.items.tools;

import betterwithmods.entity.EntityBroadheadArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/18/16
 */
public class ItemBroadheadArrow extends ItemArrow {
    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        if (stack != ItemStack.field_190927_a && stack.getItem() == this)
            return new EntityBroadheadArrow(worldIn, shooter);
        return super.createArrow(worldIn, stack, shooter);
    }
}
