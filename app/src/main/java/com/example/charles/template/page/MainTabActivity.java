package com.example.charles.template.page;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.charles.module1.page.main.Module1Fragment;
import com.charles.user.function.main.UserFragment;
import com.example.charles.template.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 */
@Route(path = "/app/MainTabActivity")
public class MainTabActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        initView();
    }

    private void initView() {
        fragmentList.add(new Module1Fragment());
        fragmentList.add(new UserFragment());

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_frameLayout, fragmentList.get(0));
        fragmentTransaction.add(R.id.main_frameLayout, fragmentList.get(1));
        fragmentTransaction.hide(fragmentList.get(1));
        fragmentTransaction.commit();

        TabLayout tabLayout = findViewById(R.id.main_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void changeFragment(int position) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (position == i) {
                fragmentTransaction.show(fragmentList.get(i));
            } else {
                fragmentTransaction.hide(fragmentList.get(i));
            }
        }
        fragmentTransaction.commit();
    }
}
