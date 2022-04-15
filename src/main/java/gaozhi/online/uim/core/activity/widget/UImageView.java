package gaozhi.online.uim.core.activity.widget;

import com.github.pagehelper.util.StringUtil;
import gaozhi.online.uim.core.asynchronization.TaskExecutor;
import gaozhi.online.uim.core.utils.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  显示图片
 * @date 2022/3/15 10:36
 */
public class UImageView extends ULabel {
    private Image image;
    private String url;

    public UImageView() {
        this(null);
    }

    public UImageView(Image image) {
        setImage(image);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        if (image == null) {
            return;
        }
        setIcon(new ImageIcon(image));
    }

    public void setImageUrl(String url, int size, boolean circle) {
        setImageUrl(url, null, size, circle, false);
    }

    public void setImageUrl(String url, Image defaultImage, int size, boolean circle) {
        setImageUrl(url, defaultImage, size, circle, false);
    }

    public void setImageUrl(String url, Image defaultImage, int size, boolean circle, boolean reload) {
        if (url == null || url.isBlank()) {
            return;
        }
        if (!reload && url.equals(this.url)) {
            return;
        }
        new TaskExecutor().executeInBackThread(() -> {
            try {
                Image image = ImageUtil.readURL(url);
                Image scaleImage = ImageUtil.getScaleImage(image, size);
                if (circle) {
                    scaleImage = ImageUtil.convertCircular(image, size);
                }
                setImage(scaleImage);
                new TaskExecutor().executeInUIThread(() -> {

                });
            } catch (IOException e) {
                setImage(ImageUtil.getScaleImage(defaultImage, size));
                e.printStackTrace();
            }
        });
    }
}
