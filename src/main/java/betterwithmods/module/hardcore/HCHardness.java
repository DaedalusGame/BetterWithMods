package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.tweaks.AxeLeaves;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by tyler on 4/20/17.
 */
public class HCHardness extends Feature {

    @Override
    public String getFeatureDescription() {
        return "Makes certain block hardness more \"realistic\"";
    }

    @Override
    public boolean requiresMinecraftRestartToEnable() {
        return true;
    }
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        applyHCHardness();
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


    public static void applyHCHardness() {
        Blocks.FIRE.setFireInfo(Blocks.LEAVES, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 60, 100);
        rebalanceVanillaHardness();

        ToolsManager.setAxesAsEffectiveAgainst(Blocks.COCOA, Blocks.SKULL, Blocks.VINE, Blocks.WEB, Blocks.CACTUS);

        if (ModuleLoader.isFeatureEnabled(AxeLeaves.class)) {
            ToolsManager.setAxesAsEffectiveAgainst(Blocks.LEAVES, Blocks.LEAVES2);
            for (ItemStack stack : OreDictionary.getOres("treeLeaves")) {
                if (stack.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();
                    if (block == Blocks.LEAVES || block == Blocks.LEAVES2) continue;
                    if (block instanceof BlockLeaves) {
                        ToolsManager.setAxesAsEffectiveAgainst(block);
                        block.setHarvestLevel("axe", 0);
                    } else {
                        ToolsManager.setAxesAsEffectiveAgainst(block);
                        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                            block.setHarvestLevel("axe", 0);
                        else if (stack.getItemDamage() < 16) {
                            for (int i = 0; i < 4; i++) {
                                int multi = 4 * i;
                                int meta = stack.getMetadata() + multi;
                                if (meta < 16) {
                                    IBlockState state = block.getStateFromMeta(meta);
                                    block.setHarvestLevel("axe", 0, state);
                                }
                            }
                        }
                    }

                }
            }
        }
        ToolsManager.setPickaxesAsEffectiveAgainst(Blocks.LEVER, Blocks.GLASS, Blocks.STAINED_GLASS, Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE,
                Blocks.STONE_BUTTON, Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.PISTON_EXTENSION,
                Blocks.GLOWSTONE, Blocks.BEACON, Blocks.MONSTER_EGG,
                Blocks.REDSTONE_LAMP, Blocks.LIT_REDSTONE_LAMP
        );
    }


//    @Override
//    public boolean hasSubscriptions() {
//        return true;
//    }
}
