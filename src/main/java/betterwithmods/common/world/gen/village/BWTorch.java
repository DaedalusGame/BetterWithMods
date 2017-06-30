package betterwithmods.common.world.gen.village;

import betterwithmods.common.world.BWMapGenVillage;
import betterwithmods.module.hardcore.HCVillages;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

/**
 * Created by primetoxinz on 6/30/17.
 */
public class BWTorch extends StructureVillagePieces.Torch {
    private BWMapGenVillage.VillageStatus status;

    public BWTorch() {
    }

    public BWTorch(BWStart start, int p_i45568_2_, Random rand, StructureBoundingBox p_i45568_4_, EnumFacing facing) {
        super(start, p_i45568_2_, rand, p_i45568_4_, facing);
        this.status = start.status;
    }

    public static StructureBoundingBox findPieceBox(BWStart start, List<StructureComponent> p_175856_1_, Random rand, int p_175856_3_, int p_175856_4_, int p_175856_5_, EnumFacing facing) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175856_3_, p_175856_4_, p_175856_5_, 0, 0, 0, 3, 4, 2, facing);
        return StructureComponent.findIntersecting(p_175856_1_, structureboundingbox) != null ? null : structureboundingbox;
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

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
        }
        IBlockState planks = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
        ;
        IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
        if (HCVillages.disableVillagerSpawning) {
            iblockstate = planks;
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.setBlockState(worldIn, iblockstate, 1, 0, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 1, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, iblockstate, 1, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, planks, 1, 3, 0, structureBoundingBoxIn);
        if (status == BWMapGenVillage.VillageStatus.NORMAL) {
            this.placeTorch(worldIn, EnumFacing.EAST, 2, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.NORTH, 1, 3, 1, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.WEST, 0, 3, 0, structureBoundingBoxIn);
            this.placeTorch(worldIn, EnumFacing.SOUTH, 1, 3, -1, structureBoundingBoxIn);
        }
        return true;
    }
}
