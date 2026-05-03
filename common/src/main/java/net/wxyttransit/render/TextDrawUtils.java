package net.wxyttransit.render;

import mtr.MTR;
import mtr.client.ClientCache;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.wxyttransit.client.GraphicsTexture;
import org.apache.http.util.TextUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;

/**
 * 合并版 通用文本绘制工具（兼容原JS所有功能）
 * 支持：居中/左对齐/右对齐、XY缩放、自动最大字号、双语绘制、滚动文本、自动换行
 */
public class TextDrawUtils {

    public static InputStream readStream(ResourceLocation identifier) throws IOException {
        final java.util.List<Resource> resources = Minecraft.getInstance().getResourceManager().getResourceStack(identifier);
        if(resources.isEmpty())return null;
        return Utilities.getInputStream(resources.get(0));
    }
    public static BufferedImage readBufferedImage(ResourceLocation identifier) {
        try (InputStream is = readStream(identifier)) {
            if (is != null) {
                return GraphicsTexture.createArgbBufferedImage(ImageIO.read(is));
            }
            else return new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
    }
    public static Font getFont(ResourceLocation rl) {
        try {
            // 关键修复：先把字体读成 byte[]，避免流被关闭或解析不完整
            InputStream inputStream = Utilities.getInputStream(Minecraft.getInstance().getResourceManager().getResource(rl));
            byte[] fontBytes = inputStream.readAllBytes(); // 读取全部数据

            // 从完整字节数组创建字体，彻底解决 BufferUnderflow
            return Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(fontBytes));

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // 失败时返回安全默认字体
            return new Font("SimHei", Font.PLAIN, 16);
        }
    }
    public static String gcp(String text){
        return TextUtil.getCjkParts(text);
    }
    public static String gncp(String text){
        return TextUtil.getNonCjkParts(text);
    }

    // ==================== 基础配置常量 ====================
    public enum Align {
        LEFT, CENTER, RIGHT
    }

    // ==================== 【核心：万能文本绘制方法】 ====================
    /**
     * 万能通用文本绘制（合并所有旧draw函数功能）
     * @param g2d           绘图上下文
     * @param text          文本
     * @param font          字体
     * @param x             基准X坐标
     * @param y             基准Y坐标
     * @param align         对齐方式：LEFT/CENTER/RIGHT
     * @param maxW          最大宽度（0不限制）
     * @param maxH          最大高度（0不限制）
     * @param autoFontSize  是否自动计算最大字号
     * @param scaleX        是否X轴缩放适配宽度
     * @param scaleY        是否Y轴缩放适配高度
     */
    public static void drawText(Graphics2D g2d, String text, Font font,
                                int x, int y, Align align,
                                int maxW, int maxH,
                                boolean autoFontSize,
                                boolean scaleX, boolean scaleY) {
        if (text == null || text.isEmpty()) return;
        AffineTransform oldTransform = g2d.getTransform();
        RenderingHints oldHints = g2d.getRenderingHints();

        try {
            // 抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. 自动计算最大字号
            if (autoFontSize && maxH > 0) {
                float size = calculateMaxFontSize(g2d, font, text, 0, maxH, 0, 0, false);
                font = font.deriveFont(size);
            }
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int textW = fm.stringWidth(text);
            int textH = fm.getHeight();

            // 2. 计算缩放比例
            double sx = 1.0, sy = 1.0;
            if (scaleX && maxW > 0 && textW > maxW) sx = (double) maxW / textW;
            if (scaleY && maxH > 0 && textH > maxH) sy = (double) maxH / textH;

            // 3. 根据对齐方式计算绘制位置
            double drawX = x;
            if (align == Align.CENTER) {
                drawX = x / sx - textW / 2.0;
            } else if (align == Align.RIGHT) {
                drawX = (x - (textW * sx)) / sx;
            }

            // 4. 应用变换
            g2d.scale(sx, sy);
            g2d.drawString(text, (float) drawX, y);

        } finally {
            // 恢复画布状态（必须！）
            g2d.setTransform(oldTransform);
            g2d.setRenderingHints(oldHints);
        }
    }

