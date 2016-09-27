package betterwithmods.integration.minetweaker.utils;

import minetweaker.mc1102.oredict.MCOreDictEntry;

public class StackOreDictEntry extends MCOreDictEntry
{
    private int amount;

    public StackOreDictEntry(String name, int amount) {
        super(name);
        this.amount = amount;
    }

    @Override
    public int getAmount()
    {
        return amount;
    }
}
