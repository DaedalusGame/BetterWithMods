package betterwithmods.common.world.gen.village;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Random;

/**
 * Created by tyler on 5/21/17.
 */
public abstract class AbandonedVillagePiece extends StructureVillagePieces.Village implements VillagerRegistry.IVillageCreationHandler {
    public AbandonedVillagePiece() {
    }

    public AbandonedVillagePiece(StructureVillagePieces.Start start, int type) {
        super(start, type);
    }


    @Override
    protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
        //NO-OP
//        super.spawnVillagers(worldIn,structurebb,x,y,z,count);
    }

    @Override
    protected void placeTorch(World p_189926_1_, EnumFacing p_189926_2_, int p_189926_3_, int p_189926_4_, int p_189926_5_, StructureBoundingBox p_189926_6_) {
        //NO-OP
    }

    @Override
    public abstract StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size);
}
