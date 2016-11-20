package betterwithmods.blocks;

import betterwithmods.BWMBlocks;
import betterwithmods.BWMItems;
import betterwithmods.api.IMultiLocations;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockHemp extends BlockCrops implements IPlantable, IMultiLocations {
    public static final PropertyBool TOP = PropertyBool.create("top");

    public BlockHemp() {
        super();
        this.setCreativeTab(BWCreativeTabs.BWTAB);
        this.setDefaultState(getDefaultState().withProperty(TOP, false));

    }

    @Override
    public String[] getLocations() {
        return new String[]{"hemp_seed"};
    }

    @Override
    public boolean isMaxAge(IBlockState state) {
        return state.getValue(TOP);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return (world.getLight(pos) > 12 || world.canSeeSky(pos) || world.canSeeSky(pos.up()) || isBelowLightBlock(world, pos)) && (canBePlantedHere(world, pos) || canPlantGrowOnBlock(world.getBlockState(pos.down()).getBlock()));
    }

    private boolean isBelowLightBlock(World world, BlockPos pos) {
        return world.getBlockState(pos.up()).getBlock() instanceof BlockLight || world.getBlockState(pos.up(2)).getBlock() instanceof BlockLight;
    }

    public boolean canBePlantedHere(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), EnumFacing.UP, this);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        checkAndDropBlock(world, pos, state);
        BlockPos up = pos.up();

        int meta = state.getValue(AGE);
        boolean isTop = state.getValue(TOP);

        double growthChance = 30D;

        if (world.getBlockState(pos.up(2)).getBlock() instanceof BlockLight && world.isAirBlock(pos.up())) {
            if (world.getBlockState(pos.up(2)).getValue(BlockLight.ACTIVE))
                growthChance /= 1.5D;
        }
        if (world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down()))
            growthChance /= 1.33D;
        else if (world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), EnumFacing.UP, this))
            growthChance /= 1.2D;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState check = world.getBlockState(pos.offset(facing));
            if (check.getBlock() instanceof BlockCrops)
                growthChance /= 1.1D;
        }

        if (meta < 7) {
            if (world.getLightFromNeighbors(up) > 12) {
                if (rand.nextInt(MathHelper.floor(growthChance)) == 0)
                    world.setBlockState(pos, state.withProperty(AGE, meta + 1));
            }
        } else if (meta == 7 && world.isAirBlock(up) && !isTop) {
            if (world.getLightFromNeighbors(up) > 12) {
                if (rand.nextInt(MathHelper.floor(growthChance)) == 0)
                    world.setBlockState(up, state.withProperty(AGE, 7).withProperty(TOP, true));
            }
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, 0);
    }

    private boolean canPlantGrowOnBlock(Block block) {
        return block == this;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!canBlockStay(world, pos, state)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        int meta = state.getValue(AGE);
        return meta > 6 ? 2 : 0;
    }

    @Override
    protected Item getSeed() {
        return Item.getItemFromBlock(BWMBlocks.HEMP);
    }

    @Override
    protected Item getCrop() {
        return BWMItems.MATERIAL;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, TOP);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 8)
            return super.getStateFromMeta(7).withProperty(TOP, true);
        return super.getStateFromMeta(meta).withProperty(TOP, false);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();//super.getDrops(world, pos, state, fortune);
        int age = getAge(state);
        Random rand = world instanceof World ? ((World) world).rand : new Random();

        ret.add(new ItemStack(this.getItemDropped(state, rand, fortune), 1, this.damageDropped(state)));

        if (age >= getMaxAge()) {
            for (int i = 0; i < 1 + fortune; ++i) {
                if (rand.nextInt(2 * getMaxAge()) <= age) {
                    ret.add(new ItemStack(this.getSeed(), 1, 0));
                }
            }
        }
        return ret;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(TOP))
            return 8;
        return super.getMetaFromState(state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        int meta = state.getValue(AGE);
        return meta > 6 ? this.getCrop() : this.getSeed();
    }

}
