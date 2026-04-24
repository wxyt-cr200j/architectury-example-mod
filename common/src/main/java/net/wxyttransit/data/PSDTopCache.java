package net.wxyttransit.data;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.wxyttransit.block.WxytPSDTop;

import java.util.HashMap;
import java.util.Map;

public class PSDTopCache {
    public static Map<String, RegistryObject<WxytPSDTop>> PSDTopMap=new HashMap<>();
    public static void register(String id,RegistryObject<WxytPSDTop> top){
        PSDTopMap.put(id,top);
    }
    public static WxytPSDTop get(String id){
        return PSDTopMap.get(id).get();
    }
}
