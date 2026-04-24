package net.wxyttransit.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ColorButton extends Button {
    //public ResourceLocation image;
    public int x1,y1,w1,h1,c;Component co;//PoseStack ps1;
    public ColorButton(int x, int y, int w, int h, Component com, OnPress func,int color){
        super(x,y,w,h,com,func,null);
        x1=x;y1=y;w1=w;h1=h;c=color;
        co=com;
        //ps1=ps;
    }
    public boolean checkIsOver(int x,int y,int w,int h,int mx,int my){
        return mx>x && mx<=x+w&&my>y && my<=y+h ;
    }
   @Override
    public void render(GuiGraphics g, int mx, int my,float f)
    {
       // this.render();
        g.fill(x1+2,y1+2,x1+w1-2,y1+h1-2,checkIsOver(x1,y1,w1,h1,mx,my)?c:c-0x00111111);
        g.drawCenteredString( Minecraft.getInstance().font,co,x1+w1/2,y1+h1/3,0xFFFFFF);

    }

}
