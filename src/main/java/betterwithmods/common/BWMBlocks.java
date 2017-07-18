package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.*;
import betterwithmods.common.blocks.mechanical.*;
import betterwithmods.common.blocks.mini.*;
import betterwithmods.common.blocks.tile.*;
import betterwithmods.common.blocks.tile.gen.TileEntityCreativeGen;
import betterwithmods.common.blocks.tile.gen.TileEntityWaterwheel;
import betterwithmods.common.blocks.tile.gen.TileEntityWindmillHorizontal;
import betterwithmods.common.blocks.tile.gen.TileEntityWindmillVertical;
import betterwithmods.common.items.ItemHempSeed;
import betterwithmods.common.items.ItemSimpleSlab;
import betterwithmods.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class BWMBlocks {
    public static final Material POTTERY = new Material(MapColor.STONE);

    public static final Block ANCHOR = new BlockAnchor().setRegistryName("anchor");
    public static final Block ROPE = new BlockRope().setRegistryName("rope");
    public static final Block SINGLE_MACHINES = new BlockMechMachines().setRegistryName("single_machine");
    public static final Block AXLE = new BlockAxle().setRegistryName("axle");
    public static final Block GEARBOX = new BlockGearbox().setRegistryName("gearbox");
    public static final Block HAND_CRANK = new BlockCrank().setRegistryName("hand_crank");
    public static final Block WICKER = new BlockWicker().setRegistryName("wicker");
    public static final Block GRATE = new BlockMultiPane().setRegistryName("grate");
    public static final Block SLATS = new BlockMultiPane().setRegistryName("slats");
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
    public static final Block WOOD_SIDING = new BlockSiding(BlockMini.MINI).setRegistryName("wood_siding");
    public static final Block WOOD_MOULDING = new BlockMoulding(BlockMini.MINI).setRegistryName("wood_moulding");
    public static final Block WOOD_CORNER = new BlockCorner(BlockMini.MINI).setRegistryName("wood_corner");
    public static final Block WOOD_BENCH = new BlockWoodBench().setRegistryName("wood_bench");
    public static final Block WOOD_TABLE = new BlockWoodTable().setRegistryName("wood_table");
    public static final Block WOLF = new BlockWolf().setRegistryName("companion_cube");
    public static final Block BLOCK_DISPENSER = new BlockBDispenser().setRegistryName("block_dispenser");
    public static final Block BAMBOO_CHIME = new BlockChime(Material.WOOD).setRegistryName("bamboo_chime");
    public static final Block METAL_CHIME = new BlockChime(Material.IRON).setRegistryName("metal_chime");
    public static final Block BUDDY_BLOCK = new BlockBUD().setRegistryName("buddy_block");
    public static final Block CREATIVE_GENERATOR = new BlockCreativeGenerator().setRegistryName("creative_generator");
    public static final Block LIGHT = new BlockLight().setRegistryName("light");
    public static final Block PLATFORM = new BlockPlatform().setRegistryName("platform");
    public static final Block MINING_CHARGE = new BlockMiningCharge().setRegistryName("mining_charge");
    public static final Block FERTILE_FARMLAND = new BlockFertileFarmland().setRegistryName("fertile_farmland");
    public static final Block PUMP = new BlockPump().setRegistryName("screw_pump");
    public static final Block ADVANCED_GEARBOX = new BlockAdvGearbox().setRegistryName("advanced_gearbox");
    public static final Block VINE_TRAP = new BlockVineTrap().setRegistryName("vine_trap");
    public static final BlockLiquid TEMP_LIQUID_SOURCE = (BlockLiquid) new BlockTemporaryWater().setRegistryName("temporary_water");
    public static final Block RAW_PASTRY = new BlockRawPastry().setRegistryName("raw_pastry");
    public static final Block STEEL_ANVIL = new BlockSteelAnvil().setRegistryName("steel_anvil");
    public static final Block STONE_SIDING = new BlockSiding(Material.ROCK).setRegistryName("stone_siding");
    public static final Block STONE_MOULDING = new BlockMoulding(Material.ROCK).setRegistryName("stone_moulding");
    public static final Block STONE_CORNER = new BlockCorner(Material.ROCK).setRegistryName("stone_corner");
    public static final Block STUMP = new BlockStump().setRegistryName("stump");
    public static final Block DIRT_SLAB = new BlockDirtSlab().setRegistryName("dirt_slab");
    public static final Block BROKEN_GEARBOX = new BlockBrokenGearbox().setRegistryName("broken_gearbox");
    public static final Block COOKING_POTS = new BlockCookingPot().setRegistryName("cooking_pot");
    public static final Block ADVANCED_BELLOWS = new BlockAdvBellows().setRegistryName("advanced_bellows");
    public static final Block IRON_WALL = new BlockIronWall().setRegistryName("iron_wall");
    public static final Block STAKE = new BlockStake().setRegistryName("stake");
    public static final Block NETHER_GROWTH = new BlockNetherGrowth().setRegistryName("nether_growth");
    public static Block BEACON;

    private static final List<Block> BLOCKS = new ArrayList<>();

    public static List<Block> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static void registerBlocks() {
        registerBlock(ANCHOR);
        registerBlock(ROPE);
        registerBlock(SINGLE_MACHINES, new ItemBlockMeta(SINGLE_MACHINES));
        registerBlock(AXLE);
        registerBlock(GEARBOX);
        registerBlock(HAND_CRANK);
        registerBlock(WICKER);
        registerBlock(GRATE, new ItemBlockMeta(GRATE));
        registerBlock(SLATS, new ItemBlockMeta(SLATS));
        registerBlock(PLANTER, new ItemBlockPlanter(PLANTER));
        registerBlock(VASE, new ItemBlockMeta(VASE));
        registerBlock(URN, new ItemBlockUrn(URN));
        registerBlock(UNFIRED_POTTERY, new ItemBlockMeta(UNFIRED_POTTERY));
        registerBlock(STOKED_FLAME, null);
        registerBlock(HIBACHI);
        registerBlock(BELLOWS);
        registerBlock(KILN, null);
        registerBlock(HEMP, new ItemHempSeed(HEMP));
        registerBlock(DETECTOR);
        registerBlock(LENS);
        registerBlock(LIGHT_SOURCE, null);
        registerBlock(SAW);
        registerBlock(AESTHETIC, new ItemBlockMeta(AESTHETIC));
        registerBlock(BOOSTER);
        registerBlock(WINDMILL_BLOCK, null);
        registerBlock(WATERWHEEL, null);
        registerBlock(WOOD_SIDING, new ItemBlockMini(WOOD_SIDING));
        registerBlock(WOOD_MOULDING, new ItemBlockMini(WOOD_MOULDING));
        registerBlock(WOOD_CORNER, new ItemBlockMini(WOOD_CORNER));
        registerBlock(WOOD_BENCH, new ItemBlockMeta(WOOD_BENCH));
        registerBlock(WOOD_TABLE, new ItemBlockMeta(WOOD_TABLE));
        registerBlock(WOLF);
        registerBlock(BLOCK_DISPENSER);
        registerBlock(BAMBOO_CHIME, new ItemBlockMeta(BAMBOO_CHIME));
        registerBlock(METAL_CHIME, new ItemBlockMeta(METAL_CHIME));
        registerBlock(BUDDY_BLOCK);
        registerBlock(CREATIVE_GENERATOR);
        registerBlock(LIGHT);
        registerBlock(PLATFORM);
        registerBlock(MINING_CHARGE);
        registerBlock(FERTILE_FARMLAND);
        registerBlock(PUMP);
        registerBlock(ADVANCED_GEARBOX);
        registerBlock(VINE_TRAP);
        registerBlock(RAW_PASTRY, new ItemBlockMeta(RAW_PASTRY));
        registerBlock(STEEL_ANVIL);
        registerBlock(STONE_SIDING, new ItemBlockMini(STONE_SIDING));
        registerBlock(STONE_MOULDING, new ItemBlockMini(STONE_MOULDING));
        registerBlock(STONE_CORNER, new ItemBlockMini(STONE_CORNER));
        registerBlock(STUMP, new ItemBlockMeta(STUMP));
        registerBlock(DIRT_SLAB, new ItemSimpleSlab(DIRT_SLAB, Blocks.DIRT));
        registerBlock(BROKEN_GEARBOX);
        registerBlock(COOKING_POTS, new ItemBlockMeta(COOKING_POTS) {
            @Override
            public int getItemStackLimit(ItemStack stack) {
                return stack.getMetadata() == BlockCookingPot.EnumType.DRAGONVESSEL.getMeta() ? 1 : 64;
            }
        });

        registerBlock(TEMP_LIQUID_SOURCE, null);
        registerBlock(ADVANCED_BELLOWS);
        registerBlock(IRON_WALL);
        registerBlock(STAKE);
        registerBlock(NETHER_GROWTH, new ItemBlockSpore(NETHER_GROWTH));
        registerBlock(new BlockBeacon());
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityMill.class, "bwm.millstone");
        GameRegistry.registerTileEntity(TileEntityPulley.class, "bwm.pulley");
        GameRegistry.registerTileEntity(TileEntityFilteredHopper.class, "bwm.hopper");
        GameRegistry.registerTileEntity(TileEntityCauldron.class, "bwm.cauldron");
        GameRegistry.registerTileEntity(TileEntityCrucible.class, "bwm.crucible");
        GameRegistry.registerTileEntity(TileEntityDragonVessel.class, "bwm.vessel");
        GameRegistry.registerTileEntity(TileEntityTurntable.class, "bwm.turntable");
        GameRegistry.registerTileEntity(TileEntitySteelAnvil.class, "bwm.steelAnvil");
        GameRegistry.registerTileEntity(TileEntityVase.class, "bwm.vase");
        GameRegistry.registerTileEntity(TileEntityWindmillVertical.class, "bwm.vertWindmill");
        GameRegistry.registerTileEntity(TileEntityWindmillHorizontal.class, "bwm.horizWindmill");
        GameRegistry.registerTileEntity(TileEntityWaterwheel.class, "bwm.waterwheel");
        GameRegistry.registerTileEntity(TileEntityBlockDispenser.class, "bwm.block_dispenser");
        GameRegistry.registerTileEntity(TileEntityCreativeGen.class, "creativeGenerator");
        GameRegistry.registerTileEntity(TileEntityMultiType.class, "bwm.multiType");
        GameRegistry.registerTileEntity(TileEntityGearbox.class, "bwm.gearbox");
        GameRegistry.registerTileEntity(TileEntityAdvBellows.class, "bwm.bellows");
        GameRegistry.registerTileEntity(TileStake.class, "bwm.stake");
        GameRegistry.registerTileEntity(TileCamo.class, "bwm.camo");
        GameRegistry.registerTileEntity(TileEntityBeacon.class, "bwm.beacon");
    }

