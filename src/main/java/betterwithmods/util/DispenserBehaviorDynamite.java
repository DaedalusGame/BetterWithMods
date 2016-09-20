package betterwithmods.util;

import betterwithmods.BWMItems;
import betterwithmods.entity.EntityDynamite;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DispenserBehaviorDynamite extends BehaviorProjectileDispense
{

	@Override
	protected IProjectile getProjectileEntity(World world, IPosition pos, ItemStack stack)
	{
		return new EntityDynamite(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BWMItems.dynamite, 1));
	}

}
