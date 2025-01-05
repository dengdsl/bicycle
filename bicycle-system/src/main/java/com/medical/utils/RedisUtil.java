package com.medical.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static StringRedisTemplate redisTemplate;

    /**
     * 通过构造方法注入redisTemplate对象
     * */
    public RedisUtil(StringRedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }


    /**
     * 将数据保存到redis中，设置过期时间
     * @param key 键名
     * @param value 要保存的值
     * @param expire 过期时间
     * */
    public static void setWithExpiration(String key, Object value, Long expire){
        if (expire > 0) {
            redisTemplate.opsForValue().set(key, value.toString(), expire, TimeUnit.SECONDS);
        }else {
            setWithExpiration(key, value);
        }
    }

    /**
     * 将数据保存到redis中，不设置过期时间
     * @param key 键名
     * @param value 要保存的值
     * */
    public static void setWithExpiration(String key, Object value){
            redisTemplate.opsForValue().set(key, value.toString());
    }

    /**
     * 删除redis中的一个或者多个键
     * @param key
     * */
    @SuppressWarnings("unchecked")
    public static void delete(String... key) {
        if (key.length == 1) {
            redisTemplate.delete(key[0]);
        }else {
            redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
        }
    }

    /**
     * 获取key的值
     *
     * @param key 键
     * @return Object
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 给key赋值一个新的key名
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     */
    public static void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key 键
     * @param item 项
     * @param value 值
     */
    public static void hSet(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param second 时间(秒)
     */
    public static void expire(String key, Long second) {
        redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true=存在,false=不存在
     */
    public static Boolean exists(String key) {
        if (key == null) {
            throw new HttpMessageNotReadableException("captchaKey必填");
        }
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取key的值，admin用户信息
     *
     * @param key 键
     * @return Object
     */
    public static Object getAdmin(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取key的值，其他用户信息
     *
     * @param key 键
     * @return Object
     */
    public static Object getSessionUser(String key) {
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate.opsForValue().get(key);
    }

}
