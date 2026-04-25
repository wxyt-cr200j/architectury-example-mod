package net.wxyttransit.block;

import mtr.block.BlockPSDAPGGlassBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.wxyttransit.WxytItems;
import net.wxyttransit.gui.GUIPSDAPGDoorSettings;
import net.wxyttransit.gui.GUIPSDAPGGlassSettings;
import org.jetbrains.annotations.NotNull;

public abstract class WxytPSDAPGGlassBase extends BlockPSDAPGGlassBase implements EntityBlockMapper {

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(player.getMainHandItem().is( WxytItems.WXYT_BRUSH.get())){
			BlockEntity entity = world.getBlockEntity(pos);

			if(entity instanceof WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase base&&world.isClientSide) {
				Minecraft.getInstance().setScreen(new GUIPSDAPGGlassSettings<>(Component.translatable("gui.wxyttransit.psdapgsettings"),base ));
			}
		}
		return IBlock.checkHoldingBrush(world, player, () -> {
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.above(y));
				if (state.is(scanState.getBlock())) {
					connectGlass(world, pos.above(y), scanState);
				}
			}
		});
	}
	@Override
	public @NotNull RenderShape getRenderShape(BlockState state){
		return RenderShape.INVISIBLE;
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SIDE_EXTENDED);
	}

	private void connectGlass(Level world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos leftPos = pos.relative(facing.getCounterClockWise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = state.is(leftState.getBlock());

		if (leftValid) {
			final EnumSide side = IBlock.getStatePropertySafe(leftState, SIDE_EXTENDED);
			EnumSide newLeftSide;

			if (side == EnumSide.RIGHT) {
				newLeftSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newLeftSide = EnumSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockAndUpdate(leftPos, leftState.setValue(SIDE_EXTENDED, newLeftSide));
		}

		final BlockPos rightPos = pos.relative(facing.getClockWise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = state.is(rightState.getBlock());

		if (rightValid) {
			final EnumSide side = IBlock.getStatePropertySafe(rightState, SIDE_EXTENDED);
			EnumSide newRightSide;

			if (side == EnumSide.LEFT) {
				newRightSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newRightSide = EnumSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockAndUpdate(rightPos, rightState.setValue(SIDE_EXTENDED, newRightSide));
		}

		EnumSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumSide.LEFT;
		} else {
			newSide = EnumSide.SINGLE;
		}

		world.setBlockAndUpdate(pos, state.setValue(SIDE_EXTENDED, newSide));
	}
	public static abstract class TileEntityPSDAPGGlassBase extends BlockEntityMapper implements IGui {

		public Integer type = 0;
		public String district = "shenzhen";

		public TileEntityPSDAPGGlassBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			type = compoundTag.getInt("type");
			district = compoundTag.getString("district");
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt("type",type);
			compoundTag.putString("district",district);

		}

		public AABB getRenderBoundingBox() {
			return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}


		public void refresh(){
			setChanged();



			if (level != null) {
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE); // 触发更新包发送
			}
			//syncData(this);
			//	load(saveWithoutMetadata());
			//load(saveWithFullMetadata());
		}
	}
}
