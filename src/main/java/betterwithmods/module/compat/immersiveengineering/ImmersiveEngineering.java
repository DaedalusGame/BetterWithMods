package betterwithmods.module.compat.immersiveengineering;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mini.BlockCorner;
import betterwithmods.common.blocks.mini.BlockMoulding;
import betterwithmods.common.blocks.mini.BlockSiding;
import betterwithmods.common.blocks.mini.ItemBlockMini;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.compat.CompatFeature;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.module.hardcore.HCSaw;
import betterwithmods.module.tweaks.HighEfficiencyRecipes;
import betterwithmods.util.RecipeUtils;
import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.api.crafting.SqueezerRecipe;
import blusunrize.immersiveengineering.api.energy.ThermoelectricHandler;
import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.plant.BlockIECrop;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.HashSet;

import static betterwithmods.common.BWOreDictionary.registerOre;
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
        overrideIndustrialHempDrops = loadPropBool("Override Industrial Hemp Drops", "Replaces Hemp Fiber with BWM Hemp, making it require a Millstone", true);
    }

    public static Block SIDING = new BlockSiding(Material.WOOD) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

    }.setRegistryName("ie_siding");
    public static Block MOULDING = new BlockMoulding(Material.WOOD) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

    }.setRegistryName("ie_moulding");
    public static Block CORNER = new BlockCorner(Material.WOOD) {
        @Override
        public int getUsedTypes() {
            return 3;
        }
    }.setRegistryName("ie_corner");

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        BWMBlocks.registerBlock(SIDING, new ItemBlockMini(SIDING));
        BWMBlocks.registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        BWMBlocks.registerBlock(CORNER, new ItemBlockMini(CORNER));
        BWMBlocks.registerBlock(TREATED_AXLE);
        GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        BWMBlocks.setInventoryModel(SIDING);
        BWMBlocks.setInventoryModel(MOULDING);
        BWMBlocks.setInventoryModel(CORNER);
        BWMBlocks.setInventoryModel(TREATED_AXLE);
    }

    BelljarHandler.DefaultPlantHandler bwmHempHandler = new BelljarHandler.DefaultPlantHandler() {
        private HashSet<ComparableItemStack> validSeeds = new HashSet<>();

        @Override
        protected HashSet<ComparableItemStack> getSeedSet() {
            return validSeeds;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IBlockState[] getRenderedPlant(ItemStack seed, ItemStack soil, float growth, TileEntity tile) {
            int age = Math.min(7, Math.round(growth * 7));
            if (age > 6)
                return new IBlockState[]{BWMBlocks.HEMP.getStateFromMeta(age), BWMBlocks.HEMP.getStateFromMeta(8)};
            return new IBlockState[]{BWMBlocks.HEMP.getStateFromMeta(age)};
        }

        @Override
        @SideOnly(Side.CLIENT)
        public float getRenderSize(ItemStack seed, ItemStack soil, float growth, TileEntity tile) {
            return .7875f;
        }
    };

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack conveyorStack = ConveyorHandler.getConveyorStack("immersiveengineering:conveyor");
        addOredictRecipe(Utils.copyStackWithAmount(conveyorStack, 8), "LLL", "IRI", Character.valueOf('I'), "ingotIron", Character.valueOf('R'), "dustRedstone", Character.valueOf('L'), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "plankTreatedWood", 'R', BWMBlocks.ROPE));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TREATED_AXLE), "W", "R", "W", 'W', "mouldingTreatedWood", 'R', BWMBlocks.ROPE));
        Fluid seedOil = FluidRegistry.getFluid("plantoil");
        SqueezerRecipe.addRecipe(new FluidStack(seedOil, 120), null, new ItemStack(BWMBlocks.HEMP, 1, 0), 6400);

        BelljarHandler.registerHandler(bwmHempHandler);
        bwmHempHandler.register(new ItemStack(BWMBlocks.HEMP), new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP), new ItemStack(BWMBlocks.HEMP)}, new ItemStack(Blocks.DIRT), BWMBlocks.HEMP.getDefaultState());
        if (overrideIndustrialHempDrops) {
            BelljarHandler.DefaultPlantHandler ieHempHandler = (BelljarHandler.DefaultPlantHandler) BelljarHandler.getHandler(new ItemStack(IEContent.itemSeeds));
            ieHempHandler.register(new ItemStack(IEContent.itemSeeds), new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP), new ItemStack(IEContent.itemSeeds)}, new ItemStack(Blocks.DIRT), IEContent.blockCrop.getDefaultState());
        }
        ThermoelectricHandler.registerSourceInKelvin("blockHellfire", 4000);

        Block treated_wood = getBlock(modid + ":treated_wood");
        Block treated_slab = getBlock(modid + ":treated_wood_slab");
        Item material = getItem(modid + ":material");
        for (int i = 0; i < 3; i++) {
            SawRecipes.addSawRecipe(treated_wood, i, new ItemStack(SIDING, 2, i));
            SawRecipes.addSawRecipe(SIDING, i, new ItemStack(MOULDING, 2, i));
            SawRecipes.addSawRecipe(MOULDING, i, new ItemStack(CORNER, 2, i));
            SawRecipes.addSawRecipe(CORNER, i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2));
            SawRecipes.addSawRecipe(treated_slab, i, new ItemStack(MOULDING, 2, i));
            GameRegistry.addRecipe(new ItemStack(treated_wood, 1, i), "MM", 'M', new ItemStack(SIDING, 1, i));
            GameRegistry.addRecipe(new ItemStack(SIDING, 1, i), "MM", 'M', new ItemStack(MOULDING, 1, i));
            GameRegistry.addRecipe(new ItemStack(MOULDING, 1, i), "MM", 'M', new ItemStack(CORNER, 1, i));


            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(getBlock(modid + ":treated_wood_stairs" + i), 1), "M ", "MM", 'M', new ItemStack(MOULDING, 1, i)).setMirrored(true));

        }
        registerOre("sidingTreatedWood", new ItemStack(SIDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("slabTreatedWood", new ItemStack(SIDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("mouldingTreatedWood", new ItemStack(MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        registerOre("cornerTreatedWood", new ItemStack(CORNER, 1, OreDictionary.WILDCARD_VALUE));
        if (ModuleLoader.isFeatureEnabled(HighEfficiencyRecipes.class)) {
            Block wood_dec = getBlock("immersiveengineering:wooden_decoration");
            if (ModuleLoader.isFeatureEnabled(HCSaw.class)) {
                RecipeUtils.removeRecipes(wood_dec);
            }
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wood_dec, 3, 0), "MMM", 'M', "mouldingTreatedWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(wood_dec, 6, 1), "SSS", " M ", "M M", 'M', "mouldingTreatedWood", 'S', "sidingTreatedWood").setMirrored(true));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material, 1, 0), "mouldingTreatedWood"));
        }

    }

    @SubscribeEvent
    public void overrideHempDrops(BlockEvent.HarvestDropsEvent e) {
        if (!overrideIndustrialHempDrops)
            return;
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

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
