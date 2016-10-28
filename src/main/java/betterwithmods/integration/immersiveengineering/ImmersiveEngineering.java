package betterwithmods.integration.immersiveengineering;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.integration.ModIntegration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 9/10/16.
 */
public class ImmersiveEngineering extends ModIntegration {
    public static final Block TREATED_AXLE = new BlockImmersiveAxle().setRegistryName("immersive_axle");

    @Override
    public void preInit() {
        BWMBlocks.registerBlock(TREATED_AXLE);
        if (BWMod.proxy.isClientside())
            BWMBlocks.setInventoryModel(TREATED_AXLE);
        GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
    }

    @Override
    public void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "plankTreatedWood", 'R', BWMBlocks.ROPE));
    }
}
