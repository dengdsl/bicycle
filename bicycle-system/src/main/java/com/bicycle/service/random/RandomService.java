package com.bicycle.service.random;

public interface RandomService {

    /**
     * 随机生成16位编号
     * */
    String randomId(String prefix);

    /**
     * 随机生成指定长度位编号
     * */
    String randomId(String prefix, Integer len);

    /**
     * 生成32为随机的二维码编号
     * */
    String randomQrcode();
}
