package betterwithmods.common.registry;

import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.stream.Collectors;

public class OreStack {
    private final String oreName;
    private final List<ItemStack> oreStacks;
    private int stackSize;

    public OreStack(String name, int stack) {
        this.oreName = name;
        this.stackSize = stack;
        this.oreStacks = OreDictionary.getOres(oreName);
        System.out.println(oreStacks);
    }

    public OreStack copy() {
        return new OreStack(oreName, stackSize);
    }

    public String getOreName() {
        return this.oreName;
    }

    public List<ItemStack> getItems() {
        return setOreSize(this.oreStacks, getStackSize());
    }

    public List<ItemStack> getOres() {
        return this.oreStacks;
    }

    public List<ItemStack> setOreSize(List<ItemStack> list, int count) {
        return list.stream().map(i -> InvUtils.setCount(i, count)).collect(Collectors.toList());
    }

    public void addToStack(int amount) {
        this.stackSize += amount;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public boolean isEmpty() {
        return oreStacks.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", oreName,oreStacks);
    }
}
