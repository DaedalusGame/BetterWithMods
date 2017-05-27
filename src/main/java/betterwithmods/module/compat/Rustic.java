package betterwithmods.module.compat;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.module.tweaks.HighEfficiencyRecipes;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import static betterwithmods.common.BWMBlocks.registerBlock;
import static betterwithmods.common.BWMBlocks.setInventoryModel;

/**
 * Created by tyler on 5/27/17.
 */
@SuppressWarnings("unused")
public class Rustic extends CompatFeature {
    public static final Block SIDING = new BlockSiding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 2;
        }
    }.setRegistryName("rustic_compat_siding");
    public static final Block MOULDING = new BlockMoulding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 2;
        }
    }.setRegistryName("rustic_compat_moulding");
    public static final Block CORNER = new BlockCorner(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 2;
        }
    }.setRegistryName("rustic_compat_corner");


    public Rustic() {
        super("rustic");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerBlock(SIDING, new ItemBlockMini(SIDING));
        registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        registerBlock(CORNER, new ItemBlockMini(CORNER));
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        setInventoryModel(SIDING);
        setInventoryModel(MOULDING);
        setInventoryModel(CORNER);

    }
    public String[] woods = {"spruce", "birch", "jungle", "acacia", "dark_oak","olive","ironwood"};
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ItemStack rope = new ItemStack(getBlock(new ResourceLocation("rustic", "rope")));
        RecipeUtils.removeRecipes(rope);
        GameRegistry.addRecipe(new ShapedOreRecipe(rope,"F","F","F",'F',"fiberHemp"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(getBlock(new ResourceLocation("rustic","candle")), 6),"S","T","I", 'S', "string", 'T', "tallow", 'I', "ingotIron"));
        Block plank = getBlock(new ResourceLocation("rustic","planks"));
        for(int i = 0; i<((BlockMini)SIDING).getUsedTypes();i++) {
            SawRecipes.addSawRecipe(plank,i,new ItemStack(SIDING,2,i));
            SawRecipes.addSawRecipe(SIDING,i,new ItemStack(MOULDING,2,i));
            SawRecipes.addSawRecipe(MOULDING,i,new ItemStack(CORNER,2,i));
            SawRecipes.addSawRecipe(CORNER,i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR,2));
        }
        if (ModuleLoader.isFeatureEnabled(HighEfficiencyRecipes.class)) {
            for (int i = 0;i<woods.length;i++) {
                ItemStack moulding = i > 4 ? new ItemStack(MOULDING,1,i-5) : new ItemStack(BWMBlocks.WOOD_MOULDING, 1,i);
                ItemStack siding = i > 4 ? new ItemStack(SIDING,1,i-5) : new ItemStack(BWMBlocks.WOOD_SIDING, 1,i);
                GameRegistry.addShapedRecipe(new ItemStack(getBlock(new ResourceLocation("rustic", "chair_" + woods[i])),4), "S  ", "SSS", "M M", 'S', siding, 'M', moulding);
                GameRegistry.addShapedRecipe(new ItemStack(getBlock(new ResourceLocation("rustic", "table_" + woods[i])), 2), "SSS", "M M", 'S', siding, 'M', moulding);
                if(i > 4) {
                    GameRegistry.addRecipe(new ItemStack(getBlock(new ResourceLocation("rustic","fence_gate_"+woods[i]))), "MSM",'S', siding, 'M', moulding);
                    GameRegistry.addRecipe(new ItemStack(getBlock(new ResourceLocation("rustic","fence_"+woods[i])),3), "MMM",'M', moulding);
                }
            }
        }
    }
}

