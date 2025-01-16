package com.bicycle.utils;

import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.exception.CustomException;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 生成二维码
 */
@Component
public class QrCodeUtils {

    private static SystemConfigMapper systemConfigMapper;

    // 构造函数注入
    public QrCodeUtils(SystemConfigMapper systemConfigMapper) {
        QrCodeUtils.systemConfigMapper = systemConfigMapper;
    }

    /**
     * 根据自行车信息生成二维码
     */
    public static void generateQRCode(String text, String filePath) {
        try {
            SystemConfigEntry qrcodeHeight = systemConfigMapper.getConfigByName("qrcodeHeight");
            SystemConfigEntry qrcodeWidth = systemConfigMapper.getConfigByName("qrcodeWidth");
            SystemConfigEntry qrcodeFontSize = systemConfigMapper.getConfigByName("qrcodeFontSize");
            SystemConfigEntry qrcodeMargin = systemConfigMapper.getConfigByName("qrcodeMargin");
            int fontSize = Integer.parseInt(qrcodeFontSize.getValue());
            int margin = Integer.parseInt(qrcodeMargin.getValue());
            int width = Integer.parseInt(qrcodeWidth.getValue());
            int height = Integer.parseInt(qrcodeHeight.getValue());
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // 添加文字说明
            int textHeight = fontSize + margin;
            BufferedImage combinedImage = new BufferedImage(width, height + textHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = combinedImage.createGraphics();
            g.setColor(Color.WHITE); // 背景颜色
            g.fillRect(0, 0, combinedImage.getWidth(), combinedImage.getHeight());
            // 绘制二维码
            g.drawImage(qrImage, 0, 0, null);
            // 绘制文字
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics fontMetrics = g.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(text);
            int x = (width - textWidth) / 2; // 居中对齐
            int y = height - fontSize / 2;
            g.drawString(text, x, y);
            g.dispose();
            // 保存最终图像
            ImageIO.write(combinedImage, "PNG", new File(filePath));
        } catch (WriterException | IOException e) {
            throw new CustomException(HttpEnum.FAILED.getCode(), "生成二维码失败: " + e.getMessage());
        }
    }
}
