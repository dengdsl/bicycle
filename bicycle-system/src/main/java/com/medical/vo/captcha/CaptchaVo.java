package com.medical.vo.captcha;

import lombok.Data;

@Data
public class CaptchaVo {

    /**图片验证码的key**/
    private String key;
    /**图片验证码base64字符串**/
    private String url;

}
