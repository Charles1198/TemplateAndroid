package com.charles.module1.page.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.charles.common.base.BaseFragment;
import com.charles.common.util.LogUtil;
import com.charles.common.util.ToastUtil;
import com.charles.module1.R;
import com.charles.module1.page.avatar.AvatarActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 */
public class Module1Fragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module1_fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.module1_RecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);

        List<String> functionList = new ArrayList<>();
        functionList.add("更换头像");
        functionList.add("图片操作");
        Module1FragmentAdapter adapter = new Module1FragmentAdapter(functionList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view1, position) -> jumpTo(position));
    }

    private void jumpTo(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(context, AvatarActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
