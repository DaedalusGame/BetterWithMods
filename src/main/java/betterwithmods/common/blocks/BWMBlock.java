package betterwithmods.common.blocks;

import betterwithmods.api.block.ITurnable;
import betterwithmods.client.BWCreativeTabs;
import betterwithmods.common.blocks.mini.BlockMini;
import betterwithmods.common.blocks.tile.TileBasic;
import betterwithmods.util.item.ToolsManager;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BWMBlock extends Block implements ITurnable {
    public BWMBlock(Material material) {
        super(material);
        setCreativeTab(BWCreativeTabs.BWTAB);
        if (material == Material.WOOD || material == BlockMini.MINI) {
            ToolsManager.setAxesAsEffectiveAgainst(this);
            this.setSoundType(SoundType.WOOD);
            this.setHarvestLevel("axe", 0);
        } else if (material == Material.ROCK) {
            this.setSoundType(SoundType.STONE);
            setHarvestLevel("pickaxe", 1);
            ToolsManager.setPickaxesAsEffectiveAgainst(this);
        }
    }

    @Override
    public EnumFacing getFacingFromBlockState(IBlockState state) {
        return null;
    }

    @Override
    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return null;
    }

    @Override
    public boolean canRotateOnTurntable(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateHorizontally(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canRotateVertically(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void rotateAroundYAxis(World world, BlockPos pos, boolean reverse) {
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileBasic) {
            ((TileBasic) worldIn.getTileEntity(pos)).onBreak();
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(worldIn,pos,state);
    }


}
