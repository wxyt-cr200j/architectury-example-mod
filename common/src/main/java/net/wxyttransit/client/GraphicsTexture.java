package net.wxyttransit.client;

import net.wxyttransit.WxytTransit;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.wxyttransit.WxytTransit;
import net.wxyttransit.mixin.NativeImageAccessor;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.awt.image.*;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.UUID;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class GraphicsTexture implements Closeable {

    //private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    private final DynamicTexture dynamicTexture;
    public final ResourceLocation identifier;

    public final BufferedImage bufferedImage;
    public final Graphics2D graphics;

    public final int width, height;
    private boolean isClosed = false;

    public GraphicsTexture(int width, int height, ResourceLocation path) {
        this.width = width;
        this.height = height;
        dynamicTexture = new DynamicTexture(new NativeImage(width, height, false));
        identifier = path;
        Minecraft.getInstance().execute(() -> {
            Minecraft.getInstance().getTextureManager().register(identifier, dynamicTexture);
        });
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public GraphicsTexture(int width, int height){
        this(width, height, new ResourceLocation(WxytTransit.MOD_ID, String.format("dynamic/graphics/%s", UUID.randomUUID().toString().replace("-", "_"))));
    }

    public static BufferedImage createArgbBufferedImage(BufferedImage src) {
        BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(src, 0, 0, null);
        graphics.dispose();

        return newImage;
    }

    public void upload() {
        upload(bufferedImage);
    }

    public synchronized void upload(BufferedImage image) {
        if (image == null) {
           // WxytTransit.LOGGER.warn("BufferedImage is null, skipping upload GraphicsTexture");
            return;
        }

        if (image.getWidth() != width || image.getHeight() != height) {
           // WxytTransit.LOGGER.warn("BufferedImage size does not match GraphicsTexture size: expect(" + width + "," + height + ") actual(" + image.getWidth() + "," + image.getHeight() + ")," + "skipping upload GraphicsTexture");
            return;
        }

        if (isClosed || dynamicTexture.getPixels() == null) {
           // WxytTransit.LOGGER.info("GraphicsTexture already closed");
            return;
        }

        if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
           // WxytTransit.LOGGER.warn("Image type is not TYPE_INT_ARGB, converting...");
            image = createArgbBufferedImage(image);
        }

        Raster raster = image.getRaster();
        if (raster == null) {
           // WxytTransit.LOGGER.error("Image raster is null");
            return;
        }

        DataBuffer dataBuffer = raster.getDataBuffer();
        if (!(dataBuffer instanceof DataBufferInt)) {
           // WxytTransit.LOGGER.error("Image data buffer is not an instance of DataBufferInt");
            return;
        }

        IntBuffer imgData = IntBuffer.wrap(((DataBufferInt) dataBuffer).getData());
        if (imgData == null || imgData.remaining() < width * height) {
           // WxytTransit.LOGGER.error("Image data buffer is null or insufficient data");
            return;
        }

        NativeImage nativeImage = dynamicTexture.getPixels();
        if (nativeImage == null) {
           // WxytTransit.LOGGER.error("DynamicTexture pixels are null");
            return;
        }

        long pixelAddr = ((NativeImageAccessor) (Object) nativeImage).getPixels();
        if (pixelAddr == 0) {
           // WxytTransit.LOGGER.error("Pixel address is 0");
            return;
        }

        ByteBuffer target = MemoryUtil.memByteBuffer(pixelAddr, width * height * 4);
        if (target == null || target.remaining() < width * height * 4) {
           // WxytTransit.LOGGER.error("Target ByteBuffer is null or insufficient size");
            return;
        }

        for (int i = 0; i < width * height; i++) {
            // ARGB to RGBA
            int pixel = imgData.get();
            target.put((byte) ((pixel >> 16) & 0xFF)); // R
            target.put((byte) ((pixel >> 8) & 0xFF));  // G
            target.put((byte) (pixel & 0xFF));         // B
            target.put((byte) ((pixel >> 24) & 0xFF));  // A
        }

        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> {
                try {
                    dynamicTexture.upload();
                } catch (Exception e) {
                   // WxytTransit.LOGGER.error("Failed to upload texture", e);
                }
            });
        } else {
            try {
                dynamicTexture.upload();
            } catch (Exception e) {
               // WxytTransit.LOGGER.error("Failed to upload texture", e);
            }
        }
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void close() {
        close(10000);
    }

    public void close(int delay) {
        if (isClosed) {
           // WxytTransit.LOGGER.info("GraphicsTexture already closed");
            return;
        }
        isClosed = true;
        graphics.dispose();
        Minecraft.getInstance().execute(() -> {
            try {
                Minecraft.getInstance().getTextureManager().release(identifier);
                dynamicTexture.close();
            } catch (Exception e) {
                // WxytTransit.LOGGER.error("Failed to close GraphicsTexture", e);
            }
        });
    }
}
