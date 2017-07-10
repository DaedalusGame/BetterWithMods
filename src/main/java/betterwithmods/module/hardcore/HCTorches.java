package betterwithmods.module.hardcore;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 5/10/17.
 */
public class HCTorches extends Feature {
    private boolean removeAllOtherTorchRecipes;
    @Override
    public void setupConfig() {
        removeAllOtherTorchRecipes = loadPropBool("Remove All Torch Recipes", "Disables any torch recipe added by other mods that ruin the balance",true);
    }

    @Override
    public String getFeatureDescription() {
        return "Change Torch Recipe to only provide 1 from each coal. Makes Nethercoal more useful, as it converts 1 coal into 4 Nethercoal.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        //TODO
//        if(removeAllOtherTorchRecipes) {
//            BWMRecipes.removeRecipes(Blocks.TORCH);
//        } else {
//            BWMRecipes.removeShaped(new ItemStack(Blocks.TORCH), new ItemStack[][]{new ItemStack[]{new ItemStack(Items.COAL,1,0)},new ItemStack[]{new ItemStack(Items.STICK)}});
//            BWMRecipes.removeShaped(new ItemStack(Blocks.TORCH), new ItemStack[][]{new ItemStack[]{new ItemStack(Items.COAL,1,1)},new ItemStack[]{new ItemStack(Items.STICK)}});
//        }
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.TORCH, "C", "S", 'C', Ingredient.fromStacks(new ItemStack(Items.COAL, 1, 0), new ItemStack(Items.COAL, 1, 1)), 'S', "stickWood").setRegistryName(new ResourceLocation("minecraft", "torch")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.TORCH, "C", "S", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL), 'S', "stickWood").setRegistryName(new ResourceLocation("betterwithmods", "torch")));
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Blocks.TORCH, 4), "C", "S", 'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL), 'S', "stickWood").setRegistryName(new ResourceLocation("betterwithmods", "torch")));
    }

    private IRecipe addHardcoreRecipe(IRecipe recipe) {
        return registerHardcoreRecipe("HCTorches", recipe);
    }
}
