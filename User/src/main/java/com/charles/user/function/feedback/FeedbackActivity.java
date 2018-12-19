package com.charles.user.function.feedback;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.charles.common.base.BaseActivity;
import com.charles.user.R;

import java.io.IOException;

/**
 * @author charles
 * @date 2018/10/17
 */
@Route(path = "/user/FeedbackActivity")
public class FeedbackActivity extends BaseActivity implements View.OnClickListener, FeedbackView {
    private final int IMAGE_REQUEST_CODE = 0;

    private EditText feedbackEdt;
    private ImageView feedbackImg;
    private TextView feedbackContentCountTv;
    private Button submitBtn;

    private FeedbackPresenter feedbackPresenter;

    private Bitmap feedbackImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_feedback);

        initData();
        initView();
    }

    private void initData() {
        feedbackPresenter = new FeedbackPresenter(this);
    }

    private void initView() {
        feedbackEdt = findViewById(R.id.feedback_edt);
        feedbackImg = findViewById(R.id.feedback_image);
        feedbackContentCountTv = findViewById(R.id.feedback_content_count);
        submitBtn = findViewById(R.id.feedback_submit_btn);
        submitBtn.setEnabled(false);

        feedbackEdt.addTextChangedListener(new MyTextWatcher(feedbackEdt));

        feedbackImg.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        findViewById(R.id.feedback_back_tv).setOnClickListener(this);
    }

    private class MyTextWatcher implements TextWatcher {
        private TextView tv;

        /**
         * 绑定 TextView
         *
         * @param tv
         */
        MyTextWatcher(TextView tv) {
            this.tv = tv;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = tv.getText().toString();
            int contentCount = s.length();
            feedbackContentCountTv.setText(String.format("%d/100", contentCount));
            submitBtn.setEnabled(contentCount > 0);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.feedback_back_tv) {
            finish();
        } else if (viewId == R.id.feedback_submit_btn) {
            feedbackPresenter.submitFeedback();
        } else if (viewId == R.id.feedback_image) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public String getFeedback() {
        return feedbackEdt.getText().toString();
    }

    @Override
    public Bitmap getFeedbackImage() {
        return feedbackImageBitmap;
    }

    @Override
    public void submitFeedbackSucceed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提交反馈成功")
                .setMessage("感谢您的反馈建议，我们会认真听取您的建议，为您提供更好的服务")
                .setPositiveButton("关闭", null)
                .create().show();

    }

    @Override
    public void submitFeedbackFailed(String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从相册中返回的数据
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                feedbackImg.setImageURI(data.getData());


                try {
                    feedbackImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
