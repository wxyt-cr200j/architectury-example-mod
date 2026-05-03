package net.wxyttransit.shenzhen;

import mtr.client.ClientCache;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;
import net.wxyttransit.render.TextDrawUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RenderShenzhenMetroNone {
    static Font SANS = TextDrawUtils.getFont(new ResourceLocation("wxyttransit:font/lanting.ttf"));

    public static void draw(DataObject obj, GraphicsTexture gt){
      //  //System.out.println(654);
        Graphics2D g = gt.graphics;
        int wid = gt.width;
        int hei = gt.height;
        int partHei = gt.height/obj.details.size();
// 透明清除（必须是支持透明的图像）
        Composite gComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.fillRect(0,0,wid,hei);
        g.setComposite(gComposite);
        AffineTransform baseTrans = g.getTransform();
        g.setClip(0,0,wid,hei);
        int i = 0;
        obj.details.forEach(details->{
           // //System.out.println(details.stationDetails.get(0));
            draw(g,obj,details,wid,partHei);
            g.translate(0,partHei);
        });
        g.setTransform(baseTrans);
        gt.upload();
    }


        // ==================== 字体常量（JS → Java） ====================

        // 颜色
        public static final Color whiteColor = Color.decode("#e7dfd3");

        // ==================== drawBlank（主绘制逻辑） ====================
        private static void draw(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int wid, int hei)  {
            g.setColor(whiteColor);
            Composite gComposite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            g.fillRect(0,0,wid,hei);
            g.setComposite(gComposite);
            int arrow = obj.arrow;
            int csi = route.currentStationIndex;

            double lineLen = hei * 2;
            double lineWid = hei * 0.03;

            AffineTransform baseTransform = g.getTransform();
            double scale = 1.0;
            int baseW = wid;
            int baseH = hei;

            // 绘制底部色带
            try {
                Color routeColor = new Color(route.routeColor);
                double botwid = hei * 0.1;
                g.setColor(routeColor);
                g.fillRect(0, hei - (int) botwid, wid, (int) botwid);
            } catch (Exception ignored) {}

            // 缩放适配
            if (hei * 3 > wid) {
                scale = (double) wid / (hei * 3);
                g.scale(scale, scale);
                wid = (int) (wid / scale);
                g.translate(0, (int) ((hei / scale - hei) / 2));
            }


            // 恢复画布
            g.setTransform(baseTransform);
            wid = baseW;
            hei = baseH;
        }

}
