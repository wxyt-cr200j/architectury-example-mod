package net.wxyttransit.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.MTRClient;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.wxyttransit.block.WxytPSDAPGGlassBase;
import net.wxyttransit.block.WxytPSDAPGGlassBase;
import net.wxyttransit.client.ModelGetter;
import net.wxyttransit.client.SzmetroBbModel;

import static net.wxyttransit.WxytTransit.MOD_ID;

public class RenderWxytPSDAPGGlass<T extends WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase> extends BlockEntityRendererMapper<T> implements IGui, IBlock {

	private int type;
	private int lightType;
	private String district;
	private String lightDistrict;

	public RenderWxytPSDAPGGlass(BlockEntityRenderDispatcher dispatcher, String district, int type, int lightType) {
		super(dispatcher);
		this.type = type;
        this.lightType = lightType;
        this.district = district;
		this.lightDistrict = district;
	}

	@Override
	public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		////System.out.println("1145141919810");
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		T base = entity;
			if(base.district!=null)district = base.district;
			if(base.type!=null)type = base.type;

		final BlockPos pos = entity.getBlockPos();
		final BlockState state=entity.getBlockState();
		final Direction facing = IBlock.getStatePropertySafe(world, pos, WxytPSDAPGGlassBase.FACING);
		//String loc =String.format("wxyttransit:textures/block/psd_door_shenzhen_%s_%s.png",state.getValue(WxytPSDAPGGlassBase.HALF).toString().toLowerCase(),state.getValue(WxytPSDAPGGlassBase.SIDE).toString().toLowerCase());
		String loc1 =String.format("wxyttransit:models/block/psd_glass_%s_%s_%s_%s.bbmodel",district,state.getValue(WxytPSDAPGGlassBase.HALF).toString().toLowerCase(),state.getValue(WxytPSDAPGGlassBase.SIDE_EXTENDED).toString().toLowerCase(),type);
		final BakedModel model= ModelGetter.getBakedModel(state,pos);
		/*if (IBlock.getStatePropertySafe(world, pos, WxytPSDAPGGlassBase.TEMP)) {
			return;
		}*/



		/*PoseStack poseStack=new PoseStack();
		poseStack.pushPose();
		UtilitiesClient.rotateYDegrees(poseStack,-facing.toYRot());
		poseStack.translate(open  * (side ? -1 : 1), 0, 0);
		UtilitiesClient.rotateYDegrees(poseStack,facing.toYRot());
		matrices.pushPose();
		matrices.mulPoseMatrix(poseStack.last().pose());*/


		//Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrices.last(),vertexConsumers.getBuffer(RenderType.cutout()),state,model,1,1,1,light,overlay);
		//Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrices.last(),vertexConsumers.getBuffer(open>0?MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"),false):MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/white.png"))),state,modelLight,1,1,1,light,overlay);

		/*matrices.popPose();
		poseStack.popPose();
		poseStack.pushPose();
		UtilitiesClient.rotateYDegrees(poseStack,-facing.toYRot());
		poseStack.translate(8, 32, 0);
		UtilitiesClient.rotateYDegrees(poseStack,facing.toYRot());
		matrices.pushPose();
		matrices.mulPoseMatrix(poseStack.last().pose());*/


		//Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matrices.last(),vertexConsumers.getBuffer(open>0?MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"),false):MoreRenderLayers.getExterior(new ResourceLocation("mtr:textures/block/white.png"))),state,modelLight,1,1,1,light,overlay);
/*
		matrices.popPose();
		poseStack.popPose();*/
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			// 1. 移到方块中心（修复偏移 0.5 格）
			matricesNew.translate(
					entity.getBlockPos().getX() + 0.5,
					entity.getBlockPos().getY(),
					entity.getBlockPos().getZ() + 0.5
			);
			int rot = (int)(facing.toYRot());
			// 2. 正确旋转方向（修复南北背对背）
			switch (rot){
				case 0:{UtilitiesClient.rotateYDegrees(matricesNew,180);break;}
				case 90:{UtilitiesClient.rotateYDegrees(matricesNew,90);break;}
				case 180:{UtilitiesClient.rotateYDegrees(matricesNew,0);break;}
				case 270:{UtilitiesClient.rotateYDegrees(matricesNew,270);break;}
				//default:UtilitiesClient.rotateYDegrees(matricesNew,0);
			}

