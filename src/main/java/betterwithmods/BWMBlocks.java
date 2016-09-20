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

	public static final Block anchor = new BlockAnchor().setRegistryName("anchor");
	public static final Block rope = new BlockRope().setRegistryName("rope");
	public static final Block singleMachines = new BlockMechMachines().setRegistryName("single_machine");
	public static final Block axle = new BlockAxle().setRegistryName("axle");
	public static final Block gearbox = new BlockGearbox().setRegistryName("gearbox");
	public static final Block handCrank = new BlockCrank().setRegistryName("hand_crank");
	public static final Block pane = new BlockBTWPane().setRegistryName("pane");
	public static final Block grate = new BlockNewBTWPane(0).setRegistryName("grate");// TODO
																						// remove
																						// typenum
	public static final Block slats = new BlockNewBTWPane(1).setRegistryName("slats");
	public static final Block planter = new BlockPlanter().setRegistryName("planter");
	public static final Block urn = new BlockUrn().setRegistryName("urn");
	public static final Block unfiredPottery = new BlockUnfiredPottery().setRegistryName("unfired_pottery");
	public static final Block stokedFlame = new BlockFireStoked().setRegistryName("stoked_flame");
	public static final Block hibachi = new BlockHibachi().setRegistryName("hibachi");
	public static final Block bellows = new BlockBellows().setRegistryName("bellows");
	public static final Block kiln = new BlockKiln().setRegistryName("kiln");
	public static final Block hemp = new BlockHemp().setRegistryName("hemp");
	public static final Block detector = new BlockDetector().setRegistryName("detector");
	public static final Block lens = new BlockLens().setRegistryName("lens");
	public static final Block lightSource = new BlockInvisibleLight().setRegistryName("invisible_light");
	public static final Block saw = new BlockSaw().setRegistryName("saw");
	public static final Block aesthetic = new BlockAesthetic().setRegistryName("aesthetic");
	public static final Block booster = new BlockGearBoostedRail().setRegistryName("booster");
	public static final Block windmillBlock = new BlockWindmill().setRegistryName("windmill_block");
	public static final Block waterwheel = new BlockWaterwheel().setRegistryName("waterwheel");
	public static final Block woodSiding = new BlockSiding(Material.WOOD).setRegistryName("wood_siding");
	public static final Block woodMoulding = new BlockMoulding(Material.WOOD).setRegistryName("wood_moulding");
	public static final Block woodCorner = new BlockCorner(Material.WOOD).setRegistryName("wood_corner");
	public static final Block debarkedNew = new BlockDebarkedNew().setRegistryName("debarked_new");
	public static final Block debarkedOld = new BlockDebarkedOld().setRegistryName("debarked_old");
	public static final Block woodBench = new BlockWoodBench().setRegistryName("wood_bench");
	public static final Block woodTable = new BlockWoodTable().setRegistryName("wood_table");
	public static final Block wolf = new BlockWolf().setRegistryName("companion_cube");
	public static final Block blockDispenser = new BlockBDispenser().setRegistryName("block_dispenser");
	public static final Block bambooChime = new BlockChime(Material.WOOD, "bamboo_chime")
			.setRegistryName("bamboo_chime");
	public static final Block metalChime = new BlockChime(Material.IRON, "metal_chime").setRegistryName("metal_chime");
	public static final Block buddyBlock = new BlockBUD().setRegistryName("buddy_block");
	public static final Block creativeGenerator = new BlockCreativeGenerator().setRegistryName("creativeGenerator");
	public static final Block light = new BlockLight().setRegistryName("light");
	public static final Block platform = new BlockPlatform().setRegistryName("platform");
	public static final Block miningCharge = new BlockMiningCharge().setRegistryName("mining_charge");
	public static final Block fertileFarmland = new BlockFertileFarmland().setRegistryName("fertile_farmland");
	public static final Block pump = new BlockPump().setRegistryName("screw_pump");

	public static final Block treatedAxle = new BlockImmersiveAxle().setRegistryName("immersive_axle");

	public static final BlockLiquid tempLiquidSource = (BlockLiquid) new BlockTemporaryWater()
			.setRegistryName("temporary_water");

	public static void registerBlocks() {
		registerBlock(anchor);
		registerBlock(rope);
		registerBlock(singleMachines, new ItemBlockMeta(singleMachines));
		registerBlock(axle);
		registerBlock(gearbox);
		registerBlock(handCrank);
		registerBlock(pane, new ItemBlockPane(pane));
		registerBlock(grate, new ItemBlockPane(grate));
		registerBlock(slats, new ItemBlockPane(slats));
		registerBlock(planter, new ItemBlockPlanter(planter));
		registerBlock(urn, new ItemBlockMeta(urn));
		registerBlock(unfiredPottery, new ItemBlockMeta(unfiredPottery));
		registerBlock(stokedFlame, null);
		registerBlock(hibachi);
		registerBlock(bellows);
		registerBlock(kiln, null);
		registerBlock(hemp);
		registerBlock(detector);
		registerBlock(lens);
		registerBlock(lightSource, null);
		registerBlock(saw);
		registerBlock(aesthetic);
		registerBlock(booster);
		registerBlock(windmillBlock, null);
		registerBlock(waterwheel, null);
		registerBlock(woodSiding, new ItemBlockMini(woodSiding));
		registerBlock(woodMoulding, new ItemBlockMini(woodMoulding));
		registerBlock(woodCorner, new ItemBlockMini(woodCorner));
		registerBlock(debarkedNew, new ItemBlockMeta(debarkedNew));
		registerBlock(debarkedOld, new ItemBlockMeta(debarkedOld));
		registerBlock(woodBench, new ItemBlockMeta(woodBench));
		registerBlock(woodTable, new ItemBlockMeta(woodTable));
		registerBlock(wolf);
		registerBlock(blockDispenser);
		registerBlock(bambooChime);
		registerBlock(metalChime);
		registerBlock(buddyBlock);
		registerBlock(creativeGenerator);
		registerBlock(light);
		registerBlock(platform);
		registerBlock(miningCharge);
		registerBlock(fertileFarmland);
		registerBlock(pump);
		registerBlock(treatedAxle);
		registerBlock(tempLiquidSource, null);
	}

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityMill.class, "bwm.millstone");
		GameRegistry.registerTileEntity(TileEntityPulley.class, "bwm.pulley");
		GameRegistry.registerTileEntity(TileEntityFilteredHopper.class, "bwm.hopper");
		GameRegistry.registerTileEntity(TileEntityCauldron.class, "bwm.cauldron");
		GameRegistry.registerTileEntity(TileEntityCrucible.class, "bwm.crucible");
		GameRegistry.registerTileEntity(TileEntityTurntable.class, "bwm.turntable");

		GameRegistry.registerTileEntity(TileEntityWindmillVertical.class, "bwm.vertWindmill");
		GameRegistry.registerTileEntity(TileEntityWindmillHorizontal.class, "bwm.horizWindmill");

		GameRegistry.registerTileEntity(TileEntityWaterwheel.class, "bwm.waterwheel");
		GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "bwm.block_dispenser");
		GameRegistry.registerTileEntity(TileEntityCreativeGen.class, "creativeGenerator");
		GameRegistry.registerTileEntity(TileEntityImmersiveAxle.class, "bwm.immersive_axle");
	}

	public static void linkBlockModels() {
		setInventoryModel(anchor);
		setInventoryModel(rope);
		setInventoryModel(singleMachines);
		setInventoryModel(axle);
		setInventoryModel(gearbox);
		setInventoryModel(handCrank);
		setInventoryModel(pane);
		setInventoryModel(grate);
		setInventoryModel(slats);
		setInventoryModel(planter);
		setInventoryModel(urn);
		setInventoryModel(unfiredPottery);
		setInventoryModel(hibachi);
		setInventoryModel(bellows);
		setInventoryModel(hemp);
		setInventoryModel(detector);
		setInventoryModel(lens);
		setInventoryModel(saw);
		setInventoryModel(aesthetic);
		setInventoryModel(booster);
		setInventoryModel(woodSiding);
		setInventoryModel(woodMoulding);
		setInventoryModel(woodCorner);
		setInventoryModel(debarkedNew);
		setInventoryModel(debarkedOld);
		setInventoryModel(woodBench);
		setInventoryModel(woodTable);
		setInventoryModel(wolf);
		setInventoryModel(blockDispenser);
		setInventoryModel(bambooChime);
		setInventoryModel(metalChime);
		setInventoryModel(buddyBlock);
		setInventoryModel(creativeGenerator);
		setInventoryModel(light);
		setInventoryModel(platform);
		setInventoryModel(miningCharge);
		setInventoryModel(fertileFarmland);
		setInventoryModel(pump);
		setInventoryModel(treatedAxle);
	}

	private static void setInventoryModel(Block block) {
		BWMItems.setInventoryModel(Item.getItemFromBlock(block));
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
	public static Block registerBlock(Block block, @Nullable Item item) {
        block.setUnlocalizedName("bwm"+ block.getRegistryName().toString().substring(BWMod.MODID.length()));
		Block registeredBlock = GameRegistry.register(block);
		if (item != null)
			GameRegistry.register(item.setRegistryName(block.getRegistryName()));
		return registeredBlock;
	}

	/**
	 * Register a Block and a new ItemBlock generated from it.
	 * 
	 * @param block Block instance to register.
	 * @return Registered block.
	 */
	public static Block registerBlock(Block block) {
		return registerBlock(block, new ItemBlock(block));
	}
}
