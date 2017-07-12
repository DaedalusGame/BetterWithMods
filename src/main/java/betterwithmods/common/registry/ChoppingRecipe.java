package betterwithmods.common.registry;

import betterwithmods.BWMod;
import betterwithmods.common.BWOreDictionary;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by blueyu2 on 12/12/16.
 */
public class ChoppingRecipe extends ToolDamageRecipe {
    private BWOreDictionary.Wood wood;

    public ChoppingRecipe(BWOreDictionary.Wood wood) {
        super(new ResourceLocation(BWMod.MODID, "chopping"), wood.getPlank(2), Ingredient.fromStacks(wood.getLog(1)), ChoppingRecipe::isAxe);
        this.wood = wood;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ItemStack getExampleStack() {
        return new ItemStack(Items.IRON_AXE);
    }

    private static boolean isAxe(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem().getToolClasses(stack).contains("axe")) {
                if (stack.getItem().getRegistryName().getResourceDomain().equals("tconstruct")) {
                    if (stack.getItemDamage() >= stack.getMaxDamage())
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void dropExtra(PlayerEvent.ItemCraftedEvent event) {
        if (event.player == null)
            return;
        if (isMatch(event.craftMatrix, event.player.world)) {
            if (!event.player.getEntityWorld().isRemote) {
                event.player.entityDropItem(wood.getSawdust(1), 0);
                event.player.entityDropItem(wood.getSawdust(1), 0);
            } else {
                event.player.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 0.25F, 2.5F);
            }
        }
    }
}

