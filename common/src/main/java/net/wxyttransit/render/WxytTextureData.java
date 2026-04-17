package net.wxyttransit.render;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class WxytTextureData {
    private HashMap<String, BiConsumer<DataObject,GraphicsTexture>> map = new HashMap<>();

  //  private HashMap<Pair<String,DataObject>,GraphicsTexture> textureHashMap = new HashMap<>();
    public static WxytTextureData data=new WxytTextureData();
    public int textureScale = 1024;
    public WxytTextureData(){

    }
    public void addRenderConsumer(String string,BiConsumer<DataObject,GraphicsTexture> consumer){
        map.put(string,consumer);
       // System.out.println(map);
    }
    public BiConsumer<DataObject,GraphicsTexture> getRenderConsumer(String string){
        //System.out.println(string);
        if(map.containsKey(string))return map.get(string);
        else return (DataObject,GraphicsTexture) ->{};
    }
    public GraphicsTexture getTexture(String string,DataObject obj,int w,int h){
        GraphicsTexture gt = new GraphicsTexture(w,h);
        getRenderConsumer(string).accept(obj,gt);
        return gt;
    }
    public ResourceLocation getTextureLocation(String string, DataObject obj, int w, int h){
        return getTexture(string, obj, w, h).identifier;
    }
}
