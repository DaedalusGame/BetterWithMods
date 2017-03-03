package betterwithmods.integration.mcmultipart;

import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAxle;
import betterwithmods.common.blocks.mini.BlockCorner;
import betterwithmods.common.blocks.mini.BlockMoulding;
import betterwithmods.common.blocks.mini.BlockSiding;
import betterwithmods.util.DirUtils;
import mcmultipart.api.addon.IMCMPAddon;
import mcmultipart.api.addon.IWrappedBlock;
import mcmultipart.api.multipart.IMultipartRegistry;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.ref.MCMPCapabilities;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.EnumCornerSlot;
import mcmultipart.api.slot.EnumEdgeSlot;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 2/26/17
 */
//@MCMPAddon
public class MCMultipart implements IMCMPAddon {

    @Override
    public void registerParts(IMultipartRegistry registry) {
        MinecraftForge.EVENT_BUS.register(this);

        registerProxy(registry, BWMBlocks.WOOD_SIDING, this::siding).setPlacementInfo(this::getSidingState);
        registerProxy(registry, BWMBlocks.STONE_SIDING, this::siding).setPlacementInfo(this::getSidingState);

        registerProxy(registry, BWMBlocks.WOOD_MOULDING, this::moulding).setPlacementInfo(this::getMouldingState);
        registerProxy(registry, BWMBlocks.STONE_MOULDING, this::moulding).setPlacementInfo(this::getMouldingState);

        registerProxy(registry, BWMBlocks.WOOD_CORNER, this::corner).setPlacementInfo(this::getCornerState);
        registerProxy(registry, BWMBlocks.STONE_CORNER, this::corner).setPlacementInfo(this::getCornerState);

        registerProxy(registry, BWMBlocks.DIRT_SLAB, s -> EnumFaceSlot.DOWN);

//        registry.registerPartWrapper(BWMBlocks.AXLE, new PartAxle());
//        registry.registerStackWrapper(Item.getItemFromBlock(BWMBlocks.AXLE), i -> true, BWMBlocks.AXLE);

    }

    private IBlockState getCornerState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
                                       EntityLivingBase placer, EnumHand hand, IBlockState state) {
        return state;
    }

    private IBlockState getMouldingState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
                                         EntityLivingBase placer, EnumHand hand, IBlockState state) {
        double p = Math.abs(hitX * facing.getFrontOffsetX() + hitY * facing.getFrontOffsetY() + hitZ * facing.getFrontOffsetZ());
        if (p == 0.5) {
            int ori = DirUtils.getPlacementMeta("moulding", facing.getOpposite(), hitX, hitY, hitZ);
            return state.withProperty(BlockMoulding.ORIENTATION, ori);
        }
        return state;

    }

    private IBlockState getSidingState(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
                                       EntityLivingBase placer, EnumHand hand, IBlockState state) {
        boolean occupied = false;
        switch (facing.getAxis()) {
            case X:
                occupied = hitX == 0.5;
                break;
            case Y:
                occupied = hitY == 0.5;
                break;
            case Z:
                occupied = hitZ == 0.5;
                break;
        }
        if (occupied)
            return state.withProperty(BlockSiding.ORIENTATION, facing.ordinal());
        return state;
    }

    public IPartSlot corner(IBlockState state) {
        int o = state.getValue(BlockCorner.ORIENTATION);
        EnumCornerSlot[] order = new EnumCornerSlot[]{EnumCornerSlot.CORNER_NNP, EnumCornerSlot.CORNER_NNN, EnumCornerSlot.CORNER_PNP, EnumCornerSlot.CORNER_PNN, EnumCornerSlot.CORNER_NPP, EnumCornerSlot.CORNER_NPN, EnumCornerSlot.CORNER_PPP, EnumCornerSlot.CORNER_PPN, EnumCornerSlot.CORNER_NNN,};
        return order[o];
    }

    public IPartSlot siding(IBlockState state) {
        return EnumFaceSlot.VALUES[state.getValue(BlockSiding.ORIENTATION)];
    }

    public IPartSlot moulding(IBlockState state) {
        int o = state.getValue(BlockMoulding.ORIENTATION);
        EnumFacing[] facing = new EnumFacing[]{EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST};
        EnumFacing one = o < 4 ? EnumFacing.DOWN : o < 8 ? EnumFacing.UP : o < 10 ? EnumFacing.NORTH : EnumFacing.SOUTH;

        EnumFacing two = o < 8 ? facing[o % 4] : facing[o % 2];
        Optional<EnumEdgeSlot> slot = Optional.ofNullable(EnumEdgeSlot.fromFaces(one, two));
        return slot.orElse(EnumEdgeSlot.EDGE_XNN);
    }


    public IWrappedBlock registerProxy(IMultipartRegistry registry, Block block, Function<IBlockState, IPartSlot> placement, Function<IBlockState, IPartSlot> world) {
        registry.registerPartWrapper(block, new MultipartProxy(block, placement, world));
        return registry.registerStackWrapper(Item.getItemFromBlock(block), s -> true, block);
    }

    public IWrappedBlock registerProxy(IMultipartRegistry registry, Block block, Function<IBlockState, IPartSlot> wrapper) {
        return registerProxy(registry, block, wrapper, wrapper);
    }

    public static int getPowerLevel(World world, BlockPos pos, EnumFacing side) {
        if (MultipartHelper.getPart(world, pos, EnumCenterSlot.CENTER).orElse(null) instanceof PartAxle) {
            IBlockState state = MultipartHelper.getPartState(world, pos, EnumCenterSlot.CENTER).orElse(null);
            if (state != null && state.getValue(BlockAxle.AXIS).apply(side)) {
                return state.getValue(BlockAxle.SIGNAL);
            }
        }
        return 0;
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<TileEntity> e) {
        register(e, "miniblock");
    }

    private void register(AttachCapabilitiesEvent<TileEntity> e, String id) {
        e.addCapability(new ResourceLocation(BWMod.MODID, id), new ICapabilityProvider() {
            private PartTile tile;

            @Override
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                return capability == MCMPCapabilities.MULTIPART_TILE;
            }

            @Nullable
            @Override
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                if (capability == MCMPCapabilities.MULTIPART_TILE) {
                    if (tile == null) {
                        tile = new PartTile(e.getObject());
                    }

                    return MCMPCapabilities.MULTIPART_TILE.cast(tile);
                }

                return null;
            }
        });
    }

}
