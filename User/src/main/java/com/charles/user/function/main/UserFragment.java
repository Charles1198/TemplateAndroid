package com.charles.user.function.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charles.common.base.BaseFragment;
import com.charles.common.kv.Kv;
import com.charles.common.kv.KvKey;
import com.charles.user.R;
import com.charles.user.function.range.RangeActivity;
import com.charles.user.function.setting.SettingActivity;

/**
 * @author charles
 * @date 2018/10/4
 * @description
 */
public class UserFragment extends BaseFragment implements View.OnClickListener {
    private TextView examTargetTv;

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

    @Override
    public void onResume() {
        super.onResume();

        int range = Kv.getInt(KvKey.EXAM_RANGE, 3);
        examTargetTv.setText(String.format("教师资格证 %s", RangeActivity.RANGE_STRINGS[range]));
    }

    private void initView(View view) {
        examTargetTv = view.findViewById(R.id.user_exam_target_tv);
        examTargetTv.setOnClickListener(this);
        view.findViewById(R.id.user_setting_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.user_setting_btn) {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        } else if (viewId == R.id.user_exam_target_tv) {
            startActivity(new Intent(getActivity(), RangeActivity.class));
        }
    }
}
