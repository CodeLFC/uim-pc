package gaozhi.online.uim.example.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO
 * @date 2022/4/13 12:32
 */
public class ImageUtil {
    public static Image getScaleImage(Image image, int newSize) {
        return getScaleImage(image, newSize, 0);
    }

    public static Image getScaleImage(Image image, int newSize, int operation) {
        if (image == null)
            return null;
        int iWidth = image.getWidth(null);
        int iHeight = image.getHeight(null);
        int hints = Image.SCALE_SMOOTH;
        switch (operation) {
            case 1:// 按宽度缩放
                return image.getScaledInstance(newSize, (newSize * iHeight) / iWidth, hints);
            case 2:// 按高度缩放
                return image.getScaledInstance((newSize * iWidth) / iHeight, newSize, hints);
            default:// 哪边大按哪边缩放
                if (iWidth > iHeight) {
                    return image.getScaledInstance(newSize, (newSize * iHeight) / iWidth, hints);
                } else {
                    return image.getScaledInstance((newSize * iWidth) / iHeight, newSize, hints);
                }
        }
    }

    public static Image convertCircular(Image image, int radius) {
        return convertCircular(toBufferedImage(image), radius);
    }

    /**
     * @description: TODO 裁剪为圆形
     * @author LiFucheng
     * @date 2022/4/13 12:34
     * @version 1.0
     */
    public static BufferedImage convertCircular(BufferedImage bi1, int radius) {
        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(radius, radius, BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, radius, radius);
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(bi1, 0, 0, null);
        g2.dispose();
        return bi2;

    }

    /**
     * 将image对象 转成 BufferedImage
     *
     * @param image
     * @return
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // 获取 Image 对象的高度和宽度
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        //通过 BufferedImage 绘制图像并保存在其对象中
        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return bi;
    }
}
