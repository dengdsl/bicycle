package com.medical.validate.account;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class LoginValidate {

    /**登录账号**/
    @NotNull
    private String account;

    /**登录密码**/
    @NotNull
    private String password;

    /**图片验证码**/
    @NotNull
    private String code;

    /**图片验证码获取的key**/
    @NotNull
    private String captchaKey;

    /**
     * 将集合中的属性转换对象属性
     * */
    public LoginValidate to(JSONObject loginValidate) {
        try {
            this.setAccount((String) loginValidate.get("account"));
            this.setPassword((String) loginValidate.get("password"));
            this.setCode((String) loginValidate.get("code"));
            this.setCaptchaKey((String) loginValidate.get("captchaKey"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
