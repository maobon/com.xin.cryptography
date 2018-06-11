package com.xin.cryptography.crypto;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    // /** 算法/模式/填充 **/
    private static final String TRANSFORMATION = "AES/CFB/NoPadding";

    /**
     * @param str
     * @return
     */
    public static SecretKeySpec createKey(String str) {
        StringBuilder sb = new StringBuilder(16);
        sb.append(str);
        while (sb.length() < 16) {
            sb.append(0);
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        return new SecretKeySpec(sb.toString().getBytes(), "AES");
    }

    /**
     * @param str
     * @return
     */
    public static IvParameterSpec createIV(String str) {
        StringBuilder sb = new StringBuilder(16);
        sb.append(str);
        while (sb.length() < 16) {
            sb.append(0);
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }
        return new IvParameterSpec(sb.toString().getBytes());
    }

    public static String encrypt(String src, String password, String IV) {
        SecretKeySpec keySpec = createKey(password);
        IvParameterSpec IVSpec = createIV(IV);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IVSpec);
            byte[] encrypted = cipher.doFinal(src.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedData, String password, String IV) {
        SecretKeySpec keySpec = createKey(password);
        IvParameterSpec IVSpec = createIV(IV);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, IVSpec);
            byte[] result = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
