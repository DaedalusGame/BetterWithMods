package betterwithmods.integration.immersiveengineering;

import betterwithmods.BWMBlocks;
import betterwithmods.integration.ICompatModule;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 9/10/16.
 */
@SuppressWarnings("unused")
public class ImmersiveEngineering implements ICompatModule {
    public static final Block TREATED_AXLE = new BlockImmersiveAxle().setRegistryName("immersive_axle");

    public static final String MODID = "immersiveengineering";

    @Override
    public void preInit() {
        BWMBlocks.registerBlock(TREATED_AXLE);
        GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        BWMBlocks.setInventoryModel(TREATED_AXLE);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {

    }


    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {

    }

    @Override
    public void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "plankTreatedWood", 'R', BWMBlocks.ROPE));
        Fluid seedOil = FluidRegistry.getFluid("plantoil");
        SqueezerRecipe.addRecipe(new FluidStack(seedOil, 120), null, new ItemStack(BWMBlocks.HEMP, 1, 0), 6400);
    }

    @Override
    public void postInit() {

    }
}
