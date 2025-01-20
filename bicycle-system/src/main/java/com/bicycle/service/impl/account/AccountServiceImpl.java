package com.bicycle.service.impl.account;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.validate.user.SettingUserValidate;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.bicycle.entry.account.SystemAccountEntry;
import com.bicycle.entry.menus.SystemMenusEntry;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.mapper.account.SystemAccountMapper;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.mapper.menus.SystemMenuMapper;
import com.bicycle.service.account.IAccountService;
import com.bicycle.utils.*;
import com.bicycle.validate.account.LoginValidate;
import com.bicycle.vo.account.SystemAdminAuthVo;
import com.bicycle.vo.account.SystemLoginVo;
import com.bicycle.vo.account.SystemUserVo;
import com.bicycle.vo.captcha.CaptchaVo;
import com.bicycle.vo.menus.SystemMenusVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户登录相关相关接口
 * */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    private static final String CAPTCHA_PREFIX = "captcha_";
    private static final String TOKEN_PREFIX = "token_admin_";

    // 注入验证码对象
    @Resource
    private DefaultKaptcha defaultKaptcha;

    // 用户mapper
    @Resource
    private SystemAccountMapper mapper;

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Resource
    private SystemMenuMapper menuMapper;



    /**
     * 获取图片验证码
    * */
    @Override
    public AjaxResult<Object> captcha() {
        // 生成文字验证码
        String text = defaultKaptcha.createText();
        log.info("生成的文字验证码为：" + text);
        // 生成图片验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        // 将图片转换为base64字符串
        String imageBase64 = "data:image/jpeg;base64," + CaptchaUtil.convertImageToBase64(image);
        log.info("生成的验证码图片："+ imageBase64);
        // 将图片验证码设置到redis缓存中， 有效期为5秒钟
        UUID uuid = UUID.randomUUID();
        String key = CAPTCHA_PREFIX + uuid;
        RedisUtil.setWithExpiration(key, text, 5000L);
        // 返回图片验证码
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setKey(key);
        captchaVo.setUrl(imageBase64);
        return AjaxResult.success(captchaVo);
    }

    /**
     * 登录
     * */
    @Override
    public AjaxResult<Object> login(String loginValidate) {
        // 将解密后的字符串转换为登录请求参数对象
        JSONObject jsonObject = JSON.parseObject(loginValidate);
        LoginValidate validate = new LoginValidate().to(jsonObject);
        String account = validate.getAccount();
        String password = validate.getPassword();
        String code = validate.getCode();
        String captchaKey = validate.getCaptchaKey();
        // 从redis环境中获取图片验证码
        Boolean exists = RedisUtil.exists(captchaKey);
        // 验证码已过期
        if (!exists) {
            return AjaxResult.failed(HttpEnum.CAPTCHA_ERROR.getCode(), "验证码已过期");
        }
        String redisCode = (String) RedisUtil.get(captchaKey);
        // 验证码错误
        if (!code.equals(redisCode)) {
            return AjaxResult.failed(HttpEnum.CAPTCHA_ERROR.getCode(), "验证码错误");
        }
        QueryWrapper<SystemAccountEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        // 获取用户信息
        SystemAccountEntry systemUserEntry = mapper.selectOne(queryWrapper);
        // 用户已禁用
        if (systemUserEntry.getUserState() == 1) {
            return AjaxResult.failed(HttpEnum.ACCOUNT_DISABLE_ERROR.getCode(), HttpEnum.ACCOUNT_DISABLE_ERROR.getMessage());
        }
        try {
            //String encrypt = RSAUtils.encrypt(systemUserEntry.getPassword());
            //String pas = RSAUtils.decrypt(encrypt);
            //// 将数据库密码进行解密
            //String oldPassword = RSAUtils.decrypt(systemUserEntry.getPassword());
            String oldPassword = systemUserEntry.getPassword();
            // 对比机密之后的密码是否相同
            if (!password.equals(oldPassword)) {
                return AjaxResult.failed(HttpEnum.PASSWORD_LOGIN_ERROR.getCode(), HttpEnum.PASSWORD_LOGIN_ERROR.getMessage());
            }
            // 实现账号登录
            StpUtil.login(systemUserEntry.getId());
            // 获取登录token
            String tokenValue = StpUtil.getTokenValue();
            // 将token信息存入redis缓存
            RedisUtil.setWithExpiration(TOKEN_PREFIX + systemUserEntry.getAccount(), tokenValue);
            SystemLoginVo loginVo = new SystemLoginVo();
            loginVo.setId(systemUserEntry.getId());
            loginVo.setToken(tokenValue);
            log.info("\n登录成功：account=" + systemUserEntry.getAccount() + TOKEN_PREFIX + systemUserEntry.getAccount() + "：" + loginVo);
            return AjaxResult.success(loginVo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 退出登录
     * */
    @Override
    public AjaxResult<Object> logout(String token) {
        return null;
    }

    /**
     * 获取配置信息
     * */
    @Override
    public AjaxResult<Object> config() {
        SystemConfigEntry webName = systemConfigMapper.getConfigByName("webName");
        SystemConfigEntry webLogo = systemConfigMapper.getConfigByName("webLogo");
        SystemConfigEntry webFavicon = systemConfigMapper.getConfigByName("webFavicon");
        SystemConfigEntry filings = systemConfigMapper.getConfigByName("filings");
        SystemConfigEntry loginBg = systemConfigMapper.getConfigByName("loginBg");
        SystemConfigEntry theme = systemConfigMapper.getConfigByName("theme");
        SystemConfigEntry mobileTheme = systemConfigMapper.getConfigByName("mobileTheme");
        SystemConfigEntry filingsName = systemConfigMapper.getConfigByName("filingsName");
        SystemConfigEntry loginFooterBg = systemConfigMapper.getConfigByName("loginFooterBg");

        Map<String, String> config = new HashMap<>();
        config.put("webName", webName.getValue());
        config.put("webLogo", webLogo.getValue());
        config.put("webFavicon", webFavicon.getValue());
        config.put("filings", filings.getValue());
        config.put("filingsName", filingsName.getValue());
        config.put("loginBg", loginBg.getValue());
        config.put("theme", theme.getValue());
        config.put("mobileTheme", mobileTheme.getValue());
        config.put("loginFooterBg", loginFooterBg.getValue());
        return AjaxResult.success(config);
    }

    /**
     * 获取个人信息
     * */
    @Override
    public AjaxResult<Object> self(Long adminId) {
        // 获取用户信息
        SystemAccountEntry userEntry = mapper.selectOne(new QueryWrapper<SystemAccountEntry>().eq("id", adminId).eq("user_state", 0));
        SystemUserVo userVo = new SystemUserVo();
        BeanUtils.copyProperties(userEntry, userVo);

        // 判断用户是否是超级管理员，超级管理员将所有的权限返回
        List<String> permissions = new LinkedList<>();
        if (userEntry.getId() != 1) {
            // 从数据库中查询权限列表
            List<SystemMenusEntry> systemMenusEntries = menuMapper.queryUserPermission(userEntry.getId());
            if (systemMenusEntries != null && systemMenusEntries.size() > 0) {
                for (SystemMenusEntry systemMenusEntry : systemMenusEntries) {
                    permissions.add(systemMenusEntry.getPerms());
                }
            }else{
                permissions.add("");
            }
        }else {
            permissions.add("*");
        }
        // 返回数据
        SystemAdminAuthVo  adminAuthVo = new SystemAdminAuthVo();
        adminAuthVo.setUser(userVo);
        adminAuthVo.setPermissions(permissions);
        return AjaxResult.success(adminAuthVo);
    }

    /**
     * 获取菜单列表
     * */
    @Override
    public AjaxResult<Object> routes(Long adminId) {
            List<SystemMenusEntry> systemMenusEntries;
            // 如果是超级管理员，查询所有菜单
            if (adminId == 1) {
                QueryWrapper<SystemMenusEntry> wrapper = new QueryWrapper<>();
                wrapper.ne("menu_type", "A");
                wrapper.orderByDesc("menu_sort");
                systemMenusEntries = menuMapper.selectList(wrapper);
            }else {
                // 查询用户菜单列表
                systemMenusEntries = menuMapper.queryUserRoutes(adminId);
                // 是否存在菜单列表
            }
            List<SystemMenusVo> menusVoList = new ArrayList<>();
            for (SystemMenusEntry systemMenusEntry : systemMenusEntries) {
                SystemMenusVo menusVo = new SystemMenusVo();
                menusVo.setId(systemMenusEntry.getId());
                menusVo.setPid(systemMenusEntry.getPid());
                menusVo.setMenuType(systemMenusEntry.getMenuType());
                menusVo.setMenuName(systemMenusEntry.getMenuName());
                menusVo.setMenuIcon(systemMenusEntry.getMenuIcon());
                menusVo.setMenuSort(systemMenusEntry.getMenuSort());
                menusVo.setPerms(systemMenusEntry.getPerms());
                menusVo.setPaths(systemMenusEntry.getPaths());
                menusVo.setComponent(systemMenusEntry.getComponent());
                menusVo.setIsCache(systemMenusEntry.getIsCache());
                menusVo.setIsShow(systemMenusEntry.getIsShow());
                menusVo.setIsDisable(systemMenusEntry.getIsDisable());
                menusVo.setSelected(systemMenusEntry.getSelected());
                menusVo.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(systemMenusEntry.getUpdateTime()));
                menusVoList.add(menusVo);
            }
            String jsonString = JSONArray.toJSONString(menusVoList, SerializerFeature.WriteMapNullValue);
            JSONArray jsonArray = JSONArray.parseArray(jsonString);
            // 调用工具类将菜单列表转换为树型结构
            JSONArray jsonArrayList = ArrayUtils.listToTree(jsonArray, "id", "pid", "children");
            return AjaxResult.success(jsonArrayList);
    }

    /**
     * 管理员设置自身信息
     * */
    @Override
    public AjaxResult<Object> updateUserInfo(SettingUserValidate userValidate) {
        String loginId = (String) StpUtil.getLoginId();
        QueryWrapper<SystemAccountEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", Long.parseLong(loginId));
        queryWrapper.eq("account", userValidate.getAccount());
        SystemAccountEntry userVo = mapper.selectOne(queryWrapper);
        if (userVo == null) {
            return AjaxResult.failed(HttpEnum.NO_PERMISSION.getCode(), "账号不存在或无权修改别人的账号");
        }

        // 密码不正确
        if ((userValidate.getPassword() != null && !userValidate.getPassword().isEmpty()) && !userVo.getPassword().equals(userValidate.getCurrPassword())) {
            return AjaxResult.failed(HttpEnum.FAILED.getCode(), "原密码不正确");
        }

        // 验证新密码和再次输入的密码是否相等
        if (userValidate.getPassword() != null && userValidate.getPasswordConfirm() != null && !userValidate.getPassword().isEmpty() && !userValidate.getPasswordConfirm().isEmpty() &&  !userValidate.getPassword().equals(userValidate.getPasswordConfirm())) {
            return AjaxResult.failed(HttpEnum.FAILED.getCode(), "两次输入的密码不一致");
        }

        // 更新用户信息
        userVo.setUsername(userValidate.getUsername());
        userVo.setAvatar(userValidate.getAvatar());
        if (userValidate.getPassword() != null && userValidate.getPasswordConfirm() != null && !userValidate.getPassword().isEmpty() && !userValidate.getPasswordConfirm().isEmpty()) {
            userVo.setPassword(userValidate.getPasswordConfirm());
        }
        userVo.setUpdateTime(new Date());
        mapper.updateById(userVo);
        StpUtil.logout();
        return AjaxResult.success("更新成功");
    }

}
