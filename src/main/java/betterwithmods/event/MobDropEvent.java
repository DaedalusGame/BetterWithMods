package betterwithmods.event;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.util.player.EntityPlayerExt;
import betterwithmods.util.player.Profiles;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropEvent {
    public static FakePlayer player;

    //Initializing a static fake player for saws, so spawn isn't flooded with player equipping sounds when mobs hit the saw.
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load evt) {
        if (evt.getWorld() instanceof WorldServer) {
            player = FakePlayerFactory.get((WorldServer) evt.getWorld(), Profiles.BWMSAW);
            ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
            sword.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 2);
            player.setHeldItem(EnumHand.MAIN_HAND, sword);
        }
    }

    //Not sure if this would be needed, but can't be too safe.
    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload evt) {
        if (evt.getWorld() instanceof WorldServer) {
            if (player != null) {
                player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                player = null;
            }
        }
    }



    @SubscribeEvent
    public void mobDiesBySaw(LivingDropsEvent evt) {
        BlockPos pos = evt.getEntityLiving().getPosition().down();
        if (isChoppingBlock(evt.getEntityLiving().getEntityWorld(), pos) || isBattleAxe(evt.getEntityLiving())) {
            if (!(evt.getEntityLiving() instanceof EntityPlayer)) {
                for (EntityItem item : evt.getDrops()) {
                    ItemStack stack = item.getEntityItem();
                    if (stack.getMaxStackSize() != 1 && evt.getEntity().getEntityWorld().rand.nextBoolean())
                        item.setEntityItemStack(new ItemStack(stack.getItem(), stack.getCount() + 1, stack.getItemDamage()));
                }
            }
            if (evt.getEntityLiving() instanceof EntityAgeable)
                addDrop(evt, new ItemStack(BWMItems.MATERIAL, 1, 5));
            int headChance = evt.getEntityLiving().getEntityWorld().rand.nextInt(12);
            if (headChance < 5) {
                if (evt.getEntityLiving() instanceof EntitySkeleton)
                    addDrop(evt, new ItemStack(Items.SKULL, 1, 0));
                else if (evt.getEntityLiving() instanceof EntityWitherSkeleton)
                    addDrop(evt, new ItemStack(Items.SKULL, 1, 1));
                else if (evt.getEntityLiving() instanceof EntityZombie)
                    addDrop(evt, new ItemStack(Items.SKULL, 1, 2));
                else if (evt.getEntityLiving() instanceof EntityCreeper)
                    addDrop(evt, new ItemStack(Items.SKULL, 1, 4));
                else if (evt.getEntityLiving() instanceof EntityPlayer) {
                    addDrop(evt, EntityPlayerExt.getPlayerHead((EntityPlayer) evt.getEntityLiving()));
                }
            }
        }
    }

    private boolean isChoppingBlock(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == BWMBlocks.AESTHETIC) {
            IBlockState state = world.getBlockState(pos);
            return state.getValue(BlockAesthetic.blockType) == BlockAesthetic.EnumType.CHOPBLOCK || state.getValue(BlockAesthetic.blockType) == BlockAesthetic.EnumType.CHOPBLOCKBLOOD;
        }
        return false;
    }

    private boolean isBattleAxe(EntityLivingBase entity) {
        DamageSource source = entity.getLastDamageSource();
        if (source != null && source.getSourceOfDamage() != null) {
            Entity e = source.getSourceOfDamage();
            if (e instanceof EntityLivingBase) {
                ItemStack held = ((EntityLivingBase) e).getHeldItemMainhand();
                if (held != ItemStack.EMPTY && held.isItemEqual(new ItemStack(BWMItems.STEEL_BATTLEAXE))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addDrop(LivingDropsEvent evt, ItemStack drop) {
        EntityItem item = new EntityItem(evt.getEntityLiving().getEntityWorld(), evt.getEntityLiving().posX, evt.getEntityLiving().posY, evt.getEntityLiving().posZ, drop);
        item.setDefaultPickupDelay();
        evt.getDrops().add(item);
    }



}
