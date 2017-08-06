package betterwithmods.common.entity;

import betterwithmods.BWMod;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;

public class EntityJungleSpider extends EntityCaveSpider {
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation(BWMod.MODID, "entity/jungle_spider"));

    public EntityJungleSpider(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn))
        {
            if (entityIn instanceof EntityLivingBase)
            {
                int duration = 0;

                if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    duration = 7;
                }
                else if (this.world.getDifficulty() == EnumDifficulty.HARD)
                {
                    duration = 15;
                }

                if (duration > 0)
                {
                    ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.HUNGER, duration * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.BLOCK, blockpos) > 7)
        {
            return false;
        }

        return true;
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos pos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        IBlockState topBlock = world.getBlockState(world.getHeight(pos).down());

        if(topBlock.getBlock() == Blocks.LEAVES && topBlock.getValue(BlockLeaves.DECAYABLE)) {
            this.setPosition(posX,world.getHeight(pos).getY() - 2 - rand.nextInt(32),posZ);
            return super.getCanSpawnHere();
        }

        return false;
    }
}
