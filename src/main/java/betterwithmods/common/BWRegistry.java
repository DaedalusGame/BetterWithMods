package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.api.block.IDebarkable;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBWMPane;
import betterwithmods.common.blocks.BlockRope;
import betterwithmods.common.blocks.behaviors.BehaviorDiodeDispense;
import betterwithmods.common.blocks.behaviors.BehaviorSilkTouch;
import betterwithmods.common.entity.*;
import betterwithmods.common.entity.item.EntityFallingBlockCustom;
import betterwithmods.common.entity.item.EntityItemBuoy;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.potion.BWPotion;
import betterwithmods.common.registry.*;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.common.registry.steelanvil.SteelShapedOreRecipe;
import betterwithmods.common.registry.steelanvil.SteelShapedRecipe;
import betterwithmods.common.registry.steelanvil.SteelShapelessRecipe;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCLumber;
import betterwithmods.util.ColorUtils;
import betterwithmods.util.DispenserBehaviorDynamite;
import betterwithmods.util.InvUtils;
import betterwithmods.util.RecipeUtils;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class BWRegistry {
    public static final Potion POTION_TRUESIGHT = new BWPotion(false, 14270531, 4, 1).setRegistryName("true_sight");
    private static int availableEntityId = 0;

    public static void preInit() {
        BWMBlocks.registerBlocks();
        BWMItems.registerItems();
        BWMBlocks.registerTileEntities();
        BWOreDictionary.registerOres();
        registerEntities();
        registerPotions();
        registerBlockDispenserBehavior();
        registerHopperFilters();
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new MechanicalCapability.CapabilityMechanicalPower(), MechanicalCapability.DefaultMechanicalPower.class);

        KilnStructureManger.registerKilnBlock(Blocks.BRICK_BLOCK.getDefaultState());
        KilnStructureManger.registerKilnBlock(Blocks.NETHER_BRICK.getDefaultState());
    }

    public static void init() {
        GameRegistry.registerFuelHandler(new BWFuelHandler());
        registerHeatSources();
        BWSounds.registerSounds();

        RecipeSorter.register("bwm:chopping", ChoppingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:cutting", CuttingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:dyetag", DyeWithTagRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:steel_shapeless", SteelShapelessRecipe.class, RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register("bwm:steel_shaped", SteelShapedRecipe.class, RecipeSorter.Category.SHAPED, "after:forge:shapedore");
        RecipeSorter.register("bwm:steel_shaped_ore", SteelShapedOreRecipe.class, RecipeSorter.Category.SHAPED, "after:forge:shapedore");

    }

    public static void postInit() {
        RecipeUtils.gatherCookableFood();
        registerWood();
        BWOreDictionary.postInitOreDictGathering();
        ColorUtils.initColors();
    }

    /**
     * All names should be snake_case by convention (enforced in 1.11).
     */
    private static void registerEntities() {
        BWRegistry.registerEntity(EntityExtendingRope.class, "extending_rope", 64, 20, true);
        BWRegistry.registerEntity(EntityDynamite.class, "bwm_dynamite", 10, 50, true);
        BWRegistry.registerEntity(EntityMiningCharge.class, "bwm_mining_charge", 10, 50, true);
        BWRegistry.registerEntity(EntityItemBuoy.class, "entity_item_buoy", 64, 20, true);
        BWRegistry.registerEntity(EntityShearedCreeper.class, "entity_sheared_creeper", 64, 1, true);
        BWRegistry.registerEntity(EntityBroadheadArrow.class, "entity_broadhead_arrow", 64, 1, true);
        BWRegistry.registerEntity(EntityFallingGourd.class, "entity_falling_gourd", 64, 1, true);
        BWRegistry.registerEntity(EntityFallingBlockCustom.class, "falling_block_custom", 64, 20, true);
        BWRegistry.registerEntity(EntitySpiderWeb.class, "bwm_spider_web", 64, 20, true);
    }


    public static void registerHopperFilters() {
        HopperFilters.addFilter(1, Blocks.LADDER, 0, BWRegistry::isNotBlock);
        HopperFilters.addFilter(2, Blocks.TRAPDOOR, 0, stack -> isNarrow(stack) || isParticulate(stack));
        HopperFilters.addFilter(3, BWMBlocks.GRATE, OreDictionary.WILDCARD_VALUE, stack -> isNarrow(stack) || isFlat(stack) || isParticulate(stack));
        HopperFilters.addFilter(4, BWMBlocks.SLATS, OreDictionary.WILDCARD_VALUE, stack -> isParticulate(stack) || isFlat(stack));
        HopperFilters.addFilter(5, BWMBlocks.PANE, BlockBWMPane.EnumPaneType.WICKER.getMeta(), BWRegistry::isParticulate);
        HopperFilters.addFilter(6, Blocks.SOUL_SAND, 0, stack -> false);
        HopperFilters.addFilter(7, Blocks.IRON_BARS, 0, stack -> isNotBlock(stack) && stack.getMaxStackSize() > 1);
    }

    public static void registerBlockDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(BWMItems.DYNAMITE, new DispenserBehaviorDynamite());
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
                    worldIn.spawnEntity(miningCharge);
                    worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ,
                            SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return stack;
                });
        BlockBDispenser.BLOCK_COLLECT_REGISTRY.putObject(Blocks.STONE, new BehaviorSilkTouch());
        BlockBDispenser.BLOCK_COLLECT_REGISTRY.putObject(BWMBlocks.STUMP, new BehaviorSilkTouch());
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntityWolf.class, (world, pos, entity, stack) -> {
            InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.STRING, 1 + world.rand.nextInt(3)));
            world.playSound(null, pos, SoundEvents.ENTITY_WOLF_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
            entity.setDead();
            return InvUtils.asList(new ItemStack(BWMBlocks.WOLF));
        });

        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntitySheep.class, (world, pos, entity, stack) -> {
            EntitySheep sheep = (EntitySheep) entity;
            if(sheep.isShearable(new ItemStack(Items.SHEARS),world,pos)) {
                return InvUtils.asList(sheep.onSheared(new ItemStack(Items.SHEARS),world,pos,0));
            }
            return NonNullList.create();
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntityChicken.class, (world, pos, entity, stack) -> {
            InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.FEATHER, 1 + world.rand.nextInt(2)));
            world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
            entity.setDead();
            return InvUtils.asList(new ItemStack(Items.EGG));
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntityCow.class, (world, pos, entity, stack) -> {
            if(stack.isItemEqual(new ItemStack(Items.BUCKET))) {
                stack.shrink(1);
                world.playSound(null, pos, SoundEvents.ENTITY_COW_MILK, SoundCategory.BLOCKS, 1.0F, 1.0F);

                InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.MILK_BUCKET));
            }
            return NonNullList.create();
        });
    }

    /**
     * Registers an entity for this mod. Handles automatic available ID
     * assignment.
     */
    public static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange,
                                      int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithmods:" + entityName), entityClass, entityName, availableEntityId, BWMod.instance, trackingRange,
                updateFrequency, sendsVelocityUpdates);
        availableEntityId++;
    }


    public static void registerHeatSources() {
        BWMHeatRegistry.setBlockHeatRegistry(Blocks.FIRE, 3);
        BWMHeatRegistry.setBlockHeatRegistry(BWMBlocks.STOKED_FLAME, 8);
    }


    public static void registerWood() {
        boolean hardcoreLumber = ModuleLoader.isFeatureEnabled(HCLumber.class);
        for (BlockPlanks.EnumType type : BlockPlanks.EnumType.values()) {
            ItemStack log;
            ItemStack plank = new ItemStack(Blocks.PLANKS, hardcoreLumber ? 4 : 6, type.getMetadata());
            if (type.getMetadata() < 4) {
                log = new ItemStack(Blocks.LOG, 1, type.getMetadata());

            } else {
                log = new ItemStack(Blocks.LOG2, 1, type.getMetadata() - 4);
            }
            Block block = ((ItemBlock) log.getItem()).getBlock();
            ItemStack bark = new ItemStack(BWMItems.BARK, 1, type.getMetadata());
            ItemStack sawdust = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2);
            if (hardcoreLumber) {
                removeRecipe(plank, log);
                if (Loader.isModLoaded("thermalexpansion")) {
                    registerTESawmill(plank, log);
                }
                GameRegistry.addRecipe(new ChoppingRecipe(new ItemStack(Blocks.PLANKS, 2, type.getMetadata()), bark, sawdust, log));
            }
            SawManager.INSTANCE.addRecipe(block, log.getMetadata(), plank, bark, sawdust);
            SawManager.INSTANCE.addRecipe(Blocks.PLANKS, type.getMetadata(),
                    new ItemStack(BWMBlocks.WOOD_SIDING, 2, type.getMetadata()));
            plank = new ItemStack(Blocks.PLANKS, hardcoreLumber ? 3 : 5, type.getMetadata());
            if (type.getMetadata() < 4) {
                log = new ItemStack(BWMBlocks.DEBARKED_OLD, 1, type.getMetadata());
            } else {
                log = new ItemStack(BWMBlocks.DEBARKED_NEW, 1, type.getMetadata() - 4);
            }
            block = ((ItemBlock) log.getItem()).getBlock();
            SawManager.INSTANCE.addRecipe(block, log.getMetadata(), plank, sawdust);
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
                            if (!planks.isEmpty()) {

                                ItemStack[] output = new ItemStack[3];
                                output[0] = new ItemStack(planks.getItem(), hardcoreLumber ? 4 : 6, planks.getMetadata());
                                output[1] = new ItemStack(BWMItems.BARK, 1, 0);
                                output[2] = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2);

                                if (block instanceof IDebarkable)
                                    output[1] = ((IDebarkable) block).getBark(block.getStateFromMeta(log.getMetadata()));

                                if (hardcoreLumber) {
                                    removeRecipe(output[0], new ItemStack(log.getItem(), 1, i));
                                    if (Loader.isModLoaded("thermalexpansion")) {
                                        registerTESawmill(output[0], new ItemStack(log.getItem(), 1, i));
                                    }
                                    GameRegistry.addRecipe(new ChoppingRecipe(new ItemStack(planks.getItem(), 2, planks.getMetadata()), output[1], output[2], new ItemStack(log.getItem(), 1, i)));
                                }
                                SawManager.INSTANCE.addRecipe(block, i, output);
                                SawManager.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                            }
                        }
                    } else {
                        ItemStack planks = getRecipeOutput(log);
                        if (planks.isEmpty()) {
                            ItemStack[] output = new ItemStack[3];
                            output[0] = new ItemStack(planks.getItem(), hardcoreLumber ? 4 : 6, planks.getMetadata());
                            output[1] = new ItemStack(BWMItems.BARK, 1, 0);
                            output[2] = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2);

                            if (block instanceof IDebarkable)
                                output[1] = ((IDebarkable) block).getBark(block.getStateFromMeta(log.getMetadata()));

                            if (hardcoreLumber) {
                                removeRecipe(output[0], log);
                                if (Loader.isModLoaded("thermalexpansion")) {
                                    registerTESawmill(output[0], log);
                                }
                                GameRegistry.addRecipe(new ChoppingRecipe(new ItemStack(planks.getItem(), 2, planks.getMetadata()), output[1], output[2], log));
                            }
                            SawManager.INSTANCE.addRecipe(block, log.getMetadata(), output);
                            SawManager.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                        }
                    }
                }
            }
        }
    }

    private static void registerTESawmill(ItemStack output, ItemStack input) {
        if (output == null || input == null) return;
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("energy", 800);
        toSend.setTag("inputs", new NBTTagCompound());
        toSend.setTag("primaryOutput", new NBTTagCompound());
        toSend.setTag("secondaryOutput", new NBTTagCompound());
        input.writeToNBT(toSend.getCompoundTag("inputs"));
        output.writeToNBT(toSend.getCompoundTag("primaryOutput"));
        ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST).writeToNBT(toSend.getCompoundTag("secondaryOutput"));
        toSend.setInteger("secondaryChance", 100);
        FMLInterModComms.sendMessage("thermalexpansion", "addsawmillrecipe", toSend);
    }

    private static ItemStack getRecipeOutput(ItemStack input) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe recipe : recipes) {
            if (recipe instanceof ShapedRecipes) {
                ShapedRecipes shaped = (ShapedRecipes) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.recipeItems[0].isItemEqual(input)) {
                        return shaped.getRecipeOutput();
                    }
                }
            } else if (recipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.getInput()[0] instanceof ItemStack) {
                        ItemStack stack = (ItemStack) shaped.getInput()[0];
                        if (stack.isItemEqual(input)) {
                            return shaped.getRecipeOutput();
                        }
                    }
                }
            }
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
        return ItemStack.EMPTY;
    }

    private static void removeRecipe(ItemStack output, ItemStack input) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> toRemove = new ArrayList<>();
        for (IRecipe recipe : recipes) {
            if (recipe instanceof ShapedRecipes) {
                ShapedRecipes shaped = (ShapedRecipes) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.recipeItems[0].isItemEqual(input)) {
                        if (output.isItemEqual(shaped.getRecipeOutput()))
                            toRemove.add(recipe);
                    }
                }
            } else if (recipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.getInput()[0] instanceof ItemStack) {
                        ItemStack stack = (ItemStack) shaped.getInput()[0];
                        if (stack.isItemEqual(input)) {
                            if (output.isItemEqual(shaped.getRecipeOutput()))
                                toRemove.add(recipe);
                        }
                    }
                }
            } else if (recipe instanceof ShapelessRecipes) {
                ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
                if (shapeless.recipeItems.size() == 1 && shapeless.recipeItems.get(0).isItemEqual(input)) {
                    if (output.isItemEqual(shapeless.getRecipeOutput()))
                        toRemove.add(recipe);
                }
            } else if (recipe instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;
                if (shapeless.getRecipeSize() == 1) {
                    if (shapeless.getInput().get(0) instanceof ItemStack) {
                        if (((ItemStack) shapeless.getInput().get(0)).isItemEqual(input)) {
                            if (output.isItemEqual(shapeless.getRecipeOutput()))
                                toRemove.add(recipe);
                        }
                    }
                }
            }
        }
        for (IRecipe remove : toRemove) {
            CraftingManager.getInstance().getRecipeList().remove(remove);
        }
    }

    private static void registerPotions() {
        registerPotion(POTION_TRUESIGHT);
    }

    private static void registerPotion(Potion potion) {
        String potionName = potion.getRegistryName().toString().substring(BWMod.MODID.length() + ":".length());
        potion.setPotionName("bwm.effect." + potionName);
        GameRegistry.register(potion);
    }

    private static boolean isNotBlock(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            return block instanceof BlockRope || block instanceof BlockBush || block instanceof BlockTorch || block instanceof BlockSand || block instanceof BlockGravel || BWOreDictionary.isOre(stack, "treeSapling");
        }
        return true;
    }

    private static boolean isParticulate(ItemStack stack) {
        Item item = stack.getItem();
        return BWOreDictionary.listContains(stack, OreDictionary.getOres("sand")) || item instanceof ItemSeeds || BWOreDictionary.listContains(stack, OreDictionary.getOres("listAllseeds")) || item == Items.GUNPOWDER || item == Items.SUGAR || item == Items.BLAZE_POWDER || BWOreDictionary.listContains(stack, OreDictionary.getOres("foodFlour")) || BWOreDictionary.listContains(stack, BWOreDictionary.dustNames)
                || item == BWMItems.DIRT_PILE || item == BWMItems.GRAVEL_PILE || item == BWMItems.SAND_PILE;
    }

    private static boolean isFlat(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        if (item == BWMItems.MATERIAL) {
            return meta == 1 || meta == 4 || (meta > 5 && meta < 10) || (meta > 31 && meta < 35);
        }
        return item == Item.getItemFromBlock(Blocks.WOOL) || item == Item.getItemFromBlock(Blocks.CARPET) || item == Items.LEATHER || item == Items.MAP || item == Items.FILLED_MAP || BWOreDictionary.listContains(stack, OreDictionary.getOres("string")) || BWOreDictionary.listContains(stack, OreDictionary.getOres("paper"));
    }

    private static boolean isNarrow(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        return item == Item.getItemFromBlock(Blocks.RED_FLOWER) || item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER) || item == Items.BONE || item == Items.ARROW || item == Items.SPECTRAL_ARROW || item == Items.TIPPED_ARROW || BWOreDictionary.listContains(stack, OreDictionary.getOres("stickWood")) || BWOreDictionary.listContains(stack, BWOreDictionary.cropNames) || item == Items.REEDS || item == Items.BLAZE_ROD || (item == BWMItems.MATERIAL && (meta == 8 || meta == 9));
    }
}
