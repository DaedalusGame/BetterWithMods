package betterwithmods.util;

import betterwithmods.util.item.ToolsManager;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

/**
 * Static methods that handle hardness rebalancing.
 * @author Koward
 */
public final class HardcoreHardnessFunctions {
    private HardcoreHardnessFunctions() {}

    public static void applyChanges() {
        Blocks.FIRE.setFireInfo(Blocks.LEAVES, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 60, 100);
        rebalanceVanillaHardness();

        editToolsDurability();
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

    private static void editToolsDurability() {
        Items.WOODEN_PICKAXE.setMaxDamage(1);//Adjusted to 0 (1 use) at crafting by Event
        Items.STONE_PICKAXE.setMaxDamage(6-1);
        Items.IRON_PICKAXE.setMaxDamage(500-1);

        Items.WOODEN_SHOVEL.setMaxDamage(10-1);
        Items.STONE_SHOVEL.setMaxDamage(50-1);
        Items.IRON_SHOVEL.setMaxDamage(500-1);

        Items.IRON_SWORD.setMaxDamage(500-1);

        Items.STONE_AXE.setMaxDamage(50-1);
        Items.IRON_AXE.setMaxDamage(500-1);
    }
}
