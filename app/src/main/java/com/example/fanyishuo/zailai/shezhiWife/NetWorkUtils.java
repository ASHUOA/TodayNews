package com.example.fanyishuo.zailai.shezhiWife;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络判断的工具类
 */

public class NetWorkUtils {


    /**
     * 检查设备网络是否连接
     * @return
     */
    public static boolean isConnection(Context context){

        //得到管理网络的服务实例
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //得到网络信息 (判断网络是否连接、3G、4G)
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //判断网络是否已经连接，如果连接的话，返回true，否则false
        return (networkInfo != null && networkInfo.isConnected());


    }


}
