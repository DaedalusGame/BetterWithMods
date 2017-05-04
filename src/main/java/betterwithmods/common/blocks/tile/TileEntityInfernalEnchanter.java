package betterwithmods.common.blocks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;

/**
 * Created by tyler on 9/11/16.
 */
public class TileEntityInfernalEnchanter extends TileBasic implements ITickable {
    private final static int RADIUS = 8;
    private int bookcaseCount;

    @Override
    public void update() {

        if (getWorld().getTotalWorldTime() % 20 == 0) {
            bookcaseCount = 0;
            for (int x = -RADIUS; x <= RADIUS; x++) {
                for (int y = -RADIUS; y <= RADIUS; y++) {
                    for (int z = -RADIUS; z <= RADIUS; z++) {
                        BlockPos pos = getPos().add(x, y, z);
                        bookcaseCount += (ForgeHooks.getEnchantPower(getWorld(), pos) > 0) ? 1 : 0;
                    }
                }
            }

            //required

            // bookcase * item.enchants + 1

        }
        if (getWorld().getTotalWorldTime() % 5 == 0) {
            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .125, y + .9, z + .125, 0, 0, 0);
            getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .875, y + .9, z + .125, 0, 0, 0);
            getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .875, y + .9, z + .875, 0, 0, 0);
            getWorld().spawnParticle(EnumParticleTypes.FLAME, x + .125, y + .9, z + .875, 0, 0, 0);
        }

    }

    public int getBookcaseCount() {
        return bookcaseCount;
    }

    public void setBookcaseCount(int bookcaseCount) {
        this.bookcaseCount = bookcaseCount;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("bookcaseCount", bookcaseCount);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        bookcaseCount = compound.getInteger("bookcaseCount");
        super.readFromNBT(compound);
    }

    public String getName() {
        return "bwm.infernalenchanter";
    }
}
