package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.util.RecipeUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 4/20/17.
 */
public class HCTools extends Feature {


    private static int woodDurability;
    private static int stoneDurability;
    private static int ironDurability;
    private static int diamondDurability;
    private static int goldDurability;

    public static boolean earlyPickaxesRebalance;
    public static boolean removeLowTools;


    @Override
    public String getFeatureDescription() {
        return "Overhaul the durability of tools to be more rewarding when reaching the next level. Completely disables wooden tools (other than pick) by default.";
    }

    @Override
    public void setupConfig() {
        earlyPickaxesRebalance = loadPropBool("Early pickaxes rebalance", "Wooden Pickaxe will have 1 usage and Stone Pickaxe 6.", true);
        removeLowTools = loadPropBool("Remove cheapest tools", "The minimum level of the hoe and the sword is iron, and the axe needs at least stone.", true);

        woodDurability = loadPropInt("Hardcore Hardness Wood Durability", "Number of usages for wooden tools. Does not change Pickaxe if earlyPickaxesRebalanced is enabled", "", 1,1, 60);
        stoneDurability = loadPropInt("Hardcore Hardness Stone Durability", "Number of usages for stone tools. Does not change Pickaxe if earlyPickaxesRebalanced is enabled", "", 50, 2, 132);
        ironDurability = loadPropInt("Hardcore Hardness Iron Durability", "Number of usages for iron tools.", "", 500, 2, 251);
        diamondDurability = loadPropInt("Hardcore Hardness Diamond Durability", "Number of usages for diamond tools.", "", 1561, 2, 1562);
        goldDurability = loadPropInt("Hardcore Hardness Gold Durability", "Number of usages for golden tools.", "", 32, 2, 33);
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (removeLowTools)
            removeLowTierToolRecipes();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        changeVanillaToolMaterials();
        if(earlyPickaxesRebalance) {
            Items.WOODEN_PICKAXE.setMaxDamage(1);
            Items.STONE_PICKAXE.setMaxDamage(6);
        }
    }

     /**
     * New values for {@link net.minecraft.item.Item.ToolMaterial}
     */
    private enum ToolMaterialOverride {
        WOOD(woodDurability, 1.01F, 0),
        STONE(stoneDurability - 1, 1.01F, 5),
        IRON(ironDurability - 1, 6.0F, 14),
        DIAMOND(diamondDurability - 1, 8.0F, 14),
        GOLD(goldDurability - 1, 12.0F, 22);
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final int enchantability;

        ToolMaterialOverride(int maxUses, float efficiency, int enchantability) {
            this.maxUses = maxUses;
            this.efficiencyOnProperMaterial = efficiency;
            this.enchantability = enchantability;
        }

        public static ToolMaterialOverride getOverride(Item.ToolMaterial material) {
            switch (material) {
                case WOOD:
                    return ToolMaterialOverride.WOOD;
                case STONE:
                    return ToolMaterialOverride.STONE;
                case IRON:
                    return ToolMaterialOverride.IRON;
                case DIAMOND:
                    return ToolMaterialOverride.DIAMOND;
                case GOLD:
                    return ToolMaterialOverride.GOLD;
                default:
                    return null;
            }
        }

        public int getMaxUses() {
            return this.maxUses;
        }

        public float getEfficiencyOnProperMaterial() {
            return this.efficiencyOnProperMaterial;
        }

        public int getEnchantability() {
            return this.enchantability;
        }
    }

    /**
     * Edit the values of {@link Item.ToolMaterial}.
     * The new values are described in {@link ToolMaterialOverride}.
     */
    private static void changeVanillaToolMaterials() {
        // Edit materials
        for (Item.ToolMaterial material : Item.ToolMaterial.values()) {
            ToolMaterialOverride newValues = ToolMaterialOverride.getOverride(material);
            if (newValues == null) continue;
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getMaxUses(), "field_78002_g", "maxUses");
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getEfficiencyOnProperMaterial(), "field_78010_h", "efficiencyOnProperMaterial");
            ReflectionHelper.setPrivateValue(Item.ToolMaterial.class, material, newValues.getEnchantability(), "field_78008_j", "enchantability");
        }
        // Change values already taken from material at that time
        for (Item item : Item.REGISTRY) {
            if (!(item instanceof ItemTool)) continue;
            ItemTool tool = (ItemTool) item;
            ToolMaterialOverride newValues = ToolMaterialOverride.getOverride(tool.getToolMaterial());
            if (newValues == null) continue;
            tool.setMaxDamage(newValues.getMaxUses());
            ReflectionHelper.setPrivateValue(ItemTool.class, tool, newValues.getEfficiencyOnProperMaterial(), "field_77864_a", "efficiencyOnProperMaterial");
        }
    }

    /**
     * Sets the wooden pickaxe to 1 usage. Why:
     * {@link Item#setMaxDamage} used with "1" gives 2 usages, and with "0" gives unbreakable item.
     * So we needed another solution to set it to 1 usage.
     */
    @SubscribeEvent
    public void onBreaking(BlockEvent.BreakEvent event) {
        if (!earlyPickaxesRebalance) return;
        EntityPlayer player = event.getPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (stack == ItemStack.EMPTY || stack.getItem() == null) return;
        if (stack.getItem() == Items.WOODEN_PICKAXE) {
            destroyItem(stack, player);
        }
    }

    private void destroyItem(ItemStack stack, EntityLivingBase entity) {
        //FIXME No sound triggered.
        int damage = stack.getMaxDamage();
        stack.damageItem(damage, entity);
    }

    public static void removeLowTierToolRecipes() {
        RecipeUtils.removeRecipes(Items.WOODEN_AXE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.WOODEN_HOE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.WOODEN_SWORD, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.STONE_HOE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.STONE_SWORD, OreDictionary.WILDCARD_VALUE);
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
