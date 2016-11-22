package betterwithmods.blocks;

import betterwithmods.BWMItems;
import betterwithmods.api.block.IMultiVariants;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
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
import net.minecraft.item.Item;
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

import java.util.Random;

public class BlockPlanter extends BWMBlock implements IMultiVariants {
    public static final PropertyEnum<EnumPlanterType> TYPE = PropertyEnum.create("plantertype", EnumPlanterType.class);

    public BlockPlanter() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(1.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumPlanterType.EMPTY));
    }

    @Override
    public String[] getVariants() {
        return new String[]{"plantertype=empty", "plantertype=dirt", "plantertype=grass", "plantertype=soul_sand", "plantertype=fertile", "plantertype=sand", "plantertype=water_still", "plantertype=gravel", "plantertype=red_sand"};
    }

    public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return (state.getValue(TYPE) == EnumPlanterType.GRASS && tintIndex > -1) ? world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D) : -1;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    public boolean isValidBlockStack(ItemStack stack) {
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
            if (item != ItemStack.EMPTY) {
                if (meta == 0 && (isValidBlockStack(item) || item.getItem() == Items.WATER_BUCKET))
                    return true;
                else if (meta == 1 && ((item.getItem() == Items.DYE && item.getItemDamage() == 15) || item.getItem() == BWMItems.FERTILIZER))
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
            if (stack != ItemStack.EMPTY) {
                boolean valid = false;

                if (stack.getItem() == Items.WATER_BUCKET) {
                    world.setBlockState(pos, planter.withProperty(TYPE, EnumPlanterType.WATER));
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
                        for (EnumPlanterType type : EnumPlanterType.values()) {
                            if (valid)
                                break;
                            if (type.getFill() != null && type.getFill() == block && stack.getItemDamage() == type.getBlockMeta()) {
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
            if (heldItem != ItemStack.EMPTY) {
                if (heldItem.getItem() == Items.DYE && heldItem.getItemDamage() == 15) {
                    world.setBlockState(pos, planter.withProperty(TYPE, EnumPlanterType.FERTILE));
                    if (!player.capabilities.isCreativeMode)
                        heldItem.shrink(1);
                    world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.25F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    return true;
                }
            }
        }
        if (meta != 6 && heldItem.getItem().getHarvestLevel(heldItem, "shovel", player, planter) > -1) {
            EnumPlanterType type = state.getValue(TYPE);
            if (!player.capabilities.isCreativeMode) {
                if (!player.inventory.addItemStackToInventory(new ItemStack(type.getFill(), 1, type.getBlockMeta())))
                    player.dropItem(new ItemStack(type.getFill(), 1, type.getBlockMeta()), false);
            }
            world.playSound(null, pos, type.getFill().getSoundType(state, world, pos, player).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(TYPE, EnumPlanterType.EMPTY));
            return true;
        } else if (meta == 2 && heldItem.getItem() instanceof ItemHoe) {
            world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            world.setBlockState(pos, state.withProperty(TYPE, EnumPlanterType.DIRT));
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
            world.setBlockState(pos, state.withProperty(TYPE, EnumPlanterType.EMPTY));
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
                            world.setBlockState(pos, this.getDefaultState().withProperty(TYPE, EnumPlanterType.GRASS));
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
                            world.setBlockState(pos, this.getDefaultState().withProperty(TYPE, EnumPlanterType.DIRT));
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
        return world.getBlockState(pos).getValue(TYPE) == EnumPlanterType.FERTILE;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing dir, IPlantable plant) {
        BlockPos up = pos.up();
        EnumPlantType plantType = plant.getPlantType(world, up);
        int meta = world.getBlockState(pos).getValue(TYPE).getMeta();
        switch (plantType) {
            case Desert:
            case Beach:
                return meta == 5 || meta == 8;
            case Cave:
                return meta == 7;
            case Crop:
                return meta == 1 || meta == 4;
            case Nether:
                return meta == 3;
            case Plains:
                return meta == 1 || meta == 2;
            case Water:
                return meta == 6;
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
        for (int i = 0; i < 9; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumPlanterType.byMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    public enum EnumPlanterType implements IStringSerializable {
        EMPTY("empty", null, 0, null),
        DIRT("dirt", Blocks.DIRT, 1, new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        GRASS("grass", Blocks.GRASS, 2, new EnumPlantType[]{EnumPlantType.Plains}),
        SOULSAND("soul_sand", Blocks.SOUL_SAND, 3, new EnumPlantType[]{EnumPlantType.Nether}),
        FERTILE("fertile", Blocks.DIRT, 4, new EnumPlantType[]{EnumPlantType.Crop, EnumPlantType.Plains}),
        SAND("sand", Blocks.SAND, 5, new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach}),
        WATER("water_still", Blocks.WATER, 6, new EnumPlantType[]{EnumPlantType.Water}),
        GRAVEL("gravel", Blocks.GRAVEL, 7, new EnumPlantType[]{EnumPlantType.Cave}),
        REDSAND("red_sand", Blocks.SAND, 1, 8, new EnumPlantType[]{EnumPlantType.Desert, EnumPlantType.Beach});
        private static final EnumPlanterType[] META_LOOKUP = new EnumPlanterType[values().length];

        static {
            for (EnumPlanterType types : values()) {
                META_LOOKUP[types.getMeta()] = types;
            }
        }

        private String name;
        private Block fill;
        private int meta;
        private int blockMeta;
        private EnumPlantType[] type;

        EnumPlanterType(String name, Block fill, int meta, EnumPlantType[] type) {
            this(name, fill, 0, meta, type);
        }

        EnumPlanterType(String name, Block fill, int blockMeta, int meta, EnumPlantType[] type) {
            this.name = name;
            this.fill = fill;
            this.blockMeta = blockMeta;
            this.meta = meta;
            this.type = type;
        }

        public static EnumPlanterType byMeta(int meta) {
            if (meta > 8)
                return EMPTY;
            return META_LOOKUP[meta];
        }

        @Override
        public String getName() {
            return name;
        }

        public Block getFill() {
            return fill;
        }

        public int getBlockMeta() {
            return blockMeta;
        }

        public int getMeta() {
            return meta;
        }

        public EnumPlantType[] getTypes() {
            return type;
        }
    }
}
