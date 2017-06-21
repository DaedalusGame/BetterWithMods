package betterwithmods.common.blocks.tile;

import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 6/20/17.
 */
public class TileHandCrank extends TileBasic implements ITickable {
    private int stage;
    private int coolDown;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        if (stage == 1 || stage == 8) {
          world.notifyNeighborsOfStateChange(pos,getBlockType(),true);
        }
        this.stage = stage;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("stage", stage);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("stage"))
            stage = compound.getInteger("stage");
        super.readFromNBT(compound);
    }

    public void crank(World world) {
        if (coolDown == 0) {
            if (stage > 0) {
                setStage((stage + 1) % 9);
                if (stage < 8)
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 2.0F);
                world.markBlockRangeForRenderUpdate(pos, pos);
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.7F);
                coolDown = 6;
            }
        } else {
            coolDown--;
        }
    }

    @Override
    public void update() {
        crank(world);
    }
}
