package betterwithmods.util.item;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Set of methods dealing with ItemTools.
 * @author Koward
 *
 */
public final class ToolsManager {
	private ToolsManager() {}
	
	private static final ItemAxe[] AXES = new ItemAxe[]{(ItemAxe) Items.DIAMOND_AXE, (ItemAxe) Items.GOLDEN_AXE, (ItemAxe) Items.IRON_AXE, (ItemAxe) Items.STONE_AXE, (ItemAxe) Items.WOODEN_AXE};
	private static final ItemPickaxe[] PICKAXES = new ItemPickaxe[]{(ItemPickaxe) Items.DIAMOND_PICKAXE, (ItemPickaxe) Items.GOLDEN_PICKAXE, (ItemPickaxe) Items.IRON_PICKAXE, (ItemPickaxe) Items.STONE_PICKAXE, (ItemPickaxe) Items.WOODEN_PICKAXE};
	private static final ItemSpade[] SHOVELS= new ItemSpade[]{(ItemSpade) Items.DIAMOND_SHOVEL, (ItemSpade) Items.GOLDEN_SHOVEL, (ItemSpade) Items.IRON_SHOVEL, (ItemSpade) Items.STONE_SHOVEL, (ItemSpade) Items.WOODEN_SHOVEL};
	private static final Set<Material> AXES_MATERIALS = new HashSet<Material>();
	private static final Set<Material> PICKAXES_MATERIALS = new HashSet<Material>();
	private static final Set<Material> SHOVELS_MATERIALS = new HashSet<Material>();
	
	public static void setAxesAsEffectiveAgainst(Block... blocks) {
		for(ItemAxe tool : AXES) setToolAsEffectiveAgainst(tool, blocks);
	}

	public static void setPickaxesAsEffectiveAgainst(Block... blocks) {
		for(ItemPickaxe tool : PICKAXES) setToolAsEffectiveAgainst(tool, blocks);
	}
	
	public static void setShovelsAsEffectiveAgainst(Block... blocks) {
		for(ItemSpade tool : SHOVELS) setToolAsEffectiveAgainst(tool, blocks);
	}

	public static void setAxesAsEffectiveAgainst(Material... materials) {
		for(Material material : materials) AXES_MATERIALS.add(material);
	}

	public static void setPickaxesAsEffectiveAgainst(Material... materials) {
		for(Material material : materials) PICKAXES_MATERIALS.add(material);
	}
	
	public static void setShovelsAsEffectiveAgainst(Material... materials) {
		for(Material material : materials) SHOVELS_MATERIALS.add(material);
	}
	
	public static Set<Block> getEffectiveBlocks(ItemTool tool) {
		return ReflectionHelper.getPrivateValue(ItemTool.class, tool, "field_150914_c", "effectiveBlocks");
	}

	public static Set<Material> getEffectiveMaterials(ItemAxe tool) {
		return AXES_MATERIALS;
	}
	public static Set<Material> getEffectiveMaterials(ItemPickaxe tool) {
		return PICKAXES_MATERIALS;
	}
	public static Set<Material> getEffectiveMaterials(ItemSpade tool) {
		return SHOVELS_MATERIALS;
	}

	public static void setToolAsEffectiveAgainst(ItemTool tool, Block... blocks) {
		Set<Block> effectiveOn = getEffectiveBlocks(tool);
		for(Block block : blocks) effectiveOn.add(block);
	}
}
