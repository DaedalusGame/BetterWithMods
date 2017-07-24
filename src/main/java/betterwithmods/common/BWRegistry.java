package betterwithmods.common;

import betterwithmods.BWMod;
import betterwithmods.api.capabilities.MechanicalCapability;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.behaviors.BehaviorDiodeDispense;
import betterwithmods.common.blocks.behaviors.BehaviorSilkTouch;
import betterwithmods.common.entity.*;
import betterwithmods.common.entity.item.EntityFallingBlockCustom;
import betterwithmods.common.entity.item.EntityItemBuoy;
import betterwithmods.common.potion.BWPotion;
import betterwithmods.common.registry.KilnStructureManager;
import betterwithmods.common.registry.ToolDamageRecipe;
import betterwithmods.common.registry.anvil.AnvilCraftingManager;
import betterwithmods.common.registry.anvil.VanillaShapedAnvilRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.module.Feature;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.compat.Quark;
import betterwithmods.module.compat.Rustic;
import betterwithmods.module.compat.bop.BiomesOPlenty;
import betterwithmods.module.hardcore.*;
import betterwithmods.util.ColorUtils;
import betterwithmods.util.DispenserBehaviorDynamite;
import betterwithmods.util.InvUtils;
import li.cil.manual.api.API;
import li.cil.manual.common.api.ManualAPIImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = BWMod.MODID)
public class BWRegistry {
    @GameRegistry.ObjectHolder("betterwithmods:true_sight")
    public static final Potion POTION_TRUESIGHT = null;
    @GameRegistry.ObjectHolder("betterwithmods:fortune")
    public static final Potion POTION_FORTUNE = null;
    @GameRegistry.ObjectHolder("betterwithmods:looting")
    public static final Potion POTION_LOOTING = null;

    private static int availableEntityId = 0;

    public static void preInit() {
        API.manualAPI = ManualAPIImpl.INSTANCE;
        BWMBlocks.registerBlocks();
        BWMItems.registerItems();
        BWMBlocks.registerTileEntities();
        BWRegistry.registerEntities();
        BWRegistry.registerBlockDispenserBehavior();
        CapabilityManager.INSTANCE.register(IMechanicalPower.class, new MechanicalCapability.CapabilityMechanicalPower(), MechanicalCapability.DefaultMechanicalPower.class);
        KilnStructureManager.registerKilnBlock(Blocks.BRICK_BLOCK.getDefaultState());
        KilnStructureManager.registerKilnBlock(Blocks.NETHER_BRICK.getDefaultState());
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        BWMBlocks.getBlocks().forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        BWMItems.getItems().forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistryModifiable<IRecipe> reg = ((IForgeRegistryModifiable)event.getRegistry());

        for(Iterator<IRecipe> iter = reg.iterator(); iter.hasNext(); ) {
            IRecipe recipe = iter.next();
            for(ItemStack output: BWMRecipes.REMOVE_RECIPE_BY_OUTPUT) {
                if (InvUtils.matches(recipe.getRecipeOutput(), output)) {
                    reg.remove(reg.getKey(recipe));
                }
            }
        }
    }

    public static void init() {
        BWRegistry.registerHeatSources();
        BWOreDictionary.registerOres();
    }

    public static void postInit() {
        BWOreDictionary.postInitOreDictGathering();
        ColorUtils.initColors();
    }

    public static void postPostInit() {
        registerRecipes();
    }

