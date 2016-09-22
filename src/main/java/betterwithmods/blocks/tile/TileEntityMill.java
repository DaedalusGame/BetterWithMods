package betterwithmods.blocks.tile;

import betterwithmods.BWMBlocks;
import betterwithmods.BWSounds;
import betterwithmods.blocks.BlockMechMachines;
import betterwithmods.craft.bulk.CraftingManagerMill;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMill extends TileBasicInventory implements ITickable {
    private int grindType = 0;
    private boolean validateContents;
    private boolean containsIngredientsToGrind;
    public int grindCounter;

    public TileEntityMill() {
        this.grindCounter = 0;
        this.validateContents = true;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (this.worldObj.isRemote)
            return;

        Block block = this.worldObj.getBlockState(this.pos).getBlock();

        if (block == null || !(block instanceof BlockMechMachines))
            return;

        BlockMechMachines mill = (BlockMechMachines) block;

        if (this.validateContents)
            validateContents();

        if (mill.isMechanicalOn(worldObj, pos))
            if (worldObj.rand.nextInt(6) == 0)
                worldObj.playSound(null, pos, BWSounds.STONEGRIND, SoundCategory.BLOCKS, 0.4F + worldObj.rand.nextFloat() * 0.1F, 0.25F + worldObj.rand.nextFloat() * 0.1F);

        if (this.containsIngredientsToGrind && mill.isMechanicalOn(worldObj, pos)) {
            if (!this.worldObj.isRemote) {
                if (grindType == 2) {
                    if (this.worldObj.rand.nextInt(20) < 2) {
                        worldObj.playSound(null, pos, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.BLOCKS, 0.75F, worldObj.rand.nextFloat() * 0.4F + 0.8F);
                    }
                } else if (grindType == 3) {
                    if (this.worldObj.rand.nextInt(20) < 2)
                        worldObj.playSound(null, pos, SoundEvents.ENTITY_WOLF_HURT, SoundCategory.BLOCKS, 2.0F, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
            this.grindCounter += 1;
            if (this.grindCounter > 199) {
                grindContents();
                this.grindCounter = 0;
                this.validateContents = true;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("GrindCounter"))
            this.grindCounter = tag.getInteger("GrindCounter");
    }

    @Override
    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 3);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("GrindCounter", this.grindCounter);
        return tag;
    }

    public int getGrindType() {
        return this.grindType;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (isCompanionCubeInInventory())
                this.worldObj.playSound(null, this.pos, SoundEvents.ENTITY_WOLF_WHINE, SoundCategory.BLOCKS, 0.5F, 1.0F);
            this.validateContents = true;
        }
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
    }


    private void ejectStack(ItemStack stack) {
        List<EnumFacing> validDirections = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState check = worldObj.getBlockState(pos.offset(facing));
            if(check.getBlock().isReplaceable(worldObj, pos.offset(facing)) || worldObj.isAirBlock(pos.offset(facing)))
                validDirections.add(facing);
        }

        if(validDirections.isEmpty()) {
            IBlockState down = worldObj.getBlockState(pos.offset(EnumFacing.DOWN));
            if(down.getBlock().isReplaceable(worldObj, pos.offset(EnumFacing.DOWN)) || worldObj.isAirBlock(pos.offset(EnumFacing.DOWN)))
                validDirections.add(EnumFacing.DOWN);
        }

        BlockPos offset;
        if(validDirections.size() > 1)
            offset = pos.offset(validDirections.get(worldObj.rand.nextInt(validDirections.size())));
        else if(validDirections.isEmpty())
            offset = pos.offset(EnumFacing.UP);
        else
            offset = pos.offset(validDirections.get(0));

        InvUtils.ejectStackWithOffset(worldObj, offset, stack);
    }

    public int getGrindProgressScaled(int scale) {
        return this.grindCounter * scale / 200;
    }

    public boolean isGrinding() {
        return this.grindCounter > 0;
    }

    public boolean isCompanionCubeInInventory() {
        for (int i = 0; i < 3; i++) {
            if (this.inventory.getStackInSlot(i) != null) {
                Item item = this.inventory.getStackInSlot(i).getItem();
                if (item != null) {
                    if (item == Item.getItemFromBlock(BWMBlocks.WOLF))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean grindContents() {
        CraftingManagerMill mill = CraftingManagerMill.getInstance();
        List<Object> ingredients = mill.getValidCraftingIngredients(inventory);

        if (ingredients != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i) instanceof ItemStack) {
                    ItemStack stack = ((ItemStack) ingredients.get(i)).copy();
                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item == Item.getItemFromBlock(BWMBlocks.WOLF)) {
                            this.worldObj.playSound(null, pos, SoundEvents.ENTITY_WOLF_DEATH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            break;
                        }
                    }
                }
            }

            ItemStack[] output = mill.craftItem(inventory);

            assert (output != null && output.length > 0);

            for (int i = 0; i < output.length; i++) {
                ItemStack stack = output[i].copy();
                if (stack != null)
                    ejectStack(stack);
            }
            return true;
        }
        return false;
    }

    private void validateContents() {
        int oldGrindType = getGrindType();
        int newGrindType = 0;
        List<Object> ingredients = CraftingManagerMill.getInstance().getValidCraftingIngredients(inventory);

        if (ingredients != null) {
            this.containsIngredientsToGrind = true;
            newGrindType = 1;
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredients.get(i) instanceof ItemStack) {
                    ItemStack stack = ((ItemStack) ingredients.get(i)).copy();
                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item == Item.getItemFromBlock(BWMBlocks.WOLF)) {
                            newGrindType = 3;
                            break;
                        }
                        if (item == Item.getItemFromBlock(Blocks.NETHERRACK)) {
                            newGrindType = 2;
                            break;
                        }
                    }
                }
            }
        } else {
            this.grindCounter = 0;
            this.containsIngredientsToGrind = false;
        }
        this.validateContents = false;
        if (oldGrindType != newGrindType) {
            this.grindType = newGrindType;
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("t", this.grindType);
        return new SPacketUpdateTileEntity(this.pos, 4, tag);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager mgr, SPacketUpdateTileEntity pkt) {
        if (!this.worldObj.isRemote)
            return;

        NBTTagCompound tag = pkt.getNbtCompound();
        if (tag.hasKey("t")) {
            if (tag.getInteger("t") != this.grindType)
                worldObj.scheduleBlockUpdate(pos, this.getBlockType(), this.getBlockType().tickRate(worldObj), 5);
            this.grindType = tag.getInteger("t");
        }
    }

    public String getName() {
        return "inv.mill.name";
    }


}
