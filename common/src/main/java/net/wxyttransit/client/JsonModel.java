package net.wxyttransit.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class JsonModel extends EntityModel<Entity> {
    private JsonObject jsonObject;
    private ModelMapper model;
    public JsonModel(JsonObject obj){
        jsonObject = obj.deepCopy();
        this.load();
    }
    public void load(){

        int texW,texH;
        texW = jsonObject.get("texture_size").getAsJsonArray().get(0).getAsInt();
        texH = jsonObject.get("texture_size").getAsJsonArray().get(1).getAsInt();

        ModelDataWrapper wrapper = new ModelDataWrapper(this,texW,texH);
        model = new ModelMapper(wrapper);
        jsonObject.getAsJsonArray("elements").forEach(element->{
            JsonObject eleObj = element.getAsJsonObject();
            ModelMapper child = new ModelMapper(wrapper);

        });

    }
    @Override
    public void setupAnim(Entity entity, float f, float g, float h, float i, float j) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {


    }
    private static <T> void getArrayFromValue(T[] array, JsonObject jsonObject, String key, Function<JsonElement, T> function) {
        if (jsonObject.has(key)) {
            JsonArray jsonArray = jsonObject.getAsJsonArray(key);

            for(int i = 0; i < array.length; ++i) {
                array[i] = function.apply(jsonArray.get(i));
            }
        }

    }
}
