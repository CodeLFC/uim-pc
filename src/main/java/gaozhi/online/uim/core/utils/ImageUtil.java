package gaozhi.online.uim.core.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 图像工具
 * @date 2022/4/2 19:20
 */
public class ImageUtil {
    public static Image readURL(String url) throws IOException {
        return ImageIO.read(new URL(url));
    }
}
