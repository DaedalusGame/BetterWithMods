package betterwithmods;

import betterwithmods.blocks.BehaviorDiodeDispense;
import betterwithmods.blocks.BlockBDispenser;
import betterwithmods.blocks.BlockBWMPane;
import betterwithmods.config.BWConfig;
import betterwithmods.craft.HopperFilters;
import betterwithmods.craft.SawInteraction;
import betterwithmods.craft.heat.BWMHeatRegistry;
import betterwithmods.entity.EntityMiningCharge;
import betterwithmods.items.ItemMaterial;
import betterwithmods.potion.BWPotion;
import betterwithmods.util.DispenserBehaviorDynamite;
import betterwithmods.util.InvUtils;
import betterwithmods.util.NetherSpawnWhitelist;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.*;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.function.Predicate;

public class BWRegistry {
    public static final Potion POTION_TRUESIGHT = new BWPotion(false, 14270531, 4, 1).setRegistryName("true_sight");
    private static int availableEntityId = 0;

    public static void init() {
        registerOres();
        registerPotions();
        registerBlockDispenserBehavior();
        registerHopperFilters();
        MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1, 0), 5);
    }

    public static void registerHopperFilters() {

        HopperFilters.filters = HashBiMap.create();
        Predicate<ItemStack> isNotBlock = stack -> {
            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                return block instanceof BlockBush || block instanceof BlockTorch || block instanceof BlockSand || block instanceof BlockGravel || InvUtils.isOre(stack, "treeSapling");
            }
            return item == Items.SKULL || item == Items.FLOWER_POT || item == Items.ITEM_FRAME;
        };
        HopperFilters.addFilter(1, Blocks.LADDER, 0, isNotBlock);
        HopperFilters.addFilter(2, Blocks.TRAPDOOR, 0, stack -> false);
        HopperFilters.addFilter(3, BWMBlocks.GRATE, OreDictionary.WILDCARD_VALUE, stack -> false);
        HopperFilters.addFilter(4, BWMBlocks.SLATS, OreDictionary.WILDCARD_VALUE, stack -> false);
        HopperFilters.addFilter(5, BWMBlocks.PANE, BlockBWMPane.EnumPaneType.WICKER.getMeta(), stack -> InvUtils.listContains(stack, InvUtils.dustNames));
        HopperFilters.addFilter(6, Blocks.SOUL_SAND, 0, stack -> stack.equals(ItemMaterial.getMaterial("ground_netherrack")) || stack.equals(ItemMaterial.getMaterial("soul_dust")));
        HopperFilters.addFilter(7, Blocks.IRON_BARS, 0, stack -> stack.getMaxStackSize() > 1);
    }

    public static void registerBlockDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
        if (BWConfig.hardcoreBuckets) {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, new BehaviorDefaultDispenseItem() {
                @Override
                public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                    ItemStack outputStack = stack;
                    BlockPos pos = source.getBlockPos().offset(source.getBlockState().getValue(BlockDispenser.FACING));
                    if (source.getWorld().isAirBlock(pos)
                            || source.getWorld().getBlockState(pos).getBlock().isReplaceable(source.getWorld(), pos)) {
                        source.getWorld().setBlockState(pos, Blocks.FLOWING_WATER.getStateFromMeta(2));
                        for (EnumFacing face : EnumFacing.HORIZONTALS) {
                            BlockPos off = pos.offset(face);
                            if (source.getWorld().isAirBlock(off) || source.getWorld().getBlockState(off).getBlock()
                                    .isReplaceable(source.getWorld(), off))
                                source.getWorld().setBlockState(off, Blocks.FLOWING_WATER.getStateFromMeta(5));
                        }
                        outputStack = new ItemStack(Items.BUCKET, 1);
                    }
                    return outputStack;
                }
            });
        }
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.REPEATER, new BehaviorDiodeDispense());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Items.COMPARATOR, new BehaviorDiodeDispense());
        BlockBDispenser.BLOCK_DISPENSER_REGISTRY.putObject(Item.getItemFromBlock(BWMBlocks.MINING_CHARGE),
                (source, stack) -> {
                    World worldIn = source.getWorld();
                    EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
                    BlockPos pos = source.getBlockPos().offset(facing);
                    EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, pos.getX() + 0.5F, pos.getY(),
                            pos.getZ() + 0.5F, null, facing);
                    miningCharge.setNoGravity(false);
                    worldIn.spawnEntityInWorld(miningCharge);
                    worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ,
                            SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return stack;
                });
    }

    /**
     * Registers an entity for this mod. Handles automatic available ID
     * assignment.
     */
    public static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange,
                                      int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(entityClass, entityName, availableEntityId, BWMod.instance, trackingRange,
                updateFrequency, sendsVelocityUpdates);
        availableEntityId++;
    }

    public static void registerOres() {
        OreDictionary.registerOre("gearWood", new ItemStack(BWMItems.MATERIAL, 1, 0));
        OreDictionary.registerOre("cropHemp", new ItemStack(BWMItems.MATERIAL, 1, 2));
        OreDictionary.registerOre("dyeBrown", new ItemStack(BWMItems.MATERIAL, 1, 5));
        OreDictionary.registerOre("slimeball", new ItemStack(BWMItems.MATERIAL, 1, 12));
        OreDictionary.registerOre("ingotSoulforgedSteel", new ItemStack(BWMItems.MATERIAL, 1, 14));
        OreDictionary.registerOre("dustNetherrack", new ItemStack(BWMItems.MATERIAL, 1, 15));
        OreDictionary.registerOre("dustHellfire", new ItemStack(BWMItems.MATERIAL, 1, 16));
        OreDictionary.registerOre("dustSoul", new ItemStack(BWMItems.MATERIAL, 1, 23));
        OreDictionary.registerOre("ingotHellfire", new ItemStack(BWMItems.MATERIAL, 1, 17));
        OreDictionary.registerOre("dustCoal", new ItemStack(BWMItems.MATERIAL, 1, 18));
        OreDictionary.registerOre("dustPotash", new ItemStack(BWMItems.MATERIAL, 1, 21));
        OreDictionary.registerOre("dustWood", new ItemStack(BWMItems.MATERIAL, 1, 22));
        OreDictionary.registerOre("dustSulfur", new ItemStack(BWMItems.MATERIAL, 1, 25));
        OreDictionary.registerOre("dustSaltpeter", new ItemStack(BWMItems.MATERIAL, 1, 26));
        OreDictionary.registerOre("nuggetIron", new ItemStack(BWMItems.MATERIAL, 1, 30));
        OreDictionary.registerOre("nuggetSoulforgedSteel", new ItemStack(BWMItems.MATERIAL, 1, 31));
        OreDictionary.registerOre("foodFlour", new ItemStack(BWMItems.MATERIAL, 1, 37));
        OreDictionary.registerOre("dustCharcoal", new ItemStack(BWMItems.MATERIAL, 1, 39));

        OreDictionary.registerOre("blockSoulforgedSteel", new ItemStack(BWMBlocks.AESTHETIC, 1, 2));
        OreDictionary.registerOre("blockHellfire", new ItemStack(BWMBlocks.AESTHETIC, 1, 3));

        OreDictionary.registerOre("barkWood", new ItemStack(BWMItems.BARK, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("craftingToolKnife", new ItemStack(BWMItems.KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("slabWood", new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        // TFC compatibility
        OreDictionary.registerOre("itemKnife", new ItemStack(BWMItems.KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("string", new ItemStack(BWMItems.MATERIAL, 1, 3));
        OreDictionary.registerOre("fiberHemp", new ItemStack(BWMItems.MATERIAL, 1, 3));
        OreDictionary.registerOre("fabricHemp", new ItemStack(BWMItems.MATERIAL, 1, 4));

        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_PORKCHOP);
        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_BEEF);
        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_CHICKEN);
        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_FISH);
        OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_MUTTON);
        OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_RABBIT);
    }

    public static void registerHeatSources() {
        BWMHeatRegistry.setBlockHeatRegistry(Blocks.FIRE, 3);
        BWMHeatRegistry.setBlockHeatRegistry(BWMBlocks.STOKED_FLAME, 8);
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
            ItemStack log;
            ItemStack plank = new ItemStack(Blocks.PLANKS, 6, type.getMetadata());
            if (type.getMetadata() < 4) {
                log = new ItemStack(Blocks.LOG, 1, type.getMetadata());

            } else {
                log = new ItemStack(Blocks.LOG2, 1, type.getMetadata() - 4);
            }
            Block block = ((ItemBlock) log.getItem()).getBlock();
            ItemStack bark = new ItemStack(BWMItems.BARK, 2, type.getMetadata());
            ItemStack sawdust = ItemMaterial.getMaterial("sawdust", 2);
            SawInteraction.INSTANCE.addRecipe(block, log.getMetadata(), plank, bark, sawdust);
            SawInteraction.INSTANCE.addRecipe(Blocks.PLANKS, type.getMetadata(),
                    new ItemStack(BWMBlocks.WOOD_SIDING, 2, type.getMetadata()));
            plank = new ItemStack(Blocks.PLANKS, 5, type.getMetadata());
            if (type.getMetadata() < 4) {
                log = new ItemStack(BWMBlocks.DEBARKED_OLD, 1, type.getMetadata());
            } else {
                log = new ItemStack(BWMBlocks.DEBARKED_NEW, 1, type.getMetadata() - 4);
            }
            block = ((ItemBlock) log.getItem()).getBlock();
            SawInteraction.INSTANCE.addRecipe(block, log.getMetadata(), plank, sawdust);
        }
        List<ItemStack> logs = OreDictionary.getOres("logWood");
        for (ItemStack log : logs) {
            if (log.getItem() != null && log.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) log.getItem()).getBlock();
                // only if not vanilla
                if (!block.getRegistryName().getResourceDomain().equals("minecraft")) {
                    if (log.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        for (int i = 0; i < 4; i++) {
                            ItemStack planks = getRecipeOutput(new ItemStack(log.getItem(), 1, i));
                            if (planks != null) {
                                ItemStack[] output = new ItemStack[3];
                                output[0] = new ItemStack(planks.getItem(), 6, planks.getMetadata());
                                output[1] = new ItemStack(BWMItems.BARK, 2, 0);
                                output[2] = ItemMaterial.getMaterial("sawdust");
                                SawInteraction.INSTANCE.addRecipe(block, i, output);
                                SawInteraction.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                            }
                        }
                    } else {
                        ItemStack planks = getRecipeOutput(log);
                        if (planks != null) {
                            ItemStack[] output = new ItemStack[3];
                            output[0] = new ItemStack(planks.getItem(), 6, planks.getMetadata());
                            output[1] = new ItemStack(BWMItems.BARK, 2, 0);
                            output[2] = ItemMaterial.getMaterial("sawdust");
                            SawInteraction.INSTANCE.addRecipe(block, log.getMetadata(), output);
                            SawInteraction.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                        }
                    }
                }
            }
        }
        BWCrafting.addKilnWood();
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
                if (shapeless.getRecipeSize() == 1) {
                    if (shapeless.getInput().get(0) instanceof ItemStack) {
                        if (((ItemStack) shapeless.getInput().get(0)).isItemEqual(input)) {
                            return shapeless.getRecipeOutput();
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void registerPotions() {
        registerPotion(POTION_TRUESIGHT);
    }

    private static void registerPotion(Potion potion) {
        String potionName = potion.getRegistryName().toString().substring(BWMod.MODID.length() + ":".length());
        potion.setPotionName("bwm.effect." + potionName);
        GameRegistry.register(potion);
    }
}
