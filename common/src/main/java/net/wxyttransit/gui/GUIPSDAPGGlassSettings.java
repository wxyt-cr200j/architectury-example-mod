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
import net.wxyttransit.block.WxytPSDAPGGlassBase;
import net.wxyttransit.data.NameColorDataBase;
import net.wxyttransit.packet.PacketHelper;
import net.wxyttransit.render.WxytPsdTypeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIPSDAPGGlassSettings<T extends WxytPSDAPGGlassBase.TileEntityPSDAPGGlassBase > extends WxytScreen {
    private T entity;
    public GUIPSDAPGGlassSettings(Component component, T entity) {
        super(component);
        this.entity = entity;
    }

    private Button button1;
    private Button button2;

    private Button button3;

    private Button button4;

    public String district ="shenzhen";
    public int type = 0;
    private List<NameColorDataBase> list_district = new ArrayList<>();
    private List<String> isChoosing_district =new ArrayList<>();
    private List<NameColorDataBase> list_type =new ArrayList<>();
    private List<String> isChoosing_type =new ArrayList<>();
    private DashboardListSelectorScreen screenListType;
    private DashboardListSelectorScreen screenListLightType;

    private DashboardListSelectorScreen screenListDistrict;

    private DashboardListSelectorScreen screenListLightDistrict;

    @Override
    protected void init(){
        isChoosing_district.clear();
        isChoosing_type.clear();
        list_district.clear();
        list_type.clear();
        district =entity.district;
        type = entity.type;
        isChoosing_district.add(district);
        isChoosing_type.add(String.valueOf(type));
        //list = new ButtonList(width/4 - 50,60,100,100,this);
        WxytPsdTypeData.data.types.forEach((string, psdType) -> {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", psdType.name);
            tag.putString("name","psdDistrict."+psdType.name);
            list_district.add(new NameColorDataBase(tag) {
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
                tag.putString("name","psdType."+psdType.name+"_"+psdType.type);
                list_type.add(new NameColorDataBase(tag) {
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
    }

    @Override
    public void render(GuiGraphics g,int mx,int my,float f){
        isChoosing_district.clear();
        isChoosing_type.clear();
        district = entity.district;
        type = entity.type;
        isChoosing_district.add(district);
        isChoosing_type.add(String.valueOf(type));
        //list = new ButtonList(width/4 - 50,60,100,100,this);

        if(button1!=null)     button1.render(g, mx, my, f);
        if(button1!=null)    button3.render(g, mx, my, f);
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
   