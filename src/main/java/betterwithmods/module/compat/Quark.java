package betterwithmods.module.compat;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.hardcore.HCTools;
import betterwithmods.module.tweaks.MobSpawning;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

@SuppressWarnings("unused")
public class Quark extends CompatFeature {

    public String[] wood = {"spruce", "birch", "jungle", "acacia", "dark_oak"};

    public Quark() {
        super("quark");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MobSpawning.NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "basalt")), 0);
        addHERecipe(new ShapedOreRecipe(null, new ItemStack(Blocks.CHEST), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING)).setRegistryName(new ResourceLocation("betterwithmods", "chest")));
        for (int i = 0; i < 5; i++)
            addHERecipe(new ShapedOreRecipe(null, new ItemStack(getBlock(new ResourceLocation(modid, "custom_chest")), 1, i), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, i + 1)).setRegistryName("betterwithmods", "quark_chest." + (i + 1)));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (HCTools.removeLowTools) {
            //TODO
//            BWMRecipes.removeRecipes(new ItemStack(Items.STONE_HOE));
//            BWMRecipes.removeRecipes(new ItemStack(Items.STONE_SWORD));
        }
    }

    private IRecipe addHERecipe(IRecipe recipe) {
        return BWMRecipes.addHardcoreRecipe("quark", recipe);
    }

}