//    /**
//     * Substitute vanilla blocks with our custom instances.
//     * Should be done at the earliest point in preInit. The earlier, the better.
//     *
//     * @throws ExistingSubstitutionException
//     */
//    public static void substituteBlocks() throws ExistingSubstitutionException {
//        GameRegistry.addSubstitutionAlias(
//                "minecraft:grass", GameRegistry.Type.BLOCK,
//                new BlockGrassCustom().setRegistryName("grass_custom").setUnlocalizedName("grass"));
//        GameRegistry.addSubstitutionAlias(
//                "minecraft:mycelium", GameRegistry.Type.BLOCK,
//                new BlockMyceliumCustom().setRegistryName("mycelium_custom").setUnlocalizedName("mycel"));
//    }

    /**
     * Register a block with its specified linked item. Block's registry name
     * prevail and must be set before call.
     *
     * @param block Block instance to register.
     * @param item  Item instance to register. Will have the same registered name
     *              as the block. If null, then no item will be linked to the
     *              block.
     */
    public static Block registerBlock(Block block, @Nullable Item item) {
        if (Objects.equals(block.getUnlocalizedName(), "tile.null")) {
            //betterwithmods:name => bwm:name
            block.setUnlocalizedName("bwm" + block.getRegistryName().toString().substring(BWMod.MODID.length()));
        }
        Block registeredBlock = block;
        BLOCKS.add(registeredBlock);
        if (item != null)
            BWMItems.registerItem(item.setRegistryName(block.getRegistryName()));
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

    @SideOnly(Side.CLIENT)
    public static void setInventoryModel(Block block) {
        BWMItems.setInventoryModel(Item.getItemFromBlock(block));
    }

    @SideOnly(Side.CLIENT)
    public static void registerFluidModels(Fluid fluid) {
        if (fluid == null) {
            return;
        }
        Block block = fluid.getBlock();
        if (block != null) {
            Item item = Item.getItemFromBlock(block);
            ClientProxy.FluidStateMapper mapper = new ClientProxy.FluidStateMapper(fluid);
            // item-model
            if (item != Items.AIR) {
                ModelLoader.registerItemVariants(item);
                ModelLoader.setCustomMeshDefinition(item, mapper);
            }
            // block-model
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }
    ///CLIENT END
}
