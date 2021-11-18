package com.youwu.shouyinxitong.ycupdatelib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.youwu.shouyinxitong.R;

import java.io.File;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/29
 *     desc  : 版本更新弹窗
 *     revise:
 * </pre>
 */
public class UpdateFragment extends BaseDialogFragment implements View.OnClickListener {


    /**
     * 是否强制更新
     */
    private boolean isForceUpdate;
    private String apkUrl;
    private String saveApkPath;
    private String apkName;
    private String desc;
    private String packageName;

    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE};
    private int downloadStatus = UpdateUtils.DownloadStatus.START;

    private FragmentActivity mActivity;
    private BaseDownloadTask downloadTask;

    private ProgressBar mProgress;
    private TextView mTvCancel;
    private TextView mTvOk;


    /**
     * 版本更新
     * @param isForceUpdate                     是否强制更新
     * @param apkUrl                            下载链接
     * @param apkName                           下载apk名称
     * @param desc                              更新文案
     * @param packageName                       包名
     */
    public static void showFragment(FragmentActivity activity, boolean isForceUpdate ,
                                    String apkUrl , String apkName , String desc,
                                    String packageName) {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("apk_url", apkUrl);
        bundle.putString("desc", desc);
        bundle.putString("apkName", apkName);
        bundle.putBoolean("isUpdate", isForceUpdate);
        bundle.putString("packageName",packageName);
        updateFragment.setArguments(bundle);
        updateFragment.show(activity.getSupportFragmentManager());
        FileDownloader.setup(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setLocal(Local.CENTER);
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            apkUrl = arguments.getString("apk_url");
            desc = arguments.getString("desc");
            apkName = arguments.getString("apkName");
            isForceUpdate = arguments.getBoolean("isUpdate");
            packageName = arguments.getString("packageName");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            apkUrl = outState.getString("apk_url");
            desc = outState.getString("desc");
            apkName = outState.getString("apkName");
            isForceUpdate = outState.getBoolean("isUpdate");
            packageName = outState.getString("packageName");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 如果isForceUpdate是true，那么就是强制更新，则设置cancel为false
     * 如果isForceUpdate是false，那么不是强制更新，则设置cancel为true
     */
    @Override
    protected boolean isCancel() {
        return !isForceUpdate;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_update_app;
    }

    @Override
    public void bindView(View view) {
        initView(view);
        onKeyListener();
        createFilePath();
    }


    private void initView(View view) {
        //ImageView mIvTop = view.findViewById(R.id.iv_top);
        TextView mTvDesc = view.findViewById(R.id.tv_desc);
        mProgress = view.findViewById(R.id.progress);
        mTvCancel = view.findViewById(R.id.tv_cancel);
        mTvOk = view.findViewById(R.id.tv_ok);

        mProgress.setMax(100);
        mProgress.setProgress(0);
        mTvDesc.setText(desc==null?"":desc);
        if (isForceUpdate) {
            mTvOk.setVisibility(View.VISIBLE);
            mTvCancel.setVisibility(View.GONE);
        } else {
            mTvOk.setVisibility(View.VISIBLE);
            mTvCancel.setVisibility(View.VISIBLE);
        }
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    /**
     * 这里主要是处理返回键逻辑
     */
    private void onKeyListener() {
        if(getDialog()!=null){
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        // 返回键
                        case KeyEvent.KEYCODE_BACK:
                            if (isForceUpdate) {
                                return true;
                            }
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }


    private void createFilePath() {
        //获取下载保存path
        saveApkPath = UpdateUtils.getLocalApkDownSavePath(apkName);
        Log.d("文件地址",saveApkPath);
        if (new File(saveApkPath).exists()) {
            changeUploadStatus(UpdateUtils.DownloadStatus.FINISH);
        } else {
            changeUploadStatus(UpdateUtils.DownloadStatus.START);
        }
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_ok) {
            //当下载中，点击后则是暂停下载
            //当不是下载中，点击后先判断apk是否存在，若存在则提示安装；如果不存在，则下载
            //当出现错误时，点击后继续开始下载
            switch (downloadStatus) {
                case UpdateUtils.DownloadStatus.START:
                case UpdateUtils.DownloadStatus.UPLOADING:
                    if (downloadTask != null) {
                        downloadTask.pause();
                    } else {
                        checkPermissionAndDownApk();
                    }
                    break;
                case UpdateUtils.DownloadStatus.FINISH:
                    File file = new File(saveApkPath);
                    if (file.exists()) {
                        //检测是否有apk文件，如果有直接普通安装
                        UpdateUtils.installNormal(mActivity,saveApkPath,packageName);
                        dismissDialog();
                    } else {
                        checkPermissionAndDownApk();
                    }
                    break;
                case UpdateUtils.DownloadStatus.PAUSED:
                case UpdateUtils.DownloadStatus.ERROR:
                    checkPermissionAndDownApk();
                    break;
            }
        } else if (i == R.id.tv_cancel) {
            //如果正在下载，那么就先暂停，然后finish
            if (downloadStatus == UpdateUtils.DownloadStatus.UPLOADING) {
                if (downloadTask != null && downloadTask.isRunning()) {
                    downloadTask.pause();
                }
            }
            dismissDialog();
        }
    }



    private void changeUploadStatus(int upload_status) {
        this.downloadStatus = upload_status;
        switch (upload_status) {
            case UpdateUtils.DownloadStatus.START:
                mTvOk.setText("开始下载");
                mProgress.setVisibility(View.GONE);
                break;
            case UpdateUtils.DownloadStatus.UPLOADING:
                mTvOk.setText("下载中……");
                mProgress.setVisibility(View.VISIBLE);
                break;
            case UpdateUtils.DownloadStatus.FINISH:
                mTvOk.setText("开始安装");
                mProgress.setVisibility(View.INVISIBLE);
                break;
            case UpdateUtils.DownloadStatus.PAUSED:
                mProgress.setVisibility(View.VISIBLE);
                mTvOk.setText("暂停下载");
                break;
            case UpdateUtils.DownloadStatus.ERROR:
                mProgress.setVisibility(View.VISIBLE);
                mTvOk.setText("错误，点击继续");
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongConstant")
    private void checkPermissionAndDownApk() {
        if(mActivity==null){
            return;
        }
        PermissionUtils.init(mActivity);
        boolean granted = PermissionUtils.isGranted(mPermission);
        if(granted){
            setNotification(0);
            downloadTask = downApk(apkUrl, saveApkPath, getListener());
        }else {
            /*PermissionUtils permission = PermissionUtils.permission(mPermission);
            permission.callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    setNotification(0);
                    downloadTask = downApk(mActivity, apkUrl, saveApkPath, getListener());
                }
                @Override
                public void onDenied() {
                    PermissionUtils.openAppSettings();
                    Toast.makeText(mActivity,"请允许权限",Toast.LENGTH_SHORT).show();
                }
            });
            permission.request();*/
            Toast.makeText(mActivity,"请先申请读写权限",Toast.LENGTH_SHORT).show();
        }
    }


    private BaseDownloadTask downApk(String apkUrl, String saveApkPath, FileDownloadListener listener) {
        BaseDownloadTask baseDownloadTask = FileDownloader
                .getImpl()
                .create(apkUrl)
                .setPath(saveApkPath)
                .setListener(listener);
        baseDownloadTask.start();
        return baseDownloadTask;
    }



    private FileDownloadListener listener ;
    public FileDownloadListener getListener(){
        if (listener==null){
            listener = new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    changeUploadStatus(UpdateUtils.DownloadStatus.UPLOADING);
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    float total = task.getSmallFileTotalBytes();
                    float downsize = task.getSmallFileSoFarBytes();
                    int progress = (int) ((downsize / total) * 100);
                    mProgress.setProgress(progress);
                    setNotification(progress);
                }
                @Override
                protected void completed(BaseDownloadTask task) {
                    setNotification(100);
                    if (isForceUpdate) {
                        mProgress.setProgress(100);
                    }
                    changeUploadStatus(UpdateUtils.DownloadStatus.FINISH);
                    UpdateUtils.installNormal(mActivity,saveApkPath,packageName);
                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    changeUploadStatus(UpdateUtils.DownloadStatus.PAUSED);
                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    setNotification(-1);
                    changeUploadStatus(UpdateUtils.DownloadStatus.ERROR);
                    Log.e("UpdateFragment",e.getLocalizedMessage());
                }

                @Override
                protected void warn(BaseDownloadTask task) {
                    changeUploadStatus(UpdateUtils.DownloadStatus.ERROR);
                }
            };
        }
        return listener;
    }



    protected void setNotification(int progress) {
        if (mActivity==null){
            return;
        }
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(mActivity.getPackageName(), R.layout.remote_notification_view);
        remoteViews.setTextViewText(R.id.tvTitle, getResources().getString(R.string.app_name));
        remoteViews.setProgressBar(R.id.pb, 100, progress, false);
        NotificationUtils notificationUtils = new NotificationUtils(mActivity);
        NotificationManager manager = notificationUtils.getManager();
        Notification notification = notificationUtils.setContentIntent(pendingIntent)
                .setContent(remoteViews)
                .setFlags(Notification.FLAG_AUTO_CANCEL)
                .setOnlyAlertOnce(true)
                .getNotification("来了一条消息", "下载apk", R.mipmap.ic_launcher);
        //下载成功或者失败
        if (progress == 100 || progress == -1) {
            notificationUtils.clearNotification();
        } else {
            manager.notify(1, notification);
        }
    }




}
