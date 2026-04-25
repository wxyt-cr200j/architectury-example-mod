package net.wxyttransit.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WxytPsdTypeData {
    public HashMap<String,PsdType> types = new HashMap<>();
    public HashMap<String,PsdLightType> lightTypes = new HashMap<>();
    public static WxytPsdTypeData data = new WxytPsdTypeData();
    public static class PsdType{
        public String name;
        public int type;
        public PsdType(String name,int type){
            this.name = name;
            this.type = type;
        }
    }
    public static class PsdLightType{
        public String name;
        public int type;
        public PsdLightType(String name,int type){
            this.name = name;
            this.type = type;
        }
    }
}
