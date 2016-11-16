package betterwithmods.event;

import betterwithmods.config.BWConfig;
import betterwithmods.entity.ai.EntityAIFlee;
import betterwithmods.entity.ai.EntityAISearchFood;
import betterwithmods.items.ItemBreedingHarness;
import betterwithmods.util.InvUtils;
import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.stream.Collectors;

import static net.minecraft.network.datasync.DataSerializers.OPTIONAL_ITEM_STACK;

public class MobAIEvent {
    private static final String TAG_HARNESS = "betterwithmods:harness";
    private static final DataParameter<Optional<ItemStack>> COW_DATA = EntityDataManager.createKey(EntityCow.class, OPTIONAL_ITEM_STACK),
            PIG_DATA = EntityDataManager.createKey(EntityPig.class, OPTIONAL_ITEM_STACK),
            SHEEP_DATA = EntityDataManager.createKey(EntitySheep.class, OPTIONAL_ITEM_STACK);
    private static DataParameter<Optional<ItemStack>> getHarnessData(Entity e) {
        if(e instanceof EntityCow)
            return COW_DATA;
        else if ( e instanceof EntityPig)
            return PIG_DATA;
        else if(e instanceof EntitySheep)
            return SHEEP_DATA;
        else
            return null;
    }

    public static boolean isValidAnimal(Entity animal) {
        return animal instanceof EntityCow || animal instanceof EntityPig || animal instanceof EntitySheep;
    }

    @SubscribeEvent
    public void onEntityInit(EntityEvent.EntityConstructing event) {
        if(isValidAnimal(event.getEntity())) {
            EntityDataManager manager = event.getEntity().getDataManager();
            manager.register(getHarnessData(event.getEntity()), Optional.absent());
        }
    }

    @SubscribeEvent
    public void preUpdate(EntityEvent.CanUpdate event) {
        if(isValidAnimal(event.getEntity()) && !event.getEntity().worldObj.isRemote) {
            ItemStack dataStack = event.getEntity().getDataManager().get(getHarnessData(event.getEntity())).orNull();

            NBTTagCompound cmp = event.getEntity().getEntityData().getCompoundTag(TAG_HARNESS);
            ItemStack nbtStack = ItemStack.loadItemStackFromNBT(cmp);
            if(nbtStack != null)
                System.out.println(nbtStack);
            if(dataStack != nbtStack)
                event.getEntity().getDataManager().set(getHarnessData(event.getEntity()), Optional.of(nbtStack));
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if(event.getHand() != EnumHand.MAIN_HAND)
            return;
        Entity target = event.getTarget();
        EntityPlayer player = event.getEntityPlayer();
        if(isValidAnimal(target) && !target.getPassengers().contains(player)) {
            ItemStack harness = getHarness(target);
            if(harness != null && player.getHeldItemMainhand() == null) {
                target.getDataManager().set(getHarnessData(target), Optional.absent());
                ((EntityAnimal) target).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
                if (!event.getWorld().isRemote)
                    InvUtils.ejectStackWithOffset(event.getWorld(), target.getPosition(), harness);
                return;
            }

            EnumHand hand = EnumHand.MAIN_HAND;
            ItemStack stack = player.getHeldItemMainhand();
            if(stack == null || !(stack.getItem() instanceof ItemBreedingHarness)) {
                stack = player.getHeldItemOffhand();
                hand = EnumHand.OFF_HAND;
            }

            if(stack != null && stack.getItem() instanceof ItemBreedingHarness) {
                ItemStack copyStack = stack.copy();
                copyStack.stackSize = 1;
                player.swingArm(hand);
                NBTTagCompound cmp = new NBTTagCompound();
                copyStack.writeToNBT(cmp);
                target.getDataManager().set(getHarnessData(target), Optional.of(copyStack));
                target.getEntityData().setTag(TAG_HARNESS, cmp);
                if(!event.getWorld().isRemote) {
                    event.setCanceled(true);
                    if(!player.capabilities.isCreativeMode) {
                        stack.stackSize--;
                        if(stack.stackSize <= 0)
                            player.setHeldItem(hand, (ItemStack)null);
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent e) {
        if(e.getEntityLiving() instanceof EntityAnimal) {
            EntityAnimal animal = (EntityAnimal) e.getEntityLiving();
            ItemStack stack = getHarness(animal);

            if(stack != null) {
                EntityItem item = new EntityItem(animal.worldObj,animal.posX,animal.posY,animal.posZ,stack);
                e.getDrops().add(item);
            }
        }
    }
    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent e) {
        EntityLivingBase entity = e.getEntityLiving();
        if(isValidAnimal(entity) && getHarness(entity) != null) {
            entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(-1);
        }
    }
    public static ItemStack getHarness(Entity animal) {
        return animal.getDataManager().get(getHarnessData(animal)).orNull();
    }

    public static boolean isWillingToMate(EntityVillager villager) {
        if (villager != null) {
            return ReflectionHelper.getPrivateValue(EntityVillager.class, villager, "isWillingToMate", "field_175565_bs");
        }
        return false;
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
                boolean isDiamond = stack != null && OreDictionary.getOres("gemDiamond").stream().anyMatch(stack::isItemEqual);
                if (isDiamond && !villager.isChild() && !isWillingToMate(villager) && villager.getGrowingAge() == 0) {
                    if (e.getEntityPlayer().capabilities.isCreativeMode)
                        stack.stackSize--;
                    e.getWorld().setEntityState(villager, (byte) 12);
                    ((EntityVillager) e.getTarget()).setIsWillingToMate(true);
                    if (stack.stackSize < 1)
                        e.getEntityPlayer().setHeldItem(e.getHand(), null);
                }
            }
        }
    }
}
