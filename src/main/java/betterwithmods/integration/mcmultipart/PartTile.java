package betterwithmods.integration.mcmultipart;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.tileentity.TileEntity;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/27/17
 */
public class PartTile implements IMultipartTile {
    private TileEntity tile;
    private IPartInfo info;

    public PartTile(TileEntity tile) {
        this.tile = tile;
    }

    @Override
    public void setPartInfo(IPartInfo info) {
        this.info = info;
    }

    @Override
    public TileEntity getTileEntity() {
        return tile;
    }

    public IPartInfo getInfo() {
        return info;
    }

}
