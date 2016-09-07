package betterwithmods.blocks;

import betterwithmods.client.BWCreativeTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

/**
 * @author mrebhan
 */

public class BlockPlatform extends Block {

	public BlockPlatform() {
		super(Material.WOOD);
		this.setUnlocalizedName("bwm:platform");
		setCreativeTab(BWCreativeTabs.BWTAB);
		this.setHardness(2F);
		this.setSoundType(SoundType.WOOD);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
