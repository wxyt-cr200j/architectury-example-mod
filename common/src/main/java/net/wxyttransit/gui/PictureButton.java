package net.wxyttransit.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PictureButton extends Button {
    public ResourceLocation image;
    public int x1,y1,w1,h1;//PoseStack ps1;
    public PictureButton(int x, int y, int w, int h, Component com, OnPress func, ResourceLocation rl){
        super(x,y,w,h,com,func,null);
        x1=x;y1=y;w1=w;h1=h;
        image=rl;
        //ps1=ps;
    }
    public boolean checkIsOver(int x,int y,int w,int h,int mx,int my){
        return mx>x && mx<=x+w&&my>y && my<=y+h ;
    }
   @Override
    public void render(GuiGraphics g, int mx, int my,float f)
    {
        int oldRs=RenderSystem.getShaderTexture(0);
        //RenderSystem.setShaderTexture(0,image);


        //ps1.translate(x1,y1,0);
        //fill(ps1,0,0,100,100,0xFFFFFFFF);
        g.fill(x1+2,y1+2,x1+w1-2,y1+h1-2,checkIsOver(x1,y1,w1,h1,mx,my)?0xFFFFFFFF:0xFFAAAAAA);
        g.blit(image,x1+4,y1+4,0,0,0,w1-8,h1-8,w1-8,h1-8);

       // RenderSystem.setShaderTexture(0,oldRs);


    }

}
