package com.bicycle.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class CaptchaUtil {

    /**
     * 将图片转换为base64字符串
     * @param image BufferedImage
     * @return String
     * */
    public static String convertImageToBase64(BufferedImage image){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            /**
             * 将image对象一jpeg的格式输出到bos流中
             * jpg：指定格式为jpeg
             * bos：一个内存中的字节数组输出流，，允许将字节写入到内存中，在需要的时候将其转换为字节数组
             * */
            ImageIO.write(image, "jpg", bos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
}