    /**
     * All names should be snake_case by convention (enforced in 1.11).
     */
    private static void registerEntities() {
        BWRegistry.registerEntity(EntityExtendingRope.class, "extending_rope", 64, 20, true);
        BWRegistry.registerEntity(EntityDynamite.class, "bwm_dynamite", 10, 50, true);
        BWRegistry.registerEntity(EntityUrn.class, "bwm_urn", 10, 50, true);
        BWRegistry.registerEntity(EntityMiningCharge.class, "bwm_mining_charge", 10, 50, true);
        BWRegistry.registerEntity(EntityItemBuoy.class, "entity_item_buoy", 64, 20, true);
        BWRegistry.registerEntity(EntityShearedCreeper.class, "entity_sheared_creeper", 64, 1, true);
        BWRegistry.registerEntity(EntityBroadheadArrow.class, "entity_broadhead_arrow", 64, 1, true);
        BWRegistry.registerEntity(EntityFallingGourd.class, "entity_falling_gourd", 64, 1, true);
        BWRegistry.registerEntity(EntityFallingBlockCustom.class, "falling_block_custom", 64, 20, true);
        BWRegistry.registerEntity(EntitySpiderWeb.class, "bwm_spider_web", 64, 20, true);
        BWRegistry.registerEntity(EntityHCFishHook.class, "bwm_fishing_hook", 64, 20, true);
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
            return InvUtils.asNonnullList(new ItemStack(BWMBlocks.WOLF));
        });

        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntitySheep.class, (world, pos, entity, stack) -> {
            EntitySheep sheep = (EntitySheep) entity;
            if (sheep.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                return InvUtils.asNonnullList(sheep.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
            }
            return NonNullList.create();
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntityChicken.class, (world, pos, entity, stack) -> {
            InvUtils.ejectStackWithOffset(world, pos, new ItemStack(Items.FEATHER, 1 + world.rand.nextInt(2)));
            world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.NEUTRAL, 0.75F, 1.0F);
            entity.setDead();
            return InvUtils.asNonnullList(new ItemStack(Items.EGG));
        });
        BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntityCow.class, (world, pos, entity, stack) -> {
            if (stack.isItemEqual(new ItemStack(Items.BUCKET))) {
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

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(registerPotion(new BWPotion(false, 14270531, 4, 1).setRegistryName("true_sight")));
        event.getRegistry().register(registerPotion(new BWPotion(false, 14270531, 5, 2).setRegistryName("fortune")));
        event.getRegistry().register(registerPotion(new BWPotion(false, 14270531, 6, 2).setRegistryName("looting")));
    }

    private static Potion registerPotion(Potion potion) {
        String potionName = potion.getRegistryName().getResourcePath();
        potion.setPotionName("bwm.effect." + potionName);
        return potion;
    }

    public static void registerRecipes() {
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        replaceIRecipe(HCTools.class, reg);
        replaceIRecipe(HCDiamond.class, reg);
        replaceIRecipe(HCLumber.class, reg);
        replaceIRecipe(HCOres.class, reg);
        replaceIRecipe(HCRedstone.class, reg);
        replaceIRecipe(HCTorches.class, reg);
        replaceIRecipe(BiomesOPlenty.class, reg);
        replaceIRecipe(Quark.class, reg);
        replaceIRecipe(Rustic.class, reg);
        replaceIRecipe(HCFishing.class, reg);
        registerAnvilRecipes(reg);
    }

    private static void retrieveRecipes(String category, ForgeRegistry<IRecipe> reg) {
        List<IRecipe> recipes = BWMRecipes.getHardcoreRecipes(category);
        if (recipes != null) {
            for (IRecipe recipe : recipes) {
                ResourceLocation location = recipe.getRegistryName();
                if (reg.containsKey(location))
                    registerReplacements(reg.getValue(location), recipe);
                else
                    reg.register(recipe);
            }
        }
    }

    private static void replaceIRecipe(Class<? extends Feature> clazz, IForgeRegistry<IRecipe> reg) {
        if (ModuleLoader.isFeatureEnabled(clazz)) {
            List<IRecipe> recipes = BWMRecipes.getHardcoreRecipes(clazz.getSimpleName());
            if (recipes != null) {
                recipes.forEach(reg::register);
            }
        }
    }

    private static void registerAnvilRecipes(IForgeRegistry<IRecipe> reg) {
        List<IRecipe> recipes = BWMRecipes.getHardcoreRecipes("Anvil");
        recipes.forEach(AnvilCraftingManager.VANILLA_CRAFTING::add);
        List<IRecipe> shaped = reg.getValues().stream().filter(i -> i instanceof ShapedRecipes || i instanceof ShapedOreRecipe).collect(Collectors.toList());
        for (IRecipe recipe : shaped) {
            AnvilCraftingManager.VANILLA_CRAFTING.add(new VanillaShapedAnvilRecipe(recipe).setRegistryName(recipe.getRegistryName()));
        }
        List<IRecipe> shapeless = reg.getValues().stream().filter(i -> i instanceof ShapelessRecipes || i instanceof ShapelessOreRecipe || i instanceof ToolDamageRecipe).collect(Collectors.toList());
        shapeless.forEach(AnvilCraftingManager.VANILLA_CRAFTING::add);
    }

    private static void registerReplacements(IRecipe original, IRecipe from) {
        NonNullList<Ingredient> ing = original.getIngredients();
        for (int i = 0; i < ing.size(); i++) {
            ing.set(i, from.getIngredients().get(i));
        }
    }
}

