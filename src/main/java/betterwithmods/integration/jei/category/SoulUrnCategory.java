package betterwithmods.integration.jei.category;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.integration.jei.wrapper.SoulUrnWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/20/16
 */
public class SoulUrnCategory extends BlankRecipeCategory<SoulUrnWrapper> {
    public static final int width = 145;
    public static final int height = 80;

    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public SoulUrnCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(BWMod.MODID, "textures/gui/jei/hopper.png");
        background = guiHelper.createDrawable(location, 0, 0, width, height);
        localizedName = Translator.translateToLocal("inv.hopper.soulurn.name");
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwm.hopper.soulurn";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        int l = minecraft.fontRendererObj.getStringWidth("Throw");
        minecraft.fontRendererObj.drawString("Throw", width / 2 - l + 5, -11, 0x808080);
        minecraft.fontRendererObj.drawString("Filter", width / 2 - 50, 16, 0x808080);
        minecraft.fontRendererObj.drawString("Outputs", width / 2 + 10, -11, 0x808080);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull SoulUrnWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        int x = width / 2 - 18, y = 0;

        guiItemStacks.init(0, true, x - 27, y + 27); //filter
        guiItemStacks.init(1, true, x, y); //input item

        guiItemStacks.init(2, false, x + 28, y); //main output

        guiItemStacks.init(3, false, x + 28, y + 27); //inventory result

        guiItemStacks.init(4, false, x, y + 27); //hopper
        guiItemStacks.init(5, false, x, y + 45); //hopper

        guiItemStacks.set(ingredients);
        guiItemStacks.set(4, new ItemStack(BWMBlocks.SINGLE_MACHINES, 1, 4));
        guiItemStacks.set(5, new ItemStack(BWMBlocks.URN, 1, 0));


    }
}
