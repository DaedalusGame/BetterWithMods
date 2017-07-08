package betterwithmods.module.compat;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.hardcore.HCTools;
import betterwithmods.module.tweaks.MobSpawning;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@SuppressWarnings("unused")
public class Quark extends CompatFeature {

    public String[] wood = {"spruce", "birch", "jungle", "acacia", "dark_oak"};

    public Quark() {
        super("quark");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MobSpawning.NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "basalt")), 0);
//        for (int i = 0; i < 5; i++)
//            BWMRecipes.addOreRecipe(new ItemStack(getBlock(new ResourceLocation(modid, "custom_chest")), 1, i), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, i + 1));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (HCTools.removeLowTools) {
            //TODO
//            BWMRecipes.removeRecipes(new ItemStack(Items.STONE_HOE));
//            BWMRecipes.removeRecipes(new ItemStack(Items.STONE_SWORD));
        }
    }

}
