package net.wxyttransit.gui;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.wxyttransit.block.WxytPSDAPGDoorBase;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.data.NameColorDataBase;
import net.wxyttransit.packet.PacketHelper;
import net.wxyttransit.render.WxytPsdTypeData;
import net.wxyttransit.render.WxytTextureData;

import java.util.*;

public class GUIPSDAPGDoorSettings<T extends WxytPSDAPGDoorBase.TileEntityPSDAPGDoorBase > extends WxytScreen {
    private T entity;
    public GUIPSDAPGDoorSettings(Component component, T entity) {
        super(component);
        this.entity = entity;
    }

    private Button button1;
    private Button button2;

    private Button button3;

    private Button button4;

    public String district ="shenzhen";
    public String light_district ="shenzhen";
    public int type = 0;
    public int light_type = 0;
    private List<NameColorDataBase> list_district = new ArrayList<>();
    private List<String> isChoosing_district =new ArrayList<>();
    private List<NameColorDataBase> list_light_district =new ArrayList<>();
    private List<String> isChoosing_light_district =new ArrayList<>();
    private List<NameColorDataBase> list_type =new ArrayList<>();
    private List<String> isChoosing_type =new ArrayList<>();
    private List<NameColorDataBase> list_light_type =new ArrayList<>();
    private List<String> isChoosing_light_type =new ArrayList<>();
    private DashboardListSelectorScreen screenListType;
    private DashboardListSelectorScreen screenListLightType;

    private DashboardListSelectorScreen screenListDistrict;

    private DashboardListSelectorScreen screenListLightDistrict;

