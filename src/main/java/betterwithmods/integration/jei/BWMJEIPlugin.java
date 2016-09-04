package betterwithmods.integration.jei;

import betterwithmods.client.container.GuiCauldron;
import betterwithmods.client.container.GuiCrucible;
import betterwithmods.client.container.GuiMill;
import betterwithmods.craft.bulk.category.*;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class BWMJEIPlugin extends BlankModPlugin
{
    @Override
    public void register(@Nonnull IModRegistry reg)
    {
        IJeiHelpers helper = reg.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(new CauldronRecipeCategory(guiHelper), new StokedCauldronRecipeCategory(guiHelper), new CrucibleRecipeCategory(guiHelper),
                new StokedCrucibleRecipeCategory(guiHelper), new MillRecipeCategory(guiHelper));

        reg.addRecipeHandlers(new CauldronRecipeHandler(), new StokedCauldronRecipeHandler(), new CrucibleRecipeHandler(),
                new StokedCrucibleRecipeHandler(), new MillRecipeHandler());

        reg.addRecipes(JEIRecipeRegistry.getCauldronRecipes());
        reg.addRecipes(JEIRecipeRegistry.getStokedCauldronRecipes());
        reg.addRecipes(JEIRecipeRegistry.getCrucibleRecipes());
        reg.addRecipes(JEIRecipeRegistry.getStokedCrucibleRecipes());
        reg.addRecipes(JEIRecipeRegistry.getMillRecipes());

        reg.addRecipeClickArea(GuiCauldron.class, 81, 19, 14, 14, "bwm.cauldron", "bwm.cauldron.stoked");
        reg.addRecipeClickArea(GuiCrucible.class, 81, 19, 14, 14, "bwm.crucible", "bwm.crucible.stoked");
        reg.addRecipeClickArea(GuiMill.class, 81, 19, 14, 14, "bwm.mill");
    }
}
