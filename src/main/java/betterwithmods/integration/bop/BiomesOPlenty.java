package betterwithmods.integration.bop;

import betterwithmods.common.BWMItems;
import betterwithmods.integration.ICompatModule;
import betterwithmods.module.hardcore.HCPiles;
import betterwithmods.module.hardcore.HCSeeds;
import betterwithmods.module.tweaks.MobSpawning.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public class BiomesOPlenty implements ICompatModule {
    public static final String MODID = "biomesoplenty";
    public static Item PILES;

    @Override
    public void preInit() {
        PILES = new ItemBOPPile().setRegistryName("bop_piles");
        BWMItems.registerItem(PILES);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        BWMItems.setInventoryModel(PILES);
    }

    @Override
    public void init() {
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass")), 1);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass")), 6);
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "flesh")));
        NetherSpawnWhitelist.addBlock(Block.REGISTRY.getObject(new ResourceLocation(MODID, "ash_block")));

        HCSeeds.BLOCKS_TO_STOP.add(Block.REGISTRY.getObject(new ResourceLocation(MODID, "plant_0")));
        HCSeeds.BLOCKS_TO_STOP.add(Block.REGISTRY.getObject(new ResourceLocation(MODID, "plant_1")));
        HCSeeds.BLOCKS_TO_STOP.add(Block.REGISTRY.getObject(new ResourceLocation(MODID, "double_plant")));
        HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass")), 5, new ItemStack(BWMItems.DIRT_PILE, 3));
        HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass")), 7, new ItemStack(BWMItems.DIRT_PILE, 3));

        HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "farmland_0")), 0, new ItemStack(PILES, 3, 0));
        HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "farmland_0")), 1, new ItemStack(PILES, 3, 1));
        HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "farmland_1")), 0, new ItemStack(PILES, 3, 2));
        
        for (int i = 2; i <= 4; i++)
            HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass")), i, new ItemStack(PILES, 3, i - 2));
        for (int i = 0; i <= 2; i++) {
            Block dirt =  Block.REGISTRY.getObject(new ResourceLocation(MODID, "dirt"));
            HCPiles.registerPile(dirt, i, new ItemStack(PILES, 3, i));
            HCPiles.registerPile(Block.REGISTRY.getObject(new ResourceLocation(MODID, "grass_path")), i, new ItemStack(PILES, 3, i));

            GameRegistry.addShapedRecipe(new ItemStack(dirt,1,i), new Object[]{"PP","PP", 'P', new ItemStack(PILES, 1, i)});
        }
    }

    @Override
    public void postInit() {

    }


    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void postInitClient() {

    }
}