    @Override
    protected void init(){
        isChoosing_district.clear();
        isChoosing_light_district.clear();
        isChoosing_type.clear();
        isChoosing_light_type.clear();
        list_district.clear();
        list_light_district.clear();
        list_type.clear();
        list_light_type.clear();
        district =entity.district;
        light_district = entity.lightDistrict;
        type = entity.type;
        light_type = entity.light_type;
        isChoosing_district.add(district);
        isChoosing_light_district.add(light_district);
        isChoosing_type.add(String.valueOf(type));
        isChoosing_light_type.add(String.valueOf(light_type));
        //list = new ButtonList(width/4 - 50,60,100,100,this);
        WxytPsdTypeData.data.types.forEach((string, psdType) -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", psdType.name);
            tag.putString("name",Component.translatable("psdDistrict."+psdType.name).getString());
            list_district.add(new NameColorDataBase(tag) {
                @Override
                protected boolean hasTransportMode() {
                    return false;
                }
            });
        });
        WxytPsdTypeData.data.lightTypes.forEach((string, psdType) -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", psdType.name);
            tag.putString("name",Component.translatable("psdLightDistrict."+psdType.name).getString());
            list_light_district.add(new NameColorDataBase(tag) {
                @Override
                protected boolean hasTransportMode() {
                    return false;
                }
            });
        });
        WxytPsdTypeData.data.types.forEach((string, psdType) -> {
            if(Objects.equals(psdType.name,district)){
                CompoundTag tag = new CompoundTag();
                tag.putString("id", String.valueOf(psdType.type));
                tag.putString("name",Component.translatable("psdType."+psdType.name+"_"+psdType.type).getString());
                list_type.add(new NameColorDataBase(tag) {
                    @Override
                    protected boolean hasTransportMode() {
                        return false;
                    }
                });
            }
        });
        WxytPsdTypeData.data.lightTypes.forEach((string, psdType) -> {
            if(Objects.equals(psdType.name, light_district)){
                CompoundTag tag = new CompoundTag();
                tag.putString("id", String.valueOf(psdType.type));
                tag.putString("name",Component.translatable("psdLightType."+psdType.name+"_"+psdType.type).getString());
                list_light_type.add(new NameColorDataBase(tag) {
                    @Override
                    protected boolean hasTransportMode() {
                        return false;
                    }
                });
            }
        });
        System.out.println(list_district.get(0).id);
        System.out.println(list_district.get(0).id==isChoosing_district.get(0));
        System.out.println(Objects.equals(list_district.get(0).id, isChoosing_district.get(0)));

        System.out.println(isChoosing_district);
        button1 = Button.builder(Component.translatable("gui.wxyttransit.chooseDistrict"),btn->{

            screenListDistrict=new DashboardListSelectorScreen(()->{

                if(!isChoosing_district.isEmpty() &&!isChoosing_district.get(0).isEmpty()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(entity.getBlockPos());
                    buf.writeUtf(isChoosing_district.get(0));
                    NetworkManager.sendToServer(PacketHelper.DISTRICT_PACKET,buf);
                }
                Minecraft.getInstance().setScreen(this);
            }, list_district, isChoosing_district,true,false);
            Minecraft.getInstance().setScreen(screenListDistrict);
        }).bounds(width/2-100,20,200,20).build();
      //  list.init();
        addRenderableWidget(button1);
        button2 = Button.builder(Component.translatable("gui.wxyttransit.chooseLightDistrict"),btn->{

            screenListLightDistrict=new DashboardListSelectorScreen(()->{
                if(!isChoosing_light_district.isEmpty()&&!isChoosing_light_district.get(0).isEmpty()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(entity.getBlockPos());
                    buf.writeUtf(isChoosing_light_district.get(0));
                    NetworkManager.sendToServer(PacketHelper.LIGHT_DISTRICT_PACKET,buf);
                }
                Minecraft.getInstance().setScreen(this);
            }, list_light_district, isChoosing_light_district,true,false);
            Minecraft.getInstance().setScreen(screenListLightDistrict);
        }).bounds(width/2-100,60,200,20).build();
        //  list.init();
        addRenderableWidget(button2);
        button3 = Button.builder(Component.translatable("gui.wxyttransit.chooseType"),btn->{

            screenListType=new DashboardListSelectorScreen(()->{

                if(!isChoosing_type.isEmpty()&&!isChoosing_type.get(0).isEmpty()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(entity.getBlockPos());
                    buf.writeUtf((isChoosing_type.get(0)));
                    NetworkManager.sendToServer(PacketHelper.TYPE_PACKET,buf);
                }
                Minecraft.getInstance().setScreen(this);
            }, list_type, isChoosing_type,true,false);
            Minecraft.getInstance().setScreen(screenListType);
        }).bounds(width/2-100,100,200,20).build();
        addRenderableWidget(button3);
        button4 = Button.builder(Component.translatable("gui.wxyttransit.chooseLightType"),btn->{

            screenListLightType=new DashboardListSelectorScreen(()->{

                if(!isChoosing_light_type.isEmpty()&&!isChoosing_light_type.get(0).isEmpty()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(entity.getBlockPos());
                    buf.writeUtf((isChoosing_light_type.get(0)));
                    NetworkManager.sendToServer(PacketHelper.LIGHT_TYPE_PACKET,buf);
                }
                Minecraft.getInstance().setScreen(this);
            }, list_light_type, isChoosing_light_type,true,false);
            Minecraft.getInstance().setScreen(screenListLightType);
        }).bounds(width/2-100,140,200,20).build();
        addRenderableWidget(button4);
    }

    @Override
    public void render(GuiGraphics g,int mx,int my,float f){
        isChoosing_district.clear();
        isChoosing_light_district.clear();
        isChoosing_type.clear();
        isChoosing_light_type.clear();
        district = entity.district;
        light_district = entity.lightDistrict;
        type = entity.type;
        light_type = entity.light_type;
        isChoosing_district.add(district);
        isChoosing_light_district.add(light_district);
        isChoosing_type.add(String.valueOf(type));
        isChoosing_light_type.add(String.valueOf(light_type));
        //list = new ButtonList(width/4 - 50,60,100,100,this);

        button1.render(g, mx, my, f);
        button2.render(g, mx, my, f);
        button3.render(g, mx, my, f);
        button4.render(g, mx, my, f);
      //  list.render(g, mx, my, f);
        //entity.renderType=renderScriptIsChoosing;
    }
    @Override
    public void onClose(){

        super.onClose();
    }
    @Override
    public boolean isPauseScreen(){return false;}
}
   