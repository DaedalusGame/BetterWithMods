package betterwithmods.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BWStateMapper extends StateMapperBase
{
    private String blockString;

    public BWStateMapper(String name)
    {
        this.blockString = name;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        return new ModelResourceLocation(blockString, this.getPropertyString(state.getProperties()));
    }
}
