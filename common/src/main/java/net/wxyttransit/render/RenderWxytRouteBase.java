package net.wxyttransit.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderRouteBase;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.data.DataObject;

import java.util.Objects;

public abstract class RenderWxytRouteBase<T extends WxytPSDTop.TileEntityPSDTop> extends BlockEntityRendererMapper<T> implements IGui, IBlock {

	protected final float topPadding;
	protected final float bottomPadding;
	protected final float sidePadding;
	private final float z;
	private final boolean transparentWhite;
	private final Property<Integer> arrowDirectionProperty;
	private String type="sz_none";
	public RenderWxytRouteBase(BlockEntityRenderDispatcher dispatcher, float z, float topPadding, float bottomPadding, float sidePadding, boolean transparentWhite, Property<Integer> arrowDirectionProperty,String type) {
		super(dispatcher);
		this.z = z / 16;
		this.topPadding = topPadding / 16;
		this.bottomPadding = bottomPadding / 16;
		this.sidePadding = sidePadding / 16;
		this.transparentWhite = transparentWhite;
		this.arrowDirectionProperty = arrowDirectionProperty;
		this.type = type;
	}

@Override
	public final void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			matricesNew.translate(0.5 + entity.getBlockPos().getX(), entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
			UtilitiesClient.rotateYDegrees(matricesNew, -facing.toYRot());
		});

		renderAdditionalUnmodified(storedMatrixTransformations.copy(), state, facing, light);

		if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			final long platformId = entity.getPlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE);

			if (platformId != 0) {
				storedMatrixTransformations.add(matricesNew -> {
					matricesNew.translate(0, 1, 0);
					UtilitiesClient.rotateZDegrees(matricesNew, 180);
					matricesNew.translate(-0.5, -getAdditionalOffset(state), z);
				});

				final int leftBlocks = getTextureNumber(world, pos, facing, true);
				final int rightBlocks = getTextureNumber(world, pos, facing, false);
				final int color = getShadingColor(facing, ARGB_WHITE);

				if (!Objects.equals(type, "") &&type != null) {
					final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
					final float height = 1 - topPadding - bottomPadding;
					final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

					final ResourceLocation resourceLocation;
					resourceLocation=WxytTextureData.data.getTextureLocation(type,new DataObject(platformId), (int) (width*WxytTextureData.data.textureScale), (int) (height*WxytTextureData.data.textureScale));
					RenderTrains.scheduleRender(resourceLocation, false, RenderTrains.QueuedRenderLayer.EXTERIOR, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						IDrawing.drawTexture(matricesNew, vertexConsumer, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
						matricesNew.popPose();
					});
				}

				renderAdditional(storedMatrixTransformations, platformId, state, leftBlocks, rightBlocks, facing.getOpposite(), color, light);
			}
		}
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
	}

	protected float getAdditionalOffset(BlockState state) {
		return 0;
	}

	protected boolean isLeft(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.LEFT;
	}

	protected boolean isRight(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.RIGHT;
	}


	protected abstract void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light);

	private int getTextureNumber(BlockGetter world, BlockPos pos, Direction facing, boolean searchLeft) {
		int number = 0;
		final Block thisBlock = world.getBlockState(pos).getBlock();

		while (true) {
			final BlockState state = world.getBlockState(pos.relative(searchLeft ? facing.getCounterClockWise() : facing.getClockWise(), number));

			if (state.getBlock() == thisBlock) {
				final boolean isLeft = isLeft(state);
				final boolean isRight = isRight(state);

				if (number == 0 || (searchLeft ? !isRight : !isLeft)) {
					number++;
					if (searchLeft ? isLeft : isRight) {
						break;
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		return number - 1;
	}

	public static int getShadingColor(Direction facing, int grayscaleColorByte) {
		final int colorByte = Math.round((grayscaleColorByte & 0xFF) * (facing.getAxis() == Direction.Axis.X ? 0.75F : 1));
		return ARGB_BLACK | ((colorByte << 16) + (colorByte << 8) + colorByte);
	}
	protected enum RenderType {ARROW, ROUTE, NONE,THIS}
}
