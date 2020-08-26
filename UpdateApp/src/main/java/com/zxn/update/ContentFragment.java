package com.zxn.update;

import androidx.fragment.app.Fragment;

/**
 * Created by zxn on 2020/8/26.
 */
public abstract class ContentFragment extends Fragment {

    protected OnUpdateListener mOnUpdateListener;


    public void setOnUpdateListener(OnUpdateListener listener) {
        mOnUpdateListener = listener;
    }

    public interface OnUpdateListener {
        void onCancelClick();

        void onConfirmClick();
    }

    protected abstract void changeUploadStatus(int upload_status);

    protected abstract void setProgress(int progress);

}
