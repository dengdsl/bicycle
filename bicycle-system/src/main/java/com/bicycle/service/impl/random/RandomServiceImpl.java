package com.bicycle.service.impl.random;

import com.bicycle.service.random.RandomService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class RandomServiceImpl implements RandomService {

    private static final String[] chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    /**
     * 用于生成20位随机id
     */
    @Override
    public String randomId(String prefix) {
        // 获取当前时间戳的前十位数字
        LocalDate currentDate = LocalDate.now();
        String datePart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 计算需要补充的零的数量
        int zeroCount = Math.max(0, 16 - prefix.length() - datePart.length());

        // 生成零的部分
        StringBuilder zeros = new StringBuilder();
        for (int i = 0; i < zeroCount; i++) {
            zeros.append((int) (Math.random() * 10)); // 生成0-9之间的随机数
        }

        // 拼接字母、时间戳和零
        return prefix + datePart + zeros;
    }

    /**
     * 用于生成20位随机id
     */
    @Override
    public String randomId(String prefix, Integer len) {
        // 获取当前时间戳的前十位数字
        LocalDate currentDate = LocalDate.now();
        String datePart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 计算需要补充的零的数量
        int zeroCount = Math.max(0, len - prefix.length() - datePart.length());

        // 生成零的部分
        StringBuilder zeros = new StringBuilder();
        for (int i = 0; i < zeroCount; i++) {
            zeros.append((int) (Math.random() * 10)); // 生成0-9之间的随机数
        }

        // 拼接字母、时间戳和零
        return prefix + datePart + zeros;
    }

    /**
     * 生成32位随机二维码编码
     * */
    @Override
    public String randomQrcode() {
        StringBuilder qrcode = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            Random random = new Random();
            // 随机选择一个字符
            String randomChar = chars[random.nextInt(chars.length)];
            qrcode.append(randomChar);
        }
        return qrcode.toString();
    }
}
