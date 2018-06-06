package com.xin.cryptography.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xin.cryptography.R;
import com.xin.cryptography.crypto.DSAUtil;
import com.xin.cryptography.crypto.RSAUtil;

import java.security.KeyPair;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;


public class RSAFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RSAFragment";

    private TextView tvInfo;

    private KeyPair rsaKeyPair;

    private static final String SRC_DATA = "你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好我叫辛奕你好好好好好";
    private String encryptedData;

    private String sign;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rsa, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View rootView) {
        rootView.findViewById(R.id.btn_generate_keypair).setOnClickListener(this);
        rootView.findViewById(R.id.btn_get_keypair_from_file).setOnClickListener(this);
        rootView.findViewById(R.id.btn_rsa_encrypt_use_pub).setOnClickListener(this);
        rootView.findViewById(R.id.btn_rsa_decrypt_use_pri).setOnClickListener(this);
        rootView.findViewById(R.id.btn_rsa_encrypt_use_pri).setOnClickListener(this);
        rootView.findViewById(R.id.btn_rsa_decrypt_use_pub).setOnClickListener(this);

        rootView.findViewById(R.id.btn_rsa_sign_use_pri).setOnClickListener(this);
        rootView.findViewById(R.id.btn_rsa_verify_use_pub).setOnClickListener(this);

        tvInfo = rootView.findViewById(R.id.tv_info);
    }

    @Override
    public void onClick(View v) {
        long startTime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.btn_generate_keypair:
                rsaKeyPair = RSAUtil.generateRSAKeyPair();
                tvInfo.setText(String.format("%s ms", System.currentTimeMillis() - startTime));
                break;

            case R.id.btn_get_keypair_from_file:
                break;

            // 公钥加密 私钥解密
            case R.id.btn_rsa_encrypt_use_pub:
                encryptedData = RSAUtil.encrypt(SRC_DATA, rsaKeyPair.getPublic());
                Toast.makeText(getActivity(), "加密完成", Toast.LENGTH_SHORT).show();
                buildInfoContent(SRC_DATA, System.currentTimeMillis() - startTime);
                break;

            case R.id.btn_rsa_decrypt_use_pri:
                String decrypt = RSAUtil.decrypt(encryptedData, rsaKeyPair.getPrivate());
                Toast.makeText(getActivity(), decrypt, Toast.LENGTH_SHORT).show();
                buildInfoContent(SRC_DATA, System.currentTimeMillis() - startTime);
                break;

            // 私钥加密 公钥解密
            case R.id.btn_rsa_encrypt_use_pri:
                encryptedData = RSAUtil.encrypt(SRC_DATA, rsaKeyPair.getPrivate());
                Toast.makeText(getActivity(), "加密完成", Toast.LENGTH_SHORT).show();
                buildInfoContent(SRC_DATA, System.currentTimeMillis() - startTime);
                break;

            case R.id.btn_rsa_decrypt_use_pub:
                String decr = RSAUtil.decrypt(encryptedData, rsaKeyPair.getPublic());
                Toast.makeText(getActivity(), decr, Toast.LENGTH_SHORT).show();
                buildInfoContent(SRC_DATA, System.currentTimeMillis() - startTime);
                break;

            // 签名与验签 私钥签名 公钥验签
            case R.id.btn_rsa_sign_use_pri:
                sign = RSAUtil.sign(SRC_DATA.getBytes(), rsaKeyPair.getPrivate());
                Log.wtf(TAG, "签名值=> " + sign);
                break;

            case R.id.btn_rsa_verify_use_pub:
//                boolean verify = RSAUtil.verify(SRC_DATA.getBytes(), rsaKeyPair.getPublic(), sign);
//                Toast.makeText(getActivity(), "验签结果# " + verify, Toast.LENGTH_SHORT).show();

                KeyPair keyPair = DSAUtil.generateKeyPair();
                if(keyPair == null) return;
                DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();
                DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();

                String signVal = DSAUtil.sign(SRC_DATA.getBytes(), privateKey);
                Log.wtf(TAG, "签名值= " + signVal);
                boolean verify = DSAUtil.verify(SRC_DATA.getBytes(), publicKey, signVal);
                Toast.makeText(getActivity(), verify?"验签通过":"验签失败", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void buildInfoContent(String srcData, long costTime) {
        byte[] encode = Base64.encode(srcData.getBytes(), Base64.DEFAULT);
        tvInfo.setText("原文长度= " + encode.length + "\n" + "耗时= " + costTime + " ms");
    }
}
