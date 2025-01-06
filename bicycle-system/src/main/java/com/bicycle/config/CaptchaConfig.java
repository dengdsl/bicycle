package com.bicycle.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 图片验证码配置类
 * */
@Configuration
public class CaptchaConfig {

    // 将DefaultKaptcha对象注入Spring IOC容器中
    @Bean
    public DefaultKaptcha captchaProducer(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // 图片边框
        properties.setProperty("kaptcha.border", "yes");
        // 边框颜色
        properties.setProperty("kaptcha.border.color", "black");
        //边框厚度
        properties.setProperty("kaptcha.border.thickness", "1");
        // 图片宽
        properties.setProperty("kaptcha.image.width", "160");
        // 图片高
        properties.setProperty("kaptcha.image.height", "60");
        //图片实现类
        properties.setProperty("kaptcha.producer.impl", "com.google.code.kaptcha.impl.DefaultKaptcha");
        //文本实现类
        properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
        //文本集合，验证码值从此集合中获取
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        //验证码长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        // 字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "38");
        //文字间隔
        properties.setProperty("kaptcha.textproducer.char.space", "4");
        //干扰实现类
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        //干扰颜色
        properties.setProperty("kaptcha.noise.color", "black");
        //干扰图片样式
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        //背景实现类
        properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        //背景颜色为渐变，开始颜色： #C4C4C4， 结束颜色： #F9F9F9
        properties.setProperty("kaptcha.background.type", "gradient");
        properties.setProperty("kaptcha.background.clear.from", "lightGray");
        properties.setProperty("kaptcha.background.clear.to", "white");
        //文字渲染器
        properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
