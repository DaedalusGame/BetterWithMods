package betterwithmods.module.hardcore;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 5/10/17.
 */
public class HCTorches extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Change Torch Recipe to only provide 1 from each coal. Makes Nethercoal more useful, as it converts 1 coal into 4 Nethercoal.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        RecipeUtils.removeRecipes(Blocks.TORCH);
        GameRegistry.addRecipe(new ShapedOreRecipe(Blocks.TORCH,"C","S",'C', "coal",'S',"stickWood"));
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.TORCH,4), "C","S",'C', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL),'S',"stickWood"));
    }
}
