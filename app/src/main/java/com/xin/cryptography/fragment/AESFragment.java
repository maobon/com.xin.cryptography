package com.xin.cryptography.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xin.cryptography.R;
import com.xin.cryptography.crypto.AESUtil;


public class AESFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AESFragment";
    private String str;
    private static final String SRC_DATA = "这是一段很长很长的文字这是一段很长很长的文字这是一段很长很长的文字这是一段很长很长的文字这是一段很长很长的文字";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aes, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View view) {
        view.findViewById(R.id.btn_aes_encrypt).setOnClickListener(this);
        view.findViewById(R.id.btn_aes_decrypt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_aes_encrypt:
                str = AESUtil.encrypt(SRC_DATA, "buzhidao", "123");
                Toast.makeText(getActivity(), "加密完成", Toast.LENGTH_SHORT).show();
                Log.wtf(TAG, "data encrypted = " + str);
                break;

            case R.id.btn_aes_decrypt:
                String decrypt = AESUtil.decrypt(str, "buzhidao", "123");
                Toast.makeText(getActivity(), decrypt, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
