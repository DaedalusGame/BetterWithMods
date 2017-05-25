package betterwithmods.module.compat.bop;

import betterwithmods.common.BWMItems;
import betterwithmods.module.compat.CompatFeature;
import betterwithmods.module.hardcore.HCPiles;
import betterwithmods.module.hardcore.HCSeeds;
import betterwithmods.module.tweaks.MobSpawning.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class BiomesOPlenty extends CompatFeature {
    public static Item PILES;

    public BiomesOPlenty() {
        super("biomesoplenty");
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        PILES = new ItemBOPPile().setRegistryName("bop_piles");
        BWMItems.registerItem(PILES);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient(FMLPreInitializationEvent event) {
        BWMItems.setInventoryModel(PILES);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "grass")), 1);
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "grass")), 6);
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "flesh")));
        NetherSpawnWhitelist.addBlock(getBlock(new ResourceLocation(modid, "ash_block")));

        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(0));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(1));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(7));
        HCSeeds.BLOCKS_TO_STOP.add(getBlock(new ResourceLocation(modid, "plant_0")).getStateFromMeta(8));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 5, new ItemStack(BWMItems.DIRT_PILE, 3));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), 7, new ItemStack(BWMItems.DIRT_PILE, 3));

        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 0, new ItemStack(PILES, 3, 0));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_0")), 1, new ItemStack(PILES, 3, 1));
        HCPiles.registerPile(getBlock(new ResourceLocation(modid, "farmland_1")), 0, new ItemStack(PILES, 3, 2));

        for (int i = 2; i <= 4; i++)
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass")), i, new ItemStack(PILES, 3, i - 2));
        for (int i = 0; i <= 2; i++) {
            Block dirt = getBlock(new ResourceLocation(modid, "dirt"));
            HCPiles.registerPile(dirt, i, new ItemStack(PILES, 3, i));
            HCPiles.registerPile(getBlock(new ResourceLocation(modid, "grass_path")), i, new ItemStack(PILES, 3, i));

            GameRegistry.addShapedRecipe(new ItemStack(dirt, 1, i), new Object[]{"PP", "PP", 'P', new ItemStack(PILES, 1, i)});
        }
    }
}
