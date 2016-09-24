package betterwithmods;

import javax.annotation.Nullable;

import betterwithmods.blocks.*;
import betterwithmods.blocks.mini.*;
import betterwithmods.blocks.tile.*;
import betterwithmods.blocks.tile.gen.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BWMBlocks {
	private BWMBlocks() {
	}

	public static final Block ANCHOR = new BlockAnchor().setRegistryName("anchor");
	public static final Block ROPE = new BlockRope().setRegistryName("rope");
	public static final Block SINGLE_MACHINES = new BlockMechMachines().setRegistryName("single_machine");
	public static final Block AXLE = new BlockAxle().setRegistryName("axle");
	public static final Block GEARBOX = new BlockGearbox().setRegistryName("gearbox");
	public static final Block HAND_CRANK = new BlockCrank().setRegistryName("hand_crank");
	public static final Block PANE = new BlockBWMPane().setRegistryName("pane");
	public static final Block GRATE = new BlockBWMNewPane().setRegistryName("grate");
	public static final Block SLATS = new BlockBWMNewPane().setRegistryName("slats");
	public static final Block PLANTER = new BlockPlanter().setRegistryName("planter");
	public static final Block VASE = new BlockVase().setRegistryName("vase");
	public static final Block URN = new BlockUrn().setRegistryName("urn");
	public static final Block UNFIRED_POTTERY = new BlockUnfiredPottery().setRegistryName("unfired_pottery");
	public static final Block STOKED_FLAME = new BlockFireStoked().setRegistryName("stoked_flame");
	public static final Block HIBACHI = new BlockHibachi().setRegistryName("hibachi");
	public static final Block BELLOWS = new BlockBellows().setRegistryName("bellows");
	public static final Block KILN = new BlockKiln().setRegistryName("kiln");
	public static final Block HEMP = new BlockHemp().setRegistryName("hemp");
	public static final Block DETECTOR = new BlockDetector().setRegistryName("detector");
	public static final Block LENS = new BlockLens().setRegistryName("lens");
	public static final Block LIGHT_SOURCE = new BlockInvisibleLight().setRegistryName("invisible_light");
	public static final Block SAW = new BlockSaw().setRegistryName("saw");
	public static final Block AESTHETIC = new BlockAesthetic().setRegistryName("aesthetic");
	public static final Block BOOSTER = new BlockGearBoostedRail().setRegistryName("booster");
	public static final Block WINDMILL_BLOCK = new BlockWindmill().setRegistryName("windmill_block");
	public static final Block WATERWHEEL = new BlockWaterwheel().setRegistryName("waterwheel");
	public static final Block WOOD_SIDING = new BlockSiding(Material.WOOD).setRegistryName("wood_siding");
	public static final Block WOOD_MOULDING = new BlockMoulding(Material.WOOD).setRegistryName("wood_moulding");
	public static final Block WOOD_CORNER = new BlockCorner(Material.WOOD).setRegistryName("wood_corner");
	public static final Block DEBARKED_NEW = new BlockDebarkedNew().setRegistryName("debarked_new");
	public static final Block DEBARKED_OLD = new BlockDebarkedOld().setRegistryName("debarked_old");
	public static final Block WOOD_BENCH = new BlockWoodBench().setRegistryName("wood_bench");
	public static final Block WOOD_TABLE = new BlockWoodTable().setRegistryName("wood_table");
	public static final Block WOLF = new BlockWolf().setRegistryName("companion_cube");
	public static final Block BLOCK_DISPENSER = new BlockBDispenser().setRegistryName("block_dispenser");
	public static final Block BAMBOO_CHIME = new BlockChime(Material.WOOD).setRegistryName("bamboo_chime");
	public static final Block METAL_CHIME = new BlockChime(Material.IRON).setRegistryName("metal_chime");
	public static final Block BUDDY_BLOCK = new BlockBUD().setRegistryName("buddy_block");
	public static final Block CREATIVE_GENERATOR = new BlockCreativeGenerator().setRegistryName("creativeGenerator");
	public static final Block LIGHT = new BlockLight().setRegistryName("light");
	public static final Block PLATFORM = new BlockPlatform().setRegistryName("platform");
	public static final Block MINING_CHARGE = new BlockMiningCharge().setRegistryName("mining_charge");
	public static final Block FERTILE_FARMLAND = new BlockFertileFarmland().setRegistryName("fertile_farmland");
	public static final Block PUMP = new BlockPump().setRegistryName("screw_pump");

	public static final Block TREATED_AXLE = new BlockImmersiveAxle().setRegistryName("immersive_axle");

	public static final BlockLiquid TEMP_LIQUID_SOURCE = (BlockLiquid) new BlockTemporaryWater()
			.setRegistryName("temporary_water");

	public static void registerBlocks() {
		registerBlock(ANCHOR);
		registerBlock(ROPE);
		registerBlock(SINGLE_MACHINES, new ItemBlockMeta(SINGLE_MACHINES));
		registerBlock(AXLE);
		registerBlock(GEARBOX);
		registerBlock(HAND_CRANK);
		registerBlock(PANE, new ItemBlockPane(PANE));
		registerBlock(GRATE, new ItemBlockPane(GRATE));
		registerBlock(SLATS, new ItemBlockPane(SLATS));
		registerBlock(PLANTER, new ItemBlockPlanter(PLANTER));
		registerBlock(VASE, new ItemBlockMeta(VASE));
		registerBlock(URN, new ItemBlockMeta(URN));
		registerBlock(UNFIRED_POTTERY, new ItemBlockMeta(UNFIRED_POTTERY));
		registerBlock(STOKED_FLAME, null);
		registerBlock(HIBACHI);
		registerBlock(BELLOWS);
		registerBlock(KILN, null);
		registerBlock(HEMP);
		registerBlock(DETECTOR);
		registerBlock(LENS);
		registerBlock(LIGHT_SOURCE, null);
		registerBlock(SAW);
		registerBlock(AESTHETIC);
		registerBlock(BOOSTER);
		registerBlock(WINDMILL_BLOCK, null);
		registerBlock(WATERWHEEL, null);
		registerBlock(WOOD_SIDING, new ItemBlockMini(WOOD_SIDING));
		registerBlock(WOOD_MOULDING, new ItemBlockMini(WOOD_MOULDING));
		registerBlock(WOOD_CORNER, new ItemBlockMini(WOOD_CORNER));
		registerBlock(DEBARKED_NEW, new ItemBlockMeta(DEBARKED_NEW));
		registerBlock(DEBARKED_OLD, new ItemBlockMeta(DEBARKED_OLD));
		registerBlock(WOOD_BENCH, new ItemBlockMeta(WOOD_BENCH));
		registerBlock(WOOD_TABLE, new ItemBlockMeta(WOOD_TABLE));
		registerBlock(WOLF);
		registerBlock(BLOCK_DISPENSER);
		registerBlock(BAMBOO_CHIME);
		registerBlock(METAL_CHIME);
		registerBlock(BUDDY_BLOCK);
		registerBlock(CREATIVE_GENERATOR);
		registerBlock(LIGHT);
		registerBlock(PLATFORM);
		registerBlock(MINING_CHARGE);
		registerBlock(FERTILE_FARMLAND);
		registerBlock(PUMP);
		registerBlock(TREATED_AXLE);
		registerBlock(TEMP_LIQUID_SOURCE, null);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMill.class, "bwm.millstone");
		GameRegistry.registerTileEntity(TileEntityPulley.class, "bwm.pulley");
		GameRegistry.registerTileEntity(TileEntityFilteredHopper.class, "bwm.hopper");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "bwm.cauldron");
		GameRegistry.registerTileEntity(TileEntityCrucible.class, "bwm.crucible");
		GameRegistry.registerTileEntity(TileEntityTurntable.class, "bwm.turntable");

		GameRegistry.registerTileEntity(TileEntityVase.class, "bwm.vase");

		GameRegistry.registerTileEntity(TileEntityWindmillVertical.class, "bwm.vertWindmill");
		GameRegistry.registerTileEntity(TileEntityWindmillHorizontal.class, "bwm.horizWindmill");

		GameRegistry.registerTileEntity(TileEntityWaterwheel.class, "bwm.waterwheel");
		GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "bwm.block_dispenser");
		GameRegistry.registerTileEntity(TileEntityCreativeGen.class, "creativeGenerator");
		GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
		GameRegistry.registerTileEntity(TileEntityMultiType.class, "bwm.multiType");
	}

	public static void linkBlockModels() {
		setInventoryModel(ANCHOR);
		setInventoryModel(ROPE);
		setInventoryModel(SINGLE_MACHINES);
		setInventoryModel(AXLE);
		setInventoryModel(GEARBOX);
		setInventoryModel(HAND_CRANK);
		setInventoryModel(PANE);
		setInventoryModel(GRATE);
		setInventoryModel(SLATS);
		setInventoryModel(PLANTER);
		setInventoryModel(URN);
		setInventoryModel(VASE);
		setInventoryModel(UNFIRED_POTTERY);
		setInventoryModel(HIBACHI);
		setInventoryModel(BELLOWS);
		setInventoryModel(HEMP);
		setInventoryModel(DETECTOR);
		setInventoryModel(LENS);
		setInventoryModel(SAW);
		setInventoryModel(AESTHETIC);
		setInventoryModel(BOOSTER);
		setInventoryModel(WOOD_SIDING);
		setInventoryModel(WOOD_MOULDING);
		setInventoryModel(WOOD_CORNER);
		setInventoryModel(DEBARKED_NEW);
		setInventoryModel(DEBARKED_OLD);
		setInventoryModel(WOOD_BENCH);
		setInventoryModel(WOOD_TABLE);
		setInventoryModel(WOLF);
		setInventoryModel(BLOCK_DISPENSER);
		setInventoryModel(BAMBOO_CHIME);
		setInventoryModel(METAL_CHIME);
		setInventoryModel(BUDDY_BLOCK);
		setInventoryModel(CREATIVE_GENERATOR);
		setInventoryModel(LIGHT);
		setInventoryModel(PLATFORM);
		setInventoryModel(MINING_CHARGE);
		setInventoryModel(FERTILE_FARMLAND);
		setInventoryModel(PUMP);
		setInventoryModel(TREATED_AXLE);
	}

	/**
	 * Register a block with its specified linked item. Block's registry name
	 * prevail and must be set before call.
	 * 
	 * @author Koward
	 * 
	 * @param block
	 *            Block instance to register.
	 * @param item
	 *            Item instance to register. Will have the same registered name
	 *            as the block. If is null, then no item will be linked to the
	 *            block.
	 */
	private static Block registerBlock(Block block, @Nullable Item item) {
		block.setUnlocalizedName("bwm" + block.getRegistryName().toString().substring(BWMod.MODID.length()));
		Block registeredBlock = GameRegistry.register(block);
		if (item != null)
			GameRegistry.register(item.setRegistryName(block.getRegistryName()));
		return registeredBlock;
	}

	/**
	 * Register a Block and a new ItemBlock generated from it.
	 * 
	 * @param block
	 *            Block instance to register.
	 * @return Registered block.
	 */
	private static Block registerBlock(Block block) {
		return registerBlock(block, new ItemBlock(block));
	}

	private static void setInventoryModel(Block block) {
		BWMItems.setInventoryModel(Item.getItemFromBlock(block));
	}
}
