package net.wxyttransit.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class ButtonList {
    int x,y,w,h,nowY,nowPage,maxWidget;
    WxytScreen screen;
    private HashMap<Component, Consumer<Button>> map=new HashMap<>();
    private ArrayList<Button> list=new ArrayList<>();
    public ButtonList(int x, int y, int w, int h, WxytScreen screen){
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.nowY=y;
        this.nowPage=0;
        this.maxWidget=h/20;
        this.screen=screen;
    }
    public void init(){
        map.forEach((component, buttonConsumer) -> {
            Button btn=Button.builder(component,buttonConsumer::accept).size(w,20).build();
            list.add(btn);
            screen.addRenderWidget(btn);
        });
    }
    public void render(GuiGraphics g,int mx,int my,float f){
        float h1 = f*2;
       // g.enableScissor(x,y,w,h);
        int nowYY = nowY;
        if(f>0)nowPage=Math.min(nowPage+1,map.size()/maxWidget);
        else if (f<0) {
            nowPage=Math.max(nowPage-1,0);
        }
        System.out.println(f);
        System.out.println(maxWidget);
        for(int i = maxWidget*nowPage;i<Math.min(maxWidget*(nowPage+1),map.size());i++){
            Button btn =list.get(i);btn.setX(x);btn.setY(y+20*(i-maxWidget*nowPage));
           // screen.addRenderWidget(btn);
            btn.render(g, mx, my, f);

        }
       // g.enableScissor(0,0,screen.width,screen.height);
    }
    public void put(Component com,Consumer<Button> btn){
        map.put(com, btn);
    }
}
