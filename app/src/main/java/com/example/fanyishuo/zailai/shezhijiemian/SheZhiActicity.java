package com.example.fanyishuo.zailai.shezhijiemian;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanyishuo.zailai.App.AppApplication;
import com.example.fanyishuo.zailai.Nowife.NetUtils;
import com.example.fanyishuo.zailai.R;

import java.io.File;
import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fanyishuo on 2017/8/22.
 */

public class SheZhiActicity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv;

    ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private TextView t5;
    private TextView clea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shezhijiemian);
        tv = (TextView) findViewById(R.id.tiaoshi);
        tv.setOnClickListener(this);

        //注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
        registerReceiver(mConnectivityBroadcastReceiver, filter);


        CheckBox cb2 = (CheckBox) findViewById(R.id.cb2);
        boolean b = getSharedPreferences("FLAG", MODE_PRIVATE).getBoolean("key", true);
        SharedPreferences sp = getSharedPreferences("FLAG", MODE_PRIVATE);
        final SharedPreferences.Editor edit = sp.edit();
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit.putBoolean("key", true).commit();
                    JPushInterface.resumePush(SheZhiActicity.this);
                } else {
                    edit.putBoolean("key", false).commit();
                    JPushInterface.stopPush(SheZhiActicity.this);
                }
            }
        });
        if (b) {
            cb2.setChecked(true);
        } else {
            cb2.setChecked(false);
        }

        t5 = (TextView) findViewById(R.id.t5);
        clea = (TextView) findViewById(R.id.clear);
        t5.setOnClickListener(this);
        clea.setOnClickListener(this);
    }

    //测试一下
    public void onTestBaseUrl(View v) {
        Toast.makeText(SheZhiActicity.this, NetUtils.getInstance().getBASE_URL(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tiaoshi:
                // TODO: 2017/8/10 做一个alertDialog
                String[] strings = {"最佳效果", "较省流量", "极省流量"};
                int mode = AppApplication.getAppContext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).getInt(NetUtils.PICTURE_LOAD_MODE_KEY, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("非wifi网络流量");
                builder.setSingleChoiceItems(strings, mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //// TODO: 2017/8/10  要保存我们的图片加载模式
                        AppApplication.getAppContext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).edit().putInt(NetUtils.PICTURE_LOAD_MODE_KEY, which).commit();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                tv.setText(NetUtils.getInstance().getBASE_URL());
                break;
            case R.id.t5:
                try {
                    //去计算缓存大小
                    String totalCacheSize = getTotalCacheSize();
                    t5.setText(totalCacheSize);

                } catch (Exception e) {
                    e.printStackTrace();
                }

        break;
        case R.id.clear:
        clearAllCache(SheZhiActicity.this);
        String totalCacheSize = null;
        try {
            totalCacheSize = getTotalCacheSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        t5.setText(totalCacheSize);

        break;

    }
}

    /**
     * 计算app的缓存大小其实计算的是 getCacheDir()这个file和getExternalCacheDir()这个file大小的和
     */
    public String getTotalCacheSize() throws Exception {
        long cacheSize = getFolderSize(this.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(this.getExternalCacheDir());
        }
//        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "universalimageloader/Cache");
        return getFormatSize(cacheSize);
    }
    /**
     * 计算文件夹的大小
     */
    public static long getFolderSize(File file) throws Exception {
        if (!file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化得到的总大小 默认是byte,  然后根据传入的大小,自动转化成合适的大小单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    /**
     * 清理app的缓存 其实是清除的getCacheDir 和getExternalCacheDir这两个文件
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 删除一个文件夹
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
 * 接受网络状态的改变
 */
public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            boolean isMobileConnectivity = true;

            //如果能走到这，说明网络已经发生变化
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType()) {
                    Toast.makeText(SheZhiActicity.this, "wifi可用，下载吧", Toast.LENGTH_SHORT).show();
                    isMobileConnectivity = false;
                } else if (ConnectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {
                    Toast.makeText(SheZhiActicity.this, "现在是移动网络，当心", Toast.LENGTH_SHORT).show();
                    isMobileConnectivity = true;
                    //获得现在的网络状态 是移动网络，去改变我们的访问接口

                } else {
                    Toast.makeText(SheZhiActicity.this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(SheZhiActicity.this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
            }

            //// TODO: 2017/8/10 改变一下网络状态
            NetUtils.getInstance().changeNetState(isMobileConnectivity);
        }
    }

}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解注册
        unregisterReceiver(mConnectivityBroadcastReceiver);
    }
}
