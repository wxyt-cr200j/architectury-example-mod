package net.wxyttransit.block;

import mtr.BlockEntityTypes;
import mtr.Items;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDDoor;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wxyttransit.WxytBlockEntityTypes;
import net.wxyttransit.WxytItems;
import org.jetbrains.annotations.Nullable;

public class WxytPSDDoor extends WxytPSDAPGDoorBase {

	private final int style;

	public WxytPSDDoor(int style) {
		super();
		this.style = style;
		//System.out.println(new WxytPSDDoor(11) instanceof BlockPSDAPGDoorBase);
	}

	@Override
	public Item asItem() {
		return  WxytItems.WXYT_PSD_DOOR_ITEM.get();
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityPSDDoor(style, pos, state);
	}




	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TileEntityPSDDoor(blockPos,blockState);
	}

	public static class TileEntityPSDDoor extends WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase {
		public TileEntityPSDDoor( BlockPos pos, BlockState state) {
			super( WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get() , pos, state);
		}
		public TileEntityPSDDoor(int style, BlockPos pos, BlockState state) {
			super( WxytBlockEntityTypes.PSD_DOOR_0_TILE_ENTITY.get() , pos, state);
		}
	}
}
