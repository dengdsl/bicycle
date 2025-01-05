package com.medical.utils;

import io.micrometer.common.util.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 备注，解密前台公钥加密的数据，请调用decryptWithPrivate方法
 * 每次重启之后，都会生成一个一对新的公私钥
 */

@Component
public class RSAUtils {

    //秘钥大小
    private static final int KEY_SIZE = 2048;

    //后续放到常量类中去
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String FILE_NAME = "public_private.pem";
    public static final String KEY_FILE_PATH = Objects.requireNonNull(RSAUtils.class.getClassLoader().getResource("")).getPath();
    private static KeyPair keyPair;

    /**存放公钥和密钥**/
    private static Map<String, String> rsaMap;

    private static BouncyCastleProvider bouncyCastleProvider = null;

    //BouncyCastleProvider内的方法都为静态方法，GC不会回收
    public static synchronized BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }

    //生成RSA，并存放
    static {
        try {
            // 判断公钥私钥文件是否存在
            File keyFile = new File(KEY_FILE_PATH + FILE_NAME);
            if (keyFile.exists()) {
                loadRSAFromKeyFile(keyFile);
            }else {
                //解决方案
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", getInstance());
                SecureRandom random = new SecureRandom();
                generator.initialize(KEY_SIZE, random);
                keyPair = generator.generateKeyPair();
                //将公钥和私钥存放，登录时会不断请求获取公钥
                //建议放到redis的缓存中，避免在分布式场景中，出现拿着server1的公钥去server2解密的问题
                storeRSA(keyFile);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // 将RSA存入缓存
    private static void storeRSA(File keyFile) {
        rsaMap = new HashMap<>();
        // 公钥
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        rsaMap.put(PUBLIC_KEY, publicKeyStr);

        // 私钥
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
        rsaMap.put(PRIVATE_KEY, privateKeyStr);

        // 将密钥对写入文件
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFile))) {
            oos.writeObject(keyPair);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从密钥对文件中加载RSA
    private static void loadRSAFromKeyFile(File keyFile) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile))) {
            keyPair = (KeyPair) ois.readObject();

            // 将加载的密钥对存放到缓存
            rsaMap = new HashMap<>();
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
            rsaMap.put(PUBLIC_KEY, publicKeyStr);

            PrivateKey privateKey = keyPair.getPrivate();
            String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
            rsaMap.put(PRIVATE_KEY, privateKeyStr);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 私钥解密(解密前台公钥加密的密文)
     *
     * @param encryptText 公钥加密的数据
     * @return 私钥解密出来的数据
     * @throws Exception e
     */
    public static String decryptWithPrivate(String encryptText) throws Exception {
        if (StringUtils.isBlank(encryptText)) {
            return "";
        }
        byte[] en_byte = Base64.decodeBase64(encryptText.getBytes());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", getInstance());
        PrivateKey privateKey = keyPair.getPrivate();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] res = cipher.doFinal(en_byte);
        return new String(res);
    }

    ///**
    // * java端 使用公钥加密(此方法暂时用不到)
    // *
    // * @param plaintext 明文内容
    // * @return byte[]
    // */
    //public static byte[] encrypt(String plaintext)  {
    //    String encode = URLEncoder.encode(plaintext, StandardCharsets.UTF_8);
    //    RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
    //    //获取公钥指数
    //    BigInteger e = rsaPublicKey.getPublicExponent();
    //    //获取公钥系数
    //    BigInteger n = rsaPublicKey.getModulus();
    //    //获取明文字节数组
    //    BigInteger m = new BigInteger(encode.getBytes());
    //    //进行明文加密
    //    BigInteger res = m.modPow(e, n);
    //    return res.toByteArray();
    //
    //}
    //
    ///**
    // * java端 使用私钥解密(此方法暂时用不到)
    // *
    // * @param cipherText 加密后的字节数组
    // * @return 解密后的数据
    // */
    //public static String decrypt(byte[] cipherText) {
    //    RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
    //    // 获取私钥参数-指数/系数
    //    BigInteger d = prk.getPrivateExponent();
    //    BigInteger n = prk.getModulus();
    //    // 读取密文
    //    BigInteger c = new BigInteger(cipherText);
    //    // 进行解密
    //    BigInteger m = c.modPow(d, n);
    //    // 解密结果-字节数组
    //    byte[] mt = m.toByteArray();
    //    //转成String,此时是乱码
    //    String en = new String(mt);
    //    // 再进行编码,最后返回解密后得到的明文
    //    return URLDecoder.decode(en, StandardCharsets.UTF_8);
    //}
    /**
     * java端 使用公钥加密
     *
     * @param plaintext 明文内容
     * @return 加密后的字符串
     */
    public static String encrypt(String plaintext) {
        try {
            String encode = URLEncoder.encode(plaintext, StandardCharsets.UTF_8);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            // 获取公钥指数
            BigInteger e = rsaPublicKey.getPublicExponent();
            // 获取公钥系数
            BigInteger n = rsaPublicKey.getModulus();
            // 获取明文字节数组
            BigInteger m = new BigInteger(encode.getBytes());
            // 进行明文加密
            BigInteger res = m.modPow(e, n);
            // 返回Base64编码的加密结果
            return Base64.encodeBase64String(res.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * java端 使用私钥解密
     *
     * @param cipherText 加密后的Base64编码字符串
     * @return 解密后的数据
     */
    public static String decrypt(String cipherText) {
        try {
            RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
            // 获取私钥参数-指数/系数
            BigInteger d = prk.getPrivateExponent();
            BigInteger n = prk.getModulus();
            // 读取Base64编码的密文
            byte[] cipherBytes = Base64.decodeBase64(cipherText.getBytes());
            BigInteger c = new BigInteger(cipherBytes);
            // 进行解密
            BigInteger m = c.modPow(d, n);
            // 解密结果-字节数组
            byte[] mt = m.toByteArray();
            // 转成String
            String en = new String(mt, StandardCharsets.UTF_8);
            // 再进行解码，返回解密后得到的明文
            return URLDecoder.decode(en, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取公钥
     *
     * @return 公钥
     */
    public static String getPublicKey() {
        return rsaMap.get(PUBLIC_KEY);
    }

    /**
     * 获取私钥
     *
     * @return 私钥
     */
    public static String getPrivateKey() {
        return rsaMap.get(PRIVATE_KEY);
    }
}