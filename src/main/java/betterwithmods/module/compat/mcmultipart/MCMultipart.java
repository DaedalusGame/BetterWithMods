package betterwithmods.module.compat.mcmultipart;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mini.BlockSiding;
import betterwithmods.module.compat.CompatFeature;
import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.IWrappedBlock;
import mcmultipart.api.multipart.IMultipartRegistry;
import net.minecraft.item.Item;

/**
 * Created by primetoxinz on 6/19/17.
 */
//why not work? :( @MCMPAddon
public class MCMultipart extends CompatFeature implements IMCMPAddon {
    public MCMultipart() {
        super("mcmultipart");
    }

    @Override
    public void registerParts(IMultipartRegistry registry) {
        BlockSiding siding = (BlockSiding) BWMBlocks.WOOD_SIDING;
        registry.registerPartWrapper(siding, new PartSiding(siding));
        IWrappedBlock partSiding = registry.registerStackWrapper(Item.getItemFromBlock(siding), stack -> true, siding);
        partSiding.setPlacementInfo(siding::getStateForPlacement);

    }


}
