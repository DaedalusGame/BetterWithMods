package betterwithmods.util.item;

import com.google.common.base.Objects;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/25/17
 */
public class Stack {
    private Object data;
    private int meta;
    private Type type;

    public Stack(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            this.data = ((ItemBlock) stack.getItem()).getBlock();
            this.type = Type.BLOCK;
        } else {
            this.data = stack.getItem();
            this.type = Type.ITEM;
        }
        this.meta = stack.getMetadata();

    }

    public Stack(Item item, int meta) {
        this.data = item;
        this.meta = meta;
        this.type = Type.ITEM;
    }

    public Stack(Block block, int meta) {
        this.data = block;
        this.meta = meta;
        this.type = Type.BLOCK;
    }


    public Object getData() {
        return this.data;
    }

    public int getMeta() {
        return meta;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        Stack stack = (Stack) o;
        boolean wild = meta == OreDictionary.WILDCARD_VALUE || stack.meta == OreDictionary.WILDCARD_VALUE;
        return wild ? stack.data == this.data : (stack.data == this.data && this.meta == stack.meta);
    }

    @Override
    public int hashCode() {
        boolean wild = meta == OreDictionary.WILDCARD_VALUE;
        return Objects.hashCode(data);
    }

    private enum Type {
        BLOCK,
        ITEM
    }

    @Override
    public String toString() {
        return String.format("%s->%s:%s:%s", getType(), getType() == Type.BLOCK ? ((Block)getData()).getUnlocalizedName() : ((Item)getData()).getUnlocalizedName(new ItemStack((Item) data,0,meta)), getMeta(), hashCode());
    }
}
