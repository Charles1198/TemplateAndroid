package com.charles.user.function.range;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.charles.common.kv.Kv;
import com.charles.common.kv.KvKey;
import com.charles.common.util.AnimUtil;
import com.charles.user.R;

/**
 * @author charles
 */
public class RangeActivity extends AppCompatActivity implements View.OnClickListener {
    public static String[] RANGE_STRINGS = new String[]{"幼儿园", "小学", "初中", "高中"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_range);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        findViewById(R.id.range_back_img).setOnClickListener(this);
        findViewById(R.id.range_0_tv).setOnClickListener(this);
        findViewById(R.id.range_1_tv).setOnClickListener(this);
        findViewById(R.id.range_2_tv).setOnClickListener(this);
        findViewById(R.id.range_3_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AnimUtil.scaleAnim(v, 1, 1.1f,200);

        int id = v.getId();
        int rangeIndex;
        if (id == R.id.range_0_tv) {
            rangeIndex = 0;
        } else if (id == R.id.range_1_tv) {
            rangeIndex = 1;
        } else if (id == R.id.range_2_tv) {
            rangeIndex = 2;
        } else {
            rangeIndex = 3;
        }

        Kv.setInt(KvKey.EXAM_RANGE, rangeIndex);
        finish();
    }
}
