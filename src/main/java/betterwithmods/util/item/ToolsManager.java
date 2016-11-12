package betterwithmods.util.item;

import betterwithmods.BWMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Collections;
import java.util.Set;

/**
 * Set of methods dealing with ItemTools.
 *
 * @author Koward
 */
public final class ToolsManager {
    private ToolsManager() {
    }

    public static void setAxesAsEffectiveAgainst(Block... blocks) {
        for (Item item : BWMod.itemRegistry) {
            if (!(item instanceof ItemAxe)) continue;
            ItemAxe tool = (ItemAxe) item;
            setToolAsEffectiveAgainst(tool, blocks);
        }
    }

    public static void setPickaxesAsEffectiveAgainst(Block... blocks) {
        for (Item item : BWMod.itemRegistry) {
            if (!(item instanceof ItemPickaxe)) continue;
            ItemPickaxe tool = (ItemPickaxe) item;
            setToolAsEffectiveAgainst(tool, blocks);
        }
    }

    private static Set<Block> getEffectiveBlocks(ItemTool tool) {
        return ReflectionHelper.getPrivateValue(ItemTool.class, tool, "field_150914_c", "effectiveBlocks");
    }

    private static void setToolAsEffectiveAgainst(ItemTool tool, Block... blocks) {
        Set<Block> effectiveOn = getEffectiveBlocks(tool);
        Collections.addAll(effectiveOn, blocks);
    }
}
