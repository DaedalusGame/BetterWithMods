package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.anvil.ShapedAnvilRecipe;
import betterwithmods.common.registry.anvil.ShapelessAnvilRecipe;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCTools;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 5/16/17.
 */
public class AnvilRecipes extends Feature {
    public AnvilRecipes() {
        canDisable = false;
    }

    public static ShapedAnvilRecipe addSteelShapedRecipe(ResourceLocation recipeName, ItemStack output, Object... input) {
        if (recipeName.getResourceDomain().equals("minecraft"))
            recipeName = new ResourceLocation("betterwithmods", recipeName.getResourcePath());
        ShapedAnvilRecipe recipe = new ShapedAnvilRecipe(null, output, input);
        addAnvilRecipe(recipe.setRegistryName(recipeName));
        return recipe;
    }

    public static ShapelessAnvilRecipe addSteelShapelessRecipe(ResourceLocation recipeName, ItemStack output, Object... input) {
        if (recipeName.getResourceDomain().equals("minecraft"))
            recipeName = new ResourceLocation("betterwithmods", recipeName.getResourcePath());
        ShapelessAnvilRecipe recipe = new ShapelessAnvilRecipe(null, output, input);
        addAnvilRecipe(recipe.setRegistryName(recipeName));
        return recipe;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addSteelShapedRecipe(new ResourceLocation("block_dispenser"), new ItemStack(BWMBlocks.BLOCK_DISPENSER), "MMMM", "MUUM", "STTS", "SRRS", 'M', Blocks.MOSSY_COBBLESTONE, 'U', new ItemStack(BWMBlocks.URN, 1, 8), 'S', "stone", 'R', "dustRedstone", 'T', Blocks.REDSTONE_TORCH);
        addSteelShapedRecipe(new ResourceLocation("buddy_block"), new ItemStack(BWMBlocks.BUDDY_BLOCK), "SSLS", "LTTS", "STTL", "SLSS", 'S', "stone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POLISHED_LAPIS));
        addSteelShapedRecipe(new ResourceLocation("detector"), new ItemStack(BWMBlocks.DETECTOR), "CCCC", "LTTL", "SRRS", "SRRS", 'C', "cobblestone", 'L', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POLISHED_LAPIS), 'T', Blocks.REDSTONE_TORCH, 'S', "stone", 'R', "dustRedstone");
        addSteelShapedRecipe(new ResourceLocation("steel_helmet"), new ItemStack(BWMItems.STEEL_HELMET), "SSSS", "S  S", "S  S", " PP ", 'P', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("steel_chest"), new ItemStack(BWMItems.STEEL_CHEST), "P  P", "SSSS", "SSSS", "SSSS", 'P', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("steel_pants"), new ItemStack(BWMItems.STEEL_PANTS), "SSSS", "PSSP", "P  P", "P  P", 'P', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("steel_boots"), new ItemStack(BWMItems.STEEL_BOOTS), " SS ", " SS ", "SPPS", 'P', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE), 'S', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("polished_lapis"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POLISHED_LAPIS, 2), "LLL", "LLL", "GGG", " R ", 'L', "gemLapis", 'R', "dustRedstone", 'G', "nuggetGold");
        if (ModuleLoader.isFeatureEnabled(HCTools.class)) {
            addSteelShapedRecipe(new ResourceLocation("steel_axe"), new ItemStack(BWMItems.STEEL_AXE), "X ", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        } else {
            addSteelShapedRecipe(new ResourceLocation("steel_axe"), new ItemStack(BWMItems.STEEL_AXE), "XX", "XH", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        }
        addSteelShapedRecipe(new ResourceLocation("steel_hoe"), new ItemStack(BWMItems.STEEL_HOE), "XX", " H", " H", " H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("steel_pickaxe"), new ItemStack(BWMItems.STEEL_PICKAXE), "XXX", " H ", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("steel_shovel"), new ItemStack(BWMItems.STEEL_SHOVEL), "X", "H", "H", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("steel_sword"), new ItemStack(BWMItems.STEEL_SWORD), "X", "X", "X", "H", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("steel_battleaxe"), new ItemStack(BWMItems.STEEL_BATTLEAXE), "XXX", "XHX", " H ", " H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("steel_mattock"), new ItemStack(BWMItems.STEEL_MATTOCK), " XXX", "X H ", "  H ", "  H ", 'X', "ingotSoulforgedSteel", 'H', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        addSteelShapedRecipe(new ResourceLocation("armor_plate"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ARMOR_PLATE), "BSPB", 'B', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP), 'S', "ingotSoulforgedSteel", 'P', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.PADDING));
        addSteelShapedRecipe(new ResourceLocation("broadhead"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BROADHEAD, 6), " N ", " N ", "NNN", " N ", 'N', "nuggetSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("steel_block"), new ItemStack(BWMBlocks.AESTHETIC, 1, 2), "XXXX", "XXXX", "XXXX", "XXXX", 'X', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("chopping_block"), new ItemStack(BWMBlocks.AESTHETIC, 6, 0), "X  X", "XXXX", 'X', "stone");
        addSteelShapedRecipe(new ResourceLocation("chain_mail"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHAIN_MAIL, 2), "N N ", " N N", "N N ", " N N", 'N', "nuggetIron");
        addSteelShapedRecipe(new ResourceLocation("steel_gear"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_GEAR), " NN ", "NIIN", "NIIN", " NN ", 'N', "nuggetSoulforgedSteel", 'I', "ingotSoulforgedSteel");
        addSteelShapedRecipe(new ResourceLocation("steel_spring"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.STEEL_SPRING), "NNN", "NNN", "NNN", "NNN", 'N', "nuggetSoulforgedSteel");
        for (BlockMini.EnumType type : BlockMini.EnumType.values()) {
            addSteelShapedRecipe(new ResourceLocation("stone_wall." + type.getMetadata()), new ItemStack(BWMBlocks.STONE_SIDING, 8, type.getMetadata()), "XXXX", 'X', type.getBlock());
            addSteelShapedRecipe(new ResourceLocation("stone_moulding." + type.getMetadata()), new ItemStack(BWMBlocks.STONE_MOULDING, 8, type.getMetadata()), "XXXX", 'X', new ItemStack(BWMBlocks.STONE_SIDING, 1, type.getMetadata()));
            addSteelShapedRecipe(new ResourceLocation("stone_corner." + type.getMetadata()), new ItemStack(BWMBlocks.STONE_CORNER, 8, type.getMetadata()), "XXXX", 'X', new ItemStack(BWMBlocks.STONE_MOULDING, 1, type.getMetadata()));
        }
        addSteelShapedRecipe(new ResourceLocation("iron_wall"), new ItemStack(BWMBlocks.IRON_WALL, 8, 0), "XXXX", "XXXX", "XXXX", "XXXX", 'X', new ItemStack(Blocks.IRON_BARS));
        addSteelShapedRecipe(new ResourceLocation("steel_gearbox"), new ItemStack(BWMBlocks.STEEL_GEARBOX), "SGSS", "SGLG", "GLGS", "SSGS", 'S', "ingotSoulforgedSteel", 'G', "gearSoulforgedSteel", 'L', "latchRedstone");
    }

    private static IRecipe addAnvilRecipe(IRecipe recipe) {
        return registerHardcoreRecipe("Anvil", recipe);
    }
}
