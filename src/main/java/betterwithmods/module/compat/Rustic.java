package betterwithmods.module.compat;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.gameplay.SawRecipes;
import betterwithmods.module.hardcore.HCSaw;
import betterwithmods.module.tweaks.HighEfficiencyRecipes;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static betterwithmods.common.BWMBlocks.registerBlock;
import static betterwithmods.common.BWMBlocks.setInventoryModel;
import static betterwithmods.common.BWOreDictionary.registerOre;

/**
 * Created by tyler on 5/27/17.
 */
@SuppressWarnings("unused")
public class Rustic extends CompatFeature {
    public static final Block SIDING = new BlockSiding(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_siding");
    public static final Block MOULDING = new BlockMoulding(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_moulding");
    public static final Block CORNER = new BlockCorner(Material.ROCK) {
        @Override
        public int getUsedTypes() {
            return 3;
        }

        @Override
        public Material getMaterial(IBlockState state) {
            int type = state.getValue(BlockMini.TYPE);
            return type < 2 ? BlockMini.MINI : Material.ROCK;
        }
    }.setRegistryName("rustic_compat_corner");
    public String[] woods = {"oak", "spruce", "birch", "jungle", "acacia", "big_oak", "olive", "ironwood"};

    public Rustic() {
        super("rustic");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        registerBlock(SIDING, new ItemBlockMini(SIDING));
        registerBlock(MOULDING, new ItemBlockMini(MOULDING));
        registerBlock(CORNER, new ItemBlockMini(CORNER));
        ItemBark.barks.add("olive");
        ItemBark.barks.add("ironwood");
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        setInventoryModel(SIDING);
        setInventoryModel(MOULDING);
        setInventoryModel(CORNER);

    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ItemStack rope = new ItemStack(getBlock(new ResourceLocation("rustic", "rope")));
        RecipeUtils.removeRecipes(rope);
        RecipeUtils.addOreRecipe(rope, "F", "F", "F", 'F', "fiberHemp");
        RecipeUtils.addOreRecipe(new ItemStack(getBlock(new ResourceLocation("rustic", "candle")), 6), "S", "T", "I", 'S', "string", 'T', "tallow", 'I', "ingotIron");
        Block plank = getBlock("rustic:planks");
        Block log = getBlock("rustic:log");
        for (int i = 0; i < 2; i++) {
            SawRecipes.addSawRecipe(log, i, new ItemStack[]{new ItemStack(plank, 4, i), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2), ItemBark.getStack(woods[i + 6], 1)});
            SawRecipes.addSawRecipe(plank, i, new ItemStack(SIDING, 2, i));
            SawRecipes.addSawRecipe(SIDING, i, new ItemStack(MOULDING, 2, i));
            SawRecipes.addSawRecipe(MOULDING, i, new ItemStack(CORNER, 2, i));
            SawRecipes.addSawRecipe(CORNER, i, ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR, 2));
            RecipeUtils.addOreRecipe(new ItemStack(plank, 1, i), "MM", 'M', new ItemStack(SIDING, 1, i));
            RecipeUtils.addOreRecipe(new ItemStack(SIDING, 1, i), "MM", 'M', new ItemStack(MOULDING, 1, i));
            RecipeUtils.addOreRecipe(new ItemStack(MOULDING, 1, i), "MM", 'M', new ItemStack(CORNER, 1, i));
        }
        boolean isHCSawEnabled = ModuleLoader.isFeatureEnabled(HCSaw.class);
        Block wooden_stake = getBlock("rustic:crop_stake");
        if (isHCSawEnabled) {
            RecipeUtils.removeRecipes(wooden_stake);
        }
        RecipeUtils.addOreRecipe(new ItemStack(wooden_stake, 3), "M", "M", "M", 'M', "mouldingWood");
        if (ModuleLoader.isFeatureEnabled(HighEfficiencyRecipes.class)) {
            for (int i = 0; i < woods.length; i++) {
                ItemStack moulding = i >= 6 ? new ItemStack(MOULDING, 1, i - 6) : new ItemStack(BWMBlocks.WOOD_MOULDING, 1, i);
                ItemStack siding = i >= 6 ? new ItemStack(SIDING, 1, i - 6) : new ItemStack(BWMBlocks.WOOD_SIDING, 1, i);
                ItemStack chair = new ItemStack(getBlock("rustic:chair_" + woods[i]), 4);
                ItemStack table = new ItemStack(getBlock("rustic:table_" + woods[i]), 2);
                if (isHCSawEnabled) {
                    RecipeUtils.removeRecipes(chair);
                    RecipeUtils.removeRecipes(table);
                }
                RecipeUtils.addOreRecipe(chair, "S  ", "SSS", "M M", 'S', siding, 'M', moulding);
                RecipeUtils.addOreRecipe(table, "SSS", "M M", 'S', siding, 'M', moulding);
                if (i >= 6) {
                    ItemStack fencegate = new ItemStack(getBlock("rustic:fence_gate_" + woods[i]));
                    ItemStack fence = new ItemStack(getBlock("rustic:fence_" + woods[i]), 3);
                    if (isHCSawEnabled) {
                        RecipeUtils.removeRecipes(fencegate);
                        RecipeUtils.removeRecipes(fence);
                    }
                    RecipeUtils.addOreRecipe(fencegate, "MSM", 'S', siding, 'M', moulding);
                    RecipeUtils.addOreRecipe(fence, "MMM", 'M', moulding);
                }
            }
        }
        AnvilRecipes.addSteelShapedRecipe(new ItemStack(SIDING, 8, 2), "SSSS", 'S', getBlock("rustic:slate"));
        AnvilRecipes.addSteelShapedRecipe(new ItemStack(MOULDING, 8, 2), "SSSS", 'S', new ItemStack(SIDING, 1, 2));
        AnvilRecipes.addSteelShapedRecipe(new ItemStack(CORNER, 8, 2), "SSSS", 'S', new ItemStack(MOULDING, 1, 2));

        registerOre("sidingWood", new ItemStack(SIDING, 1, 0), new ItemStack(SIDING, 1, 1));
        registerOre("mouldingWood", new ItemStack(MOULDING, 1, 0), new ItemStack(MOULDING, 1, 1));
        registerOre("cornerWood", new ItemStack(CORNER, 1, 0), new ItemStack(CORNER, 1, 1));
    }
}

