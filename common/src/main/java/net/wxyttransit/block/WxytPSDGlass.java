package net.wxyttransit.block;

import mtr.Items;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wxyttransit.WxytBlockEntityTypes;
import net.wxyttransit.WxytItems;
import org.jetbrains.annotations.Nullable;

public class WxytPSDGlass extends WxytPSDAPGGlassBase {

	private final int style;

	public WxytPSDGlass(int style) {
		super();
		this.style = style;
	}

	@Override
	public Item asItem() {
		return WxytItems.WXYT_PSD_DOOR_ITEM.get();
	}
	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new WxytPSDGlass.TileEntityPSDGlass( pos, state);
	}




	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new WxytPSDGlass.TileEntityPSDGlass(blockPos,blockState);
	}

	public static class TileEntityPSDGlass extends WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase {
		public TileEntityPSDGlass( BlockPos pos, BlockState state) {
			super( WxytBlockEntityTypes.PSD_GLASS_0_TILE_ENTITY.get() , pos, state);
		}
		public TileEntityPSDGlass(int style, BlockPos pos, BlockState state) {
			super( WxytBlockEntityTypes.PSD_GLASS_0_TILE_ENTITY.get() , pos, state);
		}
	}
}