			// 3. 不需要任何额外偏移！删掉你注释那行
		});
		final StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
		final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
		SzmetroBbModel model1=SzmetroBbModel.get(new ResourceLocation(loc1));
		////System.out.println(type);
		//Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state,mat,vertexConsumers,light,overlay);
		RenderTrains.scheduleRender(new ResourceLocation(String.format("wxyttransit:textures/block/psd_glass_%s_%s_%s_%s.png",district,state.getValue(WxytPSDAPGGlassBase.HALF).toString().toLowerCase(),state.getValue(WxytPSDAPGGlassBase.SIDE_EXTENDED).toString().toLowerCase(),type)), false,RenderTrains.QueuedRenderLayer.EXTERIOR, (matricesNew, vertexConsumer) -> {

			//storedMatrixTransformationsLight.transform(matricesNew);

			//UtilitiesClient.rotateYDegrees(poseStack1,-facing.toYRot());

			//UtilitiesClient.rotateYDegrees(poseStack1,facing.toYRot());
			//matricesNew.pushPose();
			storedMatrixTransformations.transform(matricesNew);
			//Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matricesNew.last(),vertexConsumer,state,model,1,1,1,light,overlay);

		//	Minecraft.getInstance().getTextureManager().bindForSetup(new ResourceLocation(loc));
			if(model1!=null)model1.render(matricesNew,light,vertexConsumer);


			matricesNew.popPose();

			//matricesNew.translate(open  * (side ? -1 : 1), 0, 0);
			//Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(matricesNew.last(),vertexConsumer,state,model,1,1,1,light,overlay);

			//RenderBlockModel.renderBakedModel(matricesNew, vertexConsumer,world,state,pos,model);
			//Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state,mat,vertexConsumers,light,overlay);
			//matricesNew.popPose();
		});
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	private static class ModelSingleCube extends EntityModel<Entity> {

		private final ModelMapper cube;

		private ModelSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int length, int height, int depth) {
			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
			cube = new ModelMapper(modelDataWrapper);
			cube.texOffs(0, 0).addBox(x - 8, y - 16, z - 8, length, height, depth, 0, false);
			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			cube.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			cube.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}

	private static class ModelAPGDoorBottom extends EntityModel<Entity> {

		private final ModelMapper bone;

		private ModelAPGDoorBottom() {
			final int textureWidth = 34;
			final int textureHeight = 27;

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			bone = new ModelMapper(modelDataWrapper);
			bone.texOffs(0, 0).addBox(-8, -16, -7, 16, 16, 1, 0, false);
			bone.texOffs(0, 17).addBox(-8, -6, -8, 16, 6, 1, 0, false);

			final ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
			cube_r1.setPos(0, -6, -8);
			bone.addChild(cube_r1);
			cube_r1.setRotationAngle(-0.7854F, 0, 0);
			cube_r1.texOffs(0, 24).addBox(-8, -2, 0, 16, 2, 1, 0, false);

			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			bone.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			bone.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}

	private static class ModelAPGDoorLight extends EntityModel<Entity> {

		private final ModelMapper bone;

		private ModelAPGDoorLight() {
			final int textureWidth = 8;
			final int textureHeight = 8;

			final ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);

			bone = new ModelMapper(modelDataWrapper);
			bone.texOffs(0, 4).addBox(-0.5F, -9, -7, 1, 1, 3, 0.05F, false);

			final ModelMapper cube_r1 = new ModelMapper(modelDataWrapper);
			cube_r1.setPos(0, -9.05F, -4.95F);
			bone.addChild(cube_r1);
			cube_r1.setRotationAngle(0.3927F, 0, 0);
			cube_r1.texOffs(0, 0).addBox(-0.5F, 0.05F, -3.05F, 1, 1, 3, 0.05F, false);

			modelDataWrapper.setModelPart(textureWidth, textureHeight);
			bone.setModelPart();
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
			bone.render(matrices, vertices, 0, 0, 0, packedLight, packedOverlay);
		}

		@Override
		public void setupAnim(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		}
	}
}
