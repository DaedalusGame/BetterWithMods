package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.entity.ai.EntityAIFlee;
import betterwithmods.entity.ai.EntityAISearchFood;
import betterwithmods.items.ItemBreedingHarness;
import betterwithmods.util.InvUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.stream.Collectors;

import static net.minecraft.network.datasync.DataSerializers.OPTIONAL_ITEM_STACK;

public class MobAIEvent {
    private static final String TAG_HARNESS = "betterwithmods:harness";
    private static final DataParameter<ItemStack> COW_DATA = EntityDataManager.createKey(EntityCow.class, OPTIONAL_ITEM_STACK),
            PIG_DATA = EntityDataManager.createKey(EntityPig.class, OPTIONAL_ITEM_STACK),
            SHEEP_DATA = EntityDataManager.createKey(EntitySheep.class, OPTIONAL_ITEM_STACK);

    private static DataParameter<ItemStack> getHarnessData(Entity e) {
        if (e instanceof EntityCow)
            return COW_DATA;
        else if (e instanceof EntityPig)
            return PIG_DATA;
        else if (e instanceof EntitySheep)
            return SHEEP_DATA;
        else
            return null;
    }

    public static boolean isValidAnimal(Entity animal) {
        return animal instanceof EntityCow || animal instanceof EntityPig || animal instanceof EntitySheep;
    }

    public static boolean isWillingToMate(EntityVillager villager) {
        if (villager != null) {
            return ReflectionHelper.getPrivateValue(EntityVillager.class, villager, "isWillingToMate", "field_175565_bs");
        }
        return false;
    }

    public static ItemStack getHarness(EntityLivingBase living) {
        return living.getDataManager().get(getHarnessData(living));
    }

    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event) {
        if (isValidAnimal(event.getEntity())) {
            EntityDataManager manager = event.getEntity().getDataManager();
            manager.register(getHarnessData(event.getEntity()), ItemStack.EMPTY);
        }
    }

    @SubscribeEvent
    public void onEntity(EntityJoinWorldEvent e) {
        if (isValidAnimal(e.getEntity())) {
            EntityLiving animal = (EntityLiving) e.getEntity();
            ItemStack dataStack = animal.getDataManager().get(getHarnessData(animal));

            NBTTagCompound cmp = animal.getEntityData().getCompoundTag(TAG_HARNESS);
            ItemStack nbtStack = new ItemStack(cmp);
            if (dataStack != nbtStack)
                e.getEntity().getDataManager().set(getHarnessData(e.getEntity()), nbtStack);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() != EnumHand.MAIN_HAND)
            return;
        Entity target = event.getTarget();
        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        if (isValidAnimal(target) && !target.getPassengers().contains(player)) {
            EntityLiving animal = (EntityLiving) target;
            IItemHandlerModifiable playerInv = (IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            ItemStack harness = getHarness(animal);
            ItemStack held = player.getHeldItemMainhand();
            if (harness != ItemStack.EMPTY) {
                if (held == ItemStack.EMPTY) {
                    InvUtils.addItemStackToInv(playerInv, harness);
                    animal.getDataManager().set(getHarnessData(animal), ItemStack.EMPTY);
                    animal.getEntityData().setTag(TAG_HARNESS, new NBTTagCompound());
                    world.playSound(null, animal.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL, 0.5f, 1.3f);
                    world.playSound(null, animal.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.NEUTRAL, 0.5f, 1.3f);
                }
            } else if (held != ItemStack.EMPTY && held.getItem() instanceof ItemBreedingHarness) {
                if (getHarness(animal) != ItemStack.EMPTY)
                    return;
                InvUtils.consumeItemsInInventory(playerInv, held, 1);
                ItemStack copyStack = held.copy();
                copyStack.setCount(1);
                NBTTagCompound cmp = new NBTTagCompound();
                copyStack.writeToNBT(cmp);
                animal.getDataManager().set(getHarnessData(animal), copyStack);
                animal.getEntityData().setTag(TAG_HARNESS, cmp);
                if (animal instanceof EntitySheep)
                    ((EntitySheep) animal).setSheared(true);
                world.playSound(null, animal.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL, 1, 1);
                world.playSound(null, animal.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.NEUTRAL, 1, 1f);
                player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent e) {
        EntityLivingBase entity = e.getEntityLiving();
        if (isValidAnimal(entity)) {
            if (getHarness(entity) != ItemStack.EMPTY)
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(-1);
            else
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        }
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) evt.getEntity();
            if (entity instanceof EntityAnimal) {
                ((EntityAnimal) entity).tasks.addTask(3, new EntityAISearchFood((EntityAnimal) entity));
                if (!(entity instanceof EntityTameable)) {
                    float speed = 1.25F;
                    if (entity instanceof EntityCow)
                        speed = 2.0F;
                    else if (entity instanceof EntityChicken)
                        speed = 1.4F;
                    ((EntityAnimal) entity).tasks.addTask(0, new EntityAIFlee((EntityCreature) entity, speed));
                }
            } else if (entity instanceof EntityVillager) {
                if (BWConfig.hardcoreVillagers) {
                    EntityVillager villager = (EntityVillager) entity;
                    villager.tasks.removeTask(new EntityAIVillagerMate(villager));
                    villager.tasks.addTask(0, new betterwithmods.entity.ai.EntityAIVillagerMate(villager, 1));
                    villager.tasks.addTask(0, new EntityAITempt(villager, 1d, false, new HashSet<>(OreDictionary.getOres("gemDiamond").stream().map(ItemStack::getItem).collect(Collectors.toList()))));
                }
            }
        }
    }

    @SubscribeEvent
    public void mateVillagers(PlayerInteractEvent.EntityInteractSpecific e) {
        //TODO fix trading menu opening when breeding
        if (BWConfig.hardcoreVillagers) {
            if (e.getTarget() instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) e.getTarget();
                ItemStack stack = e.getItemStack();
                boolean isDiamond = stack != ItemStack.EMPTY && OreDictionary.getOres("gemDiamond").stream().anyMatch(stack::isItemEqual);
                if (isDiamond && !villager.isChild() && !isWillingToMate(villager) && villager.getGrowingAge() == 0) {
                    if (e.getEntityPlayer().capabilities.isCreativeMode)
                        stack.shrink(1);
                    e.getWorld().setEntityState(villager, (byte) 12);
                    ((EntityVillager) e.getTarget()).setIsWillingToMate(true);
                    if (stack.getCount() < 1)
                        e.getEntityPlayer().setHeldItem(e.getHand(), ItemStack.EMPTY);
                }
            }
        }
    }
}
