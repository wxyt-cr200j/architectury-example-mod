package net.wxyttransit.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TSWHalfButton extends PictureButton{
    public Component coma,comb;
    public TSWHalfButton(int x, int y, int w, int h, Component com, Component com1, OnPress func, ResourceLocation rl)
    {
        super(x,y,w,h,com,func,rl);
        coma=com;comb=com1;
    }

    public void render(GuiGraphics g, Font font, int mx, int my, int f)
    {
        super.render(g,mx,my,f);
        PoseStack ps = g.pose();
        ps.pushPose();
        ps.scale(2,2,2);//width/20,height/5,width/20*6,height/5*2
        g.drawString(font,coma,super.x1/2+super.w1/12/2,super.y1/2+super.h1/2-super.h1/3-super.h1/12,0xFFFFFF);
        ps.popPose();
        g.drawString(font,comb,super.x1+super.w1/12,super.y1+super.h1-super.h1/9*2-super.h1/12*2,0xFFFFFF);

    }
}
