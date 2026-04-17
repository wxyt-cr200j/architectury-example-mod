/*Thanks to xhg78999 :) */


package net.wxyttransit.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import mtr.mappings.ModelDataWrapper;
import mtr.mappings.ModelMapper;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;

public class BlockBenchModel extends EntityModel<Entity> {
    public final List<ModelMapper> parts = new ArrayList<>();
    public ModelMapper bone;

    public BlockBenchModel(String model) {
        this.parseBBModel(model);
    }
    public BlockBenchModel(JsonObject model){
        //System.out.println(14524);
        this.parseBBModel(model);
    }

    public void parseBBModel(String model)
    {
        this.parseBBModel(JsonParser.parseString(model).getAsJsonObject());
    }
    public void parseBBModel(JsonObject model) {
        //JsonObject model = (JsonObject)JsonParser.parseString(raw);

        try {
            JsonObject resolution = model.getAsJsonObject("resolution");
            int textureWidth = resolution.get("width").getAsInt();
            int textureHeight = resolution.get("height").getAsInt();
            ModelDataWrapper modelDataWrapper = new ModelDataWrapper(this, textureWidth, textureHeight);
            this.bone = new ModelMapper(modelDataWrapper);
            model.getAsJsonArray("elements").forEach((element) -> {
                JsonObject elementObject = element.getAsJsonObject();
                ModelMapper child = new ModelMapper(modelDataWrapper);
                Double[] origin = new Double[]{(double)0.0F, (double)0.0F, (double)0.0F};
                getArrayFromValue(origin, elementObject, "origin", JsonElement::getAsDouble);
                Double[] rotation = new Double[]{(double)0.0F, (double)0.0F, (double)0.0F};
                getArrayFromValue(rotation, elementObject, "rotation", JsonElement::getAsDouble);
                Integer[] uvOffset = new Integer[]{0, 0};
                getArrayFromValue(uvOffset, elementObject, "uv_offset", JsonElement::getAsInt);
                Double[] posFrom = new Double[]{(double)0.0F, (double)0.0F, (double)0.0F};
                getArrayFromValue(posFrom, elementObject, "from", JsonElement::getAsDouble);
                Double[] posTo = new Double[]{(double)0.0F, (double)0.0F, (double)0.0F};
                getArrayFromValue(posTo, elementObject, "to", JsonElement::getAsDouble);
                double inflate = elementObject.has("inflate") ? elementObject.get("inflate").getAsDouble() : (double)0.0F;
                boolean mirror = elementObject.has("shade") && !elementObject.get("shade").getAsBoolean();
                child.setPos(-origin[0].floatValue(), -origin[1].floatValue(), origin[2].floatValue());
                this.bone.addChild(child);
                child.setRotationAngle(-((float)Math.toRadians(rotation[0])), -((float)Math.toRadians(rotation[1])), (float)Math.toRadians(rotation[2]));
                child.texOffs(uvOffset[0], uvOffset[1]).addBox(origin[0].floatValue() - posTo[0].floatValue(), origin[1].floatValue() - posTo[1].floatValue(), posFrom[2].floatValue() - origin[2].floatValue(), Math.round(posTo[0].floatValue() - posFrom[0].floatValue()), Math.round(posTo[1].floatValue() - posFrom[1].floatValue()), Math.round(posTo[2].floatValue() - posFrom[2].floatValue()), (float)inflate, mirror);
            });
            modelDataWrapper.setModelPart(textureWidth, textureHeight);
            this.bone.setModelPart();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }
@Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bone.render(poseStack, buffer, 0.0F, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay);
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
