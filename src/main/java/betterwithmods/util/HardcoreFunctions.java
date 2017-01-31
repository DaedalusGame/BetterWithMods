package betterwithmods.util;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.util.item.ToolsManager;
import betterwithmods.world.gen.feature.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Various static methods that handle various hardcore things.
 *
 * @author Koward
 */
public final class HardcoreFunctions {
    private HardcoreFunctions() {
    }

    /**
     * Hardcore Stumping
     * Checks various properties to find a {@link BlockPlanks.EnumType} for the state.
     *
     * @param state State of the wooden block to get wood type from.
     * @return The type if it has any, null otherwise.
     */
    @Nullable
    public static BlockPlanks.EnumType getWoodType(IBlockState state) {
        if (state.getProperties().containsKey(BlockPlanks.VARIANT)) {
            return state.getValue(BlockPlanks.VARIANT);
        } else if (state.getProperties().containsKey(BlockOldLog.VARIANT)) {
            return state.getValue(BlockOldLog.VARIANT);
        } else if (state.getProperties().containsKey(BlockNewLog.VARIANT)) {
            return state.getValue(BlockNewLog.VARIANT);
        } else return null;
    }

    /**
     * Hardcore Stumping
     * <p>
     * Edited version of {@link BlockSapling#isTwoByTwoOfType(World, BlockPos, int, int, BlockPlanks.EnumType)} build 2185
     */
    private static boolean isTwoByTwoOfType(BlockSapling sapling, World worldIn, BlockPos pos, int p_181624_3_, int p_181624_4_, BlockPlanks.EnumType type) {
        return sapling.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_, 0, p_181624_4_ + 1), type) && sapling.isTypeAt(worldIn, pos.add(p_181624_3_ + 1, 0, p_181624_4_ + 1), type);
    }

    /**
     * Hardcore Stumping
     * <p>
     * Edited version of {@link BlockSapling#generateTree(World, BlockPos, IBlockState, Random)} build 2185
     */
    public static void generateTreeWithStump(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!(state.getBlock() instanceof BlockSapling)) return;
        BlockSapling sapling = (BlockSapling) state.getBlock();
        WorldGenerator worldgenerator = rand.nextInt(10) == 0 ? new WorldGenBigTreeWithStump(true) : new WorldGenTreesWithStump(true);
        int i = 0;
        int j = 0;
        boolean flag = false;

        switch (state.getValue(BlockSapling.TYPE)) {
            case SPRUCE:
                label114:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.SPRUCE)) {
                            worldgenerator = new WorldGenMegaPineTreeWithStump(false, rand.nextBoolean());
                            flag = true;
                            break label114;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTaiga2WithStump(true);
                }

                break;
            case BIRCH:
                worldgenerator = new WorldGenBirchTreeWithStump(true, false);
                break;
            case JUNGLE:
                IBlockState iblockstate = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
                IBlockState iblockstate1 = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                label269:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.JUNGLE)) {
                            worldgenerator = new WorldGenMegaJungleWithStump(true, 10, 20, iblockstate, iblockstate1);
                            flag = true;
                            break label269;
                        }
                    }
                }

                if (!flag) {
                    i = 0;
                    j = 0;
                    worldgenerator = new WorldGenTreesWithStump(true, 4 + rand.nextInt(7), iblockstate, iblockstate1, false);
                }

                break;
            case ACACIA:
                worldgenerator = new WorldGenSavannaTreeWithStump(true);
                break;
            case DARK_OAK:
                label390:

                for (i = 0; i >= -1; --i) {
                    for (j = 0; j >= -1; --j) {
                        if (isTwoByTwoOfType(sapling, worldIn, pos, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                            worldgenerator = new WorldGenCanopyTreeWithStump(true);
                            flag = true;
                            break label390;
                        }
                    }
                }

                if (!flag) {
                    return;
                }

            case OAK:
        }

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        if (flag) {
            worldIn.setBlockState(pos.add(i, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), iblockstate2, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), iblockstate2, 4);
        } else {
            worldIn.setBlockState(pos, iblockstate2, 4);
        }

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            if (flag) {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
            } else {
                worldIn.setBlockState(pos, state, 4);
            }
        }
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
