package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemBark;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.module.Feature;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 5/16/17.
 */
public class CauldronRecipes extends Feature {
    public CauldronRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 8), "dustPotash", new OreStack("dustHellfire", 4));
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL, 4), "dustHellfire", "dustCoal");
        //Flour OreDict is foodFlour, Donuts need sugar
        addCauldronRecipe(new ItemStack(BWMItems.DONUT, 4, 0), "foodFlour", Items.SUGAR);
        addCauldronRecipe(new ItemStack(Items.BREAD), "foodFlour");
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), new OreStack("dustHellfire", 8));
        addCauldronRecipe(new ItemStack(Items.DYE, 1, 2), "blockCactus");
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FILAMENT), "string", "dustGlowstone", "dustRedstone");
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.ELEMENT), Items.BLAZE_POWDER, "dustRedstone", "string");
        String[] barkNames = {"barkOak", "barkSpruce", "barkBirch", "barkJungle", "barkAcacia", "barkDarkOak"};
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            int meta = type.getMetadata();
            addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER), new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER), new OreStack(barkNames[meta], ItemBark.getTanningStackSize(meta))});
            addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2), new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2), new OreStack(barkNames[meta], ItemBark.getTanningStackSize(meta))});
        }
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER), new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER), "dung"});
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2), new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2), "dung"});
        addCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), "dustSulfur", "dustSaltpeter", "dustCharcoal");
        addCauldronRecipe(new ItemStack(Items.GUNPOWDER, 2, 0), "dustSulfur", "dustSaltpeter", "dustCoal");
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL, 2), "dustHellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW));
        addCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FUSE), Items.GUNPOWDER, "string");
        addCauldronRecipe(new ItemStack(BWMBlocks.AESTHETIC, 4, BlockAesthetic.EnumType.CHOPBLOCK.getMeta()), new Object[]{new ItemStack(BWMBlocks.AESTHETIC, 4, BlockAesthetic.EnumType.CHOPBLOCKBLOOD.getMeta()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP)});
        addCauldronRecipe(new ItemStack(Blocks.PISTON,4), new Object[]{new ItemStack(Blocks.STICKY_PISTON,4), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP)});

        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{new ItemStack(Items.LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_STRAP, 8)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT, 2)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2),new Object[]{new ItemStack(Items.LEATHER_HELMET, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 2),new Object[]{new ItemStack(Items.LEATHER_BOOTS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 3),new Object[]{new ItemStack(Items.LEATHER_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE, 4),new Object[]{new ItemStack(Items.LEATHER_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.COOKED_PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.PORKCHOP)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.COOKED_BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.BEEF, 4)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.COOKED_MUTTON, 6)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),new Object[]{new ItemStack(Items.ROTTEN_FLESH, 10)});
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP),new Object[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TALLOW),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH)});

        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH), "logWood");
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH), new OreStack("plankWood", 6));
        addStokedCauldronRecipe(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH), new OreStack("dustWood", 16));
        addCauldronRecipe(new ItemStack(BWMItems.CHICKEN_SOUP, 3),new Object[]{new ItemStack(Items.COOKED_CHICKEN), new ItemStack(Items.CARROT), new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.BOWL, 3)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOCOLATE, 2), new ItemStack(Items.BUCKET), "foodCocoapowder", new ItemStack(Items.SUGAR), new ItemStack(Items.MILK_BUCKET));
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET),new Object[]{new ItemStack(Items.COOKED_FISH), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addCauldronRecipe(new ItemStack(BWMItems.CHOWDER, 2), new ItemStack(Items.BUCKET),new Object[]{new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), new ItemStack(Items.MILK_BUCKET), new ItemStack(Items.BOWL, 2)});
        addCauldronRecipe(new ItemStack(BWMItems.HEARTY_STEW, 5), "listAllmeatcooked", Items.CARROT, Items.BAKED_POTATO, new ItemStack(Items.BOWL, 5), new ItemStack(Blocks.BROWN_MUSHROOM, 3), "foodFlour");
        addCauldronRecipe(new ItemStack(BWMItems.KIBBLE,2), new ItemStack(Items.DYE,4, EnumDyeColor.WHITE.getMetadata()),new ItemStack(Items.ROTTEN_FLESH,4), new ItemStack(Items.SUGAR));
    }

    public static void addCauldronRecipe(ItemStack output, Object... inputs) {
        CauldronManager.getInstance().addRecipe(output, inputs);
    }

    public static void addCauldronRecipe(ItemStack output, ItemStack secondary, Object... inputs) {
        CauldronManager.getInstance().addRecipe(output, secondary, inputs);
    }

    public static void addStokedCauldronRecipe(ItemStack output, Object... inputs) {
        StokedCauldronManager.getInstance().addRecipe(output, inputs);
    }

    public static void addStokedCauldronRecipe(ItemStack output, ItemStack secondary, Object... inputs) {
        StokedCauldronManager.getInstance().addRecipe(output, secondary, inputs);
    }
}
