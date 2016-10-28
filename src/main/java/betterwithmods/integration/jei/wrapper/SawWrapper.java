package betterwithmods.integration.jei.wrapper;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tyler on 9/4/16.
 */
public class SawWrapper extends BlankRecipeWrapper {
    public List<ItemStack> inputs = Lists.newArrayList(), outputs = Lists.newArrayList();
    public ItemStack input;

    public SawWrapper(String string, List<ItemStack> output) {
        this.input = fromString(string);
        inputs.add(input);
        outputs.addAll(output);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setOutputs(ItemStack.class, getOutputs());
    }

    @Nonnull
    @Deprecated
    @Override
    public List<ItemStack> getInputs() {
        return inputs;
    }

    @Nonnull
    @Deprecated
    @Override
    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public ItemStack fromString(String string) {
        Pattern pattern = Pattern.compile("Block\\{(.*)\\}");
        String b = string.substring(0, string.lastIndexOf(':'));
        String m = string.substring(string.lastIndexOf(':') + 1);
        Matcher matcher = pattern.matcher(b);
        matcher.find();
        return new ItemStack(Block.REGISTRY.getObject(new ResourceLocation(matcher.group(1))), 1, Integer.parseInt(m));
    }
}
