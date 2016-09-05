package betterwithmods.integration.minetweaker.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;


public class ForgeHelper {
    @SuppressWarnings("rawtypes")
    public static Map translate = null;
    @SuppressWarnings("rawtypes")
    public static List seeds = null;

    static {
        try {
            seeds = ReflectionHelper.getStaticObject(ForgeHooks.class, "seedList");
            translate = ReflectionHelper.getFinalObject(ReflectionHelper.getStaticObject(I18n.class, "localizedName", "field_74839_a"), "languageList", "field_74816_c");
        } catch (Exception e) {
        }
    }

    private ForgeHelper() {
    }

    public static Object getSeedEntry(ItemStack stack, int weight) {
        Object seedEntry = ReflectionHelper.getInstance(ReflectionHelper.getConstructor("net.minecraftforge.common.ForgeHooks$SeedEntry", ItemStack.class, int.class), stack, weight);

        if (seedEntry == null) {
            throw new NullPointerException("Failed to instantiate SeedEntry");
        }

        return seedEntry;
    }



    public static boolean isLangActive(String lang) {
        return FMLCommonHandler.instance().getSide() == Side.SERVER ? null : FMLClientHandler.instance().getCurrentLanguage().equals(lang);
    }
}
