package net.wxyttransit.gui;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.wxyttransit.block.WxytPSDTop;
import net.wxyttransit.data.NameColorDataBase;
import net.wxyttransit.packet.PacketHelper;
import net.wxyttransit.render.WxytTextureData;
import org.msgpack.value.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIPSDTopSettings<T extends WxytPSDTop.TileEntityPSDTop> extends WxytScreen {
    private T entity;
    public GUIPSDTopSettings(Component component, T entity) {
        super(component);
        this.entity = entity;
    }

    private Button button1;
    public String renderScriptIsChoosing="sz_now";
    private List<NameColorDataBase> list=new ArrayList<>();
    private List<String> isChoosing=new ArrayList<>();
    private DashboardListSelectorScreen screenList;
    @Override
    protected void init(){
        renderScriptIsChoosing=entity.renderType;
        isChoosing.clear();
        list.clear();
        isChoosing.add(renderScriptIsChoosing);
        //list = new ButtonList(width/4 - 50,60,100,100,this);
        WxytTextureData.data.map.forEach(((string, consumer) -> {
            CompoundTag tag =new CompoundTag();
            tag.putString("id",string);
            tag.putString("name",Component.translatable("renderScript."+string).getString());
            list.add(new NameColorDataBase(tag) {
                @Override
                protected boolean hasTransportMode() {
                    return false;
                }
            });
        }));
        button1 = Button.builder(Component.translatable("gui.wxyttransit.chooseRenderType"),btn->{
            //System.out.println(list.toString());
            //System.out.println(isChoosing.toString());
            screenList=new DashboardListSelectorScreen(()->{
                if(!isChoosing.get(0).isEmpty()) {
                    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                    buf.writeBlockPos(entity.getBlockPos());
                    buf.writeUtf(isChoosing.get(0));
                    NetworkManager.sendToServer(PacketHelper.RENDER_TYPE_PACKET,buf);
                }
                Minecraft.getInstance().setScreen(this);
            },list,isChoosing,true,false);
            Minecraft.getInstance().setScreen(screenList);
        }).bounds(width/2-100,20,200,20).build();
      //  list.init();
        addRenderableWidget(button1);
    }

    @Override
    public void render(GuiGraphics g,int mx,int my,float f){
        renderScriptIsChoosing=entity.renderType;
        isChoosing.clear();
        list.clear();
        isChoosing.add(renderScriptIsChoosing);
        //list = new ButtonList(width/4 - 50,60,100,100,this);
        WxytTextureData.data.map.forEach(((string, consumer) -> {
            CompoundTag tag =new CompoundTag();
            tag.putString("id",string);
            tag.putString("name",Component.translatable("renderScript."+string).getString());
            list.add(new NameColorDataBase(tag) {
                @Override
                protected boolean hasTransportMode() {
                    return false;
                }
            });
        }));
        if(button1!=null)   button1.render(g, mx, my, f);
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
   