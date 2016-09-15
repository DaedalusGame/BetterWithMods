package betterwithmods;

import betterwithmods.blocks.*;
import betterwithmods.blocks.mini.BlockCorner;
import betterwithmods.blocks.mini.BlockMoulding;
import betterwithmods.blocks.mini.BlockSiding;
import betterwithmods.blocks.mini.ItemBlockMini;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.heat.BWMHeatRegistry;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.items.*;
import betterwithmods.items.tools.*;
import betterwithmods.util.DispenserBehaviorDynamite;
import betterwithmods.util.NetherSpawnWhitelist;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

public class BWRegistry {
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
    public static Block light;
    public static Block miningCharge;
    public static Block fertileFarmland;
    public static Block pump;
    public static BlockLiquid tempLiquidSource;

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

    public static void init() {
        anchor = new BlockAnchor();
        rope = new BlockRope();
        singleMachines = new BlockMechMachines();
        axle = new BlockAxle();
        gearbox = new BlockGearbox();
        handCrank = new BlockCrank();
        pane = new BlockBTWPane();
        grate = new BlockNewBTWPane(0);
        slats = new BlockNewBTWPane(1);
        planter = new BlockPlanter();
        urn = new BlockUrn();
        unfiredPottery = new BlockUnfiredPottery();
        stokedFlame = new BlockFireStoked();
        hibachi = new BlockHibachi();
        bellows = new BlockBellows();
        kiln = new BlockKiln();
        hemp = new BlockHemp();
        detector = new BlockDetector();
        lens = new BlockLens();
        lightSource = new BlockInvisibleLight();
        saw = new BlockSaw();
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
        bambooChime = new BlockChime(Material.WOOD,"bamboo_chime");
        metalChime = new BlockChime(Material.IRON,"metal_chime");
        buddyBlock = new BlockBUD();
        creativeGenerator = new BlockCreativeGenerator();
        light = new BlockLight();
        platform = new BlockPlatform();
        miningCharge = new BlockMiningCharge();
        fertileFarmland = new BlockFertileFarmland();
        pump = new BlockPump();

        material = new ItemMaterial();
        windmill = new ItemMechanical();
        bark = new ItemBark();
        donut = new BWMFood("donut",2, 0.5F, false);
        knife = new ItemKnife(ToolMaterial.IRON);
        dynamite = new ItemDynamite();
        fertilizer = new ItemFertilizer();
        steelAxe = new ItemSoulforgedAxe();
        steelHoe = new ItemSoulforgedHoe();
        steelPickaxe = new ItemSoulforgedPickaxe();
        steelShovel = new ItemSoulforgedShovel();
        steelSword = new ItemSoulforgedSword();
        
        tempLiquidSource = new BlockTemporaryWater();

        registerOres();
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(dynamite, new DispenserBehaviorDynamite());

        if (BWConfig.hardcoreBuckets) {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, new BehaviorDefaultDispenseItem() {
                @Override
                public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                    BlockPos pos = source.getBlockPos().offset(source.func_189992_e().getValue(BlockDispenser.FACING));
                    if (source.getWorld().isAirBlock(pos) || source.getWorld().getBlockState(pos).getBlock().isReplaceable(source.getWorld(), pos)) {
                        source.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
                        for (EnumFacing face : EnumFacing.HORIZONTALS) {
                            BlockPos off = pos.offset(face);
                            if (source.getWorld().isAirBlock(off) || source.getWorld().getBlockState(off).getBlock().isReplaceable(source.getWorld(), off))
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
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Item.getItemFromBlock(miningCharge), (source, stack) -> {
            World worldIn = source.getWorld();
            EnumFacing facing = source.func_189992_e().getValue(BlockBDispenser.FACING);
            BlockPos pos = source.getBlockPos().offset(facing);
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), null, facing);
            miningCharge.func_189654_d(false);
            worldIn.spawnEntityInWorld(miningCharge);
            worldIn.playSound((EntityPlayer) null, miningCharge.posX, miningCharge.posY, miningCharge.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return stack;
        });
        MinecraftForge.addGrassSeed(new ItemStack(hemp, 1, 0), 5);

    }
    
	private static int availableEntityId = 0;
	
	/**
	 * Registers an entity for this mod. Handles automatic available ID assignment.
	 * 
	 * @author Koward
	 * 
	 * @param entityClass
	 * @param entityName
	 * @param trackingRange
	 * @param updateFrequency
	 * @param sendsVelocityUpdates
	 */
    public static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates){
    	EntityRegistry.registerModEntity(entityClass, entityName, availableEntityId, BWMod.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    	availableEntityId++;
    }

    public static void registerMiniBlock(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
        GameRegistry.register(new ItemBlockMini(block).setRegistryName(name));
    }

    public static void registerMetaBlock(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
        GameRegistry.register(new ItemBlockMeta(block).setRegistryName(name));
    }

    public static void registerPlanter(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
        GameRegistry.register(new ItemBlockPlanter(block).setRegistryName(name));
    }

    public static void registerPaneBlock(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
        GameRegistry.register(new ItemBlockPane(block).setRegistryName(name));
    }

    public static void registerBlock(Block block, String name) {
        GameRegistry.register(block.setRegistryName(name));
        GameRegistry.register(new ItemBlock(block).setRegistryName(name));
    }

    public static void registerItem(Item item, String name) {
        GameRegistry.register(item.setRegistryName(name));
    }

    public static void registerOres() {
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

        OreDictionary.registerOre("barkWood", new ItemStack(bark, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolKnife", new ItemStack(knife, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("slabWood", new ItemStack(woodSiding, 1, OreDictionary.WILDCARD_VALUE));
        //TFC compatibility
        OreDictionary.registerOre("itemKnife", new ItemStack(knife, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("string", new ItemStack(material, 1, 3));
        OreDictionary.registerOre("fiberHemp", new ItemStack(material, 1, 3));
        OreDictionary.registerOre("fabricHemp", new ItemStack(material, 1, 4));
    }

    public static void registerHeatSources() {
        BWMHeatRegistry.setBlockHeatRegistry(Blocks.FIRE, 3);
        BWMHeatRegistry.setBlockHeatRegistry(stokedFlame, 8);
    }

    public static void registerNetherWhitelist() {
        NetherSpawnWhitelist.addBlock(Blocks.NETHERRACK);
        NetherSpawnWhitelist.addBlock(Blocks.NETHER_BRICK);
        NetherSpawnWhitelist.addBlock(Blocks.SOUL_SAND);
        NetherSpawnWhitelist.addBlock(Blocks.GRAVEL);
        NetherSpawnWhitelist.addBlock(Blocks.QUARTZ_BLOCK);
    }

    public static void registerWood() {
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            ItemStack log = null;
            ItemStack plank = new ItemStack(Blocks.PLANKS, 6, type.getMetadata());
            if (type.getMetadata() < 4) {
                log = new ItemStack(Blocks.LOG, 1, type.getMetadata());

            } else {
                log = new ItemStack(Blocks.LOG2, 1, type.getMetadata() - 4);
            }
            Block block = ((ItemBlock) log.getItem()).getBlock();
            ItemStack bark = new ItemStack(BWRegistry.bark,2,type.getMetadata());
            ItemStack sawdust = ItemMaterial.getMaterial("sawdust",2);
            SawInteraction.addBlock(block,log.getMetadata(),plank,bark,sawdust);
            SawInteraction.addBlock(Blocks.PLANKS,type.getMetadata(),new ItemStack(BWRegistry.woodSiding,2,type.getMetadata()));
        }
        List<ItemStack> logs = OreDictionary.getOres("logWood");
        for (ItemStack log : logs) {
            if (log.getItem() != null && log.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) log.getItem()).getBlock();
                //only if not vanilla
                if (!block.getRegistryName().getResourceDomain().equals("minecraft")) {
                    if(log.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        for(int i = 0; i < 4; i++) {
                            ItemStack planks = getRecipeOutput(new ItemStack(log.getItem(), 1, i));
                            if(planks != null) {
                                ItemStack[] output = new ItemStack[3];
                                output[0] = new ItemStack(planks.getItem(), 6, planks.getMetadata());
                                output[1] = new ItemStack(BWRegistry.bark, 2, 0);
                                output[2] = ItemMaterial.getMaterial("sawdust");
                                SawInteraction.addBlock(block, i, output);
                                SawInteraction.addBlock(planks, new ItemStack(BWRegistry.woodSiding, 2, 0));
                            }
                        }
                    }
                    else {
                        ItemStack planks = getRecipeOutput(log);
                        if (planks != null) {
                            ItemStack[] output = new ItemStack[3];
                            output[0] = new ItemStack(planks.getItem(), 6, planks.getMetadata());
                            output[1] = new ItemStack(BWRegistry.bark, 2, 0);
                            output[2] = ItemMaterial.getMaterial("sawdust");
                            SawInteraction.addBlock(block, log.getMetadata(), output);
                            SawInteraction.addBlock(planks, new ItemStack(BWRegistry.woodSiding, 2, 0));
                        }
                    }
                }
            }
        }
    }


    private static ItemStack getRecipeOutput(ItemStack input) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            if (recipe instanceof ShapelessRecipes) {
                ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
                if (shapeless.recipeItems.size() == 1 && shapeless.recipeItems.get(0).isItemEqual(input)) {
                    return shapeless.getRecipeOutput();
                }
            } else if (recipe instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;
                if(shapeless.getRecipeSize() == 1) {
                    if(shapeless.getInput().get(0) instanceof ItemStack) {
                        if(((ItemStack)shapeless.getInput().get(0)).isItemEqual(input)) {
                            return shapeless.getRecipeOutput();
                        }
                    }
                }
            }
        }
        return null;
    }
}
