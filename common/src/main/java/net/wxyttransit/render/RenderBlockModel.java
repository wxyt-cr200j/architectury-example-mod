package net.wxyttransit.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static net.wxyttransit.client.ModelGetter.getBakedModel;

public class RenderBlockModel {

    public static void renderBakedModel(PoseStack poseStack, VertexConsumer consumer, BlockAndTintGetter level, BlockState state, BlockPos pos, BakedModel model) {
        boolean useAO=Minecraft.getInstance().options.ambientOcclusion().get();

        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher brd = mc.getBlockRenderer();
        ModelBlockRenderer bmr = brd.getModelRenderer();
        RandomSource rand = RandomSource.create();
        poseStack.pushPose();
        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
        if (useAO) {

            bmr.tesselateWithAO(level, model, state, pos, poseStack, consumer, true, rand, 42,OverlayTexture.NO_OVERLAY);
        } else {
            bmr.tesselateWithoutAO(level, model, state, pos, poseStack, consumer, true, rand, 42, OverlayTexture.NO_OVERLAY);
        } poseStack.popPose();

    }
    /**
     * 快捷调用：自动获取模型、光照
     */
    public static void renderBlockAt(PoseStack poseStack, VertexConsumer consumer, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;
        BakedModel model = getBakedModel(state,pos);
        int light = LevelRenderer.getLightColor(level, pos);
        renderBakedModel(poseStack, consumer, level, state, pos, model);
    }
}
