package betterwithmods.manual.client.manual.segment;

import betterwithmods.module.compat.jei.JEI;
import com.google.common.base.Strings;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.gui.Focus;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

public final class JEISegment extends TextSegment implements InteractiveSegment {
    private static final int NORMAL_COLOR = 0x333399;
    private static final int NORMAL_COLOR_HOVER = 0x6666CC;
    private static final int FADE_TIME = 500;

    private final String recipeOutput;
    private long lastHovered = System.currentTimeMillis() - FADE_TIME;

    public JEISegment(final Segment parent, final String text, final String recipeOutput) {
        super(parent, text);
        this.recipeOutput = recipeOutput;
    }

    private static int fadeColor(final int c1, final int c2, final float t) {
        final int r1 = (c1 >>> 16) & 0xFF;
        final int g1 = (c1 >>> 8) & 0xFF;
        final int b1 = c1 & 0xFF;
        final int r2 = (c2 >>> 16) & 0xFF;
        final int g2 = (c2 >>> 8) & 0xFF;
        final int b2 = c2 & 0xFF;
        final int r = (int) (r1 + (r2 - r1) * t);
        final int g = (int) (g1 + (g2 - g1) * t);
        final int b = (int) (b1 + (b2 - b1) * t);
        return (r << 16) | (g << 8) | b;
    }

    @Override
    protected Optional<Integer> color() {
        final int color, hoverColor;
        color = NORMAL_COLOR;
        hoverColor = NORMAL_COLOR_HOVER;

        final int timeSinceHover = (int) (System.currentTimeMillis() - lastHovered);
        if (timeSinceHover > FADE_TIME) {
            return Optional.of(color);
        } else {
            return Optional.of(fadeColor(hoverColor, color, timeSinceHover / (float) FADE_TIME));
        }
    }

    @Override
    public Optional<String> tooltip() {
        return Optional.of(recipeOutput);
    }

    @Override
    public boolean onMouseClick(final int mouseX, final int mouseY) {
        if (JEI.JEI_RUNTIME != null) {
            IFocus<?> focus = new Focus<Object>(IFocus.Mode.OUTPUT, getStack(recipeOutput));
            JEI.JEI_RUNTIME.getRecipesGui().show(focus);
            return true;
        }
        return false;
    }

    @Nullable
    public ItemStack getStack(final String data) {
        final int splitIndex = data.lastIndexOf('@');
        final String name, optMeta;
        if (splitIndex > 0) {
            name = data.substring(0, splitIndex);
            optMeta = data.substring(splitIndex);
        } else {
            name = data;
            optMeta = "";
        }
        final int meta = (Strings.isNullOrEmpty(optMeta)) ? 0 : Integer.parseInt(optMeta.substring(1));
        final Item item = Item.REGISTRY.getObject(new ResourceLocation(name));
        return new ItemStack(item, 1, meta);
    }

    @Override
    public void notifyHover() {
        lastHovered = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("[%s](%s)", text(), recipeOutput);
    }
}
