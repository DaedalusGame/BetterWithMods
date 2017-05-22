package betterwithmods.common.world.gen.village;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

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
    protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) { }
}
