package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

/**
 * Created by tyler on 4/20/17.
 */
public class HCOres extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Makes Ores only smelt into a single nugget, making it much harder to create large amounts of metal";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        RecipeUtils.removeRecipes(Items.COMPASS, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.COMPASS), " N ", "NRN", " N ", 'N', "nuggetIron", 'R', "dustRedstone"));
        RecipeUtils.removeRecipes(Items.CLOCK, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.CLOCK), " N ", "NQN", " N ", 'N', "nuggetGold", 'Q', "gemQuartz"));
        RecipeUtils.removeRecipes(Items.BUCKET, 0);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.BUCKET), "N N", " N ", 'N', "nuggetIron"));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        for (ItemStack ore : InvUtils.oreNames) {
            String suffix = InvUtils.getSuffix(ore, "ore");
            if (suffix != null) {
                RecipeUtils.removeFurnaceRecipe(ore);
                ItemStack nugget = OreDictionary.getOres("nugget" + suffix).stream().findFirst().orElse(ItemStack.EMPTY);
                if (!nugget.isEmpty()) {
                    FurnaceRecipes.instance().getSmeltingList().put(ore,nugget);
                    List<ItemStack> dusts = OreDictionary.getOres("dust"+suffix);
                    if (dusts.size() > 0) {
                        dusts.forEach(RecipeUtils::removeFurnaceRecipe);
                        dusts.forEach(dust -> FurnaceRecipes.instance().getSmeltingList().put(dust, nugget));
                    }
                }
            }
        }
    }
}
