package betterwithmods.common.world.gen.village;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

/**
 * Created by tyler on 5/21/17.
 */
public class Church extends AbandonedVillagePiece {
    public Church() {
    }

    public Church(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox p_i45564_4_, EnumFacing facing) {
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = p_i45564_4_;
    }
    @Override
    public Class<?> getComponentClass() {
        return Church.class;
    }
    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
     * Mineshafts at the end, it adds Fences...
     */
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0) {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
        }

        IBlockState iblockstate = Blocks.COBBLESTONE.getDefaultState();
        IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
        IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 3, 0, 8, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 3, 10, 0, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 10, 3, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 10, 3, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 0, 4, 7, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 4, 4, 4, 7, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 3, 4, 8, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 3, 10, 4, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 3, 5, 7, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 0, 4, 9, 4, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, iblockstate, iblockstate, false);
        this.setBlockState(worldIn, iblockstate, 0, 11, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 4, 11, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 2, 11, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 2, 11, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 1, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 1, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 2, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 3, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 3, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 1, 1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 2, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 3, 1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate2, 1, 2, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate3, 3, 2, 7, structureBoundingBoxIn);
        this.placeTorch(worldIn, EnumFacing.SOUTH, 2, 4, 7, structureBoundingBoxIn);
        this.placeTorch(worldIn, EnumFacing.EAST, 1, 4, 6, structureBoundingBoxIn);
        this.placeTorch(worldIn, EnumFacing.WEST, 3, 4, 6, structureBoundingBoxIn);
        this.placeTorch(worldIn, EnumFacing.NORTH, 2, 4, 5, structureBoundingBoxIn);
        IBlockState iblockstate4 = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);

        for (int i = 1; i <= 9; ++i) {
            this.setBlockState(worldIn, iblockstate4, 3, i, 3, structureBoundingBoxIn);
        }

        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
        this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.NORTH);

        if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR) {
            this.setBlockState(worldIn, iblockstate1, 2, 0, -1, structureBoundingBoxIn);

            if (this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock() == Blocks.GRASS_PATH) {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, structureBoundingBoxIn);
            }
        }

        for (int k = 0; k < 9; ++k) {
            for (int j = 0; j < 5; ++j) {
                this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, k, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, iblockstate, j, -1, k, structureBoundingBoxIn);
            }
        }

        this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
        return true;
    }

    protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
        return 2;
    }

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
        return new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getInt(random, 0 + size, 1 + size));
    }



    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 5, 12, 9, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new StructureVillagePieces.Church(startPiece, p5, random, structureboundingbox, facing) : null;
    }
}
