package betterwithmods.proxy;

import betterwithmods.blocks.BlockPlanter;
import betterwithmods.blocks.ItemBlockPlanter;
import betterwithmods.client.BWStateMapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import betterwithmods.BWRegistry;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.client.model.TESRVerticalWindmill;
import betterwithmods.client.model.TESRWaterwheel;
import betterwithmods.client.model.TESRWindmill;
import betterwithmods.entity.EntityDynamite;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderInformation()
	{
		registerBlock("betterwithmods:axle", BWRegistry.axle, "dir=0,signal=0");
		registerItemModel(Item.getItemFromBlock(BWRegistry.gearbox), 0, "betterwithmods:gearbox");
		registerItemModel(Item.getItemFromBlock(BWRegistry.saw), 0, "betterwithmods:saw");
		registerItemModel(Item.getItemFromBlock(BWRegistry.stokedFlame), 0, "betterwithmods:stoked_flame");
		ModelLoader.setCustomStateMapper(BWRegistry.stokedFlame, new BWStateMapper("betterwithmods:stoked_flame"));
		registerItemModel(Item.getItemFromBlock(BWRegistry.anchor), 0, "betterwithmods:anchor");
		String[] variant = {"potterytype=crucible", "potterytype=planter", "potterytype=urn"};
		registerBlock("betterwithmods:unfired_pottery", BWRegistry.unfiredPottery, variant);
		variant = new String[] {"plantertype=empty", "plantertype=dirt", "plantertype=grass", "plantertype=soul_sand", "plantertype=fertile", "plantertype=sand", "plantertype=water_still", "plantertype=gravel", "plantertype=red_sand"};
		registerBlock("betterwithmods:planter", BWRegistry.planter, variant);
		variant = new String[] {"underhopper=false,urntype=empty", "underhopper=false,urntype=12", "underhopper=false,urntype=25", "underhopper=false,urntype=37", "underhopper=false,urntype=50", "underhopper=false,urntype=62",
		"underhopper=false,urntype=75", "underhopper=false,urntype=87", "underhopper=false,urntype=filled", "underhopper=false,urntype=void"};
		registerBlock("betterwithmods:urn", BWRegistry.urn, variant);
		registerItemModel(Item.getItemFromBlock(BWRegistry.booster), 0, "betterwithmods:gear_rail");
		String[] names = {"gear", "nethercoal", "hemp", "hemp_fibers", "hemp_cloth", "dung", "tanned_leather", "scoured_leather", "leather_strap", "leather_belt", "wood_blade",
				"windmill_blade", "glue", "tallow", "ingot_steel", "ground_netherrack", "hellfire_dust", "concentrated_hellfire", "coal_dust", "filament", "polished_lapis",
				"potash", "sawdust", "soul_dust", "screw", "brimstone", "niter", "element", "fuse", "blasting_oil", "nugget_iron", "nugget_steel", "leather_cut",
				"tanned_leather_cut", "scoured_leather_cut", "redstone_latch", "nether_sludge", "flour", "haft", "charcoal_dust", "sharpening_stone", "knife_blade"};
		for(int i = 0; i < 42; i++)
		{
			registerItemModel(BWRegistry.material, i, "betterwithmods:" + names[i]);
		}
		String[] bark = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "blood_wood"};
		for(int i = 0; i < 6; i++)
		{
			registerItemModel(BWRegistry.bark, i, "betterwithmods:bark_" + bark[i]);
		}
		registerItemModel(Item.getItemFromBlock(BWRegistry.pane), 2, "betterwithmods:wicker");
		String[] woodTypes = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};
		for(int i = 0; i < woodTypes.length; i++)
		{
			registerItemModel(Item.getItemFromBlock(BWRegistry.grate), i, "betterwithmods:grate_" + woodTypes[i]);
			registerItemModel(Item.getItemFromBlock(BWRegistry.slats), i, "betterwithmods:slats_" + woodTypes[i]);
		}
		variant = new String[] {"axis=y,variant=oak", "axis=y,variant=spruce", "axis=y,variant=birch", "axis=y,variant=jungle"};
		registerBlock("betterwithmods:debarked_old", BWRegistry.debarkedOld, variant);
		variant = new String[] {"axis=y,variant=acacia", "axis=y,variant=dark_oak"};
		registerBlock("betterwithmods:debarked_new", BWRegistry.debarkedNew, variant);
		registerItemModel(Item.getItemFromBlock(BWRegistry.hemp), 0, "betterwithmods:hemp_seed");
		registerItemModel(Item.getItemFromBlock(BWRegistry.rope), 0, "betterwithmods:rope");
		registerBlock("betterwithmods:hand_crank", BWRegistry.handCrank, "stage=0");
		registerItemModel(Item.getItemFromBlock(BWRegistry.hibachi), 0, "betterwithmods:hibachi");
		registerItemModel(Item.getItemFromBlock(BWRegistry.bellows), 0, "betterwithmods:bellows");
		registerItemModel(Item.getItemFromBlock(BWRegistry.lens), 0, "betterwithmods:lens");
		registerMechMachines();
		registerItemModel(Item.getItemFromBlock(BWRegistry.kiln), 0, "betterwithmods:kiln");
		registerItemModel(Item.getItemFromBlock(BWRegistry.detector), 0, "betterwithmods:detector");
		variant = new String[] {"blocktype=chopping", "blocktype=chopping_blood", "blocktype=steel", "blocktype=hellfire", "blocktype=rope", "blocktype=flint"};
		registerBlock("betterwithmods:aesthetic", BWRegistry.aesthetic, variant);
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
		registerMiniBlockNBT(BWRegistry.woodSiding, "betterwithmods:wood_siding");
		registerMiniBlockNBT(BWRegistry.woodMoulding, "betterwithmods:wood_moulding");
		registerMiniBlockNBT(BWRegistry.woodCorner, "betterwithmods:wood_corner");
		registerFurnitureModel(BWRegistry.woodBench, 6, "betterwithmods:wood_bench");
		registerFurnitureModel(BWRegistry.woodTable, 6, "betterwithmods:wood_table");
		ModelLoader.setCustomStateMapper(BWRegistry.windmillBlock, new BWStateMapper("betterwithmods:windmill_block"));
		registerBlock("betterwithmods:block_dispenser", BWRegistry.blockDispenser, "facing=north,triggered=false");
		registerBlock("betterwithmods:companion_cube", BWRegistry.wolf, "facing=north");
		registerTool(BWRegistry.steelSword, "betterwithmods:steel_sword");
		registerTool(BWRegistry.steelShovel, "betterwithmods:steel_shovel");
		registerTool(BWRegistry.steelPickaxe, "betterwithmods:steel_pickaxe");
		registerTool(BWRegistry.steelHoe, "betterwithmods:steel_hoe");
		registerTool(BWRegistry.steelAxe, "betterwithmods:steel_axe");
		variant = new String[] {"active=false,type=oak", "active=false,type=spruce", "active=false,type=birch", "active=false,type=jungle", "active=false,type=acacia", "active=false,type=dark_oak"};
		registerBlock("betterwithmods:bamboo_chime", BWRegistry.bambooChime, variant);
		registerBlock("betterwithmods:metal_chime", BWRegistry.metalChime, variant);
		if(Loader.isModLoaded("immersiveengineering"))
			registerBlock("betterwithmods:immersive_axle", BWRegistry.treatedAxle, "active=false,dir=0");
	}

	@Override
	public void registerColors()
	{
		final BlockColors col = Minecraft.getMinecraft().getBlockColors();
		col.registerBlockColorHandler(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
			{
				if(state.getBlock() instanceof BlockPlanter)
					return ((BlockPlanter)state.getBlock()).colorMultiplier(state, worldIn, pos, tintIndex);
				return -1;
			}
		}, new Block[] {BWRegistry.planter});
		final ItemColors itCol = Minecraft.getMinecraft().getItemColors();
		itCol.registerItemColorHandler(new IItemColor() {
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				if(stack.getItem() instanceof ItemBlock && stack.getItem() instanceof ItemBlockPlanter)
					return ((ItemBlockPlanter)stack.getItem()).getColorFromItemstack(stack, tintIndex);
				return -1;
			}
		}, new Block[] {BWRegistry.planter});
	}

	private void registerMechMachines()
	{
		Block block = BWRegistry.singleMachines;
		ModelLoader.setCustomStateMapper(block, new BWStateMapper("betterwithmods:single_machine"));
		Item item = Item.getItemFromBlock(block);
		registerItemModel(item, 0, "betterwithmods:single_machine");
		registerItemModel(item, 2, "betterwithmods:crucible");
		registerItemModel(item, 3, "betterwithmods:cauldron");
		registerItemModel(item, 4, "betterwithmods:hopper");
		registerItemModel(item, 5, "betterwithmods:turntable");
	}

	private void registerFurnitureModel(Block block, int maxMeta, String location)
	{
		for(int i = 0; i < maxMeta; i++)
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(location, "supported=false,type=" + i));
		ModelLoader.setCustomStateMapper(block, new BWStateMapper(location));
	}
	
	@Override
	public void postRender()
	{
		//RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new DynamiteRenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), BWRegistry.dynamite, Minecraft.getMinecraft().getRenderItem()));
	}
	
	private void registerItemModel(Item item, int meta, String name)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, "inventory"));
	}

	private void registerBlock(String location, Block block, String... variants)
	{
		for(int i = 0; i < variants.length; i++)
			if(!variants[i].equals(""))
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(location, variants[i]));
		ModelLoader.setCustomStateMapper(block, new BWStateMapper(location));
	}

	private void registerTool(Item item, final String name)
	{
		ItemMeshDefinition def = new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				return new ModelResourceLocation(name, "inventory");
			}
		};
		ModelLoader.setCustomMeshDefinition(item, def);
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(name, "inventory"));
	}

	private void registerMiniBlockNBT(Block block, final String name)
	{
		Item item = Item.getItemFromBlock(block);
		final String[] woodType = {"_oak", "_spruce", "_birch", "_jungle", "_acacia", "_dark_oak"};
		ItemMeshDefinition def = new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack)
			{
				if(stack.hasTagCompound())
				{
					if(stack.getTagCompound().hasKey("type"))
						return new ModelResourceLocation(name + woodType[stack.getTagCompound().getInteger("type")], "inventory");
				}
				else if(stack.getItemDamage() < 6)
				{
					return new ModelResourceLocation(name + woodType[stack.getItemDamage()], "inventory");
				}
				return new ModelResourceLocation(name + "_oak", "inventory");
			}
		};
		ModelLoader.setCustomMeshDefinition(item, def);
		ModelResourceLocation[] resource = new ModelResourceLocation[6];
		for(int i = 0; i < 6; i++)
			resource[i] = new ModelResourceLocation(name + woodType[i], "inventory");
		ModelBakery.registerItemVariants(item, resource);
		ModelLoader.setCustomStateMapper(block, new BWStateMapper(name));
	}

	@Override
	public boolean isClientside()
	{
		return true;
	}
}
