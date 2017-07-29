package betterwithmods.module.hardcore;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockDirtSlab;
import betterwithmods.module.Feature;
import betterwithmods.util.player.PlayerHelper;
import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.UUID;

public class HCMovement extends Feature {
    public final static UUID PENALTY_SPEED_UUID = UUID.fromString("aece6a05-d163-4871-aaf3-ebab43b0fcfa");

    public static final HashMap<Material, Float> MATERIAL_MOVEMENT = Maps.newHashMap();
    public static final HashMap<IBlockState, Float> BLOCK_OVERRIDE_MOVEMENT = Maps.newHashMap();
    public static final float DEFAULT_SPEED = 0.75f;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MATERIAL_MOVEMENT.put(Material.ROCK, 1.1f);
        MATERIAL_MOVEMENT.put(Material.IRON, 1.1f);
        MATERIAL_MOVEMENT.put(Material.CLOTH, 1.0f);
        MATERIAL_MOVEMENT.put(Material.CARPET, 1.0f);
        MATERIAL_MOVEMENT.put(Material.GLASS, 1.0f);
        MATERIAL_MOVEMENT.put(Material.GROUND, 0.80f);
        MATERIAL_MOVEMENT.put(Material.CLAY, 0.75f);
        MATERIAL_MOVEMENT.put(Material.SAND, 0.75f);
        MATERIAL_MOVEMENT.put(Material.SNOW, 0.75f);
        MATERIAL_MOVEMENT.put(Material.LEAVES, 0.70f);
        MATERIAL_MOVEMENT.put(Material.PLANTS, 0.70f);
        MATERIAL_MOVEMENT.put(Material.VINE, 0.70f);
        MATERIAL_MOVEMENT.put(Material.GRASS, 0.75f);

        BLOCK_OVERRIDE_MOVEMENT.put(Blocks.GRASS_PATH.getDefaultState(), 1.1f);
        BLOCK_OVERRIDE_MOVEMENT.put(BWMBlocks.DIRT_SLAB.getDefaultState().withProperty(BlockDirtSlab.VARIANT, BlockDirtSlab.DirtSlabType.PATH), 1.1f);
    }


    @SubscribeEvent
    public void onWalk(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == Side.SERVER) {
            EntityPlayer player = event.player;
            if (player.onGround) {
                BlockPos blockpos = new BlockPos(MathHelper.floor(player.posX), MathHelper.floor(player.posY - 0.2D), MathHelper.floor(player.posZ));
                IBlockState state = player.world.getBlockState(blockpos);
                float speed;
                if (BLOCK_OVERRIDE_MOVEMENT.containsKey(state)) {
                    speed = BLOCK_OVERRIDE_MOVEMENT.get(state);
                } else if (MATERIAL_MOVEMENT.containsKey(state.getMaterial())) {
                    speed = MATERIAL_MOVEMENT.get(state.getMaterial());
                } else {
                    speed = DEFAULT_SPEED;
                }
                if (!player.world.getBlockState(player.getPosition()).getMaterial().isSolid()) {
                    state = player.world.getBlockState(player.getPosition());
                    if (BLOCK_OVERRIDE_MOVEMENT.containsKey(state)) {
                        speed = BLOCK_OVERRIDE_MOVEMENT.get(state);
                    } else if (MATERIAL_MOVEMENT.containsKey(state.getMaterial())) {
                        speed *= MATERIAL_MOVEMENT.get(state.getMaterial());
                    }
                }
                PlayerHelper.changeSpeed(player, "HCMovement", speed, PENALTY_SPEED_UUID);
            }
        }
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
