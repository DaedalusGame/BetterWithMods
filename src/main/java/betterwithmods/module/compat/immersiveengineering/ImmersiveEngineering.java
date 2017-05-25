package betterwithmods.module.compat.immersiveengineering;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.compat.CompatFeature;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.plant.BlockIECrop;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static blusunrize.immersiveengineering.common.IERecipes.addOredictRecipe;

/**
 * Created by tyler on 9/10/16.
 */
@SuppressWarnings("unused")
public class ImmersiveEngineering extends CompatFeature {
    public static final Block TREATED_AXLE = new BlockImmersiveAxle().setRegistryName("immersive_axle");

    public static boolean overrideIndustrialHempDrops;

    public ImmersiveEngineering() {
        super("immersiveengineering");
    }

    @Override
    public void setupConfig() {
        loadPropBool("Override Industrial Hemp Drops", "Replaces Hemp Fiber with BWM Hemp, making it require a Millstone", true);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMBlocks.registerBlock(TREATED_AXLE);
        GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
        if (overrideIndustrialHempDrops) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        BWMBlocks.setInventoryModel(TREATED_AXLE);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack conveyorStack = ConveyorHandler.getConveyorStack("immersiveengineering:conveyor");
        addOredictRecipe(Utils.copyStackWithAmount(conveyorStack, 8), "LLL", "IRI", Character.valueOf('I'), "ingotIron", Character.valueOf('R'), "dustRedstone", Character.valueOf('L'), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "plankTreatedWood", 'R', BWMBlocks.ROPE));
        Fluid seedOil = FluidRegistry.getFluid("plantoil");
        SqueezerRecipe.addRecipe(new FluidStack(seedOil, 120), null, new ItemStack(BWMBlocks.HEMP, 1, 0), 6400);
    }

    @SubscribeEvent
    public void overrideHempDrops(BlockEvent.HarvestDropsEvent e) {
        IBlockState state = e.getState();
        if (state.getBlock() instanceof BlockIECrop) {
            e.getDrops().clear();
            int meta = state.getBlock().getMetaFromState(state);
            if (meta >= 4) {
                e.getDrops().add(new ItemStack(IEContent.itemSeeds));
                e.getDrops().add(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP));
            }
        }
    }
}
