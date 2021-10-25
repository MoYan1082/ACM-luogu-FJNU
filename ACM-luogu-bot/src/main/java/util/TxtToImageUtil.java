package util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TxtToImageUtil {

    /**
     * 测试系统内的所有已安装的字体
     */
    public void showAllFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontList = ge.getAvailableFontFamilyNames();
        for (String s : fontList) {
            System.out.println(s);
        }
    }

    /**
     * 用于判断一个字符是不是中文字符
     * @param c 需要判断的字符
     * @return 如果是中文字符返回True，否则返回false
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 将文本写入.jpg文件
     * @param text 需要生成图片的文字
     * @param imagePath 图片保存的位置
     * @throws Exception 读写文件可能出现异常
     */
    public static synchronized void createImage(String text, String imagePath) throws IOException {
        Font font = new Font("宋体", Font.PLAIN, 50);
        File outFile = new File(imagePath);
        List<String> textList = List.of(text.split("\n"));

        int width = 0; // 计算图形的宽度和高度
        int height = font.getSize() * textList.size() + 7;
        for(int i = 0; i < textList.size(); i++) {
            Rectangle2D r = font.getStringBounds(textList.get(i), new FontRenderContext(
                    AffineTransform.getScaleInstance(1, 1),
                    false, false));
            width = Math.max(width, (int)Math.ceil(r.getWidth()));
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.OPAQUE);
        g = image.createGraphics();

        g.fillRect(0, 0, width, height);
        g.setColor(new Color(63, 144, 63));
        g.setFont(font);
        for(int i = 0; i < textList.size(); i++){
            String tmpText = textList.get(i);
            g.drawString(tmpText, 0, (i + 1) * font.getSize());
        }
        g.dispose();
        ImageIO.write(image, "jpg", outFile);
    }
}
