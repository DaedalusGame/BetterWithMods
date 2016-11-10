package betterwithmods.util;

import betterwithmods.util.item.ToolsManager;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Static methods that handle hardness rebalancing.
 *
 * @author Koward
 */
public final class HardcoreHardnessFunctions {
    private HardcoreHardnessFunctions() {
    }

    public static void applyChanges() {
        Blocks.FIRE.setFireInfo(Blocks.LEAVES, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 60, 100);
        changeVanillaMaterials();
        rebalanceVanillaHardness();

        //Adjusted to 0 (1 use) by {@link HungerEventHandler.woodenPickaxeAdjustment()}
        Items.WOODEN_PICKAXE.setMaxDamage(1);
        Items.STONE_PICKAXE.setMaxDamage(6 - 1);

        ToolsManager.setAxesAsEffectiveAgainst(Blocks.COCOA, Blocks.SKULL, Blocks.LEAVES, Blocks.LEAVES2,
                Blocks.VINE, Blocks.WEB, Blocks.CACTUS);
        ToolsManager.setAxesAsEffectiveAgainst(Material.WOOD, Material.VINE, Material.PLANTS, Material.CLOTH);
        //TODO axe hunger and damage by foliage
        ToolsManager.setPickaxesAsEffectiveAgainst(Blocks.LEVER, Blocks.GLASS, Blocks.STAINED_GLASS, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE,
                Blocks.STONE_BUTTON, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_EXTENSION,
                Blocks.GLOWSTONE, Blocks.BEACON, Blocks.MONSTER_EGG,
                Blocks.REDSTONE_LAMP, Blocks.LIT_REDSTONE_LAMP
        );
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
     * Edit the values described at {@link net.minecraft.item.Item.ToolMaterial}.
     * ATM it is in fact not possible so just every item using the materials are changed.
     */
    private static void changeVanillaMaterials() {
        applyMaterialOverride(EnumToolMaterial.WOOD, Items.WOODEN_AXE, Items.WOODEN_HOE, Items.WOODEN_PICKAXE,
                Items.WOODEN_SHOVEL, Items.WOODEN_SWORD);
        applyMaterialOverride(EnumToolMaterial.STONE, Items.STONE_AXE, Items.STONE_HOE, Items.STONE_PICKAXE,
                Items.STONE_SHOVEL, Items.STONE_SWORD);
        applyMaterialOverride(EnumToolMaterial.IRON, Items.IRON_AXE, Items.IRON_HOE, Items.IRON_PICKAXE,
                Items.IRON_SHOVEL, Items.IRON_SWORD);
        applyMaterialOverride(EnumToolMaterial.DIAMOND, Items.DIAMOND_AXE, Items.DIAMOND_HOE, Items.DIAMOND_PICKAXE,
                Items.DIAMOND_SHOVEL, Items.DIAMOND_SWORD);
        applyMaterialOverride(EnumToolMaterial.GOLD, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_PICKAXE,
                Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD);
    }

    private static void applyMaterialOverride(EnumToolMaterial material, Item... items) {
        for (Item item : items) {
            item.setMaxDamage(material.getMaxUses());
            /* TODO change efficiency&enchant
            item.setEfficiency(material.getEfficiencyOnProperMaterial());
            item.setEnchantability(material.getEnchantability());
            */
        }
    }

    /**
     * New values for {@link net.minecraft.item.Item.ToolMaterial}
     */
    private enum EnumToolMaterial {
        WOOD(10, 1.01F, 0),
        STONE(50, 1.01F, 5),
        IRON(500, 6.0F, 14),
        DIAMOND(1561, 8.0F, 14),
        GOLD(32, 12.0F, 22);
        private final int maxUses;
        private final float efficiencyOnProperMaterial;
        private final int enchantability;

        EnumToolMaterial(int par4, float par5, int par7) {
            this.maxUses = par4;
            this.efficiencyOnProperMaterial = par5;
            this.enchantability = par7;
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
