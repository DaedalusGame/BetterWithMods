package betterwithmods.integration.jei;

import betterwithmods.BWMBlocks;
import betterwithmods.client.container.GuiCauldron;
import betterwithmods.client.container.GuiCrucible;
import betterwithmods.client.container.GuiMill;
import betterwithmods.integration.jei.category.*;
import betterwithmods.integration.jei.handler.*;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class BWMJEIPlugin extends BlankModPlugin {
    @Override
    public void register(@Nonnull IModRegistry reg) {
        IJeiHelpers helper = reg.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(new CauldronRecipeCategory(guiHelper), new StokedCauldronRecipeCategory(guiHelper), new CrucibleRecipeCategory(guiHelper),
                new StokedCrucibleRecipeCategory(guiHelper), new MillRecipeCategory(guiHelper), new SawRecipeCategory(guiHelper), new KilnRecipeCategory(guiHelper), new TurntableRecipeCategory(guiHelper), new HopperRecipeCategory(guiHelper), new SoulUrnCategory(guiHelper));

        reg.addRecipeHandlers(new CauldronRecipeHandler(), new StokedCauldronRecipeHandler(), new CrucibleRecipeHandler(),
                new StokedCrucibleRecipeHandler(), new MillRecipeHandler(), new SawRecipeHandler(), new KilnRecipeHandler(), new TurntableHandler(), new HopperRecipeHandler());

        reg.addRecipes(JEIRecipeRegistry.getCauldronRecipes(helper));
        reg.addRecipes(JEIRecipeRegistry.getStokedCauldronRecipes(helper));
        reg.addRecipes(JEIRecipeRegistry.getCrucibleRecipes(helper));
        reg.addRecipes(JEIRecipeRegistry.getStokedCrucibleRecipes(helper));
        reg.addRecipes(JEIRecipeRegistry.getMillRecipes(helper));
        reg.addRecipes(JEIRecipeRegistry.getSawRecipes());
        reg.addRecipes(JEIRecipeRegistry.getKilnRecipes());
        reg.addRecipes(JEIRecipeRegistry.getTurntableRecipes());
        reg.addRecipes(JEIRecipeRegistry.getHopperRecipes());

        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 0), "bwm.mill");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 2), "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 3), "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4), "bwm.hopper", "bwm.hopper.soulurn");

        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 5), "bwm.turntable");
        reg.addRecipeCategoryCraftingItem(new ItemStack(BWMBlocks.SAW), "bwm.saw");

        reg.addRecipeCategoryCraftingItem(new ItemStack(Blocks.BRICK_BLOCK), "bwm.kiln");

        reg.addRecipeClickArea(GuiCauldron.class, 81, 19, 14, 14, "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeClickArea(GuiCrucible.class, 81, 19, 14, 14, "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeClickArea(GuiMill.class, 81, 19, 14, 14, "bwm.mill");
    }
}
