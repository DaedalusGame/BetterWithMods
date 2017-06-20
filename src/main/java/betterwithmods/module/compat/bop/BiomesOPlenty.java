package betterwithmods.module.compat.bop;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.CompatFeature;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.module.hardcore.HCPiles;
import betterwithmods.module.hardcore.HCSeeds;
import betterwithmods.module.tweaks.HighEfficiencyRecipes;
import betterwithmods.module.tweaks.MobSpawning.NetherSpawnWhitelist;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("unused")
public class BiomesOPlenty extends CompatFeature {
    public static Item PILES = new ItemBOPPile().setRegistryName("bop_piles");
    public static Block SIDING = new BlockSiding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }
    }.setRegistryName("bop_compat_siding");
    public static Block MOULDING = new BlockMoulding(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }

    }.setRegistryName("bop_compat_moulding");

    public static Block CORNER = new BlockCorner(BlockMini.MINI) {
        @Override
        public int getUsedTypes() {
            return 16;
        }

    }.setRegistryName("bop_compat_corner");
    public final String[] woods = new String[]{"sacred_oak", "cherry", "umbran", "fir", "ethereal", "magic", "mangrove", "palm", "redwood", "willow", "pine", "hellbark", "jacaranda", "mahogany", "ebony", "eucalyptus",};

    public BiomesOPlenty() {
        super("biomesoplenty");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        BWMItems.registerItem(PILES);

        BWMBlocks.registerBlock(SIDING, new ItemBlockMini(SIDING));
        BWMBlocks.registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        BWMBlocks.registerBlock(CORNER, new ItemBlockMini(CORNER));

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        BWMItems.setInventoryModel(PILES);
        BWMBlocks.setInventoryModel(SIDING);
        BWMBlocks.setInventoryModel(MOULDING);
        BWMBlocks.setInventoryModel(CORNER);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "grass")), 1);
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "grass")), 6);
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "flesh")));
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "ash_block")));

        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(0));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(1));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(7));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(8));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 5, new ItemStack(BWMItems.DIRT_PILE, 3));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 7, new ItemStack(BWMItems.DIRT_PILE, 3));

        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 0, new ItemStack(PILES, 3, 0));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 1, new ItemStack(PILES, 3, 1));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_1")), 0, new ItemStack(PILES, 3, 2));

        for (int i = 2; i <= 4; i++)
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), i, new ItemStack(PILES, 3, i - 2));
        for (int i = 0; i <= 2; i++) {
            Block dirt = getBlock(new ResourceLocation(modid, "dirt"));
            HCPiles.registerPile(dirt, i, new ItemStack(PILES, 3, i));
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass_path")), i, new ItemStack(PILES, 3, i));

            RecipeUtils.addOreRecipe(new ItemStack(dirt, 1, i), "PP", "PP", 'P', new ItemStack(PILES, 1, i));
        }

        Block plank = getBlock("biomesoplenty:planks_0");
        for (int i = 0; i < 16; i++) {
            SawRecipes.addSawRecipe(plank, i, new ItemStack(SIDING, 2, i));
            SawRecipes.addSawRecipe(SIDING, i, new ItemStack(MOULDING, 2, i));
            SawRecipes.addSawRecipe(MOULDING, i, new ItemStack(CORNER, 2, i));
            SawRecipes.addSawRecipe(CORNER, i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2));
        }
        if (ModuleLoader.isFeatureEnabled(HighEfficiencyRecipes.class)) {
            for (int i = 0; i < woods.length; i++) {
                ItemStack moulding = new ItemStack(MOULDING, 1, i);
                ItemStack siding = new ItemStack(SIDING, 1, i);
                RecipeUtils.addOreRecipe(new ItemStack(getBlock("biomesoplenty:" + woods[i] + "_fence_gate")), "MSM", 'S', siding, 'M', moulding);
                RecipeUtils.addOreRecipe(new ItemStack(getBlock("biomesoplenty:" + woods[i] + "_fence"), 3), "MMM", 'M', moulding);
                RecipeUtils.addOreRecipe(new ItemStack(getBlock("biomesoplenty:" + woods[i] + "_door")), "SS", "SS", "SS", 'S', siding);
                RecipeUtils.addOreRecipe(new ItemStack(getBlock("biomesoplenty:" + woods[i] + "_stairs")), "M ", "MM", 'M', moulding).setMirrored(true);
            }
        }

        BWOreDictionary.registerOre("sidingWood", new ItemStack(SIDING, 1, OreDictionary.WILDCARD_VALUE));
        BWOreDictionary.registerOre("mouldingWood", new ItemStack(MOULDING, 1, OreDictionary.WILDCARD_VALUE));
        BWOreDictionary.registerOre("cornerWood", new ItemStack(CORNER, 1, OreDictionary.WILDCARD_VALUE));
    }
}
