package net.wxyttransit.render;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class WxytTextureData {
    public final HashMap<String, BiConsumer<DataObject,GraphicsTexture>> map = new HashMap<>();

    private final HashMap<Pair<String,Long>,GraphicsTexture> textureHashMap = new HashMap<>();
    private final HashSet<GraphicsTexture> resourcesNeedToRefresh=new HashSet<>();
    public static WxytTextureData data=new WxytTextureData();
    public int textureScale = 1024;
    public WxytTextureData(){

    }
    public void addRenderConsumer(String string,BiConsumer<DataObject,GraphicsTexture> consumer){
        map.put(string,consumer);
       // //System.out.println(map);
    }
    public BiConsumer<DataObject,GraphicsTexture> getRenderConsumer(String string){
        ////System.out.println(string);
        if(map.containsKey(string))return map.get(string);
        else return (DataObject,GraphicsTexture) ->{};
    }
    public GraphicsTexture getTexture(String string,DataObject obj,int w,int h,boolean forceRefresh){
        GraphicsTexture gt;
        if(textureHashMap.containsKey(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id))){
            gt=textureHashMap.get(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id));
            if(gt.isClosed()){
                //System.out.println(15623);
                gt=new GraphicsTexture(w,h);
                textureHashMap.put(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id),gt);
                resourcesNeedToRefresh.add(gt);
            }
            // //System.out.println(15);
        }else{
            gt=new GraphicsTexture(w,h);
            textureHashMap.put(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id),gt);
            resourcesNeedToRefresh.add(gt);
            // //System.out.println(16);
        }
        if(resourcesNeedToRefresh.contains(gt)||forceRefresh){
            getRenderConsumer(string).accept(obj,gt);
            if(resourcesNeedToRefresh.contains(gt))resourcesNeedToRefresh.remove(gt);
        }
        return gt;
    }
    public ResourceLocation getTextureLocation(String string, DataObject obj, int w, int h,boolean forceRefresh){
        return getTexture(string, obj, w, h,forceRefresh).identifier;
    }
    public GraphicsTexture getTexture(String string,DataObject obj,int w,int h){
        GraphicsTexture gt;
        if(textureHashMap.containsKey(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id))){
            gt=textureHashMap.get(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id));
            if(gt.isClosed()){
                //System.out.println(15623);
                gt=new GraphicsTexture(w,h);
                textureHashMap.put(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id),gt);
                resourcesNeedToRefresh.add(gt);
            }
           // //System.out.println(15);
        }else{
            gt=new GraphicsTexture(w,h);
            textureHashMap.put(new Pair<>(String.format("%s_%s_%s",string,w,h),obj.id),gt);
            resourcesNeedToRefresh.add(gt);
           // //System.out.println(16);
        }
        if(resourcesNeedToRefresh.contains(gt)){
            getRenderConsumer(string).accept(obj,gt);
            resourcesNeedToRefresh.remove(gt);
        }
        return gt;
    }
    public ResourceLocation getTextureLocation(String string, DataObject obj, int w, int h){
        return getTexture(string, obj, w, h).identifier;
    }
    public void refreshAll(){
        //System.out.println("refresh");
        textureHashMap.forEach(((stringLongPair, graphicsTexture) -> {
            resourcesNeedToRefresh.add(graphicsTexture);
        }));
    }

}
