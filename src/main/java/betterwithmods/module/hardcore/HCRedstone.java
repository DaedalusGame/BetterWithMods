package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.CrucibleRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by tyler on 4/20/17.
 */
public class HCRedstone extends Feature {


    public static boolean stoneDeviceRecipesAnvil;

    @Override
    public void setupConfig() {
        stoneDeviceRecipesAnvil = loadPropBool("Stone Device Recipes Require Anvil", "Makes it so stone buttons and pressure plates require cut stone, which must be done in the anvil", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Changes the recipes for Redstone devices to be more complex";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ItemStack LATCH = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH);
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.STONE_BUTTON, "C", "R", 'C', new ItemStack(BWMBlocks.STONE_CORNER, 1, BlockMini.EnumType.STONE.getMetadata()), 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "stone_button")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.STONE_PRESSURE_PLATE, "S", "R", 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata()), 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "stone_pressure_plate")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.DISPENSER, "CCC", "CBC", "CRC", 'C', "cobblestone", 'B', Items.BOW, 'R', LATCH).setRegistryName(new ResourceLocation("minecraft", "dispenser")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.DROPPER, "CCC", "C C", "CRC", 'C', "cobblestone", 'R', LATCH).setRegistryName(new ResourceLocation("minecraft", "dropper")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Items.IRON_DOOR, 3), "RII", " II", "RII", 'I', "ingotIron", 'R', LATCH).setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "iron_door")));
        addHardcoreRecipe(new ShapedOreRecipe(null, new ItemStack(Blocks.IRON_TRAPDOOR, 2), "RII", "RII", 'I', "ingotIron", 'R', LATCH).setMirrored(true).setRegistryName(new ResourceLocation("minecraft", "iron_trapdoor")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.LEVER, "S", "C", "R", 'S', "stickWood", 'C', "cobblestone", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "lever")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.PISTON, "WIW", "CSC", "CRC", 'W', "sidingWood", 'I', "ingotIron", 'C', "cobblestone", 'S', new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumType.FULL.getMeta()), 'R', LATCH).setRegistryName(new ResourceLocation("minecraft", "piston")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.TRIPWIRE_HOOK, "I", "M", "R", 'I', "nuggetIron", 'M', "mouldingWood", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "tripwire_hook")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.WOODEN_BUTTON, "C", "R", 'C', "cornerWood", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "wooden_button")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.WOODEN_PRESSURE_PLATE, "S", "R", 'S', "sidingWood", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "wooden_pressure_plate")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Items.REPEATER, "RCR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'C', Items.CLOCK, 'S', "stone").setRegistryName("minecraft", "repeater"));
        addHardcoreRecipe(new ShapedOreRecipe(null, Items.REPEATER, "RCR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'C', Items.CLOCK, 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata())).setRegistryName("betterwithmods", "repeater"));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "II", "RR", 'I', "ingotIron", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "heavy_weighted_pressure_plate")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "II", "RR", 'I', "ingotGold", 'R', "dustRedstone").setRegistryName(new ResourceLocation("minecraft", "light_weighted_pressure_plate")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.HOPPER, "ICI", "IRI", " I ", 'I', "ingotIron", 'C', "chestWood", 'R', LATCH).setRegistryName(new ResourceLocation("minecraft", "hopper")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Items.COMPARATOR, " R ", "RQR", "SSS", 'R', Blocks.REDSTONE_TORCH, 'Q', "gemQuartz", 'S', new ItemStack(BWMBlocks.STONE_SIDING, 1, BlockMini.EnumType.STONE.getMetadata())).setRegistryName(new ResourceLocation("minecraft", "comparator")));
        addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.OBSERVER, "CCC", "LLU", "CCC", 'C', "cobblestone", 'L', LATCH, 'U', BlockUrn.getStack(BlockUrn.EnumType.FULL, 1)).setRegistryName(new ResourceLocation("minecraft", "observer")));
        if (!stoneDeviceRecipesAnvil) {
            addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.STONE_BUTTON, "S", "R", 'S', "stone", 'R', LATCH).setRegistryName(new ResourceLocation("betterwithmods", "stone_button")));
            addHardcoreRecipe(new ShapedOreRecipe(null, Blocks.STONE_PRESSURE_PLATE, "SS", "RR", 'S', "stone", 'R', LATCH).setRegistryName(new ResourceLocation("betterwithmods", "stone_pressure_plate")));
        }

        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 4, 0), new Object[]{new ItemStack(Blocks.IRON_TRAPDOOR, 2)});

    }

    private IRecipe addHardcoreRecipe(IRecipe recipe) {
        return registerHardcoreRecipe("HCRedstone", recipe);
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        CrucibleRecipes.addStokedCrucibleRecipe(new ItemStack(Items.IRON_INGOT, 6, 0), new Object[]{new ItemStack(Blocks.IRON_TRAPDOOR, 2)});
    }
}
