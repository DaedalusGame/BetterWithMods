package betterwithmods.proxy;

import betterwithmods.BWRegistry;
import betterwithmods.api.block.IBTWBlock;
import betterwithmods.blocks.BlockPlanter;
import betterwithmods.blocks.ItemBlockPlanter;
import betterwithmods.blocks.mini.ItemBlockMini;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.client.BWStateMapper;
import betterwithmods.client.model.TESRVerticalWindmill;
import betterwithmods.client.model.TESRWaterwheel;
import betterwithmods.client.model.TESRWindmill;
import betterwithmods.client.render.RenderExtendingRope;
import betterwithmods.client.render.RenderMiningCharge;
import betterwithmods.entity.EntityDynamite;
import betterwithmods.entity.EntityExtendingRope;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.items.IBWMItem;
import betterwithmods.items.ItemMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;

import static betterwithmods.BWRegistry.bark;

public class ClientProxy extends CommonProxy {

    public static ArrayList<Item> itemModelRegistry = new ArrayList<>();

    @Override
    public ItemBlock addItemBlockModel(ItemBlock itemBlock) {
        if (isClientside()) {
            itemModelRegistry.add(itemBlock);
        }
        return itemBlock;
    }

    @Override
    public void registerRenderInformation() {
        int i = 0;
        for (Item item : itemModelRegistry) {
            if (item instanceof ItemBlock) {
                String name = item.getRegistryName().toString();
                Block block = ((ItemBlock) item).getBlock();
                if (item instanceof ItemBlockMini) {
                    this.registerMiniBlockNBT(block, name);
                } else if (block instanceof IBTWBlock) {
                    ModelLoader.setCustomStateMapper(block, new BWStateMapper(name));
                    String[] variants = ((IBTWBlock) block).getVariants();
                    if (variants != null) {
                        registerBlock(name, block, variants);
                    }
                } else {
                    this.registerItemModel(item, 0, name);
                }
            } else {
                if (item instanceof IBWMItem) {
                    for (i = 0; i < ((IBWMItem) item).getMaxMeta(); i++)
                        registerItemModel(item, i, ((IBWMItem) item).getLocation(i));
                }
            }
        }
        for (i = 0; i < ItemMaterial.names.length; ++i) {
            this.registerItemModel(BWRegistry.material, i, "betterwithmods:" + ItemMaterial.names[i].toLowerCase());
        }
        ModelLoader.setCustomStateMapper(BWRegistry.stokedFlame, new BWStateMapper("betterwithmods:stoked_flame"));
        registerItemModel(Item.getItemFromBlock(BWRegistry.pane), 2, "betterwithmods:wicker");
        registerItemModel(Item.getItemFromBlock(BWRegistry.booster), 0, "betterwithmods:gear_rail");
        registerItemModel(Item.getItemFromBlock(BWRegistry.hemp), 0, "betterwithmods:hemp_seed");

        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            registerItemModel(bark, type.getMetadata(), "betterwithmods:bark_" + type.getName());
            registerItemModel(Item.getItemFromBlock(BWRegistry.grate), type.getMetadata(), "betterwithmods:grate_" + type.getName());
            registerItemModel(Item.getItemFromBlock(BWRegistry.slats), type.getMetadata(), "betterwithmods:slats_" + type.getName());
        }
        registerItemModel(BWRegistry.windmill, 0, "betterwithmods:windmill");
        registerItemModel(BWRegistry.windmill, 1, "betterwithmods:waterwheel");
        registerItemModel(BWRegistry.windmill, 2, "betterwithmods:windmill_vertical");
        registerItemModel(BWRegistry.donut, 0, "betterwithmods:donut");
        registerItemModel(BWRegistry.knife, 0, "betterwithmods:iron_knife");
        registerItemModel(BWRegistry.dynamite, 0, "betterwithmods:dynamite");
        registerItemModel(BWRegistry.fertilizer, 0, "betterwithmods:fertilizer");
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillHorizontal.class, new TESRWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindmillVertical.class, new TESRVerticalWindmill());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWaterwheel.class, new TESRWaterwheel());
        ModelLoader.setCustomStateMapper(BWRegistry.windmillBlock, new BWStateMapper("betterwithmods:windmill_block"));
        registerTool(BWRegistry.steelSword, "betterwithmods:steel_sword");
        registerTool(BWRegistry.steelShovel, "betterwithmods:steel_shovel");
        registerTool(BWRegistry.steelPickaxe, "betterwithmods:steel_pickaxe");
        registerTool(BWRegistry.steelHoe, "betterwithmods:steel_hoe");
        registerTool(BWRegistry.steelAxe, "betterwithmods:steel_axe");

        if (Loader.isModLoaded("immersiveengineering"))
            registerBlock("betterwithmods:immersive_axle", BWRegistry.treatedAxle, "active=false,dir=0");
    }

    @Override
    public void registerColors() {
        final BlockColors col = Minecraft.getMinecraft().getBlockColors();
        col.registerBlockColorHandler((state, worldIn, pos, tintIndex) ->
                state.getBlock() instanceof BlockPlanter ? ((BlockPlanter) state.getBlock()).colorMultiplier(state, worldIn, pos, tintIndex) : -1, new Block[]{BWRegistry.planter});
        final ItemColors itCol = Minecraft.getMinecraft().getItemColors();
        itCol.registerItemColorHandler((stack, tintIndex) ->
                (stack.getItem() instanceof ItemBlock && stack.getItem() instanceof ItemBlockPlanter) ? ((ItemBlockPlanter) stack.getItem()).getColorFromItemstack(stack, tintIndex) : -1, new Block[]{BWRegistry.planter});
    }

    @Override
    public void initRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, manager -> new RenderSnowball<EntityDynamite>(manager, BWRegistry.dynamite, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityMiningCharge.class, RenderMiningCharge::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtendingRope.class, RenderExtendingRope::new);
    }

    private void registerItemModel(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
    }

    private void registerBlock(String location, Block block, String... variants) {
        if (block == null)
            return;
        for (int i = 0; i < variants.length; i++)
            if (!variants[i].equals("")) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(location, variants[i]));
            }
    }

    private void registerTool(Item item, final String name) {
        ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(name, "inventory"));
        ModelBakery.registerItemVariants(item, new ModelResourceLocation(name, "inventory"));
    }

    private void registerMiniBlockNBT(Block block, final String name) {
        Item item = Item.getItemFromBlock(block);
        ModelLoader.setCustomMeshDefinition(item, stack -> (stack.hasTagCompound() && stack.getTagCompound().hasKey("type")) ? new ModelResourceLocation(name + "_" + BlockPlanks.EnumType.byMetadata(stack.getTagCompound().getInteger("type")).getName(), "inventory") : stack.getItemDamage() < 6 ? new ModelResourceLocation(name + "_" + BlockPlanks.EnumType.byMetadata(stack.getItemDamage()).getName(), "inventory") : new ModelResourceLocation(name + "_oak", "inventory"));
        ModelResourceLocation[] resource = new ModelResourceLocation[6];
        for (int i = 0; i < 6; i++)
            resource[i] = new ModelResourceLocation(name + "_" + BlockPlanks.EnumType.byMetadata(i), "inventory");
        ModelBakery.registerItemVariants(item, resource);
        ModelLoader.setCustomStateMapper(block, new BWStateMapper(name));
    }

    @Override
    public boolean isClientside() {
        return true;
    }
}
