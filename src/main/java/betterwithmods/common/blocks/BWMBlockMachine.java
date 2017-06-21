package betterwithmods.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

/**
 * Created by primetoxinz on 6/20/17.
 */
public class BWMBlockMachine extends BWMBlock {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public BWMBlockMachine(Material material) {
        super(material);
    }

}
