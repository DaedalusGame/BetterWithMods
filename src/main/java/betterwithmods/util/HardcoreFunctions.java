package betterwithmods.util;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Various static methods that handle various hardcore things.
 *
 * @author Koward
 */
public final class HardcoreFunctions {
    private HardcoreFunctions() {
    }

    public static void applyHCHardness() {
        Blocks.FIRE.setFireInfo(Blocks.LEAVES, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 60, 100);
        rebalanceVanillaHardness();
        changeVanillaToolMaterials();
        ToolsManager.setAxesAsEffectiveAgainst(Blocks.COCOA, Blocks.SKULL, Blocks.LEAVES, Blocks.LEAVES2,
                Blocks.VINE, Blocks.WEB, Blocks.CACTUS);
        ToolsManager.setPickaxesAsEffectiveAgainst(Blocks.LEVER, Blocks.GLASS, Blocks.STAINED_GLASS, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE,
                Blocks.STONE_BUTTON, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_EXTENSION,
                Blocks.GLOWSTONE, Blocks.BEACON, Blocks.MONSTER_EGG,
                Blocks.REDSTONE_LAMP, Blocks.LIT_REDSTONE_LAMP
        );
    }

    public static void removeLowTierToolRecipes() {
        RecipeUtils.removeRecipes(Items.WOODEN_AXE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.WOODEN_HOE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.WOODEN_SWORD, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.STONE_HOE, OreDictionary.WILDCARD_VALUE);
        RecipeUtils.removeRecipes(Items.STONE_SWORD, OreDictionary.WILDCARD_VALUE);
    }

    private static void rebalanceVanillaHardness() {
        Blocks.STONE.setHardness(2.25F).setResistance(10.0F);
        Blocks.PLANKS.setHardness(1.0F).setResistance(5.0F);
        Blocks.LOG.setHardness(1.25F);
        Blocks.LOG2.setHardness(1.25F);
        Blocks.SANDSTONE.setHardness(1.5F);
        Blocks.CHEST.setHardness(1.5F);
        Blocks.CRAFTING_TABLE.setHardness(1.5F);
        Blocks.FURNACE.setHardness(3.0F);
        Blocks.LIT_FURNACE.setHardness(3.0F);

        Blocks.OAK_DOOR.setHardness(1.5F);
        Blocks.SPRUCE_DOOR.setHardness(1.5F);
        Blocks.BIRCH_DOOR.setHardness(1.5F);
        Blocks.JUNGLE_DOOR.setHardness(1.5F);
        Blocks.ACACIA_DOOR.setHardness(1.5F);
        Blocks.DARK_OAK_DOOR.setHardness(1.5F);

        Blocks.STONE_PRESSURE_PLATE.setHardness(1.5F);
        Blocks.JUKEBOX.setHardness(1.5F).setResistance(10.0F);
        Blocks.OAK_FENCE.setHardness(1.5F);
        Blocks.SPRUCE_FENCE.setHardness(1.5F);
        Blocks.BIRCH_FENCE.setHardness(1.5F);
        Blocks.JUNGLE_FENCE.setHardness(1.5F);
        Blocks.ACACIA_FENCE.setHardness(1.5F);
        Blocks.DARK_OAK_FENCE.setHardness(1.5F);
        Blocks.GLOWSTONE.setHardness(0.6F);
        Blocks.TRAPDOOR.setHardness(1.5F);
        Blocks.MONSTER_EGG.setHardness(1.5F);
        Blocks.STONEBRICK.setHardness(2.25F);

        Blocks.OAK_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);
        Blocks.SPRUCE_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);
        Blocks.BIRCH_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);
        Blocks.JUNGLE_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);
        Blocks.ACACIA_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);
        Blocks.DARK_OAK_FENCE_GATE.setHardness(1.5F).setResistance(5.0F);

        Blocks.WOODEN_SLAB.setHardness(1.0F).setResistance(5.0F);
        Blocks.QUARTZ_ORE.setHardness(1.0F).setResistance(5.0F);
        Blocks.QUARTZ_BLOCK.setHardness(2.0F);
        Blocks.QUARTZ_STAIRS.setHardness(2.0F);
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
        for (Item item : BWMod.itemRegistry) {
            if (!(item instanceof ItemTool)) continue;
            ItemTool tool = (ItemTool) item;
            ToolMaterialOverride newValues = ToolMaterialOverride.getOverride(tool.getToolMaterial());
            if (newValues == null) continue;
            tool.setMaxDamage(newValues.getMaxUses());
            ReflectionHelper.setPrivateValue(ItemTool.class, tool, newValues.getEfficiencyOnProperMaterial(), "field_77864_a", "efficiencyOnProperMaterial");
        }
    }

    /**
     * New values for {@link net.minecraft.item.Item.ToolMaterial}
     */
    private enum ToolMaterialOverride {
        WOOD(BWConfig.woodDurability - 1, 1.01F, 0),
        STONE(BWConfig.stoneDurability - 1, 1.01F, 5),
        IRON(BWConfig.ironDurability - 1, 6.0F, 14),
        DIAMOND(BWConfig.diamondDurability - 1, 8.0F, 14),
        GOLD(BWConfig.goldDurability - 1, 12.0F, 22);
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final int enchantability;

        ToolMaterialOverride(int par4, float par5, int par7) {
            this.maxUses = par4;
            this.efficiencyOnProperMaterial = par5;
            this.enchantability = par7;
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
}
