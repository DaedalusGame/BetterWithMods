package betterwithmods.items;

import betterwithmods.BWRegistry;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class ItemMaterial extends Item implements ITannin {
    public static String[] names = {"Gear", "Nethercoal", "Hemp", "HempFibers", "HempCloth", "Dung", "TannedLeather", "ScouredLeather", "LeatherStrap", "LeatherBelt", "WoodBlade",
            "WindmillBlade", "Glue", "Tallow", "IngotSteel", "GroundNetherrack", "HellfireDust", "ConcentratedHellfire", "CoalDust", "Filament", "PolishedLapis",
            "Potash", "Sawdust", "SoulDust", "Screw", "Brimstone", "Niter", "Element", "Fuse", "BlastingOil", "NuggetIron", "NuggetSteel", "LeatherCut",
            "TannedLeatherCut", "ScouredLeatherCut", "RedstoneLatch", "NetherSludge", "Flour", "Haft", "CharcoalDust", "SharpeningStone", "KnifeBlade"};

    public static ItemStack getMaterial(String material) {
        return getMaterial(material,1);
    }
    public static ItemStack getMaterial(String material,int count) {
        OptionalInt meta = IntStream.range(0, names.length).filter(n -> material.toLowerCase().equals(names[n].toLowerCase())).findFirst();
        return new ItemStack(BWRegistry.material,count, meta.isPresent() ? meta.getAsInt() : 0);
    }

    public ItemMaterial() {
        this.setHasSubtypes(true);
        this.setUnlocalizedName("bwm:material");
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < names.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + names[stack.getItemDamage()];
    }

    @Override
    public int getStackSizeForTanning(int meta) {
        if (meta == 5) return 1;
        return 0;
    }
}
