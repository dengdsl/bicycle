package com.medical.service.impl.random;

import com.medical.service.random.RandomService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RandomServiceImpl implements RandomService {

    /**
     * 用于生成20位随机id
     * */
    @Override
    public String randomId(String prefix) {
        // 获取当前时间戳的前十位数字
        String timestamp = Long.toString(Instant.now().getEpochSecond()).substring(0, 10);

        // 计算需要补充的零的数量
        int zeroCount = Math.max(0, 20 - prefix.length() - timestamp.length());

        // 生成零的部分
        StringBuilder zeros = new StringBuilder();
        for (int i = 0; i < zeroCount; i++) {
            zeros.append((int) (Math.random() * 10)); // 生成0-9之间的随机数
        }

        // 拼接字母、时间戳和零
        return prefix + timestamp + zeros;
    }
}
