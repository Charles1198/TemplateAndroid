package com.charles.user.function.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charles.common.base.BaseFragment;
import com.charles.user.R;
import com.charles.user.function.setting.SettingActivity;

/**
 * @author charles
 * @date 2018/10/4
 * @description
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_user, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.user_setting_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.user_setting_btn) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        }
    }
}
