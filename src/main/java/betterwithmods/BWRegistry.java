package betterwithmods;

import betterwithmods.blocks.*;
import betterwithmods.blocks.mini.*;
import betterwithmods.blocks.tile.*;
import betterwithmods.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.HardcoreWoodInteraction;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.heat.BWMHeatRegistry;
import betterwithmods.items.*;
import betterwithmods.items.tools.*;
import betterwithmods.util.DispenserBehaviorDynamite;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BWRegistry 
{
	public static Block anchor;
	public static Block rope;
	public static Block platform;
	public static Block axle;
	public static Block gearbox;
	public static Block handCrank;
	public static Block singleMachines;
	public static Block blockDispenser;
	public static Block pane;
	public static Block grate;
	public static Block slats;
	public static Block kiln;
	public static Block planter;
	public static Block urn;
	public static Block unfiredPottery;
	public static Block stokedFlame;
	public static Block hibachi;
	public static Block bellows;
	public static Block hemp;
	public static Block detector;
	public static Block lens;
	public static Block lightSource;
	public static Block saw;
	public static Block aesthetic;
	public static Block booster;
	public static Block windmillBlock;
	public static Block waterwheel;
	public static Block woodSiding;
	public static Block woodMoulding;
	public static Block woodCorner;
	public static Block debarkedNew;
	public static Block debarkedOld;
	public static Block woodBench;
	public static Block woodTable;
	public static Block wolf;
	public static Block bambooChime;
	public static Block metalChime;
	public static Block buddyBlock;
	public static Block creativeGenerator;
	public static Block treatedAxle;
	
	public static Item material;
	public static Item windmill;
	public static Item bark;
	public static Item donut;
	public static Item knife;
	public static Item dynamite;
	public static Item fertilizer;

	public static Item steelSword;
	public static Item steelAxe;
	public static Item steelPickaxe;
	public static Item steelHoe;
	public static Item steelShovel;

	public static final ToolMaterial SOULFORGEDSTEEL = EnumHelper.addToolMaterial("soulforged_steel", 3, 1561, 8, 3, 22);


    public static void init()
	{
		anchor = new BlockAnchor().setCreativeTab(BWCreativeTabs.BWTAB);
		rope = new BlockRope().setCreativeTab(BWCreativeTabs.BWTAB);
		singleMachines = new BlockMechMachines().setCreativeTab(BWCreativeTabs.BWTAB);
		axle = new BlockAxle().setCreativeTab(BWCreativeTabs.BWTAB);
		gearbox = new BlockGearbox().setCreativeTab(BWCreativeTabs.BWTAB);
		handCrank = new BlockCrank().setCreativeTab(BWCreativeTabs.BWTAB);
		pane = new BlockBTWPane();
		grate = new BlockNewBTWPane(0);
		slats = new BlockNewBTWPane(1);
		planter = new BlockPlanter().setCreativeTab(BWCreativeTabs.BWTAB);
		urn = new BlockUrn().setCreativeTab(BWCreativeTabs.BWTAB);
		unfiredPottery = new BlockUnfiredPottery().setCreativeTab(BWCreativeTabs.BWTAB);
		stokedFlame = new BlockFireStoked();
		hibachi = new BlockHibachi().setCreativeTab(BWCreativeTabs.BWTAB);
		bellows = new BlockBellows().setCreativeTab(BWCreativeTabs.BWTAB);
		kiln = new BlockKiln();
		hemp = new BlockHemp();
		detector = new BlockDetector();
		lens = new BlockLens().setCreativeTab(BWCreativeTabs.BWTAB);
		lightSource = new BlockInvisibleLight();
		saw = new BlockSaw().setCreativeTab(BWCreativeTabs.BWTAB);
		aesthetic = new BlockAesthetic();
		booster = new BlockGearBoostedRail();
		windmillBlock = new BlockWindmill();
		waterwheel = new BlockWaterwheel();
		woodSiding = new BlockSiding(Material.WOOD);
		woodMoulding = new BlockMoulding(Material.WOOD);
		woodCorner = new BlockCorner(Material.WOOD);
		debarkedNew = new BlockDebarkedNew();
		debarkedOld = new BlockDebarkedOld();
		woodBench = new BlockWoodBench();
		woodTable = new BlockWoodTable();
		wolf = new BlockWolf();
		blockDispenser = new BlockBDispenser();
		bambooChime = new BlockChime(Material.WOOD).setUnlocalizedName("bwm:bamboo_chime");
		metalChime = new BlockChime(Material.IRON).setUnlocalizedName("bwm:metal_chime");
		buddyBlock = new BlockBUD();
		creativeGenerator = new BlockCreativeGenerator();
		registerBlock(axle, "axle");
		registerMetaBlock(singleMachines, "single_machine");
		GameRegistry.registerTileEntity(TileEntityMill.class, "bwm.millstone");
		GameRegistry.registerTileEntity(TileEntityPulley.class, "bwm.pulley");
		GameRegistry.registerTileEntity(TileEntityFilteredHopper.class, "bwm.hopper");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "bwm.cauldron");
		GameRegistry.registerTileEntity(TileEntityCrucible.class, "bwm.crucible");
		GameRegistry.registerTileEntity(TileEntityTurntable.class, "bwm.turntable");
		registerBlock(gearbox, "gearbox");
		registerBlock(handCrank, "hand_crank");
		registerBlock(booster, "booster");
		registerPaneBlock(pane, "pane");
		registerPaneBlock(grate, "grate");
		registerPaneBlock(slats, "slats");
		GameRegistry.registerTileEntity(TileEntityWaterwheel.class, "bwm.waterwheel");
		GameRegistry.registerTileEntity(TileEntityWindmillVertical.class, "bwm.vertWindmill");
		GameRegistry.registerTileEntity(TileEntityWindmillHorizontal.class, "bwm.horizWindmill");
		registerPlanter(planter, "planter");
		registerMetaBlock(urn, "urn");
		registerMetaBlock(unfiredPottery, "unfired_pottery");
		registerBlock(anchor, "anchor");
		registerBlock(rope, "rope");
		registerBlock(stokedFlame, "stoked_flame");
		registerBlock(hibachi, "hibachi");
		registerBlock(bellows, "bellows");
		registerBlock(kiln, "kiln");
		registerBlock(hemp, "hemp");
		MinecraftForge.addGrassSeed(new ItemStack(hemp, 1, 0), 5);
		registerBlock(detector, "detector");
		registerBlock(lens, "lens");
		registerBlock(lightSource, "invisible_light");
		registerBlock(saw, "saw");
		registerMetaBlock(aesthetic, "aesthetic");
		registerMetaBlock(windmillBlock, "windmill_block");
		registerMetaBlock(waterwheel, "waterwheel");
		GameRegistry.registerTileEntity(TileEntityMultiType.class, "bwm.multiType");
		registerMiniBlock(woodSiding, "wood_siding");
		registerMiniBlock(woodMoulding, "wood_moulding");
		registerMiniBlock(woodCorner, "wood_corner");
		registerMetaBlock(debarkedOld, "debarked_old");
		registerMetaBlock(debarkedNew, "debarked_new");
		registerMetaBlock(woodBench, "wood_bench");
		registerMetaBlock(woodTable, "wood_table");
		registerBlock(wolf, "companion_cube");
		registerBlock(blockDispenser, "block_dispenser");
		GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "bwm.block_dispenser");
		registerMetaBlock(bambooChime, "bamboo_chime");
		registerMetaBlock(metalChime, "metal_chime");

		if(Loader.isModLoaded("immersiveengineering")) {
			treatedAxle = new BlockImmersiveAxle();
			registerImmersiveBlock(treatedAxle, "immersive_axle");
			GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
		}
		
		material = new ItemMaterial();
		windmill = new ItemMechanical().setCreativeTab(BWCreativeTabs.BWTAB);
		bark = new ItemBark().setCreativeTab(BWCreativeTabs.BWTAB);
		donut = new ItemFood(2, 0.5F, false).setUnlocalizedName("bwm:donut").setCreativeTab(BWCreativeTabs.BWTAB);
		knife = new ItemKnife(ToolMaterial.IRON).setUnlocalizedName("bwm:iron_knife");
		dynamite = new ItemDynamite().setCreativeTab(BWCreativeTabs.BWTAB);
		fertilizer = new ItemFertilizer().setUnlocalizedName("bwm:fertilizer");
		steelAxe = new ItemSoulforgedAxe();
		steelHoe = new ItemSoulforgedHoe();
		steelPickaxe = new ItemSoulforgedPickaxe();
		steelShovel = new ItemSoulforgedShovel();
		steelSword = new ItemSoulforgedSword();
		
		registerItem(material, "material");
		registerItem(windmill, "windmill");
		registerItem(bark, "bark");
		registerItem(donut, "donut");
		registerItem(knife, "knife");
		registerItem(dynamite, "dynamite");
		registerItem(fertilizer, "fertilizer");
		registerItem(steelAxe, "steel_axe");
		registerItem(steelHoe, "steel_hoe");
		registerItem(steelPickaxe, "steel_pickaxe");
		registerItem(steelShovel, "steel_shovel");
		registerItem(steelSword, "steel_sword");
		
		registerOres();
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(dynamite, new DispenserBehaviorDynamite());

		if(BWConfig.hardcoreBuckets)
		{
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, new BehaviorDefaultDispenseItem() {
				@Override
				public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
				{
					BlockPos pos = source.getBlockPos().offset(source.func_189992_e().getValue(BlockDispenser.FACING));
					if(source.getWorld().isAirBlock(pos) || source.getWorld().getBlockState(pos).getBlock().isReplaceable(source.getWorld(), pos)) {
						source.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
						for(EnumFacing face : EnumFacing.HORIZONTALS)
						{
							BlockPos off = pos.offset(face);
							if(source.getWorld().isAirBlock(off) || source.getWorld().getBlockState(off).getBlock().isReplaceable(source.getWorld(), off))
								source.getWorld().setBlockState(off, Blocks.FLOWING_WATER.getStateFromMeta(5));
						}
						stack.setItem(Items.BUCKET);
						stack.stackSize = 1;
					}
					return stack;
				}
			});
		}
		BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.REPEATER, new BehaviorDiodeDispense());
		BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.COMPARATOR, new BehaviorDiodeDispense());
	}

	public static void registerMiniBlock(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlockMini(block).setRegistryName(name));
	}

	public static void registerMetaBlock(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlockMeta(block).setRegistryName(name));
	}

	public static void registerPlanter(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlockPlanter(block).setRegistryName(name));
	}

	public static void registerPaneBlock(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlockPane(block).setRegistryName(name));
	}

	private static void registerImmersiveBlock(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlockImmersive(block).setRegistryName(name));
	}

	public static void registerBlock(Block block, String name)
	{
		GameRegistry.register(block.setRegistryName(name));
		GameRegistry.register(new ItemBlock(block).setRegistryName(name));
	}

	public static void registerItem(Item item, String name)
	{
		GameRegistry.register(item.setRegistryName(name));
	}
	
	public static void registerOres()
	{
		OreDictionary.registerOre("gearWood", new ItemStack(material, 1, 0));
		OreDictionary.registerOre("cropHemp", new ItemStack(material, 1, 2));
		OreDictionary.registerOre("dyeBrown", new ItemStack(material, 1, 5));
		OreDictionary.registerOre("slimeball", new ItemStack(material, 1, 12));
		OreDictionary.registerOre("ingotSoulforgedSteel", new ItemStack(material, 1, 14));
		OreDictionary.registerOre("dustNetherrack", new ItemStack(material, 1, 15));
		OreDictionary.registerOre("powderedHellfire", new ItemStack(material, 1, 16));
		OreDictionary.registerOre("ingotHellfire", new ItemStack(material, 1, 17));
		OreDictionary.registerOre("dustCoal", new ItemStack(material, 1, 18));
		OreDictionary.registerOre("dustPotash", new ItemStack(material, 1, 21));
		OreDictionary.registerOre("dustWood", new ItemStack(material, 1, 22));
		OreDictionary.registerOre("dustSulfur", new ItemStack(material, 1, 25));
		OreDictionary.registerOre("dustSaltpeter", new ItemStack(material, 1, 26));
		OreDictionary.registerOre("nuggetIron", new ItemStack(material, 1, 30));
		OreDictionary.registerOre("nuggetSoulforgedSteel", new ItemStack(material, 1, 31));
		OreDictionary.registerOre("foodFlour", new ItemStack(material, 1, 37));
		OreDictionary.registerOre("dustCharcoal", new ItemStack(material, 1, 39));

		OreDictionary.registerOre("blockSoulforgedSteel", new ItemStack(aesthetic, 1, 2));
		OreDictionary.registerOre("blockHellfire", new ItemStack(aesthetic, 1, 3));
		
		OreDictionary.registerOre("barkWood", new ItemStack(bark, 1, 32767));
		OreDictionary.registerOre("craftingToolKnife", new ItemStack(knife, 1, 32767));
		OreDictionary.registerOre("slabWood", new ItemStack(woodSiding, 1, 32767));
		//TFC compatibility
		OreDictionary.registerOre("itemKnife", new ItemStack(knife, 1, 32767));
		OreDictionary.registerOre("string", new ItemStack(material, 1, 3));
		OreDictionary.registerOre("fiberHemp", new ItemStack(material, 1, 3));
		OreDictionary.registerOre("fabricHemp", new ItemStack(material, 1, 4));
	}
	
	public static void registerHeatSources()
	{
		BWMHeatRegistry.setBlockHeatRegistry(Blocks.FIRE, 3);
		BWMHeatRegistry.setBlockHeatRegistry(stokedFlame, 8);
	}
	
	public static void registerNetherWhitelist()
	{
		NetherSpawnWhitelist.addBlock(Blocks.NETHERRACK);
		NetherSpawnWhitelist.addBlock(Blocks.NETHER_BRICK);
		NetherSpawnWhitelist.addBlock(Blocks.SOUL_SAND);
		NetherSpawnWhitelist.addBlock(Blocks.GRAVEL);
		NetherSpawnWhitelist.addBlock(Blocks.QUARTZ_BLOCK);
	}
	
	public static void registerWood()
	{
		for(ItemStack woodStack : OreDictionary.getOres("logWood"))
		{
			if(woodStack.getItem() instanceof ItemBlock)
			{
				ItemBlock iBlock = (ItemBlock)woodStack.getItem();
				Block block = iBlock.getBlock();
				if(!Loader.isModLoaded("terrafirmacraft"))
					block.setHardness(5.0F);
				int meta = woodStack.getItemDamage();
				if(block == Blocks.LOG)
				{
					for(int i = 0; i < 4; i++)
					{
						HardcoreWoodInteraction.addBlock(block, i, new ItemStack(bark, 1, i));
					}
				}
				else if(block == Blocks.LOG2)
				{
					for(int i = 0; i < 2; i++)
					{
						HardcoreWoodInteraction.addBlock(block, i, new ItemStack(bark, 1, i + 4));
					}
				}
				else if(meta == 32767)
				{
					HardcoreWoodInteraction.addBlock(block, null);
				}
				else
					HardcoreWoodInteraction.addBlock(block, meta, null);
			}
		}
		for(ItemStack stack : OreDictionary.getOres("plankWood"))
		{
			if(stack.getItem() instanceof ItemBlock)
			{
				Block block = ((ItemBlock)stack.getItem()).getBlock();
				if(block == Blocks.PLANKS)
				{
					for(int i = 0; i < 6; i++)
					{
						ItemStack wood = new ItemStack(BWRegistry.woodSiding, 2, i);
						SawInteraction.addBlock(block, i, wood);
					}
				}
				else if(stack.getItemDamage() == 32767)
				{
					for(int i = 0; i < 16; i++)
					{
						ItemStack wood = new ItemStack(BWRegistry.woodSiding, 2, 0);
						SawInteraction.addBlock(block, i, wood);
					}
				}
				else
				{
					ItemStack wood = new ItemStack(BWRegistry.woodSiding, 2, 0);
					SawInteraction.addBlock(block, stack.getItemDamage(), wood);
				}
			}
		}
		for(int i = 0; i < 4; i++)
		{
			ItemStack planks = new ItemStack(Blocks.PLANKS, 5, i);
			SawInteraction.addBlock(BWRegistry.debarkedOld, i, planks);
		}
		for(int i = 0; i < 2; i++)
		{
			ItemStack planks = new ItemStack(Blocks.PLANKS, 5, 4 + i);
			SawInteraction.addBlock(BWRegistry.debarkedNew, i, planks);
		}
	}
}
