package betterwithmods.common.world.gen.village;

import betterwithmods.common.world.BWMapGenVillage;
import betterwithmods.module.hardcore.HCVillages;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

/**
 * Created by tyler on 5/25/17.
 */
public class BWStart extends StructureVillagePieces.Start {
    protected BWMapGenVillage.VillageStatus status;
    public BWStart() { }

    public BWStart(BWMapGenVillage.VillageStatus status, BiomeProvider chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<StructureVillagePieces.PieceWeight> p_i2104_6_, int p_i2104_7_) {
        super(chunkManagerIn, p_i2104_2_, rand, p_i2104_4_, p_i2104_5_, p_i2104_6_, p_i2104_7_);
        this.status = status;
    }

    @Override
    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        super.buildComponent(componentIn, listIn, rand);
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

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
        }

        IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
        if (HCVillages.disableAllComplexBlocks) {
            iblockstate1 = Blocks.PLANKS.getDefaultState();
        }

        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, iblockstate, Blocks.FLOWING_WATER.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 12, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 2, 12, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 3, 12, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 1, 13, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 1, 14, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 4, 13, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 4, 14, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 1, 13, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 1, 14, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 4, 13, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate1, 4, 14, 4, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, iblockstate, iblockstate, false);

        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 5; ++j) {
                if (j == 0 || j == 5 || i == 0 || i == 5) {
                    this.setBlockState(worldIn, iblockstate, j, 11, i, structureBoundingBoxIn);
                    this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
                }
            }
        }

        return true;
    }

}