    // ==================== 双语文本绘制（合并版） ====================
    public static void drawBilingualText(Graphics2D g2d,
                                         String cjkText, Font cjkFont,
                                         String ncjkText, Font ncjkFont,
                                         int x, int y, Align align,
                                         int maxW, int maxH) {
        if (cjkText == null) cjkText = "";
        if (ncjkText == null) ncjkText = "";

        AffineTransform oldT = g2d.getTransform();
        RenderingHints oldH = g2d.getRenderingHints();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        try {
            // 统一字号
            float h = Math.min(
                    calculateMaxFontSize(g2d, cjkFont, cjkText, 0, maxH, 0, 0, false),
                    calculateMaxFontSize(g2d, ncjkFont, ncjkText, 0, maxH, 0, 0, false)
            );
            Font fCJK = cjkFont.deriveFont(h);
            Font fNCJK = ncjkFont.deriveFont(h);

            g2d.setFont(fCJK);
            int w1 = g2d.getFontMetrics().stringWidth(cjkText);
            g2d.setFont(fNCJK);
            int w2 = g2d.getFontMetrics().stringWidth(ncjkText);
            int totalW = w1 + w2;

            // 缩放
            double sx = 1.0;
            if (maxW > 0 && totalW > maxW) sx = (double) maxW / totalW;
            g2d.scale(sx, 1);

            // 对齐
            float offset = 0;
            if (align == Align.CENTER) offset = (float) (x / sx - totalW / 2.0);
            if (align == Align.RIGHT) offset = (float) (x / sx - totalW);

            g2d.setFont(fCJK);
            g2d.drawString(cjkText, offset, y);
            g2d.setFont(fNCJK);
            g2d.drawString(ncjkText, offset + w1, y);

        } finally {
            g2d.setTransform(oldT);
            g2d.setRenderingHints(oldH);
        }
    }

    // ==================== 滚动文本 ====================
    public static double drawScrollText(Graphics2D g, String text, int x, int y,
                                        int boxW, long duration, long time) {
        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(text);
        if (textW <= boxW) {
            g.drawString(text, x + boxW / 2 - textW / 2, y);
            return 0;
        }

        double p = (time % duration) / (double) duration;
        double tx = (textW * 2.0) * p;
        AffineTransform t = g.getTransform();

        g.translate(x + boxW - tx, 0);
        g.drawString(text, 0, y);
        g.setTransform(t);

        return (textW + 40.0) / (textW * 2 + 80.0) * duration;
    }
    // ==================== 自动换行文本 ====================
    public static int drawWrapText(Graphics2D g, String text, int x, int y, int lineW) {
        if (text == null || text.isEmpty()) return 0;

        // 创建带字体属性的文本
        AttributedString as = new AttributedString(text, Map.of(TextAttribute.FONT, g.getFont()));
        AttributedCharacterIterator paragraph = as.getIterator();

        // 获取正确的起止索引（修复点）
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();

        LineBreakMeasurer lbm = new LineBreakMeasurer(paragraph, g.getFontRenderContext());
        lbm.setPosition(paragraphStart);

        int descent = 0;
        boolean first = true;

        // 使用正确的结束索引判断
        while (lbm.getPosition() < paragraphEnd) {
            TextLayout layout = lbm.nextLayout(lineW);
            y += first ? 0 : layout.getAscent();
            layout.draw(g, x, y);
            descent += first ? layout.getDescent() : layout.getAscent();
            first = false;
        }
        return descent;
    }

    // ==================== 计算最大字号（原版精确转换） ====================
    public static float calculateMaxFontSize(Graphics2D g, Font font, String text,
                                             int maxW, int maxH,
                                             int minSize, int maxSize, boolean vertical) {
        if (text == null || text.isBlank() || maxW < 0 || maxH < 0 || minSize < 0 || maxSize < 0)
        {
            ////System.out.println("invalid params");
            return 0;
        }

        final int MAX_LIMIT = 10000;
        int high = maxSize == 0 ? MAX_LIMIT : maxSize;
        int low = minSize;
        if (low > high) return low;

        float best = low;
        final float EPS = 0.5f;
        int loop = 0;

        while (high - low > EPS && loop < 25) {
            loop++;
            float mid = (low + high) / 2f;
            Font f = font.deriveFont(mid);
            FontMetrics fm = g.getFontMetrics(f);

            boolean ok;
            if (vertical) {
                int cw = fm.charWidth('龘');
                int th = (fm.getAscent() + fm.getDescent()) * text.length();
                ok = (maxW == 0 || cw <= maxW) && (maxH == 0 || th <= maxH);
            } else {
                int tw = fm.stringWidth(text);
                int th = fm.getHeight();
                ok = (maxW == 0 || tw <= maxW) && (maxH == 0 || th <= maxH);
            }

            if (ok) {
                best = mid;
                low = (int) mid;
            } else {
                high = (int) mid;
            }
        }
        return best;
    }



    public static boolean isLight(Color c) {
        double dark = 1 - (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255.0;
        return dark < 0.5;
    }


    public static int getStringWidth(String s,Graphics2D g, Font f) {
        return g.getFontMetrics(f).stringWidth(s);
    }

    // ==================== 地铁线路解析（保留） ====================
    public static LineInfo parseLineName(String name) {
        if (name == null) return new LineInfo("", "");
        String reg = "^([A-Za-z]*\\d*|.)(.+)$";
        if (name.matches(reg)) {
            return new LineInfo(name.replaceAll(reg, "$1"), name.replaceAll(reg, "$2"));
        }
        return new LineInfo("", name);
    }

    public static class LineInfo {
        public final String lineNumber;
        public final String lineSuffix;
        public LineInfo(String lineNumber, String lineSuffix) {
            this.lineNumber = lineNumber;
            this.lineSuffix = lineSuffix;
        }
    }

}
