package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by tyler on 5/21/17.
 */
public class HCSeeds extends Feature {
    private static final Random RANDOM = new Random();
    public static Set<IBlockState> BLOCKS_TO_STOP = Sets.newHashSet();
    private static Predicate<IBlockState> STOP_SEEDS = state -> {
        Block block = state.getBlock();
        return BLOCKS_TO_STOP.contains(state) || block instanceof BlockTallGrass || (block instanceof BlockDoublePlant && (state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.GRASS || state.getValue(BlockDoublePlant.VARIANT) == BlockDoublePlant.EnumPlantType.FERN));
    };

    @Override
    public String getFeatureDescription() {
        return "Requires Tilling the ground with a hoe to get seeds.";
    }

    @SubscribeEvent
    public void onHarvest(BlockEvent.HarvestDropsEvent event) {
        if(STOP_SEEDS.test(event.getState()))
            event.getDrops().clear();
    }

    public NonNullList<ItemStack> getDrops(int fortune) {
        if (RANDOM.nextInt(8) != 0) return NonNullList.create();
        ItemStack seed = net.minecraftforge.common.ForgeHooks.getGrassSeed(RANDOM, 0);
        if (seed.isItemEqual(new ItemStack(Items.WHEAT_SEEDS)) || seed.isEmpty()) {
            return NonNullList.create();
        } else {
            return NonNullList.withSize(1, seed);
        }
    }

    @SubscribeEvent
    public void onHoe(UseHoeEvent e) {
        World world = e.getWorld();
        if (!world.isRemote) {
            BlockPos pos = e.getPos();
            if (world.isAirBlock(pos.up())) {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof BlockGrass || state.getBlock() instanceof BlockDirt) {
                    InvUtils.ejectStackWithOffset(world, pos.up(), getDrops(0));
                }
            }
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1), 5);
    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1), 5);
    }

    @SubscribeEvent
    public void mobDrop(LivingDropsEvent e) {
        Iterator<EntityItem> iter = e.getDrops().iterator();
        EntityItem item;
        while(iter.hasNext()) {
            item = iter.next();
            ItemStack stack = item.getItem();
            if(BWOreDictionary.hasSuffix(stack,"crop"))
                iter.remove();
        }

    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
