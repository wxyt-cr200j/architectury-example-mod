package net.wxyttransit.shenzhen;

import mtr.client.ClientCache;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;
import net.wxyttransit.render.TextDrawUtils;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class RenderShenzhenMetroDestination {
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
    public static BufferedImage AW = TextDrawUtils.readBufferedImage(new ResourceLocation("wxyttransit:textures/sign/arrow.png"));       // 箭头图片
    public static BufferedImage AW_flip = TextDrawUtils.readBufferedImage(new ResourceLocation("wxyttransit:textures/sign/arrow_flip.png"));    // 翻转箭头图片

    // 你的原有字体（直接使用，忽略崩溃）

    // ====================== 1:1 转换 JS getRouteNameWidth ======================


    // ====================== 1:1 转换 JS drawRouteName ======================
    public static void drawRouteName(Graphics2D g, String routeName, Color routeColor, float x, float y, float hei, float wid) {
        try {
            float roundWid = hei < wid ? hei / 6 : wid / 6;
            String nameCJK = TextDrawUtils.gcp(routeName);
            String nameNCJK = TextDrawUtils.gncp(routeName);

            TextDrawUtils.LineInfo lineInfo = TextDrawUtils.parseLineName(nameCJK);
            String routeNumber = lineInfo.lineNumber;
            String routeSuffix = lineInfo.lineSuffix;

            // 计算字号
            float size1 = TextDrawUtils.calculateMaxFontSize(g, SANS, routeNumber, 0, (int) hei, 0, 0, false);
            float size2 = TextDrawUtils.calculateMaxFontSize(g, SANS, routeSuffix, 0, (int) (hei / 2.5f), 0, 0, false);
            float size3 = TextDrawUtils.calculateMaxFontSize(g, SANS, nameNCJK, 0, (int) (hei / 4.5f), 0, 0, false);

            Font font1 = SANS.deriveFont(size1);
            Font font2 = SANS.deriveFont(size2);
            Font font3 = SANS.deriveFont(size3);

            int wid1 = TextDrawUtils.getStringWidth(routeNumber, g, font1);
            int maxW2 = Math.max(TextDrawUtils.getStringWidth(routeSuffix, g, font2), TextDrawUtils.getStringWidth(nameNCJK, g, font3));
            float sum = wid1 + roundWid / 2 + maxW2 + roundWid * 2;

            // 绘制线路背景
            g.setColor(routeColor);
            g.fillRoundRect((int) (x - wid / 2), (int) (y - hei / 2), (int) wid, (int) hei, (int) roundWid, (int) roundWid);

            // 对比色文字
            Color frontColor = TextDrawUtils.isLight(routeColor) ? Color.BLACK : Color.WHITE;
            g.setColor(frontColor);

            AffineTransform base = g.getTransform();
            g.translate(x - sum / 2 + roundWid, y + hei / 2 - roundWid);

            // 自适应缩放
            if (sum + roundWid * 2 < wid) {
                g.scale(1, 1);
            } else {
                g.scale(wid / (sum + roundWid * 2), 1);
            }

            // 绘制文本（严格按JS位置）
            TextDrawUtils.drawText(g, routeNumber, font1, 0, 0, TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
            TextDrawUtils.drawText(g, routeSuffix, font2, wid1 + (int) (roundWid / 2), (int) (-hei / 4), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
            TextDrawUtils.drawText(g, nameNCJK, font3, wid1 + (int) (roundWid / 2), 0, TextDrawUtils.Align.LEFT, 0, 0, false, false, false);

            g.setTransform(base);
        } catch (Exception e) {}
    }

    // ====================== 1:1 转换 JS drawBlank 主绘制函数 ======================
    public static void draw(Graphics2D g,DataObject obj,ClientCache.PlatformRouteDetails route,int wid,int hei) {


        int arrow = obj.arrow;
        int csi = route.currentStationIndex;

        try {
            Color routeColor = new Color(route.routeColor);
            float botwid = hei * 0.1f;
            g.setColor(routeColor);
            g.fillRect(0, (int) (hei - botwid), wid, (int) botwid);

            // 终点站：提示前往对面站台
            if (csi == route.stationDetails.size() - 1) {
                float botwid2 = hei * 0.15f;
                g.setColor(routeColor);
                g.fillRect(0, (int) (hei - botwid2), wid, (int) botwid2);

                String cnText = "请前往对面站台候车";
                String enText = "Please wait at the opposite platform";
                float sizeCN = TextDrawUtils.calculateMaxFontSize(g, SANS, cnText, 0, hei / 3, 0, 0, false);
                float sizeEN = TextDrawUtils.calculateMaxFontSize(g, SANS, enText, 0, (int) (hei / 5.5f), 0, 0, false);

                g.setColor(Color.BLACK);
                TextDrawUtils.drawText(g, cnText, SANS.deriveFont(sizeCN), wid / 2, (int) (hei / 2.1f), TextDrawUtils.Align.CENTER, wid, 0, false, false, false);
                TextDrawUtils.drawText(g, enText, SANS.deriveFont(sizeEN), wid / 2, (int) ((float) hei / 2 + hei / 5.5f), TextDrawUtils.Align.CENTER, wid, 0, false, false, false);
                return;
            }

            // 正常绘制线路+终点站
            float w, h;
            if ((float) wid / 5 / (hei * 0.45f) > 1) {
                w = hei * 0.9f;
                h = hei * 0.45f;
            } else {
                w = (float) wid / 5;
                h = wid / 10f;
            }

            // 终点站文本
            String destCjk = "往" + TextDrawUtils.gcp(route.stationDetails.get(route.stationDetails.size() - 1).stationName);
            String destNcjk = "To " + TextDrawUtils.gncp(route.stationDetails.get(route.stationDetails.size() - 1).stationName);
            float sizeDestCN = TextDrawUtils.calculateMaxFontSize(g, SANS, destCjk, 0, (int) (h * 0.9f), 0, 0, false);
            float sizeDestEN = TextDrawUtils.calculateMaxFontSize(g, SANS, destNcjk, 0, (int) (h * 0.4f), 0, 0, false);

            int len1 = TextDrawUtils.getStringWidth(destCjk, g, SANS.deriveFont(sizeDestCN));
            int len2 = TextDrawUtils.getStringWidth(destNcjk, g, SANS.deriveFont(sizeDestEN));
            int maxTextW = Math.max(len1, len2);

            // 画布变换（和JS完全一致）
            AffineTransform la = g.getTransform();
            float lastwid = wid;
            float lasthei = hei;

            // 计算总宽度
            float totWid = 0;
            if (arrow == 0) totWid += w + h / 3 + 2 * h * 1.5f;
            else if (arrow != 3) totWid += w + 3 * h * 1.5f + h / 4 + h / 3;
            else totWid += w + 4 * h * 1.5f + h / 2 + h / 3;
            totWid += maxTextW;

            // 自适应缩放
            if (totWid > wid) {
                g.scale(wid / totWid, wid / totWid);
                wid = (int) totWid;
                hei = (int) (hei / (lastwid / totWid));
            }
            totWid -= 2 * h * 1.5f;
            float lineY = hei * 0.85f / 2;

            g.setColor(Color.BLACK);
            // 箭头分支判断（1:1复刻JS）
            switch (arrow) {
                case 0 -> {
                    drawRouteName(g, route.routeName, routeColor, (float) wid / 2 - totWid / 2 + w / 2, lineY, h, w);
                    TextDrawUtils.drawText(g, destCjk, SANS.deriveFont(sizeDestCN), (int) ((float) wid / 2 + totWid / 2 - len1), (int) (lineY + h * 0.05f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                    TextDrawUtils.drawText(g, destNcjk, SANS.deriveFont(sizeDestEN), (int) ((float) wid / 2 + totWid / 2 - len1), (int) (lineY + h * 0.55f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                }
                case 1 -> {
                    g.drawImage(AW, (int) ((float) wid / 2 - totWid / 2), (int) (lineY - h / 2 * 1.5f), (int) (h * 1.5f), (int) (h * 1.5f), null);
                    drawRouteName(g, route.routeName, routeColor, (float) wid / 2 - totWid / 2 + h * 1.5f + w / 2 + h / 4, lineY, h, w);
                    TextDrawUtils.drawText(g, destCjk, SANS.deriveFont(sizeDestCN), (int) ((float) wid / 2 - totWid / 2 + w + h * 1.5f + h / 3 + h / 4), (int) (lineY + h * 0.05f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                    TextDrawUtils.drawText(g, destNcjk, SANS.deriveFont(sizeDestEN), (int) ((float) wid / 2 - totWid / 2 + w + h * 1.5f + h / 3 + h / 4), (int) (lineY + h * 0.55f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                }
                case 2 -> {
                    g.drawImage(AW_flip, (int) ((float) wid / 2 + totWid / 2 - h * 1.5f), (int) (lineY - h / 2 * 1.5f), (int) (h * 1.5f), (int) (h * 1.5f), null);
                    drawRouteName(g, route.routeName, routeColor, (float) wid / 2 + totWid / 2 - h * 1.5f - w / 2 - h / 4, lineY, h, w);
                    TextDrawUtils.drawText(g, destCjk, SANS.deriveFont(sizeDestCN), (int) ((float) wid / 2 + totWid / 2 - h * 1.5f - h / 4 - h / 3 - w), (int) (lineY + h * 0.05f), TextDrawUtils.Align.RIGHT, 0, 0, false, false, false);
                    TextDrawUtils.drawText(g, destNcjk, SANS.deriveFont(sizeDestEN), (int) ((float) wid / 2 + totWid / 2 - h * 1.5f - h / 4 - h / 3 - w), (int) (lineY + h * 0.55f), TextDrawUtils.Align.RIGHT, 0, 0, false, false, false);
                }
                default -> {
                    g.drawImage(AW, (int) ((float) wid / 2 - totWid / 2), (int) (lineY - h / 2 * 1.5f), (int) (h * 1.5f), (int) (h * 1.5f), null);
                    g.drawImage(AW_flip, (int) ((float) wid / 2 + totWid / 2 - h * 1.5f), (int) (lineY - h / 2 * 1.5f), (int) (h * 1.5f), (int) (h * 1.5f), null);
                    drawRouteName(g, route.routeName, routeColor, (float) wid / 2 - totWid / 2 + h * 1.5f + h / 4 + w / 2, lineY, h, w);
                    TextDrawUtils.drawText(g, destCjk, SANS.deriveFont(sizeDestCN), (int) ((float) wid / 2 + totWid / 2 - h * 1.5f - len1 - h / 3), (int) (lineY + h * 0.05f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                    TextDrawUtils.drawText(g, destNcjk, SANS.deriveFont(sizeDestEN), (int) ((float) wid / 2 + totWid / 2 - h * 1.5f - len1 - h / 3), (int) (lineY + h * 0.55f), TextDrawUtils.Align.LEFT, 0, 0, false, false, false);
                }
            }

            // 恢复画布
            g.setTransform(la);
        } catch (Exception e) {}
    }

}
