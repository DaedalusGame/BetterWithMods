package betterwithmods;

import betterwithmods.api.IMultiLocations;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.blocks.mini.ItemBlockMini;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.client.BWStateMapper;
import betterwithmods.items.*;
import betterwithmods.items.tools.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

public final class BWMItems {
    public static final ToolMaterial SOULFORGED_STEEL = EnumHelper.addToolMaterial("soulforged_steel", 3, 1561, 8, 3,
            22);
    public static final Item MATERIAL = new ItemMaterial().setRegistryName("material");
    public static final Item WINDMILL = new ItemMechanical().setRegistryName("windmill");
    public static final Item BARK = new ItemBark().setRegistryName("bark");
    public static final Item DONUT = new ItemFood(2, 0.5F, false).setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("donut");
    public static final Item KNIFE = new ItemKnife(ToolMaterial.IRON).setRegistryName("knife");
    public static final Item DYNAMITE = new ItemDynamite().setRegistryName("dynamite");
    public static final Item FERTILIZER = new ItemFertilizer().setRegistryName("fertilizer");
    public static final Item STEEL_AXE = new ItemSoulforgedAxe().setRegistryName("steel_axe");
    public static final Item STEEL_HOE = new ItemSoulforgedHoe().setRegistryName("steel_hoe");
    public static final Item STEEL_PICKAXE = new ItemSoulforgedPickaxe().setRegistryName("steel_pickaxe");
    public static final Item STEEL_SHOVEL = new ItemSoulforgedShovel().setRegistryName("steel_shovel");
    public static final Item STEEL_SWORD = new ItemSoulforgedSword().setRegistryName("steel_sword");
    public static final Item CREEPER_OYSTER = (new ItemFood(2, 0.5F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 5, 0), 1)).setCreativeTab(BWCreativeTabs.BWTAB).setRegistryName("creeper_oyster");
    public static final Item ENDER_SPECTACLES = new ItemEnderSpectacles().setRegistryName("ender_spectacles");

    private BWMItems() {
    }

    public static void registerItems() {
        registerItem(MATERIAL);
        registerItem(WINDMILL);
        registerItem(BARK);
        registerItem(DONUT);
        registerItem(KNIFE);
        registerItem(DYNAMITE);
        registerItem(FERTILIZER);
        registerItem(STEEL_AXE);
        registerItem(STEEL_HOE);
        registerItem(STEEL_PICKAXE);
        registerItem(STEEL_SHOVEL);
        registerItem(STEEL_SWORD);
        registerItem(CREEPER_OYSTER);
        registerItem(ENDER_SPECTACLES);
    }


    /**
     * Register an Item.
     *
     * @param item Item instance to register.
     * @return Registered item.
     */
    public static Item registerItem(Item item) {
        //betterwithmods:name => bwm:name
        item.setUnlocalizedName("bwm" + item.getRegistryName().toString().substring(BWMod.MODID.length()));
        return GameRegistry.register(item);
    }

    ///CLIENT BEGIN
    @SideOnly(Side.CLIENT)
    public static void linkItemModels() {
        setInventoryModel(MATERIAL);
        setInventoryModel(WINDMILL);
        setInventoryModel(BARK);
        setInventoryModel(DONUT);
        setInventoryModel(KNIFE);
        setInventoryModel(DYNAMITE);
        setInventoryModel(FERTILIZER);
        setInventoryModel(STEEL_AXE);
        setInventoryModel(STEEL_HOE);
        setInventoryModel(STEEL_PICKAXE);
        setInventoryModel(STEEL_SHOVEL);
        setInventoryModel(STEEL_SWORD);
        setInventoryModel(CREEPER_OYSTER);
        setInventoryModel(ENDER_SPECTACLES);
    }

    @SideOnly(Side.CLIENT)
    private static void setModelLocation(Item item, int meta, String variantSettings) {
        setModelLocation(item, meta, item.getRegistryName().toString(), variantSettings);
    }

    @SideOnly(Side.CLIENT)
    private static void setModelLocation(Item item, int meta, String location, String variantSettings) {
        if (meta == OreDictionary.WILDCARD_VALUE) {
            ModelLoader.setCustomMeshDefinition(item,
                    stack -> new ModelResourceLocation(location, variantSettings));
        } else {
            ModelLoader.setCustomModelResourceLocation(item, meta,
                    new ModelResourceLocation(location, variantSettings));
        }
    }

    @SideOnly(Side.CLIENT)
    private static void setInventoryModel(ItemBlock item) {
        Block block = item.getBlock();
        if (item instanceof ItemBlockMini) {
            registerMiniBlockNBT((ItemBlockMini) item);
            ModelLoader.setCustomStateMapper(block, new BWStateMapper(block.getRegistryName().toString()));
        } else if (block instanceof IMultiVariants) {
            ModelLoader.setCustomStateMapper(block, new BWStateMapper(block.getRegistryName().toString()));
            String[] variants = ((IMultiVariants) block).getVariants();
            for (int meta = 0; meta < variants.length; meta++) {
                if (!Objects.equals(variants[meta], "")) setModelLocation(item, meta, variants[meta]);
            }
        } else if (block instanceof IMultiLocations) {
            String[] locations = ((IMultiLocations) block).getLocations();
            for (int meta = 0; meta < locations.length; meta++) {
                String location = locations[meta];
                setModelLocation(item, meta, BWMod.MODID + ":" + location, "inventory");
            }
        } else {
            setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setInventoryModel(Item item) {
        if (item.isDamageable()) {
            setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
        } else if (item instanceof IMultiLocations) {
            IMultiLocations bwmItem = (IMultiLocations) item;
            String[] locations = bwmItem.getLocations();
            for (int meta = 0; meta < locations.length; meta++) {
                String location = locations[meta];
                setModelLocation(item, meta, BWMod.MODID + ":" + location, "inventory");
            }
        } else if (item instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) item;
            setInventoryModel(itemBlock);
        } else {
            setModelLocation(item, OreDictionary.WILDCARD_VALUE, "inventory");
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerMiniBlockNBT(ItemBlockMini item) {
        //TODO use more of the BlockPlanks.EnumType instead of metadata
        ModelLoader.setCustomMeshDefinition(item,
                stack -> (stack.hasTagCompound() && stack.getTagCompound().hasKey("type")) ? new ModelResourceLocation(
                        item.getRegistryName() + "_"
                                + BlockPlanks.EnumType.byMetadata(stack.getTagCompound().getInteger("type")).getName(),
                        "inventory")
                        : stack.getItemDamage() < 6
                        ? new ModelResourceLocation(
                        item.getRegistryName() + "_"
                                + BlockPlanks.EnumType.byMetadata(stack.getItemDamage()).getName(),
                        "inventory")
                        : new ModelResourceLocation(item.getRegistryName() + "_oak", "inventory"));
        ModelResourceLocation[] resourceLocations = new ModelResourceLocation[6];
        for (int i = 0; i < 6; i++)
            resourceLocations[i] = new ModelResourceLocation(item.getRegistryName() + "_" + BlockPlanks.EnumType.byMetadata(i),
                    "inventory");
        ModelBakery.registerItemVariants(item, (ResourceLocation[]) resourceLocations);
    }
    ///CLIENT END
}
