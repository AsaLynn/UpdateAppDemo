package com.ycbjie.update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zxn.update.ContentFragment;
import com.zxn.update.UpdateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxn on 2020/8/26.
 */
public class DemoContentFragment extends ContentFragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.progress)
    ProgressBar mProgress;

    private String mParam1;

    public DemoContentFragment() {
    }

    public static DemoContentFragment newInstance() {
        DemoContentFragment fragment = new DemoContentFragment();
        return fragment;
    }

    public static DemoContentFragment newInstance(String param1) {
        DemoContentFragment fragment = new DemoContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgress.setMax(100);
        mProgress.setProgress(0);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (null != mOnUpdateListener) {
                    mOnUpdateListener.onCancelClick();
                }
                break;
            case R.id.tv_ok:
                if (null != mOnUpdateListener) {
                    mOnUpdateListener.onConfirmClick();
                }
                break;
        }
    }

    @Override
    protected void changeUploadStatus(int upload_status) {
        switch (upload_status) {
            case UpdateUtils.DownloadStatus.START:
                /*if (null != mTvOk) {
                    mTvOk.setText("开始下载");
                }*/
                if (null != mProgress) {
                    mProgress.setVisibility(View.GONE);
                }
                break;
            case UpdateUtils.DownloadStatus.UPLOADING:
                /*if (null != mTvOk) {
                    mTvOk.setText("下载中……");
                }*/
                if (null != mProgress) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                break;
            case UpdateUtils.DownloadStatus.FINISH:
                /*if (null != mTvOk) {
                    mTvOk.setText("开始安装");
                }*/
                if (null != mProgress) {
                    mProgress.setVisibility(View.INVISIBLE);
                }
                break;
            case UpdateUtils.DownloadStatus.PAUSED:
                if (null != mProgress) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                /*if (null != mTvOk) {
                    mTvOk.setText("暂停下载");
                }*/
                //break;
            case UpdateUtils.DownloadStatus.ERROR:
                if (null != mProgress) {
                    mProgress.setVisibility(View.VISIBLE);
                }
               /* if (null != mTvOk) {
                    mTvOk.setText("错误，点击继续");
                }*/
                break;
        }
    }

    @Override
    protected void setProgress(int progress) {
        if (null != mProgress) {
            mProgress.setProgress(progress);
        }
    }
}