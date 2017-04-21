package betterwithmods.common.command;

import betterwithmods.common.entity.item.EntityItemBuoy;
import net.minecraft.command.CommandGive;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;

/**
 * We're going to need to get rid of the cosmetic EntityItem that pops up in your face with /give, since that causes dupe bugs somehow.
 */
public class CommandBuoyGive extends CommandGive
{
    /**
     * Callback for when the command is executed
     *
     * @param server The server instance
     * @param sender The sender who executed the command
     * @param args The arguments that were passed
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2) {
            throw new WrongUsageException("commands.give.usage");
        }
        else {
            EntityPlayer entityplayer = getPlayer(server, sender, args[0]);
            Item item = getItemByText(sender, args[1]);
            int i = args.length >= 3 ? parseInt(args[2], 1, 64) : 1;
            int j = args.length >= 4 ? parseInt(args[3]) : 0;
            ItemStack itemstack = new ItemStack(item, i, j);
            ITextComponent name = itemstack.getTextComponent();

            if (args.length >= 5) {
                String s = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();

                try {
                    itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
                }
                catch (NBTException nbtexception) {
                    throw new CommandException("commands.give.tagError", nbtexception.getMessage());
                }
            }

            boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);

            if (flag) {
                entityplayer.world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.inventoryContainer.detectAndSendChanges();
            }
            if (flag && itemstack.isEmpty()) {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
            }
            else {
                sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.getCount());
                EntityItemBuoy entityitem = new EntityItemBuoy(entityplayer.dropItem(itemstack, false));

                if (entityitem != null) {
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(entityplayer.getName());
                }
            }

            notifyCommandListener(sender, this, "commands.give.success", name, i, entityplayer.getName());
        }
    }
}
