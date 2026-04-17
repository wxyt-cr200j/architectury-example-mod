package net.wxyttransit.shenzhen;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import mtr.client.ClientCache;
import mtr.client.IDrawing;
import mtr.mappings.UtilitiesClient;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;
import net.wxyttransit.render.TextDrawUtils;
import net.wxyttransit.render.TextUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RenderShenzhenMetroStationNow {
    static Font SOURCE_HAN_SANS_CN_BOLD = TextDrawUtils.getFont(new ResourceLocation("wxyttransit:font/source-han-sans-cn/source-han-sans-cn-bold.otf"));
    static Font ROBOTO_BOLD = TextDrawUtils.getFont(new ResourceLocation("wxyttransit:font/roboto/roboto-bold.otf"));
    static Font SANS = TextDrawUtils.getFont(new ResourceLocation("wxyttransit:font/lanting.ttf"));

    public static void draw(DataObject obj, GraphicsTexture gt){
      //  System.out.println(654);
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
           // System.out.println(details.stationDetails.get(0));
            draw(g,obj,details,wid,partHei);
            g.translate(0,partHei);
        });
        g.setTransform(baseTrans);
        gt.upload();
    }


        // ==================== 字体常量（JS → Java） ====================

        // 颜色
        public static final Color whiteColor = Color.decode("#e7dfd3");

        // ==================== drawMiddleName ====================
        private static void drawMiddleName(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int x, int wid, int hei) {
            Color routeColor = new Color(route.routeColor);
            int arrow = obj.arrow;

            double lineLen = wid * 0.6;
            double lineWid = hei * 0.03;

            String sta_cjk = TextDrawUtils.gcp(obj.station.name);
            String sta_ncjk = TextDrawUtils.gncp(obj.station.name);

            float size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.3), 0, 0, false);
            float size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.12), 0, 0, false);

            Font font_cjk = SANS.deriveFont(size_cjk);
            Font font_ncjk = SANS.deriveFont(size_ncjk);

            g.setColor(Color.BLACK);
            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.32), TextDrawUtils.Align.CENTER, (int) (lineLen / 2), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.48), TextDrawUtils.Align.CENTER, (int) (lineLen / 2), (int) lineWid, false, true, false);

            g.setColor(Color.RED);
            int ovalX = x - (int) (lineWid / 2 * 3);
            int ovalY = (int) (hei * 0.55 + lineWid / 2 - lineWid / 2 * 3);
            int ovalSize = (int) (lineWid / 2 * 6);
            g.fillOval(ovalX, ovalY, ovalSize, ovalSize);
        }

        // ==================== drawMiddleNameDest ====================
        private static void drawMiddleNameDest(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int x, int wid, int hei)  {
            Color routeColor = new Color(route.routeColor);
            int arrow = obj.arrow;

            double lineLen = wid * 0.6;
            double lineWid = hei * 0.03;

            String sta_cjk = TextDrawUtils.gcp(obj.station.name);
            String sta_ncjk = TextDrawUtils.gncp(obj.station.name);

            float size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.3), 0, 0, false);
            float size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.12), 0, 0, false);

            Font font_cjk = SANS.deriveFont(size_cjk);
            Font font_ncjk = SANS.deriveFont(size_ncjk);

            g.setColor(Color.BLACK);
            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.40), TextDrawUtils.Align.CENTER, (int) (lineLen / 2), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.56), TextDrawUtils.Align.CENTER, (int) (lineLen / 2), (int) lineWid, false, true, false);
        }

        // ==================== drawNextStation ====================
        private static void drawNextStation(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int x, int wid, int hei)  {
            int arrow = obj.arrow;
            double lineLen = wid * 0.6;
            double lineWid = hei * 0.03;

            Color routeColor = new Color(route.routeColor);
            int csi = route.currentStationIndex;
            ClientCache.PlatformRouteDetails.StationDetails last = route.stationDetails.get(csi - 1);
            ClientCache.PlatformRouteDetails.StationDetails next = route.stationDetails.get(csi + 1);

            // 绘制站点圆圈
            g.setColor(routeColor);
            int outerX = x - (int) (lineWid / 2 * 3);
            int outerY = (int) (hei * 0.55 + lineWid / 2 - lineWid / 2 * 3);
            int outerSize = (int) (lineWid / 2 * 6);
            g.fillOval(outerX, outerY, outerSize, outerSize);

            g.setColor(whiteColor);
            int innerX = x - (int) lineWid;
            int innerY = (int) (hei * 0.55 + lineWid / 2 - lineWid);
            int innerSize = (int) (lineWid * 2);
            g.fillOval(innerX, innerY, innerSize, innerSize);

            // 下一站站点名称
           // System.out.println(next.stationName);
            String sta_cjk = TextDrawUtils.gcp(next.stationName);
            String sta_ncjk = TextDrawUtils.gncp(next.stationName);

            float size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.15), 0, 0, false);
            float size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.06), 0, 0, false);

            Font font_cjk = SANS.deriveFont(size_cjk);
            Font font_ncjk = SANS.deriveFont(size_ncjk);

            g.setColor(Color.BLACK);
            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.43), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.50), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            // 下一站 文字
            sta_cjk = "下一站";
            sta_ncjk = "Next Station";

            size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.11), 0, 0, false);
            size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.04), 0, 0, false);

            font_cjk = SANS.deriveFont(size_cjk);
            font_ncjk = SANS.deriveFont(size_ncjk);

            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.71), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.76), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);
        }

        // ==================== drawLastStation ====================
        private static void drawLastStation(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int x, int wid, int hei)  {
            Color routeColor = new Color(route.routeColor);
            int csi = route.currentStationIndex;
            ClientCache.PlatformRouteDetails.StationDetails last = route.stationDetails.get(csi - 1);
            ClientCache.PlatformRouteDetails.StationDetails next = route.stationDetails.get(csi + 1);
            int arrow = obj.arrow;

            double lineLen = wid * 0.6;
            double lineWid = hei * 0.03;

            g.setColor(Color.LIGHT_GRAY);
            int outerX = x - (int) (lineWid / 2 * 3);
            int outerY = (int) (hei * 0.55 + lineWid / 2 - lineWid / 2 * 3);
            int outerSize = (int) (lineWid / 2 * 6);
            g.fillOval(outerX, outerY, outerSize, outerSize);

            g.setColor(whiteColor);
            int innerX = x - (int) lineWid;
            int innerY = (int) (hei * 0.55 + lineWid / 2 - lineWid);
            int innerSize = (int) (lineWid * 2);
            g.fillOval(innerX, innerY, innerSize, innerSize);

            // 上一站站点名称
            String sta_cjk = TextDrawUtils.gcp(last.stationName);
            String sta_ncjk = TextDrawUtils.gncp(last.stationName);

            float size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.15), 0, 0, false);
            float size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.06), 0, 0, false);

            Font font_cjk = SANS.deriveFont(size_cjk);
            Font font_ncjk = SANS.deriveFont(size_ncjk);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.43), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.50), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            // 上一站 文字
            sta_cjk = "上一站";
            sta_ncjk = "Previous Station";

            size_cjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_cjk, 0, (int) (hei * 0.11), 0, 0, false);
            size_ncjk = TextDrawUtils.calculateMaxFontSize(g, SANS, sta_ncjk, 0, (int) (hei * 0.04), 0, 0, false);

            font_cjk = SANS.deriveFont(size_cjk);
            font_ncjk = SANS.deriveFont(size_ncjk);

            g.setFont(font_cjk);
            TextDrawUtils.drawText(g, sta_cjk, font_cjk, x, (int) (hei * 0.71), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);

            g.setFont(font_ncjk);
            TextDrawUtils.drawText(g, sta_ncjk, font_ncjk, x, (int) (hei * 0.76), TextDrawUtils.Align.CENTER, (int) (lineLen / 3), (int) lineWid, false, true, false);
        }

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

            java.awt.geom.AffineTransform baseTransform = g.getTransform();
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

            // 站点逻辑
            if (csi == 0 && csi == route.stationDetails.size() - 1) {
                drawMiddleName(g,obj, route, wid / 2, wid, hei);
                Stroke stroke = new BasicStroke((float) (lineWid * 0.9));
                g.setStroke(stroke);
            }
            else if (csi == 0) {
                g.setColor(new Color(route.routeColor));
                g.fillRect((wid - (int) lineLen) / 2, (int) (hei * 0.55), (int) lineLen, (int) lineWid);

                if (arrow != 1) {
                    Stroke s1 = new BasicStroke((float) (lineWid * 0.9));
                    g.setStroke(s1);
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));

                    drawMiddleName(g, obj, route, wid / 2 - (int) lineLen / 2, wid, hei);
                    drawNextStation(g,obj, route, wid / 2 + (int) lineLen / 2, wid, hei);
                } else {
                    Stroke s1 = new BasicStroke((float) (lineWid * 0.9));
                    g.setStroke(s1);
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));

                    drawMiddleName(g,obj, route, wid / 2 + (int) lineLen / 2, wid, hei);
                    drawNextStation(g,obj, route, wid / 2 - (int) lineLen / 2, wid, hei);
                }
            }
            else if (csi == route.stationDetails.size() - 1) {
                drawMiddleNameDest(g,obj, route, wid / 2, wid, hei);
            }
            else {
                Color routeColor = new Color(route.routeColor);
                if (arrow == 0 || arrow == 2) {
                    g.fillRect(wid / 2, (int) (hei * 0.55), (int) (lineLen / 2), (int) lineWid);
                    Stroke s1 = new BasicStroke((float) (lineWid * 0.9));
                    g.setStroke(s1);
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));
                    drawNextStation(g,obj, route, wid / 2 + (int) lineLen / 2, wid, hei);
                }
                else if (arrow == 1) {
                    g.fillRect((wid - (int) lineLen) / 2, (int) (hei * 0.55), (int) (lineLen / 2), (int) lineWid);
                    Stroke s1 = new BasicStroke((float) (lineWid * 0.9));
                    g.setStroke(s1);
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));
                    drawNextStation(g,obj, route, wid / 2 - (int) lineLen / 2, wid, hei);
                } else {
                    g.fillRect((wid - (int) lineLen) / 2, (int) (hei * 0.55), (int) lineLen, (int) lineWid);
                    Stroke s1 = new BasicStroke((float) (lineWid * 0.9));
                    g.setStroke(s1);
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 - (int) lineLen / 2 + (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 - (int) lineLen / 2 + (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 - lineWid * 1.2));
                    g.drawLine(wid / 2 + (int) lineLen / 2 - (int) lineWid, (int) (hei * 0.55 + lineWid / 2),
                            wid / 2 + (int) lineLen / 2 - (int) (lineWid * 2.2), (int) (hei * 0.55 + lineWid / 2 + lineWid * 1.2));
                    drawNextStation(g,obj, route, wid / 2 + (int) lineLen / 2, wid, hei);
                    drawNextStation(g,obj, route, wid / 2 - (int) lineLen / 2, wid, hei);
                }

                g.setColor(Color.LIGHT_GRAY);
                if (arrow == 0 || arrow == 2) {
                    g.fillRect(wid / 2 - (int) lineLen / 2, (int) (hei * 0.55), (int) (lineLen / 2), (int) lineWid);
                    drawLastStation(g, obj, route, wid / 2 - (int) lineLen / 2, wid, hei);
                } else if (arrow == 1) {
                    g.fillRect(wid / 2, (int) (hei * 0.55), (int) (lineLen / 2), (int) lineWid);
                    drawLastStation(g, obj, route, wid / 2 + (int) lineLen / 2, wid, hei);
                }

                drawMiddleName(g, obj, route, wid / 2, wid, hei);
            }

            // 恢复画布
            g.setTransform(baseTransform);
            wid = baseW;
            hei = baseH;
        }

}
