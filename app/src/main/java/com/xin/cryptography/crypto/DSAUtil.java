package com.xin.cryptography.crypto;

import android.util.Base64;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;

public class DSAUtil {

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(byte[] src, DSAPrivateKey privateKey) {
        byte[] encode = Base64.encode(src, Base64.DEFAULT);
        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(encode);
            byte[] signVal = signature.sign();
            return Base64.encodeToString(signVal, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(byte[] src, DSAPublicKey publicKey, String signVal) {
        byte[] encode = Base64.encode(src, Base64.DEFAULT);
        try {
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(publicKey);
            signature.update(encode);
            return signature.verify(Base64.decode(signVal, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
