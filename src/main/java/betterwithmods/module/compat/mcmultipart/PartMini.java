package betterwithmods.module.compat.mcmultipart;

import betterwithmods.common.blocks.mini.BlockMini;
import mcmultipart.api.multipart.IMultipart;

/**
 * Created by primetoxinz on 6/19/17.
 */
public abstract class PartMini<T extends BlockMini> implements IMultipart {

    private T block;

    public PartMini(T block) {
        this.block = block;
    }

    @Override
    public T getBlock() {
        return block;
    }
}
