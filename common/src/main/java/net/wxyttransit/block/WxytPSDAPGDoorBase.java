package net.wxyttransit.block;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassEndBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.wxyttransit.WxytItems;
import net.wxyttransit.gui.GUIPSDAPGDoorSettings;
import org.jetbrains.annotations.NotNull;

public abstract class WxytPSDAPGDoorBase extends BlockPSDAPGDoorBase implements EntityBlockMapper {

	public static final int MAX_OPEN_VALUE = 32;

	public static final BooleanProperty END = BooleanProperty.create("end");
	public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");
	public static final BooleanProperty TEMP = BooleanProperty.create("temp");

	@Override
	public @NotNull RenderShape getRenderShape(BlockState state){
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (IBlock.getSideDirection(state) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			final BlockState superState = super.updateShape(state, direction, newState, world, pos, posFrom);
			if (superState.getBlock() == Blocks.AIR) {
				return superState;
			} else {
				final boolean end = world.getBlockState(pos.relative(IBlock.getSideDirection(state).getOpposite())).getBlock() instanceof BlockPSDAPGGlassEndBase;
				return superState.setValue(END, end);
			}
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos offsetPos = pos;
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			offsetPos = offsetPos.below();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.relative(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos) {
		final BlockEntity entity = world.getBlockEntity(pos);
		if (IBlock.getStatePropertySafe(state, UNLOCKED) && entity instanceof TileEntityPSDAPGDoorBase) {
			((TileEntityPSDAPGDoorBase) entity).setOpen(0);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(player.getMainHandItem().is( WxytItems.WXYT_BRUSH.get())){
			BlockEntity entity = world.getBlockEntity(pos);

			if(entity instanceof WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase base&&world.isClientSide) {
				Minecraft.getInstance().setScreen(new GUIPSDAPGDoorSettings<>(Component.translatable("gui.wxyttransit.psdapgsettings"),base ));
			}
		}
		return IBlock.checkHoldingBrush(world, player, () -> {
			final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.above(y));
				if (state.is(scanState.getBlock())) {
					lockDoor(world, pos.above(y), scanState, !unlocked);
				}
			}
			player.displayClientMessage(!unlocked ? Text.translatable("gui.mtr.psd_apg_door_unlocked") : Text.translatable("gui.mtr.psd_apg_door_locked"), true);
		});
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
		final BlockEntity entity = world.getBlockEntity(pos);
		return entity instanceof TileEntityPSDAPGDoorBase && ((TileEntityPSDAPGDoorBase) entity).isOpen() ? Shapes.empty() : super.getCollisionShape(state, world, pos, collisionContext);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(END, FACING, HALF, SIDE, TEMP, UNLOCKED);
	}

	private static void lockDoor(Level world, BlockPos pos, BlockState state, boolean unlocked) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final BlockPos leftPos = pos.relative(facing.getCounterClockWise());
		final BlockPos rightPos = pos.relative(facing.getClockWise());
		final BlockState leftState = world.getBlockState(leftPos);
		final BlockState rightState = world.getBlockState(rightPos);

		if (leftState.is(state.getBlock())) {
			final BlockState toggled = leftState.setValue(UNLOCKED, unlocked);
			world.setBlockAndUpdate(leftPos, toggled);
		}

		if (rightState.is(state.getBlock())) {
			final BlockState toggled = rightState.setValue(UNLOCKED, unlocked);
			world.setBlockAndUpdate(rightPos, toggled);
		}

		world.setBlockAndUpdate(pos, state.setValue(UNLOCKED, unlocked));
	}

	public static abstract class TileEntityPSDAPGDoorBase extends BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase implements IGui {

		private int open;
		private float openClient;
		private boolean temp = true;
		public Integer type = 0;
		public Integer light_type = 1;
		public String district = "shenzhen";
		public String lightDistrict = "shenzhen";
		private static final String KEY_OPEN = "open";
		private static final String KEY_TEMP = "temp";

		public TileEntityPSDAPGDoorBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			open = compoundTag.getInt(KEY_OPEN);
			temp = compoundTag.getBoolean(KEY_TEMP);
			type = compoundTag.getInt("type");
			light_type = compoundTag.getInt("light_type");
			district = compoundTag.getString("district");
			lightDistrict = compoundTag.getString("light_district");
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putInt(KEY_OPEN, open);
			compoundTag.putBoolean(KEY_TEMP, temp);
			compoundTag.putInt("type",type);
			compoundTag.putInt("light_type",light_type);
			compoundTag.putString("district",district);
			compoundTag.putString("light_district",lightDistrict);
			if (temp && level != null) {
				level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(TEMP, false));
				temp = false;
			}
		}

		public AABB getRenderBoundingBox() {
			return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		}

		public void setOpen(int open) {
			if (open != this.open) {
				this.open = open;
				setChanged();
				syncData();
				if (open == 1 && level != null) {
					level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(TEMP, false));
				}
				//System.out.println(open);
			}
		}

		public float getOpen(float lastFrameDuration) {
			final float change = lastFrameDuration * 0.95F;
			if (Math.abs(open - SMALL_OFFSET_16 * 2 - openClient) < change) {
				openClient = open - SMALL_OFFSET_16 * 2;
			} else if (openClient < open) {
				openClient += change;
			} else {
				openClient -= change;
			}
			return openClient / 32;
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
		public boolean isOpen() {
			return open > 0;
		}
	}
}