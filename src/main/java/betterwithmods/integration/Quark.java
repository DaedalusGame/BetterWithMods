package betterwithmods.integration;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.hardcore.HCTools;
import betterwithmods.module.tweaks.MobSpawning;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
public class Quark extends CompatFeature {

    public String[] wood = {"spruce", "birch", "jungle", "acacia", "dark_oak"};

    public Quark() {
        super("quark");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MobSpawning.NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "basalt")), 0);
        for (int i = 0; i < 5; i++)
            GameRegistry.addShapedRecipe(new ItemStack(getBlock(new ResourceLocation(modid, "custom_chest")), 1, i), "SSS", "S S", "SSS", 'S', new ItemStack(BWMBlocks.WOOD_SIDING, 1, i + 1));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (HCTools.removeLowTools) {
            RecipeUtils.removeRecipes(new ItemStack(Items.STONE_HOE));
            RecipeUtils.removeRecipes(new ItemStack(Items.STONE_SWORD));
        }
    }

}
