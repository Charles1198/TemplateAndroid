package com.charles.main.page.guide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.charles.common.kv.Kv;
import com.charles.main.R;

/**
 * @author charles
 */
public class GuideActivity extends AppCompatActivity {
    public static String GUIDE_SKIM = "guideSkim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        findViewById(R.id.guide_toLogin).setOnClickListener(v -> {
            Kv.setBool(GuideActivity.GUIDE_SKIM, true);
            ARouter.getInstance().build("/login/LoginActivity").navigation();
        });
    }
}
