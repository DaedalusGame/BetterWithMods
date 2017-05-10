package betterwithmods.module.hardcore;

import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.entity.ai.EntityAIEatFood;
import betterwithmods.common.entity.ai.ShooterSpiderWeb;
import betterwithmods.module.Feature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by tyler on 4/22/17.
 */
public class HCHunting extends Feature {

    private static final Predicate<ItemStack> isMeat = stack -> BWOreDictionary.isOre(stack, "listAllmeat");
    public static boolean spidersShootWebs;

    public static List<Class> zombiesAttack, spiderAttack;

    @Override
    public void setupConfig() {
        spidersShootWebs = loadPropBool("Spiders Shoot Web", "Spiders shoot webs at targets", true);
        String[] zombieStrings = loadPropStringList("Mobs Zombies Attack", "List of entity classes which zombies will attack", new String[]{"net.minecraft.entity.passive.EntityCow", "net.minecraft.entity.passive.EntitySheep", "net.minecraft.entity.passive.EntityPig"});
        String[] spiderStrings = loadPropStringList("Mobs Spider Attack", "List of entity classes which spiders will attack", new String[]{"net.minecraft.entity.passive.EntityChicken",});
        zombiesAttack = Arrays.stream(zombieStrings).map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignore) {
            }
            return null;
        }).collect(Collectors.toList());
        spiderAttack = Arrays.stream(spiderStrings).map(clazz -> {
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignore) {
            }
            return null;
        }).collect(Collectors.toList());
    }

    @SubscribeEvent
    public void addEntityAI(EntityJoinWorldEvent evt) {
        if (evt.getEntity() instanceof EntityCreature) {
            EntityCreature entity = (EntityCreature) evt.getEntity();
            if (entity instanceof EntityZombie) {
                ((EntityZombie) entity).tasks.addTask(0, new EntityAIEatFood(entity, isMeat));
                for (Class clazz : zombiesAttack) {
                    ((EntityZombie) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, clazz, true));
                }
            }
            if (entity instanceof EntitySpider) {
                for (Class clazz : spiderAttack) {
                    ((EntitySpider) entity).targetTasks.addTask(3, new EntityAINearestAttackableTarget(entity, clazz, true));
                }
                ((EntitySpider) entity).tasks.addTask(0, new EntityAIEatFood(entity, itemStack -> itemStack.getItem() == Items.CHICKEN || itemStack.getItem() == Items.COOKED_CHICKEN));
                if (spidersShootWebs) {
                    ((EntitySpider) entity).tasks.addTask(3, new ShooterSpiderWeb((EntitySpider) entity, 200, 15.0F));
                }
            }
        }
    }

    @Override
    public String getFeatureDescription() {
        return "Makes it so Mobs hunt other animals too. Zombies attack herd animals, Spiders eat Chickens, Wolves will eat anything";
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
