package net.wxyttransit.block;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wxyttransit.WxytBlockEntityTypes;
import net.wxyttransit.WxytItems;
import org.jetbrains.annotations.Nullable;

public class WxytPSDDoorOnly extends WxytPSDDoor{
    public WxytPSDDoorOnly(int style) {
        super(style);
    }
   @Override
    public Item asItem() {
        return  WxytItems.WXYT_PSD_DOOR_ITEM.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new WxytPSDDoorOnly.TileEntityPSDDoor(0,pos, state);
    }




    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new WxytPSDDoorOnly.TileEntityPSDDoor(blockPos,blockState);
    }

    public static class TileEntityPSDDoor extends WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase {
        public TileEntityPSDDoor( BlockPos pos, BlockState state) {
            super( WxytBlockEntityTypes.PSD_ONLY_DOOR_0_TILE_ENTITY.get() , pos, state);
        }
        public TileEntityPSDDoor(int style, BlockPos pos, BlockState state) {
            super( WxytBlockEntityTypes.PSD_ONLY_DOOR_0_TILE_ENTITY.get() , pos, state);
        }
    }
}
