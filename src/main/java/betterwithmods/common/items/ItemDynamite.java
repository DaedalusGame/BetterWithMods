package betterwithmods.common.items;

import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.entity.EntityDynamite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDynamite extends Item {
    public ItemDynamite() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        int flintIndex = -1;

        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            if (!player.inventory.mainInventory.get(i).isEmpty() && player.inventory.mainInventory.get(i).getItem() == Items.FLINT_AND_STEEL) {
                flintIndex = i;
                break;
            }
        }

        if (!world.isRemote) {
            boolean lit = false;

            if (flintIndex > -1) {
                lit = true;
                ItemStack steelStack = player.inventory.getStackInSlot(flintIndex);
                steelStack.damageItem(1, player);
                if (steelStack.getCount() < 1)
                    player.inventory.mainInventory.set(flintIndex, ItemStack.EMPTY);
            }

            player.getHeldItem(hand).shrink(1);
            EntityDynamite dynamite = new EntityDynamite(world, player, new ItemStack(this, 1), lit);
            world.spawnEntity(dynamite);

            if (lit)
                world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.AMBIENT, 1.0F, 1.0F);
            else
                world.playSound(null, new BlockPos(dynamite.posX, dynamite.posY, dynamite.posZ), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.AMBIENT, 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        list.add(I18n.format("lore.bwm:dynamite"));
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return 6000;
    }
}
