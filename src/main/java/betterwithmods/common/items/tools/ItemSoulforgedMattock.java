package betterwithmods.common.items.tools;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.util.item.ToolsManager;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Set;

public class ItemSoulforgedMattock extends ItemTool {
    private static final Set<Block> EFFECTIVE = Sets.union(ToolsManager.getEffectiveBlocks((ItemTool) BWMItems.STEEL_PICKAXE), ToolsManager.getEffectiveBlocks((ItemTool) BWMItems.STEEL_SHOVEL));

    public ItemSoulforgedMattock() {
        super(BWMItems.SOULFORGED_STEEL, EFFECTIVE);
        setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return BWOreDictionary.listContains(repair, OreDictionary.getOres("ingotSoulforgedSteel")) || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("mattock", "pickaxe", "shovel");
    }

    public boolean canHarvestBlock(IBlockState blockIn) {
        Block block = blockIn.getBlock();
        return getToolMaterial().getHarvestLevel() >= block.getHarvestLevel(blockIn);
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }
}
