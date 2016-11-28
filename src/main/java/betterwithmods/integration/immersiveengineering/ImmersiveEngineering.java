package betterwithmods.integration.immersiveengineering;

import betterwithmods.BWMBlocks;
import betterwithmods.config.BWConfig;
import betterwithmods.integration.ICompatModule;
import betterwithmods.items.ItemMaterial;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static blusunrize.immersiveengineering.common.IERecipes.addOredictRecipe;

/**
 * Created by tyler on 9/10/16.
 */
@SuppressWarnings("unused")
public class ImmersiveEngineering implements ICompatModule {
    public static final Block TREATED_AXLE = new BlockImmersiveAxle().setRegistryName("immersive_axle");

    public static final String MODID = "immersiveengineering";

    public static boolean overrideIndustrialHempDrops;

    @Override
    public void preInit() {
        BWMBlocks.registerBlock(TREATED_AXLE);
        GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
        BWConfig.config.load();
        overrideIndustrialHempDrops = BWConfig.config.getBoolean("Override Hemp Drop", BWConfig.MOD_COMPAT, true, "Replaces drop from IE Industrial Hemp with BWM Hemp");
        BWConfig.config.save();
        if (overrideIndustrialHempDrops) {
            MinecraftForge.EVENT_BUS.register(this);
        }
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

        ItemStack conveyorStack = ConveyorHandler.getConveyorStack("immersiveengineering:conveyor");
        addOredictRecipe(Utils.copyStackWithAmount(conveyorStack, 8), new Object[]{"LLL", "IRI", Character.valueOf('I'), "ingotIron", Character.valueOf('R'), "dustRedstone", Character.valueOf('L'), ItemMaterial.getMaterial("leather_cut")});
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "plankTreatedWood", 'R', BWMBlocks.ROPE));
        Fluid seedOil = FluidRegistry.getFluid("plantoil");
        SqueezerRecipe.addRecipe(new FluidStack(seedOil, 120), null, new ItemStack(BWMBlocks.HEMP, 1, 0), 6400);
    }

    @Override
    public void postInit() {

    }

    @SubscribeEvent
    public void overrideHempDrops(BlockEvent.HarvestDropsEvent e) {
        IBlockState state = e.getState();
        if (state.getBlock() instanceof BlockIECrop) {
            e.getDrops().clear();
            int meta = state.getBlock().getMetaFromState(state);
            if (meta >= 4) {
                e.getDrops().add(new ItemStack(IEContent.itemSeeds));
                e.getDrops().add(ItemMaterial.getMaterial("hemp"));
            }
        }
    }
}
