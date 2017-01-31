package betterwithmods.entity.item;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;

public class EntityFallingBlockCustom extends EntityFallingBlock {
    @SuppressWarnings("unused")
    public EntityFallingBlockCustom(World worldIn) {
        super(worldIn);
    }

    public EntityFallingBlockCustom(EntityFallingBlock entityBlock) {
        super(entityBlock.getEntityWorld(), entityBlock.prevPosX,
                entityBlock.prevPosY, entityBlock.prevPosZ, entityBlock.getBlock());
        try {
            NBTTagCompound originalData = new NBTTagCompound();
            ReflectionHelper.findMethod(EntityFallingBlock.class, entityBlock,
                    new String[]{"func_70014_b", "writeEntityToNBT"}, NBTTagCompound.class)
                    .invoke(entityBlock, originalData);
            this.readEntityFromNBT(originalData);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        if (getBlock() == null) return;//FIXME No idea why this is sometimes null
        super.onUpdate();//TODO custom onUpdate()
    }
}
