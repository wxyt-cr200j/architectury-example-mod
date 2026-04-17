package net.wxyttransit.data;

import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.wxyttransit.block.WxytPSDTop;

import java.util.HashMap;
import java.util.Map;

public class PSDTopCache {
    public static Map<String, RegistryObject<Block>> PSDTopMap=new HashMap<>();
    public static void register(String id,RegistryObject<Block> top){
        PSDTopMap.put(id,top);
    }
    public static Block getBlock(String id){
        return PSDTopMap.get(id).get();
    }
}
