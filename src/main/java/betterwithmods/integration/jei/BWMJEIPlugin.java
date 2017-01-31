package betterwithmods.integration.jei;

import betterwithmods.BWMBlocks;
import betterwithmods.client.container.GuiCauldron;
import betterwithmods.client.container.GuiCrucible;
import betterwithmods.client.container.GuiMill;
import betterwithmods.client.container.GuiSteelAnvil;
import betterwithmods.craft.HopperInteractions;
import betterwithmods.craft.KilnInteraction;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.TurntableInteraction;
import betterwithmods.craft.bulk.*;
import betterwithmods.craft.steelanvil.CraftingManagerSteelAnvil;
import betterwithmods.integration.jei.category.*;
import betterwithmods.integration.jei.handler.*;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@mezz.jei.api.JEIPlugin
public class BWMJEIPlugin extends BlankModPlugin {
    public static IJeiHelpers helper;

    @Override
    public void register(@Nonnull IModRegistry reg) {
        helper = reg.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(new CauldronRecipeCategory(guiHelper), new StokedCauldronRecipeCategory(guiHelper), new CrucibleRecipeCategory(guiHelper),
                new StokedCrucibleRecipeCategory(guiHelper), new MillRecipeCategory(guiHelper), new SawRecipeCategory(guiHelper), new KilnRecipeCategory(guiHelper),
                new TurntableRecipeCategory(guiHelper), new HopperRecipeCategory(guiHelper), new SoulUrnCategory(guiHelper), new SteelAnvilRecipeCategory(guiHelper));

        reg.addRecipeHandlers(new BulkRecipeHandler(), new BlockMetaRecipeHandler(), new TurntableHandler(), new HopperRecipeHandler(),
                new SteelAnvilShapelessRecipeHandler(), new SteelAnvilShapedRecipeHandler());

        reg.addRecipes(CraftingManagerCauldron.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCauldronStoked.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCrucible.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerCrucibleStoked.getInstance().getRecipes());
        reg.addRecipes(CraftingManagerMill.getInstance().getRecipes());
        reg.addRecipes(SawInteraction.INSTANCE.getRecipes());
        reg.addRecipes(KilnInteraction.INSTANCE.getRecipes());
        reg.addRecipes(TurntableInteraction.INSTANCE.getRecipes());
        reg.addRecipes(HopperInteractions.recipes);
        reg.addRecipes(CraftingManagerSteelAnvil.INSTANCE.getRecipes());

        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 0), "bwm.mill");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 2), "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4), "bwm.hopper", "bwm.hopper.soul_urn");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5), "bwm.turntable");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SAW), "bwm.saw");
        reg.addRecipeCategoryCraftingItem(new ItemStack(Blocks.BRICK_BLOCK), "bwm.kiln");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.STEEL_ANVIL), "bwm.steel_anvil");

        reg.addRecipeClickArea(GuiCauldron.class, 81, 19, 14, 14, "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeClickArea(GuiCrucible.class, 81, 19, 14, 14, "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeClickArea(GuiMill.class, 81, 19, 14, 14, "bwm.mill");
        reg.addRecipeClickArea(GuiSteelAnvil.class, 88, 41, 28, 23, "bwm.steel_anvil");
    }
}
