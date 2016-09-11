package betterwithmods.integration;

import betterwithmods.BWCrafting;
import betterwithmods.BWRegistry;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class ModIntegration 
{
	public static void preInit() {
		if (Loader.isModLoaded("immersiveengineering"))
			ImmersiveEngineering.preInit();
	}
	public static void init()
	{
		if(Loader.isModLoaded("Natura"))
		{
			Natura.whitelistNetherBlocks();
		}
		if(Loader.isModLoaded("harvestcraft"))
			Harvestcraft.init();
		if(Loader.isModLoaded("tconstruct"))
			TConstruct.init();
		if(Loader.isModLoaded("biomesoplenty"))
			BiomesOPlenty.init();
		if(Loader.isModLoaded("quark"))
			NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation("quark", "basalt")), 0);

			//MineChem tannin orename: molecule_tannicacid
		if(Loader.isModLoaded("minechem"))
		{
			BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 1, 6), new Object[] {new ItemStack(BWRegistry.material, 1, 7), "molecule_tannicacid"});
			BWCrafting.addOreCauldronRecipe(new ItemStack(BWRegistry.material, 2, 33), new Object[] {new ItemStack(BWRegistry.material, 2, 34), "molecule_tannicacid"});
		}/*
		if(Loader.isModLoaded("immersiveengineering")) {
			Item material = Item.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "material"));
			OreDictionary.registerOre("fiberHemp", new ItemStack(material, 1, 4));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(material, 1, 5), "FFF", "FSF", "FFF", 'F', "fiberHemp", 'S', "stickWood"));
			Block woodDevice = Block.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "woodenDevice0"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(woodDevice, 1, 4), " F ", "GBG", "GGG", 'F', "fiberHemp", 'G', "gunpowder", 'B', new ItemStack(woodDevice, 1, 1)));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Block.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "stoneDecoration")), 6, 4), "CCC", "FFF", "CCC", 'C', Items.CLAY_BALL, 'F', "fiberHemp"));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "wirecoil")), 4, 3), " F ", "FSF", " F ", 'F', "fiberHemp", 'S', "stickWood"));
		}*/
		if(Loader.isModLoaded("MineTweaker3")) {
			MineTweaker.init();
		}
		OreDict.init();
	}
}
