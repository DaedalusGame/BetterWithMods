package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HempSeed extends Feature {

    boolean hoeForSeeds;
    boolean tallgrassForSeeds;
    @Override
    public void setupConfig() {
        hoeForSeeds = loadPropBool("Hoe for seeds", "Hemp Seeds drop when tilling ground with a hoe", true);
        tallgrassForSeeds = loadPropBool("Tall grass Hemp Seeds", "Hemp Seeds will drop when breaking Tall Grass",true);
    }

    @Override
    public String getFeatureDescription() {
        return "Controls how Hemp Seeds are gained";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if(tallgrassForSeeds)
            MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1, 0), 5);
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @SubscribeEvent
    public void onHoe(UseHoeEvent e) {
        if(!hoeForSeeds)
            return;
        World world = e.getWorld();
        if(!world.isRemote){
            BlockPos pos = e.getPos();
            if(world.isAirBlock(pos.up())){
                IBlockState state = world.getBlockState(pos);
                if(state.getBlock() instanceof BlockGrass && world.rand.nextFloat() >= 0.95F){
                    InvUtils.ejectStackWithOffset(world,pos, new ItemStack(BWMBlocks.HEMP, 1, 0));
                }
            }
        }
    }
}
