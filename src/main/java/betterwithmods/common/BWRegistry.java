package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.BehaviorDiodeDispense;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBWMPane;
import betterwithmods.common.blocks.BlockRope;
import betterwithmods.common.entity.*;
import betterwithmods.common.entity.item.EntityFallingBlockCustom;
import betterwithmods.common.entity.item.EntityItemBuoy;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.potion.BWPotion;
import betterwithmods.common.registry.ChoppingRecipe;
import betterwithmods.common.registry.HopperFilters;
import betterwithmods.common.registry.SawInteraction;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCLumber;
import betterwithmods.util.*;
import betterwithmods.util.item.ItemExt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
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
        registerEntities();
        registerOres();
        registerPotions();
        registerBlockDispenserBehavior();
        registerHopperFilters();
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new MechanicalCapability.CapabilityMechanicalPower(), MechanicalCapability.DefaultMechanicalPower.class);
    }
    public static void init() {
        GameRegistry.registerFuelHandler(new BWFuelHandler());
        registerHeatSources();
        registerNetherWhitelist();
        BWSounds.registerSounds();

        ItemExt.initBuoyancy();
        ItemExt.initDesserts();
        ItemExt.initWeights();
    }
    public static void postInit() {
        RecipeUtils.gatherCookableFood();
        registerWood();
        InvUtils.postInitOreDictGathering();
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

    public static void registerOres() {
        OreDictionary.registerOre("gearWood", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR));
        OreDictionary.registerOre("cropHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP));
        OreDictionary.registerOre("dyeBrown", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG));
        OreDictionary.registerOre("dung", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG));
        OreDictionary.registerOre("slimeball", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GLUE));
        OreDictionary.registerOre("ingotSoulforgedSteel", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL));
        OreDictionary.registerOre("dustNetherrack", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GROUND_NETHERRACK));
        OreDictionary.registerOre("dustHellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        OreDictionary.registerOre("dustSoul", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST));
        OreDictionary.registerOre("ingotHellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));
        OreDictionary.registerOre("dustCoal", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST));
        OreDictionary.registerOre("dustPotash", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        OreDictionary.registerOre("dustWood", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
        OreDictionary.registerOre("dustSulfur", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
        OreDictionary.registerOre("dustSaltpeter", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER));
        OreDictionary.registerOre("nuggetIron", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_IRON));
        OreDictionary.registerOre("nuggetSoulforgedSteel", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL));
        OreDictionary.registerOre("foodFlour", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.FLOUR));
        OreDictionary.registerOre("dustCharcoal", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        OreDictionary.registerOre("foodCocoapowder", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COCOA_POWDER));
        OreDictionary.registerOre("foodChocolatebar", new ItemStack(BWMItems.CHOCOLATE));

        OreDictionary.registerOre("blockSoulforgedSteel", new ItemStack(BWMBlocks.AESTHETIC, 1, 2));
        OreDictionary.registerOre("blockHellfire", new ItemStack(BWMBlocks.AESTHETIC, 1, 3));
        //Added bark subtype entries for Roots compatibility
        OreDictionary.registerOre("barkWood", new ItemStack(BWMItems.BARK, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("barkOak", new ItemStack(BWMItems.BARK, 1, 0));
        OreDictionary.registerOre("barkSpruce", new ItemStack(BWMItems.BARK, 1, 1));
        OreDictionary.registerOre("barkBirch", new ItemStack(BWMItems.BARK, 1, 2));
        OreDictionary.registerOre("barkJungle", new ItemStack(BWMItems.BARK, 1, 3));
        OreDictionary.registerOre("barkAcacia", new ItemStack(BWMItems.BARK, 1, 4));
        OreDictionary.registerOre("barkDarkOak", new ItemStack(BWMItems.BARK, 1, 5));
        OreDictionary.registerOre("craftingToolKnife", new ItemStack(BWMItems.KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("slabWood", new ItemStack(BWMBlocks.WOOD_SIDING, 1, OreDictionary.WILDCARD_VALUE));
        // TFC compatibility
        OreDictionary.registerOre("itemKnife", new ItemStack(BWMItems.KNIFE, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("fiberHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS));
        OreDictionary.registerOre("fabricHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH));

        OreDictionary.registerOre("ingotDiamond", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT));
        OreDictionary.registerOre("nuggetDiamond", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET));

        OreDictionary.registerOre("listAllmeat", Items.PORKCHOP);
        OreDictionary.registerOre("listAllmeat", Items.BEEF);
        OreDictionary.registerOre("listAllmeat", Items.CHICKEN);
        OreDictionary.registerOre("listAllmeat", Items.FISH);
        OreDictionary.registerOre("listAllmeat", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
        OreDictionary.registerOre("listAllmeat", Items.MUTTON);
        OreDictionary.registerOre("listAllmeat", Items.RABBIT);


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
            SawInteraction.INSTANCE.addRecipe(block, log.getMetadata(), plank, bark, sawdust);
            SawInteraction.INSTANCE.addRecipe(Blocks.PLANKS, type.getMetadata(),
                    new ItemStack(BWMBlocks.WOOD_SIDING, 2, type.getMetadata()));
            plank = new ItemStack(Blocks.PLANKS, hardcoreLumber ? 3 : 5, type.getMetadata());
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
                            if (planks != ItemStack.EMPTY) {
                                ItemStack[] output = new ItemStack[3];
                                output[0] = new ItemStack(planks.getItem(), hardcoreLumber ? 4 : 6, planks.getMetadata());
                                output[1] = new ItemStack(BWMItems.BARK, 1, 0);
                                output[2] = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2);
                                if (hardcoreLumber) {
                                    removeRecipe(output[0], new ItemStack(log.getItem(), 1, i));
                                    if (Loader.isModLoaded("thermalexpansion")) {
                                        registerTESawmill(output[0], new ItemStack(log.getItem(), 1, i));
                                    }
                                    GameRegistry.addRecipe(new ChoppingRecipe(new ItemStack(planks.getItem(), 2, planks.getMetadata()), output[1], output[2], new ItemStack(log.getItem(), 1, i)));
                                }
                                SawInteraction.INSTANCE.addRecipe(block, i, output);
                                SawInteraction.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                            }
                        }
                    } else {
                        ItemStack planks = getRecipeOutput(log);
                        if (planks != ItemStack.EMPTY) {
                            ItemStack[] output = new ItemStack[3];
                            output[0] = new ItemStack(planks.getItem(), hardcoreLumber ? 4 : 6, planks.getMetadata());
                            output[1] = new ItemStack(BWMItems.BARK, 1, 0);
                            output[2] = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST, 2);
                            if (hardcoreLumber) {
                                removeRecipe(output[0], log);
                                if (Loader.isModLoaded("thermalexpansion")) {
                                    registerTESawmill(output[0], log);
                                }
                                GameRegistry.addRecipe(new ChoppingRecipe(new ItemStack(planks.getItem(), 2, planks.getMetadata()), output[1], output[2], log));
                            }
                            SawInteraction.INSTANCE.addRecipe(block, log.getMetadata(), output);
                            SawInteraction.INSTANCE.addRecipe(planks, new ItemStack(BWMBlocks.WOOD_SIDING, 2, 0));
                        }
                    }
                }
            }
        }
        BWCrafting.addKilnWood();
    }

    private static void registerTESawmill(ItemStack output, ItemStack input) {
        if (output == null || input == null) return;
        NBTTagCompound toSend = new NBTTagCompound();
        toSend.setInteger("energy", 800);
        toSend.setTag("input", new NBTTagCompound());
        toSend.setTag("primaryOutput", new NBTTagCompound());
        toSend.setTag("secondaryOutput", new NBTTagCompound());
        input.writeToNBT(toSend.getCompoundTag("input"));
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
            }
            else if (recipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.getInput()[0] instanceof ItemStack) {
                        ItemStack stack = (ItemStack)shaped.getInput()[0];
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
                        if(output.isItemEqual(shaped.getRecipeOutput()))
                            toRemove.add(recipe);
                    }
                }
            }
            else if (recipe instanceof ShapedOreRecipe) {
                ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
                if (shaped.getRecipeSize() == 1) {
                    if (shaped.getInput()[0] instanceof ItemStack) {
                        ItemStack stack = (ItemStack)shaped.getInput()[0];
                        if (stack.isItemEqual(input)) {
                            if (output.isItemEqual(shaped.getRecipeOutput()))
                                toRemove.add(recipe);
                        }
                    }
                }
            }
            else if (recipe instanceof ShapelessRecipes) {
                ShapelessRecipes shapeless = (ShapelessRecipes) recipe;
                if (shapeless.recipeItems.size() == 1 && shapeless.recipeItems.get(0).isItemEqual(input)) {
                    if(output.isItemEqual(shapeless.getRecipeOutput()))
                        toRemove.add(recipe);
                }
            } else if (recipe instanceof ShapelessOreRecipe) {
                ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;
                if (shapeless.getRecipeSize() == 1) {
                    if (shapeless.getInput().get(0) instanceof ItemStack) {
                        if (((ItemStack) shapeless.getInput().get(0)).isItemEqual(input)) {
                            if(output.isItemEqual(shapeless.getRecipeOutput()))
                                toRemove.add(recipe);
                        }
                    }
                }
            }
        }
        for(IRecipe remove : toRemove) {
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
            return block instanceof BlockRope || block instanceof BlockBush || block instanceof BlockTorch || block instanceof BlockSand || block instanceof BlockGravel || InvUtils.isOre(stack, "treeSapling");
        }
        return true;
    }

    private static boolean isParticulate(ItemStack stack) {
        Item item = stack.getItem();
        return InvUtils.listContains(stack, OreDictionary.getOres("sand")) || item instanceof ItemSeeds || InvUtils.listContains(stack, OreDictionary.getOres("listAllseeds")) || item == Items.GUNPOWDER || item == Items.SUGAR || item == Items.BLAZE_POWDER || InvUtils.listContains(stack, OreDictionary.getOres("foodFlour")) || InvUtils.listContains(stack, InvUtils.dustNames)
                || item == BWMItems.DIRT_PILE || item == BWMItems.GRAVEL_PILE || item == BWMItems.SAND_PILE;
    }

    private static boolean isFlat(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        if (item == BWMItems.MATERIAL) {
            return meta == 1 || meta == 4 || (meta > 5 && meta < 10) || (meta > 31 && meta < 35);
        }
        return item == Item.getItemFromBlock(Blocks.WOOL) || item == Item.getItemFromBlock(Blocks.CARPET) || item == Items.LEATHER || item == Items.MAP || item == Items.FILLED_MAP || InvUtils.listContains(stack, OreDictionary.getOres("string")) || InvUtils.listContains(stack, OreDictionary.getOres("paper"));
    }

    private static boolean isNarrow(ItemStack stack) {
        Item item = stack.getItem();
        int meta = stack.getMetadata();
        return item == Item.getItemFromBlock(Blocks.RED_FLOWER) || item == Item.getItemFromBlock(Blocks.YELLOW_FLOWER) || item == Items.BONE || item == Items.ARROW || item == Items.SPECTRAL_ARROW || item == Items.TIPPED_ARROW || InvUtils.listContains(stack, OreDictionary.getOres("stickWood")) || InvUtils.listContains(stack, InvUtils.cropNames) || item == Items.REEDS || item == Items.BLAZE_ROD || (item == BWMItems.MATERIAL && (meta == 8 || meta == 9));
    }
}
