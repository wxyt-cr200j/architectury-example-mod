package net.wxyttransit.client;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.jfr.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.logging.Level;

public class ModelGetter {
    @Nullable
    public static BakedModel getBakedModel(BlockState state, BlockPos pos){
        if(Platform.isFabric())if(!(Platform.getEnv() == EnvType.CLIENT))return null;
        if(Platform.isForge())if(!(Platform.getEnvironment() == Env.CLIENT))return null;
        if(state.isAir())return null;
        Minecraft mc = Minecraft.getInstance();
        ModelManager mm=mc.getModelManager();
        BlockModelShaper shaper=mm.getBlockModelShaper();
        return shaper.getBlockModel(state);
    }

    @Nullable
    public static BakedModel getBakedModel(ResourceLocation rl,String str){
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(rl,str));
    }

    public static BlockBenchModel getBlockBenchModel(ResourceLocation rl)
    {
        ////System.out.println(JsonReader.readJson(rl));
        ////System.out.println(1.14);
        return new BlockBenchModel((JsonReader.readJson(rl).deepCopy()));
    }
}
