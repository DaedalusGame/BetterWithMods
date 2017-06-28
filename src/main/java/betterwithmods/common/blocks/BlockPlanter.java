package betterwithmods.common.blocks;

import betterwithmods.api.block.IMultiVariants;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.module.hardcore.HCBonemeal;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Random;

public class BlockPlanter extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("plantertype", EnumType.class);

    public BlockPlanter() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.EMPTY));
        this.setHarvestLevel("pickaxe", 0);
    }

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(BWMBlocks.PLANTER, 1, type.getMeta());
    }

    @Override
    public String[] getVariants() {
        return new String[]{"plantertype=empty", "plantertype=dirt", "plantertype=grass", "plantertype=soul_sand", "plantertype=fertile", "plantertype=sand", "plantertype=water_still", "plantertype=gravel", "plantertype=red_sand"};
    }

    public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return (state.getValue(TYPE) == EnumType.GRASS && tintIndex > -1) ? world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D) : -1;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    private boolean isValidBlockStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            return block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.SAND || block == Blocks.GRAVEL || block == Blocks.SOUL_SAND;
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState planter = world.getBlockState(pos);
        ItemStack heldItem = player.getHeldItem(hand);
        int meta = world.getBlockState(pos).getValue(TYPE).getMeta();
        if (world.isRemote) {
            ItemStack item = hand == EnumHand.MAIN_HAND ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
            if (!item.isEmpty()) {
                if (meta == 0 && (isValidBlockStack(item) || item.getItem() == Items.WATER_BUCKET))
                    return true;
                else if (meta == 1 && (HCBonemeal.FERTILIZERS.stream().anyMatch(item::isItemEqual)))
                    return true;
                else if (meta == 2 && item.getItem() instanceof ItemHoe)
                    return true;
                else if (meta != 0 && meta != 6 && item.getItem().getHarvestLevel(player.getHeldItem(hand), "shovel", player, planter) > -1)
                    return true;
                else if (meta == 6 && item.getItem() == Items.BUCKET)
                    return true;
            }
            return false;
        }

        if (meta == 0) {
            ItemStack stack = hand == EnumHand.MAIN_HAND ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
            if (!stack.isEmpty()) {
                boolean valid = false;

                if (stack.getItem() == Items.WATER_BUCKET) {
                    world.setBlockState(pos, planter.withProperty(TYPE, EnumType.WATER));
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                    ItemStack replacement = stack.getItem().getContainerItem(stack);
                    if (stack.getCount() == 1 && !player.capabilities.isCreativeMode) {
                        EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;
                        if (hand == EnumHand.OFF_HAND)
                            slot = EntityEquipmentSlot.OFFHAND;
                        player.setItemStackToSlot(slot, replacement);
                    } else {
                        if (!player.capabilities.isCreativeMode) {
                            if (!player.inventory.addItemStackToInventory(replacement)) {
                                InvUtils.ejectStackWithOffset(world, new BlockPos(player.serverPosX, player.serverPosY, player.serverPosZ), replacement);
                            }
                        }
                    }
                    valid = true;
                } else if (stack.getItem() instanceof ItemBlock) {
                    Block block = ((ItemBlock) stack.getItem()).getBlock();
                    if (this.isValidBlockStack(stack)) {
                        if (!player.capabilities.isCreativeMode)
                            stack.shrink(1);
                        for (EnumType type : EnumType.values()) {
                            if (valid)
                                break;
                            if (BWMRecipes.getStackFromState(type.getState()).isItemEqual(stack)) {
                                world.setBlockState(pos, state.withProperty(TYPE, type));
                                valid = true;
                            }
                        }
                    }
                    if (valid)
                        world.playSound(null, pos, block.getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
                }
                return valid;
            }
        } else if (meta == 1) {
            if (!heldItem.isEmpty()) {
                if (heldItem.getItem() == Items.DYE && heldItem.getItemDamage() == 15) {
                    world.setBlockState(pos, planter.withProperty(TYPE, EnumType.FERTILE));
                    if (!player.capabilities.isCreativeMode)
                        heldItem.shrink(1);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    return true;
                }
            }
        }
        if (meta != 6 && !heldItem.isEmpty() && heldItem.getItem().getHarvestLevel(heldItem, "shovel", player, planter) > -1) {
            EnumType type = state.getValue(TYPE);
            if (!player.capabilities.isCreativeMode) {
                if (!player.inventory.addItemStackToInventory(BWMRecipes.getStackFromState(type.getState())))
                    player.dropItem(BWMRecipes.getStackFromState(type.getState()), false);
            }
            world.playSound(null, pos, type.getState().getBlock().getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.EMPTY));
            return true;
        } else if (meta == 2 && heldItem.getItem() instanceof ItemHoe) {
            world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.DIRT));
            return true;
        } else if (meta == 6 && heldItem.getItem() == Items.BUCKET) {
            if (!player.capabilities.isCreativeMode) {
                if (heldItem.getCount() == 1) {
                    EntityEquipmentSlot slot = EntityEquipmentSlot.MAINHAND;
                    if (hand == EnumHand.OFF_HAND)
                        slot = EntityEquipmentSlot.OFFHAND;
                    player.setItemStackToSlot(slot, new ItemStack(Items.WATER_BUCKET));
                } else {
                    heldItem.shrink(1);
                    if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                        player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                }
            }
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.EMPTY));
            return true;
        }
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            int meta = world.getBlockState(pos).getValue(TYPE).getMeta();
            BlockPos up = pos.up();
            if (world.isAirBlock(up)) {
                if (meta == 1) {
                    if (world.getLight(up) > 8) {
                        int xP = rand.nextInt(3) - 1;
                        int yP = rand.nextInt(3) - 1;
                        int zP = rand.nextInt(3) - 1;
                        BlockPos checkPos = pos.add(xP, yP, zP);
                        if (world.getBlockState(checkPos).getBlock() == Blocks.GRASS)
                            world.setBlockState(pos, this.getDefaultState().withProperty(TYPE, EnumType.GRASS));
                    }
                } else if (meta == 2 && rand.nextInt(30) == 0) {
                    world.getBiome(pos).plantFlower(world, rand, up);
                    if (world.getLight(up) > 8) {
                        for (int i = 0; i < 4; i++) {
                            int xP = rand.nextInt(3) - 1;
                            int yP = rand.nextInt(3) - 1;
                            int zP = rand.nextInt(3) - 1;
                            BlockPos checkPos = pos.add(xP, yP, zP);
                            if (world.getBlockState(checkPos) == Blocks.DIRT && world.getBlockState(checkPos) == Blocks.DIRT.getDefaultState())
                                world.setBlockState(checkPos, Blocks.GRASS.getDefaultState());
                        }
                    }
                }
            } else if (world.getBlockState(up).getBlock() instanceof IPlantable) {
                IPlantable plant = (IPlantable) world.getBlockState(up).getBlock();
                if (this.canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, plant) && world.getBlockState(up).getBlock().getTickRandomly()) {
                    IBlockState cropState = world.getBlockState(up);
                    world.getBlockState(up).getBlock().updateTick(world, up, cropState, rand);
                    if (meta == 4) {
                        world.getBlockState(up).getBlock().updateTick(world, up, cropState, rand);
                        if (rand.nextInt(100) == 0)
                            world.setBlockState(pos, this.getDefaultState().withProperty(TYPE, EnumType.DIRT));
                    }
                }
            }
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return world.getBlockState(pos).getValue(TYPE) == EnumType.FERTILE;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing dir, IPlantable plant) {
        BlockPos up = pos.up();
        EnumPlantType plantType = plant.getPlantType(world, up);
        return dir == EnumFacing.UP && world.getBlockState(pos).getValue(TYPE).isType(plantType);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnumType type : EnumType.VALUES)
            items.add(getStack(type));

    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumType.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    public enum EnumType implements IStringSerializable {
        EMPTY("empty", Blocks.AIR.getDefaultState(), 0, new EnumPlantType[0]),
        DIRT("dirt", Blocks.DIRT.getDefaultState(), 1, new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        GRASS("grass", Blocks.GRASS.getDefaultState(), 2, new EnumPlantType[]{EnumPlantType.Plains}),
        SOULSAND("soul_sand", Blocks.SOUL_SAND.getDefaultState(), 3, new EnumPlantType[]{EnumPlantType.Nether}),
        FERTILE("fertile", Blocks.DIRT.getDefaultState(), 4, new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        SAND("sand", Blocks.SAND.getDefaultState(), 5, new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        WATER("water_still", Blocks.WATER.getDefaultState(), 6, new EnumPlantType[]{EnumPlantType.Water}),
        GRAVEL("gravel", Blocks.GRAVEL.getDefaultState(), 7, new EnumPlantType[]{EnumPlantType.Cave}),
        REDSAND("red_sand", Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), 8, new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach});
        private static final EnumType[] VALUES = values();

        private String name;
        private IBlockState state;
        private int meta;
        private EnumPlantType[] type;

        EnumType(String name, IBlockState state, int meta, EnumPlantType[] type) {
            this.name = name;
            this.state = state;
            this.meta = meta;
            this.type = type;
        }

        public static EnumType byMeta(int meta) {
            if (meta > 8)
                return EMPTY;
            return VALUES[meta];
        }

        @Override
        public String getName() {
            return name;
        }

        public int getMeta() {
            return meta;
        }

        public boolean isType(EnumPlantType type) {
            return this.type.length != 0 && Arrays.asList(this.type).contains(type);
        }

        public IBlockState getState() {
            return state;
        }
    }
}
