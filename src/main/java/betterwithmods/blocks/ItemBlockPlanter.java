package betterwithmods.blocks;

import betterwithmods.blocks.BlockPlanter.EnumPlanterType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockPlanter extends ItemBlockMeta {
    public ItemBlockPlanter(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemstack(ItemStack stack, int colorIndex) {
        if (stack.getItemDamage() == 2 && block instanceof BlockPlanter) {
            BlockPlanter planter = (BlockPlanter) block;
            return planter.colorMultiplier(
                    planter.getDefaultState().withProperty(BlockPlanter.TYPE, EnumPlanterType.byMeta(stack.getItemDamage())),
                    null, null, colorIndex);
        }
        return -1;
    }
}
