package net.wxyttransit.shenzhen;

import mtr.client.ClientCache;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.client.GraphicsTexture;
import net.wxyttransit.data.DataObject;
import net.wxyttransit.render.TextDrawUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class RenderShenzhenMetroRoute {
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

    // ==================== 原有绘制方法（保留你之前的代码） ====================


    // ==================== 新增：计算线路名称宽度 ====================
    private static float getRouteNameWidth(Graphics2D g, String routeName, int wid, int hei) {
        try {
            float roundWid = hei < wid ? hei / 6f : wid / 6f;
            String nameCJK = TextDrawUtils.gcp(routeName);
            String nameNCJK = TextDrawUtils.gncp(routeName);

            // 解析线路号、后缀（你项目自带的 parseLineName 方法）
            TextDrawUtils.LineInfo parseResult = TextDrawUtils.parseLineName(nameCJK);
            String routeNumber = parseResult.lineNumber;
            String routeSuffix = parseResult.lineSuffix;

            float sum = 0;
            Font font1 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, routeNumber, 0, hei, 0, 0, false));
            sum += TextDrawUtils.getStringWidth(routeNumber, g, font1) + roundWid / 2;

            Font font2 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, routeSuffix, 0, (int) (hei / 2.5), 0, 0, false));
            Font font3 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, nameNCJK, 0, (int) (hei / 4.5), 0, 0, false));

            float widthSuffix = TextDrawUtils.getStringWidth(routeSuffix, g, font2);
            float widthNCJK = TextDrawUtils.getStringWidth(nameNCJK, g, font3);
            sum += Math.max(widthSuffix, widthNCJK);
            sum += roundWid * 2;

            if (sum + roundWid * 2 >= wid) {
                return sum + roundWid;
            } else {
                return wid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ==================== 新增：绘制线路名称（圆角矩形+文字） ====================
    private static void drawRouteName(Graphics2D g, String routeName, Color routeColor, int x, int y, int hei, int wid, DataObject ctx) {
        try {
            float roundWid = hei < wid ? hei / 6f : wid / 6f;
            String nameCJK = TextDrawUtils.gcp(routeName);
            String nameNCJK = TextDrawUtils.gncp(routeName);

            TextDrawUtils.LineInfo parseResult = TextDrawUtils.parseLineName(nameCJK);
            String routeNumber = parseResult.lineNumber;
            String routeSuffix = parseResult.lineSuffix;

            float sum = 0;
            Font font1 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, routeNumber, 0, hei, 0, 0, false));
            float wid1 = TextDrawUtils.getStringWidth(routeNumber, g, font1);
            sum += wid1 + roundWid / 2;

            Font font2 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, routeSuffix, 0, (int) (hei / 2.5), 0, 0, false));
            Font font3 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, nameNCJK, 0, (int) (hei / 4.5), 0, 0, false));

            float widthSuffix = TextDrawUtils.getStringWidth(routeSuffix, g, font2);
            float widthNCJK = TextDrawUtils.getStringWidth(nameNCJK, g, font3);
            float wid2 = Math.max(widthSuffix, widthNCJK);
            sum += wid2;
            sum += roundWid * 2;

            // 绘制背景圆角矩形
            g.setColor(routeColor);
            g.fillRoundRect(x - wid / 2, y - hei / 2, wid, hei, (int) roundWid, (int) roundWid);

            // 对比色文字
            Color frontColor = Color.decode(getContrastColorBlank(routeColor == Color.LIGHT_GRAY ? Color.BLACK : routeColor));
            AffineTransform base = g.getTransform();

            if (sum + roundWid * 2 < wid) {
                // 正常模式
                g.translate(x - sum / 2 + roundWid, y + hei / 2f - roundWid);
                g.setFont(font1);
                g.setColor(frontColor);
                g.drawString(routeNumber, 0, 0);

                g.setFont(font2);
                g.drawString(routeSuffix, wid1 + roundWid / 2, -hei / 4);

                g.setFont(font3);
                g.drawString(nameNCJK, wid1 + roundWid / 2, 0);
            } else {
                // 缩放模式
                g.translate(x - (float) wid / 2 + roundWid, y + hei / 2f - roundWid);
                g.scale((float) wid / (sum + roundWid * 2), 1);
                g.setFont(font1);
                g.setColor(frontColor);
                g.drawString(routeNumber, 0, 0);

                g.setFont(font2);
                g.drawString(routeSuffix, wid1 + roundWid / 2, -hei / 4);

                g.setFont(font3);
                g.drawString(nameNCJK, wid1 + roundWid / 2, 0);
            }

            g.setTransform(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== 新版：站点线路图主绘制（替换你原有的draw方法） ====================
    private static void draw(Graphics2D g, DataObject obj, ClientCache.PlatformRouteDetails route, int wid, int hei) {
        g.setColor(whiteColor);
        Composite gComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.fillRect(0, 0, wid, hei);
        g.setComposite(gComposite);

        int arrow = obj.arrow;
        int csi = route.currentStationIndex;
        Color routeColor = whiteColor;
        float botwid = hei * 0.1f;

        try {
            routeColor = new Color(route.routeColor);
            g.setColor(routeColor);
            g.fillRect(0, hei - (int) botwid, wid, (int) botwid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 基础尺寸配置
            float defaultLineHei = hei / 20f;
            float defaultSectionWid = defaultLineHei * 3;
            int stationCount = route.stationDetails.size();
            float bestLineLength = defaultLineHei * stationCount + defaultSectionWid * (stationCount - 1);
            float bestLength = bestLineLength + defaultSectionWid * 2;
            float maxInterchangeHei = defaultLineHei * 2.8f;
            int Maxcount = 0;

            // 处理换乘线路（去重）
            List<List<InterchangeRoute>> stationInterchanges = new ArrayList<>();
            for (int a = 0; a < stationCount; a++) {
                ClientCache.PlatformRouteDetails.StationDetails station = route.stationDetails.get(a);
                Map<Integer, String> tempMap = new HashMap<>();

                for (ClientCache.ColorNameTuple j : station.interchangeRoutes) {
                    if (!tempMap.containsKey(j.color)) {
                        tempMap.put(j.color, j.name);
                    }
                }

                List<InterchangeRoute> tempList = new ArrayList<>();
                for (Map.Entry<Integer, String> entry : tempMap.entrySet()) {
                    tempList.add(new InterchangeRoute(entry.getKey(), entry.getValue()));
                }
                stationInterchanges.add(tempList);
                Maxcount = Math.max(tempList.size(), Maxcount);
            }

            maxInterchangeHei += defaultLineHei * 2.7f * Maxcount;
            double scale = 1;
            AffineTransform defaultTransform = g.getTransform();
            int lastWid = wid;
            int lastHei = hei;

            // 自适应缩放
            if (bestLength > wid || maxInterchangeHei + botwid > hei / 2) {
                scale = Math.min((double) wid / bestLength, (hei / 2f - botwid) / maxInterchangeHei);
                g.scale(scale, scale);
                wid = (int) (wid / scale);
                hei = (int) (hei / scale);
            }

            float lineY = hei * 0.4f;
            List<ClientCache.PlatformRouteDetails.StationDetails> details = route.stationDetails;
            float nowSectionWid = (float) wid / (details.size() + 1);
            float nowLineLength = nowSectionWid * (details.size() - 1);
            float nowLineHei = defaultLineHei;
            float sqrt2 = (float) Math.sqrt(2);
            float nowLineLengthWithoutDestArrow = nowLineLength - sqrt2 * defaultLineHei;

            // 绘制主线
            BasicStroke bs = new BasicStroke(nowLineHei / 2);
            g.setColor(routeColor);
            g.setStroke(bs);

            if (csi != stationCount - 1) {
                if (arrow == 1) {
                    // 反向箭头
                    float fillStart = wid / 2f - (nowLineLengthWithoutDestArrow - nowLineLength / 2);
                    float fillWidth = nowLineLengthWithoutDestArrow - nowSectionWid * csi;
                    g.fillRect((int) fillStart, (int) (lineY - nowLineHei / 2), (int) fillWidth, (int) nowLineHei);

                    int arrowX1 = (int) (wid / 2f - nowLineLength / 2 + nowLineHei * 1.45);
                    int arrowY1 = (int) lineY;
                    int arrowX2 = (int) (wid / 2f - nowLineLength / 2 + nowLineHei * 2.45);
                    g.drawLine(arrowX1, arrowY1, arrowX2, (int) (lineY - nowLineHei));
                    g.drawLine(arrowX1, arrowY1, arrowX2, (int) (lineY + nowLineHei));
                } else {
                    // 正向箭头
                    float fillStart = nowSectionWid * (csi + 1);
                    float fillWidth = nowLineLengthWithoutDestArrow - nowSectionWid * csi;
                    g.fillRect((int) fillStart, (int) (lineY - nowLineHei / 2), (int) fillWidth, (int) nowLineHei);

                    int arrowX1 = (int) (wid / 2f + nowLineLength / 2 - nowLineHei * 1.45);
                    int arrowY1 = (int) lineY;
                    int arrowX2 = (int) (wid / 2f + nowLineLength / 2 - nowLineHei * 2.45);
                    g.drawLine(arrowX1, arrowY1, arrowX2, (int) (lineY - nowLineHei));
                    g.drawLine(arrowX1, arrowY1, arrowX2, (int) (lineY + nowLineHei));
                }
            }

            // 绘制已过站灰色线路
            g.setColor(Color.LIGHT_GRAY);
            if (csi != 0) {
                if (arrow == 1) {
                    float fillStart = nowSectionWid * (details.size() - csi);
                    float fillWidth = nowLineLength - nowSectionWid * (details.size() - csi - 1);
                    g.fillRect((int) fillStart, (int) (lineY - nowLineHei / 2), (int) fillWidth, (int) nowLineHei);
                } else {
                    float fillStart = wid / 2f - (nowLineLength / 2);
                    float fillWidth = nowLineLength - nowSectionWid * (details.size() - csi - 1);
                    g.fillRect((int) fillStart, (int) (lineY - nowLineHei / 2), (int) fillWidth, (int) nowLineHei);
                }
            }

            // 绘制站点、文字、换乘
            if (arrow == 1) {
                drawDots(g, obj, routeColor, nowSectionWid, lineY, nowLineHei, stationInterchanges, details, csi, wid, hei, false);
            } else {
                drawDots(g, obj, routeColor, nowSectionWid, lineY, nowLineHei, stationInterchanges, details, csi, wid, hei, true);
            }

            // 恢复画布
            g.setTransform(defaultTransform);
            wid = lastWid;
            hei = lastHei;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== 工具方法：绘制站点圆点+名称+换乘 ====================
    private static void drawDots(Graphics2D g, DataObject obj, Color routeColor, float nowSectionWid, float lineY,
                                 float nowLineHei, List<List<InterchangeRoute>> stationInterchanges,
                                 List<ClientCache.PlatformRouteDetails.StationDetails> details, int csi,
                                 int wid, int hei, boolean isIncrease) {
        for (int i = 0; i < details.size(); i++) {
            float nowLocation = isIncrease ? nowSectionWid * (1 + i) : nowSectionWid * (details.size() - i);
            ClientCache.PlatformRouteDetails.StationDetails station = details.get(i);

            // 当前站：红色实心
            if (i == csi) {
                g.setColor(routeColor);
                drawInterchanges(g, obj, nowLocation, lineY, nowLineHei, stationInterchanges.get(i), false, (int) nowSectionWid);
                g.setColor(Color.RED);
                g.fillOval((int) (nowLocation - nowLineHei * 1.2), (int) (lineY - nowLineHei * 1.2),
                        (int) (nowLineHei * 2 * 1.2), (int) (nowLineHei * 2 * 1.2));
                drawStationName(g, nowLocation, lineY - nowLineHei * 2, -50 * Math.PI / 180,
                        nowLineHei, station.stationName);
                continue;
            }

            // 非当前站：彩色/灰色
            g.setColor(i > csi ? routeColor : Color.LIGHT_GRAY);
            drawInterchanges(g, obj, nowLocation, lineY, nowLineHei, stationInterchanges.get(i), i < csi, (int) nowSectionWid);
            g.setColor(i > csi ? routeColor : Color.LIGHT_GRAY);
            g.fillOval((int) (nowLocation - nowLineHei * 1.2), (int) (lineY - nowLineHei * 1.2),
                    (int) (nowLineHei * 2 * 1.2), (int) (nowLineHei * 2 * 1.2));

            // 白色内圆
            g.setColor(whiteColor);
            g.fillOval((int) (nowLocation - nowLineHei * 0.7 * 1.2), (int) (lineY - nowLineHei * 0.7 * 1.2),
                    (int) (nowLineHei * 1.4 * 1.2), (int) (nowLineHei * 1.4 * 1.2));

            // 站点名称
            g.setColor(Color.BLACK);
            drawStationName(g, nowLocation, lineY - nowLineHei * 2.1f, -50 * Math.PI / 180,
                    nowLineHei, station.stationName);
        }
    }

    // ==================== 工具方法：绘制站点名称（旋转） ====================
    private static void drawStationName(Graphics2D g, float x, float y, double rot, float nowLineHei, String name) {
        AffineTransform lt = g.getTransform();
        g.translate(x, y);
        g.rotate(rot);

        String nameCJK = TextDrawUtils.gcp(name);
        String nameNCJK = TextDrawUtils.gncp(name);

        Font font2 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, nameCJK, 0, (int) (nowLineHei * 2), 0, 0, false));
        Font font3 = SANS.deriveFont(TextDrawUtils.calculateMaxFontSize(g, SANS, nameNCJK, 0, (int) nowLineHei, 0, 0, false));

        float limit = (float) (y / Math.sin(-rot) * 0.75);
        g.setFont(font2);
        TextDrawUtils.drawText(g, nameCJK, font2, 0, (int) (nowLineHei * 0.05), TextDrawUtils.Align.LEFT, (int) limit, 0, false, true, false);

        g.setFont(font3);
        TextDrawUtils.drawText(g, nameNCJK, font3, 0, (int) (nowLineHei * 0.95), TextDrawUtils.Align.LEFT, (int) limit, 0, false, true, false);

        g.setTransform(lt);
    }

    // ==================== 工具方法：绘制换乘指示 ====================
    private static void drawInterchanges(Graphics2D g, DataObject obj, float x, float y, float nowLineHei,
                                         List<InterchangeRoute> interchanges, boolean via,int nowSectionWid) {
        if (interchanges.isEmpty()) return;
        // 换乘小三角
        int[] xPoints = {
                (int) (x - nowLineHei / 2),
                (int) (x - nowLineHei / 2),
                (int) x,
                (int) (x + nowLineHei / 2),
                (int) (x + nowLineHei / 2)
        };
        int[] yPoints = {
                (int) y,
                (int) (y + nowLineHei * 2.1),
                (int) (y + nowLineHei * 2.7),
                (int) (y + nowLineHei * 2.1),
                (int) y
        };
        g.fillPolygon(xPoints, yPoints, 5);

        // 绘制换乘线路
        for (int i = 0; i < interchanges.size(); i++) {
            InterchangeRoute interchange = interchanges.get(i);
            Color color = via ? Color.LIGHT_GRAY : new Color(interchange.color);
            int thisHei = (int) (nowLineHei * 2.5);
            int maxWid = (int) (nowLineHei * 5);
            int sectionWid = (int) (nowSectionWid * 0.9);
            int thisWid = Math.min(maxWid, sectionWid);

            float drawY = y + nowLineHei * 2.8f + nowLineHei * 5 / 4f + 2.7f * i * nowLineHei;
            drawRouteName(g, interchange.name, color, (int) x, (int) drawY, thisHei, thisWid, obj);
        }
    }

    // ==================== 静态内部类：线路解析结果（对应JS parseLineName） ====================


    // ==================== 静态内部类：换乘线路实体 ====================
    private static class InterchangeRoute {
        int color;
        String name;

        public InterchangeRoute(int color, String name) {
            this.color = color;
            this.name = name;
        }
    }



    private static String getContrastColorBlank(Color color) {
        // 这里保留你原有项目的反色计算逻辑
        return "#FFFFFF";
    }

}
