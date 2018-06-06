package com.xin.cryptography.crypto;

import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {

    private static final String ALG = "RSA";
    private static final int RSA_KEY_SIZE = 2048;

    // Android RSA 默认Transformation
    private static final String RSA_DEFAULT_TRANSFORMATION_IN_ANDROID = "RSA/NONE/PKCS1Padding";
    // Base64 常见配置
    private static final int BASE64_MODE = Base64.DEFAULT;

    /**
     * RSA 密钥对生成
     *
     * @return 公私钥对生成
     */
    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALG);
            generator.initialize(RSA_KEY_SIZE);
            return generator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加解密通用方法
     * 使用cipher执行
     *
     * @param data 待处理数据 byte[]类型
     * @param key  秘钥
     * @param mode operation mode
     * @return 执行结果
     * @throws Exception 执行过程中发生的异常
     */
    private static byte[] process(byte[] data, Key key, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_DEFAULT_TRANSFORMATION_IN_ANDROID);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param content 待加密内容
     * @param key     秘钥
     * @return 加密后内容 Base64编码后的String
     */
    public static String encrypt(String content, Key key) {
        try {
            // 待加密内容转为Base64 byte[]
            byte[] encode = Base64.encode(content.getBytes(), BASE64_MODE);
            // 执行加密
            byte[] result = process(encode, key, Cipher.ENCRYPT_MODE);
            // 将结果使用Base64进行编码 转为String类型
            return Base64.encodeToString(result, BASE64_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     *
     * @param content 待解密内容
     * @param key     秘钥
     * @return String
     */
    public static String decrypt(String content, Key key) {
        try {
            // 待解密数据使用Base64解码 转为byte[]
            byte[] decode = Base64.decode(content, BASE64_MODE);
            // 执行解密
            byte[] result = process(decode, key, Cipher.DECRYPT_MODE);
            // 解密结果使用Base64解码后转为String
            return new String(Base64.decode(result, BASE64_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据字符串生成RSA公钥
     *
     * @param publicKey RSA公钥 String类型
     * @return PublicKey
     */
    public static PublicKey getPubKeyFromStr(String publicKey) {
        // base64解码转成byte[]
        byte[] keyDecoded = Base64.decode(publicKey, BASE64_MODE);
        // 公钥是符合x509标准的, 构造keySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyDecoded);
        try {
            // 生成RSA公钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALG);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据字符串生成RSA私钥
     *
     * @param privateKey RSA私钥 String类型
     * @return PrivateKey
     */
    public static PrivateKey getPriKeyFromStr(String privateKey) {
        // 以Base64格式解码
        byte[] keyDecoded = Base64.decode(privateKey, BASE64_MODE);
        // 创建私钥 keySpec
        // PKCS8: 描述私有密钥信息格式，该信息包括公开密钥算法的私有密钥以及可选的属性集等
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyDecoded);
        try {
            // 生成RSA私钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALG);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 对数据进行签名
     *
     * @param content
     * @return
     */
    // ## 一般都是私钥签名 公钥验签 ##
    public static String sign(byte[] content, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(privateKey);
            signature.update(Base64.encode(content, BASE64_MODE));
            byte[] signed = signature.sign();
            return Base64.encodeToString(signed, BASE64_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证数据签名
     *
     * @param content
     * @param signVal
     * @return
     */


    public static boolean verify(byte[] content, PublicKey publicKey, String signVal) {
        try {
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
            signature.update(Base64.encode(content, BASE64_MODE));
            return signature.verify(Base64.decode(signVal, BASE64_MODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
