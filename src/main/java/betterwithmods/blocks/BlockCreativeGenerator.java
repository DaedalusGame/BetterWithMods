package betterwithmods.blocks;

import betterwithmods.api.block.IMechanical;
import betterwithmods.blocks.tile.gen.TileEntityCreativeGen;
import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 8/5/16.
 */
public class BlockCreativeGenerator extends BlockGen implements IMechanical {
    public BlockCreativeGenerator() {
        super(Material.WOOD, "creativeGenerator");
        GameRegistry.registerTileEntity(TileEntityCreativeGen.class,"creativeGenerator");
        setCreativeTab(BWCreativeTabs.BWTAB);
        setDefaultState(getDefaultState().withProperty(ISACTIVE, true));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{ISACTIVE});
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCreativeGen();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return getDefaultState().withProperty(ISACTIVE, true);
    }

    @Override
    public int getMechPowerLevelToFacing(World var1, BlockPos var2, EnumFacing var3) {
        return 4;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public ItemStack getGenStack(IBlockState var1) {
        return null;
    }

    @Override
    public EnumFacing getAxleDirection(World var1, BlockPos var2) {
        return null;
    }
}
