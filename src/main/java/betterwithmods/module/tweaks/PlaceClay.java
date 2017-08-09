package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockUnfiredPottery;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import betterwithmods.module.Feature;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlaceClay extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Clay and Nether Sludge are placeable blocks.";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        KilnManager.INSTANCE.addRecipe(new KilnRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.BRICK.getMeta(), Lists.newArrayList(new ItemStack(Items.BRICK))){
            @Override
            public ItemStack getStack() {
                return new ItemStack(Items.CLAY_BALL);
            }
        });
        KilnManager.INSTANCE.addRecipe(new KilnRecipe(BWMBlocks.UNFIRED_POTTERY, BlockUnfiredPottery.EnumType.NETHER_BRICK.getMeta(), Lists.newArrayList(new ItemStack(Items.NETHERBRICK))){
            @Override
            public ItemStack getStack() {
                return ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE);
            }
        });
    }

    @SubscribeEvent
    public void onPlaceClay(PlayerInteractEvent.RightClickBlock event)
    {
        if(event.isCanceled())
            return;

        World worldIn = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        EnumFacing facing = event.getFace();
        BlockPos pos = event.getPos();
        EnumHand hand = event.getHand();

        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);
        IBlockState stateToPlace = null;

        if(itemstack.isEmpty())
            return;
        if(itemstack.getItem() == Items.CLAY_BALL)
            stateToPlace = BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.TYPE, BlockUnfiredPottery.EnumType.BRICK);
        if(itemstack.getItem() == BWMItems.MATERIAL && itemstack.getMetadata() == ItemMaterial.EnumMaterial.NETHER_SLUDGE.ordinal())
            stateToPlace = BWMBlocks.UNFIRED_POTTERY.getDefaultState().withProperty(BlockUnfiredPottery.TYPE, BlockUnfiredPottery.EnumType.NETHER_BRICK);

        if (stateToPlace != null && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(stateToPlace.getBlock(), pos, false, facing, null))
        {
            if (placeBlockAt(itemstack, player, worldIn, pos, stateToPlace))
            {
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if(!player.isCreative())
                    itemstack.shrink(1);
            }

            event.setCancellationResult(EnumActionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState newState)
    {
        if (!world.setBlockState(pos, newState, 11)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == newState.getBlock())
        {
            newState.getBlock().onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
