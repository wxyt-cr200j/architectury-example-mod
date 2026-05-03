package net.wxyttransit.client;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class JsonReader {
    private static final Gson GSON = new Gson();

    public static JsonObject readJson(String name) {
        return readJson(new ResourceLocation(name));
    }

    public static JsonObject readJson(ResourceLocation rl) {
        try {
            Minecraft client = Minecraft.getInstance();
            ResourceManager rm = client.getResourceManager();
            Optional<Resource> res = rm.getResource(rl);
            InputStream is = res.get().open();
            InputStreamReader r = new InputStreamReader(is);
            JsonObject json = GsonHelper.parse(r);
            ////System.out.println(json);
            return json.deepCopy();
        } catch (Exception e) {

            //System.out.println(1145141919);
            return GSON.fromJson("{}", JsonObject.class);
        }
    }

    public static JsonElement deepCopy(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return JsonNull.INSTANCE;
        }
        if (element.isJsonPrimitive()) {
            return element.deepCopy();
        } if (element.isJsonArray()) {
            JsonArray array = new JsonArray();
            for (JsonElement e : element.getAsJsonArray()) {
                array.add(deepCopy(e));
            }
            return array;
        }
        if (element.isJsonObject()) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                obj.add(entry.getKey(), deepCopy(entry.getValue()));
            }
            return obj;
        }
        throw new IllegalArgumentException("Unknown JsonElement type");
    }
}
    

