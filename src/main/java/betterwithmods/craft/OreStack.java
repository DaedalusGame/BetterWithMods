package betterwithmods.craft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class OreStack {
    private String oreName;
    private int stackSize;
    private List<ItemStack> oreStacks;

    public OreStack(String name) {
        this(name, 1);
    }

    public OreStack(String name, int stack) {
        this.oreName = name;
        this.stackSize = stack;
        oreStacks = generateOreStacks(name, stack);
    }

    //convenience for MineTweaker
    public OreStack(String name, int stack, ItemStack[] ores) {
        this.oreName = name;
        this.stackSize = stack;
        this.oreStacks = new ArrayList<>();
        for (ItemStack ore : ores)
            this.oreStacks.add(ore.copy());
    }

    public OreStack copy() {
        return new OreStack(oreName, stackSize);
    }

    public String getOreName() {
        return this.oreName;
    }

    public List<ItemStack> getOres() {
        if (OreDictionary.getOres(oreName).size() > 0)
            return OreDictionary.getOres(oreName);
        return null;
    }

    private List<ItemStack> generateOreStacks(String name, int amount) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack stack : OreDictionary.getOres(name)) {
            items.add(new ItemStack(stack.getItem(), amount, stack.getMetadata()));
        }
        return items;
    }

    public void addToStack(int amount) {
        this.stackSize += amount;
    }

    public List<ItemStack> getOreStacks() {
        return oreStacks;
    }

    public int getStackSize() {
        return this.stackSize;
    }
}
