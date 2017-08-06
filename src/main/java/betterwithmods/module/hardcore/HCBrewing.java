package betterwithmods.module.hardcore;

import betterwithmods.common.BWMItems;
import betterwithmods.common.entity.EntityJungleSpider;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class HCBrewing extends Feature {
    private static boolean removeMovementPotions;
    private static boolean waterBreathingAnyFish;
    private static boolean removeWitchPotionDrops;
    private static boolean modPotionCompat;
    private static int potionStackSize;
    private boolean tryChangePotions;

    @Override
    public void setupConfig() {
        removeMovementPotions = loadPropBool("Remove Movement Potions", "Removes recipes for Speed and Leaping potions.", true);
        waterBreathingAnyFish = loadPropBool("Water Breathing Any Fish", "Any fish works for brewing Water Breathing potions.", true);
        removeWitchPotionDrops = loadPropBool("Remove Witch Ingredient Drops", "Removes redstone and glowstone from witch drops", true);
        modPotionCompat = loadPropBool("Modded Potion Compatibility", "Similarly modifies non-vanilla potions.", true);
        potionStackSize = loadPropInt("Potion Stacksize", "Maximum stacksize of potion items.", 8);
    }

    @Override
    public String getFeatureDescription() {
        return "Modifies and rebalances vanilla brewing recipes and makes potions stack up to 8.";
    }

    private boolean isWitchDropBlacklisted(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.GLOWSTONE_DUST || item == Items.SUGAR || item == Items.SPIDER_EYE || item == Items.GUNPOWDER;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void mobDrops(LivingDropsEvent evt) {
        Entity entity = evt.getEntityLiving();
        List<EntityItem> drops = evt.getDrops();

        if (entity instanceof EntityWitch) {
            Iterator<EntityItem> iterator = drops.iterator();
            while (iterator.hasNext()) {
                EntityItem item = iterator.next();
                ItemStack stack = item.getItem();
                if (removeWitchPotionDrops && isWitchDropBlacklisted(stack))
                    iterator.remove();
                else if (stack.getItem() == Items.REDSTONE)
                    item.setItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WITCH_WART, stack.getCount()));
            }
        }

        if (entity instanceof EntitySquid) {
            if (entity.world.rand.nextInt(100) < 10) {
                entity.entityDropItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.MYSTERY_GLAND), 0);
            }
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        tryChangePotions = true;

        try {
            List<PotionHelper.MixPredicate<PotionType>> mixPredicates = (List<PotionHelper.MixPredicate<PotionType>>) ReflectionHelper.findField(PotionHelper.class, "field_185213_a", "POTION_TYPE_CONVERSIONS").get(null);
            List<PotionHelper.MixPredicate<Item>> mixItemPredicates = (List<PotionHelper.MixPredicate<Item>>) ReflectionHelper.findField(PotionHelper.class, "field_185214_b", "POTION_ITEM_CONVERSIONS").get(null);
            //List<PotionHelper.ItemPredicateInstance> potionItems = (List<PotionHelper.ItemPredicateInstance>) ReflectionHelper.findField(PotionHelper.class,"field_185215_c","POTION_ITEMS").get(null);

            mixPredicates.clear();
            mixItemPredicates.clear();
            //potionItems.clear(); //Don't clear this, this is just potion-related crap
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            tryChangePotions = false;
        }

        Items.POTIONITEM.setMaxStackSize(potionStackSize);
        Items.SPLASH_POTION.setMaxStackSize(potionStackSize);
        Items.LINGERING_POTION.setMaxStackSize(potionStackSize);

        if (tryChangePotions) {
            Ingredient extender = convertToPotionItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WITCH_WART));
            Ingredient strenthener = convertToPotionItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
            Ingredient inverter = convertToPotionItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POISON_SAC));
            Ingredient awkward = convertToPotionItem(Items.NETHER_WART);
            Ingredient fireResistance = convertToPotionItem(Items.MAGMA_CREAM);
            Ingredient nightVision = convertToPotionItem(Items.SPIDER_EYE);
            Ingredient poison = convertToPotionItem(Blocks.RED_MUSHROOM);
            Ingredient regeneration = convertToPotionItem(Items.GHAST_TEAR);
            Ingredient strength = convertToPotionItem(Items.BLAZE_POWDER);
            Ingredient swiftness = convertToPotionItem(Items.SUGAR);
            Ingredient leaping = convertToPotionItem(Items.RABBIT_FOOT);
            Ingredient waterBreathing = convertToPotionItem(new ItemStack(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
            if (waterBreathingAnyFish)
                waterBreathing = convertToPotionItem(Items.FISH);
            Ingredient healing = convertToPotionItem(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.MYSTERY_GLAND));

            PotionHelper.addContainerRecipe(Items.POTIONITEM, BWMItems.CREEPER_OYSTER, Items.SPLASH_POTION);
            PotionHelper.addContainerRecipe(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);

            PotionHelper.addMix(PotionTypes.WATER, awkward, PotionTypes.AWKWARD);
            PotionHelper.addMix(PotionTypes.WATER, extender, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, strenthener, PotionTypes.THICK);
            PotionHelper.addMix(PotionTypes.WATER, fireResistance, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, nightVision, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, poison, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, regeneration, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, strength, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, swiftness, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, leaping, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, waterBreathing, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, healing, PotionTypes.MUNDANE);
            PotionHelper.addMix(PotionTypes.WATER, inverter, PotionTypes.MUNDANE);

            PotionHelper.addMix(PotionTypes.AWKWARD, fireResistance, PotionTypes.FIRE_RESISTANCE);
            PotionHelper.addMix(PotionTypes.FIRE_RESISTANCE, extender, PotionTypes.LONG_FIRE_RESISTANCE);

            PotionHelper.addMix(PotionTypes.AWKWARD, nightVision, PotionTypes.NIGHT_VISION);
            PotionHelper.addMix(PotionTypes.NIGHT_VISION, extender, PotionTypes.LONG_NIGHT_VISION);

            PotionHelper.addMix(PotionTypes.AWKWARD, poison, PotionTypes.POISON);
            PotionHelper.addMix(PotionTypes.POISON, strenthener, PotionTypes.STRONG_POISON);
            PotionHelper.addMix(PotionTypes.POISON, extender, PotionTypes.LONG_POISON);

            PotionHelper.addMix(PotionTypes.AWKWARD, regeneration, PotionTypes.REGENERATION);
            PotionHelper.addMix(PotionTypes.REGENERATION, extender, PotionTypes.LONG_REGENERATION);
            PotionHelper.addMix(PotionTypes.REGENERATION, strenthener, PotionTypes.STRONG_REGENERATION);

            PotionHelper.addMix(PotionTypes.AWKWARD, strength, PotionTypes.STRENGTH);
            PotionHelper.addMix(PotionTypes.STRENGTH, strenthener, PotionTypes.STRONG_STRENGTH);
            PotionHelper.addMix(PotionTypes.STRENGTH, extender, PotionTypes.LONG_STRENGTH);

            if (!removeMovementPotions) {
                PotionHelper.addMix(PotionTypes.AWKWARD, swiftness, PotionTypes.SWIFTNESS);
                PotionHelper.addMix(PotionTypes.SWIFTNESS, strenthener, PotionTypes.STRONG_SWIFTNESS);
                PotionHelper.addMix(PotionTypes.SWIFTNESS, extender, PotionTypes.LONG_SWIFTNESS);

                PotionHelper.addMix(PotionTypes.AWKWARD, leaping, PotionTypes.LEAPING);
                PotionHelper.addMix(PotionTypes.LEAPING, extender, PotionTypes.LONG_LEAPING);
                PotionHelper.addMix(PotionTypes.LEAPING, strenthener, PotionTypes.STRONG_LEAPING);

                PotionHelper.addMix(PotionTypes.SWIFTNESS, inverter, PotionTypes.SLOWNESS);
                PotionHelper.addMix(PotionTypes.STRONG_SWIFTNESS, inverter, PotionTypes.SLOWNESS);
                PotionHelper.addMix(PotionTypes.LONG_SWIFTNESS, inverter, PotionTypes.LONG_SLOWNESS);
                PotionHelper.addMix(PotionTypes.LEAPING, inverter, PotionTypes.SLOWNESS);
                PotionHelper.addMix(PotionTypes.STRONG_LEAPING, inverter, PotionTypes.SLOWNESS);
                PotionHelper.addMix(PotionTypes.LONG_LEAPING, inverter, PotionTypes.LONG_SLOWNESS);
                PotionHelper.addMix(PotionTypes.SLOWNESS, extender, PotionTypes.LONG_SLOWNESS);
            }

            PotionHelper.addMix(PotionTypes.AWKWARD, waterBreathing, PotionTypes.WATER_BREATHING);
            PotionHelper.addMix(PotionTypes.WATER_BREATHING, extender, PotionTypes.LONG_WATER_BREATHING);

            PotionHelper.addMix(PotionTypes.AWKWARD, healing, PotionTypes.HEALING);
            PotionHelper.addMix(PotionTypes.HEALING, strenthener, PotionTypes.STRONG_HEALING);

            PotionHelper.addMix(PotionTypes.HEALING, inverter, PotionTypes.HARMING);
            PotionHelper.addMix(PotionTypes.STRONG_HEALING, inverter, PotionTypes.STRONG_HARMING);
            PotionHelper.addMix(PotionTypes.POISON, inverter, PotionTypes.HARMING);
            PotionHelper.addMix(PotionTypes.LONG_POISON, inverter, PotionTypes.HARMING);
            PotionHelper.addMix(PotionTypes.STRONG_POISON, inverter, PotionTypes.STRONG_HARMING);
            PotionHelper.addMix(PotionTypes.HARMING, strenthener, PotionTypes.STRONG_HARMING);

            PotionHelper.addMix(PotionTypes.STRENGTH, inverter, PotionTypes.WEAKNESS);
            PotionHelper.addMix(PotionTypes.STRONG_STRENGTH, inverter, PotionTypes.WEAKNESS);
            PotionHelper.addMix(PotionTypes.LONG_STRENGTH, inverter, PotionTypes.LONG_WEAKNESS);
            PotionHelper.addMix(PotionTypes.WEAKNESS, extender, PotionTypes.LONG_WEAKNESS);

            PotionHelper.addMix(PotionTypes.NIGHT_VISION, inverter, PotionTypes.INVISIBILITY);
            PotionHelper.addMix(PotionTypes.LONG_NIGHT_VISION, inverter, PotionTypes.LONG_INVISIBILITY);
            PotionHelper.addMix(PotionTypes.INVISIBILITY, extender, PotionTypes.LONG_INVISIBILITY);
        }
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (tryChangePotions && modPotionCompat) {
            ItemStack extenderToReplace = new ItemStack(Items.REDSTONE);
            ItemStack strengthenerToReplace = new ItemStack(Items.GLOWSTONE_DUST);
            ItemStack inverterToReplace = new ItemStack(Items.FERMENTED_SPIDER_EYE);
            ItemStack splashToReplace = new ItemStack(Items.GUNPOWDER);

            try {
                List<IBrewingRecipe> recipes = (List<IBrewingRecipe>) ReflectionHelper.findField(BrewingRecipeRegistry.class,"recipes").get(null);
                ListIterator<IBrewingRecipe> iterator = recipes.listIterator();

                while(iterator.hasNext())
                {
                    IBrewingRecipe recipe = iterator.next();
                    if(recipe instanceof AbstractBrewingRecipe)
                    {
                        AbstractBrewingRecipe abstractRecipe = (AbstractBrewingRecipe) recipe;
                        if(abstractRecipe.isIngredient(extenderToReplace) && isExtended(abstractRecipe.getInput(),abstractRecipe.getOutput()))
                        {
                            iterator.remove();
                            iterator.add(new BrewingRecipe(abstractRecipe.getInput(),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.WITCH_WART),abstractRecipe.getOutput()));
                        }
                        else if(abstractRecipe.isIngredient(strengthenerToReplace) && isStrong(abstractRecipe.getInput(),abstractRecipe.getOutput()))
                        {
                            iterator.remove();
                            iterator.add(new BrewingRecipe(abstractRecipe.getInput(),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE),abstractRecipe.getOutput()));
                        }
                        else if(abstractRecipe.isIngredient(inverterToReplace) && isInverted(abstractRecipe.getInput(),abstractRecipe.getOutput()))
                        {
                            iterator.remove();
                            iterator.add(new BrewingRecipe(abstractRecipe.getInput(),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POISON_SAC),abstractRecipe.getOutput()));
                        }
                        else if(abstractRecipe.isIngredient(splashToReplace) && isSplash(abstractRecipe.getInput(),abstractRecipe.getOutput()))
                        {
                            iterator.remove();
                            iterator.add(new BrewingRecipe(abstractRecipe.getInput(),new ItemStack(BWMItems.CREEPER_OYSTER),abstractRecipe.getOutput()));
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isExtended(ItemStack potionA, ItemStack potionB)
    {
        List<PotionEffect> effectsA = PotionUtils.getEffectsFromStack(potionA);
        List<PotionEffect> effectsB = PotionUtils.getEffectsFromStack(potionB);

        if(effectsA.size() != 1 || effectsB.size() != 1)
            return false;

        PotionEffect effectA = effectsA.get(0);
        PotionEffect effectB = effectsB.get(0);

        return effectA.getPotion().equals(effectB.getPotion()) && effectA.getDuration() != effectB.getDuration(); //Not equal because of fuse potions in Extra Alchemy
    }

    public boolean isStrong(ItemStack potionA, ItemStack potionB)
    {
        List<PotionEffect> effectsA = PotionUtils.getEffectsFromStack(potionA);
        List<PotionEffect> effectsB = PotionUtils.getEffectsFromStack(potionB);

        if(effectsA.size() != 1 || effectsB.size() != 1)
            return false;

        PotionEffect effectA = effectsA.get(0);
        PotionEffect effectB = effectsB.get(0);

        return effectA.getPotion().equals(effectB.getPotion()) && effectA.getAmplifier() < effectB.getAmplifier();
    }

    public boolean isInverted(ItemStack potionA, ItemStack potionB)
    {
        List<PotionEffect> effectsA = PotionUtils.getEffectsFromStack(potionA);
        List<PotionEffect> effectsB = PotionUtils.getEffectsFromStack(potionB);

        if(effectsA.size() != 1 || effectsB.size() != 1)
            return false;

        PotionEffect effectA = effectsA.get(0);
        PotionEffect effectB = effectsB.get(0);

        return !effectA.getPotion().equals(effectB.getPotion());
    }

    public boolean isSplash(ItemStack potionA, ItemStack potionB)
    {
        List<PotionEffect> effectsA = PotionUtils.getEffectsFromStack(potionA);
        List<PotionEffect> effectsB = PotionUtils.getEffectsFromStack(potionB);

        if(effectsA.size() != 1 || effectsB.size() != 1)
            return false;

        PotionEffect effectA = effectsA.get(0);
        PotionEffect effectB = effectsB.get(0);

        return effectA.getPotion().equals(effectB.getPotion()) && potionB.getItem() instanceof ItemSplashPotion;
    }

    public Ingredient convertToPotionItem(ItemStack stack)
    {

        return Ingredient.fromStacks(stack);
    }

    public Ingredient convertToPotionItem(Item item)
    {
        return Ingredient.fromItem(item);
    }

    public Ingredient convertToPotionItem(Block block)
    {
        return Ingredient.fromItem(Item.getItemFromBlock(block));
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
