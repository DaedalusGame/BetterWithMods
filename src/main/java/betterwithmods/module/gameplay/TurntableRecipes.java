package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockUnfiredPottery;
import betterwithmods.common.registry.blockmeta.managers.TurntableManager;
import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 5/16/17.
 */
public class TurntableRecipes extends Feature {
    public TurntableRecipes() {
        canDisable = false;
    }

    public static void addTurntableRecipe(Block inputBlock, int iMeta, Block outputBlock, int oMeta, ItemStack... scraps) {
        TurntableManager.INSTANCE.addTurntableRecipe(inputBlock, iMeta, outputBlock, oMeta, scraps);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        addTurntableRecipe(Blocks.CLAY, 0, BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.CRUCIBLE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.CRUCIBLE.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.PLANTER.getMeta(), ItemStack.EMPTY);
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.PLANTER.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.VASE.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.VASE.getMeta(), BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.URN.getMeta(), new ItemStack(Items.CLAY_BALL));
        addTurntableRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.URN.getMeta(), null, 0, new ItemStack(Items.CLAY_BALL));
    }
}
