package com.charles.module1.page.main;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.charles.module1.R;

import java.util.List;

/**
 * @author charles
 */
public class Module1FragmentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public Module1FragmentAdapter(@Nullable List<String> data) {
        super(R.layout.module1_item_function_card, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.function_card_title, item);
    }
}
