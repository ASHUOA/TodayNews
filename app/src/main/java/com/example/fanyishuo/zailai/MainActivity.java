package com.example.fanyishuo.zailai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.example.fanyishuo.zailai.App.AppApplication;
import com.example.fanyishuo.zailai.Nowife.NetUtils;
import com.example.fanyishuo.zailai.adapter.Myadapter;
import com.example.fanyishuo.zailai.db.DbUtils;
import com.example.fanyishuo.zailai.diergebao.SecondActiovity;
import com.example.fanyishuo.zailai.shezhiWife.NetWorkUtils;
import com.example.fanyishuo.zailai.shezhijiemian.SheZhiActicity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TabLayout tabLayout;
    private ViewPager viewpager;
    private SlidingMenu slidingMenu;
    private SlidingMenu slidingMenu2;
    private ImageView imageView;
    private ImageView qq;
    private TextView tv;

    ConnectivityBroadcastReceiver mConnectivityBroadcastReceiver;
    private ImageView lixianxuanze;
    private TextView lixiantusi;
    private TextView shezhi;
    private List<ChannelBean> wodepindao;
    private List<ChannelBean> yuanshi;
    private DbUtils dbUtils;
    private List<ChannelBean> allChannels;
    private Myadapter adapter;
    private ImageView shezhijiemian;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreatedSetTheme(this);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(8);

        //关联
        tabLayout.setupWithViewPager(viewpager);

        //侧拉
        imageView = (ImageView) findViewById(R.id.dianji);
        imageView.setOnClickListener(this);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setFadeDegree(1f);
        slidingMenu.setOffsetFadeDegree(0.4f);
        slidingMenu.setBehindOffset(300);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu_item);

        //右侧啦
        slidingMenu2 = new SlidingMenu(this);
        slidingMenu2.setMode(SlidingMenu.RIGHT);
        slidingMenu2.setFadeDegree(1f);
        slidingMenu2.setOffsetFadeDegree(0.4f);
        slidingMenu2.setBehindOffset(300);
        slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu2.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu2.setMenu(R.layout.slidingmenu_item2);

        //夜间模式
        qq = (ImageView) findViewById(R.id.qq);
        qq.setOnClickListener(this);

        //点击切换夜间模式
        ImageView qiehuan = (ImageView) findViewById(R.id.qie);
        qiehuan.setOnClickListener(this);


        //电话
        ImageView dianhua  = (ImageView) findViewById(R.id.dianhua);
        dianhua.setOnClickListener(this);


        //极光
        HashSet<String> set = new HashSet<>();
        set.add("买包");
        set.add("买手机");
        JPushInterface.setAliasAndTags(this, "wangjiangtao", set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });

        //注册非wife
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
        registerReceiver(mConnectivityBroadcastReceiver, filter);
        lixianxuanze = (ImageView) findViewById(R.id.lixianxuanze);
        lixiantusi = (TextView) findViewById(R.id.lixiantusi);
        lixianxuanze.setOnClickListener(this);
        lixiantusi.setOnClickListener(this);


        //设置wife
        shezhi = (TextView) findViewById(R.id.wife);
        shezhi.setOnClickListener(this);

        //频道
        indate();

        //设置界面
        shezhijiemian = (ImageView) findViewById(R.id.shezhi);
        shezhijiemian.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dianji:
                slidingMenu.toggle();
                break;
            case R.id.qq:

                UMShareAPI.get(this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.qie:
                ThemeUtil.ChangeCurrentTheme(this);
                break;
            case R.id.dianhua:
                Intent intent=new Intent(MainActivity.this, SecondActiovity.class);
                startActivity(intent);
                break;
            case R.id.lixianxuanze:
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
                break;
            case R.id.lixiantusi:
                Toast.makeText(MainActivity.this, NetUtils.getInstance().getBASE_URL(), Toast.LENGTH_SHORT).show();
                break;
            //设置wife
            case R.id.wife:
//isConnection 返回的true，证明网络是连接的
                if(NetWorkUtils.isConnection(this)) {
                    //有网就去请求数据
                    Toast.makeText(MainActivity.this,"安心下载",Toast.LENGTH_SHORT).show();
                }else{
                    //弹出一个对话框
                    showNetWorkSettingDialog();

                }
                break;
            case R.id.shezhi:
                Intent intent1=new Intent(MainActivity.this, SheZhiActicity.class);
                startActivity(intent1);
                break;

        }

    }
    // QQ登陆
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            String sex = data.get("sex");
            String name = data.get("name");
            String photo = data.get("iconurl");
            tv.setText(name+""+sex);
            ImageLoader.getInstance().displayImage(photo,qq);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if( UMShareAPI.get(MainActivity.this).isInstall(MainActivity.this,SHARE_MEDIA.QQ)){
                Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "no install QQ", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //频道方法
        if (requestCode==ChannelActivity.REQUEST_CODE&&resultCode==ChannelActivity.RESULT_CODE){
            String stringExtra = data.getStringExtra(ChannelActivity.RESULT_JSON_KEY);
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            List<ChannelBean> list = new Gson().fromJson(stringExtra, new TypeToken<List<ChannelBean>>() {
            }.getType());
            if (list == null || list.size() < 1) {
                return;
            }
            wodepindao.clear();
            yuanshi.clear();

            //将返回的数据,添加到我们的集合中
            wodepindao.addAll(list);
            for (ChannelBean channelBean : list) {
                boolean select = channelBean.isSelect();
                if (select) {
                    yuanshi.add(channelBean);
                }
            }
            adapter.notifyDataSetChanged();


            //保存数据库
            dbUtils.delete();
            dbUtils.add(wodepindao);

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = supportFragmentManager.getFragments();
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            for (Fragment f:fragments
                 ) {
                    transaction.remove(f);
            }
            transaction.commitAllowingStateLoss();
            recreate();

        }
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
                        Toast.makeText(MainActivity.this, "wifi可用，下载吧", Toast.LENGTH_SHORT).show();
                        isMobileConnectivity = false;
                    } else if (ConnectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {
                        Toast.makeText(MainActivity.this, "现在是移动网络，当心", Toast.LENGTH_SHORT).show();
                        isMobileConnectivity = true;
                        //获得现在的网络状态 是移动网络，去改变我们的访问接口

                    } else {
                        Toast.makeText(MainActivity.this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                }

                //// TODO: 2017/8/10 改变一下网络状态
                NetUtils.getInstance().changeNetState(isMobileConnectivity);
            }
        }
    }
        /**
         * 设置网络设置提示对话框
         */
        private void showNetWorkSettingDialog() {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("没有设置网络，请您设置网络!");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //跳转网络设置界面  隐士意图
                    Intent intent = new Intent();
                    intent.setAction("android.settings.WIRELESS_SETTINGS");
                    startActivity(intent);

                }
            });

            builder.create().show();
        }


//非wife
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解注册
        unregisterReceiver(mConnectivityBroadcastReceiver);
    }


        //频道管理
    public void shuaxi(View view){
        ChannelActivity.startChannelActivity(this, wodepindao);

    }
    //"国内", "体育", "时尚", "社会","娱乐","科技","财经","军事"
    private void indate(){
        //所有标头
        wodepindao = new ArrayList<>();
        //默认的标头
        yuanshi = new ArrayList<>();
        dbUtils = new DbUtils(this);
        allChannels = dbUtils.getAllChannels();
        if (allChannels==null||allChannels.size()<1){
            ChannelBean channelBean1=new ChannelBean("国内",true);//true显示在上面
            ChannelBean channelBean2=new ChannelBean("体育",true);
            ChannelBean channelBean3=new ChannelBean("社会",true);
            ChannelBean channelBean4=new ChannelBean("娱乐",true);
            ChannelBean channelBean5=new ChannelBean("科技",true);
            ChannelBean channelBean6=new ChannelBean("财经",false);//false显示在下面
            ChannelBean channelBean7=new ChannelBean("军事",false);
            yuanshi.add(channelBean1);
            yuanshi.add(channelBean2);
            yuanshi.add(channelBean3);
            yuanshi.add(channelBean4);
            yuanshi.add(channelBean5);
            wodepindao.add(channelBean1);
            wodepindao.add(channelBean2);
            wodepindao.add(channelBean3);
            wodepindao.add(channelBean4);
            wodepindao.add(channelBean5);
            wodepindao.add(channelBean6);
            wodepindao.add(channelBean7);
            //// TODO: 2017/8/16 保存数据库
            dbUtils.add(wodepindao);
        }else {
            wodepindao.addAll(allChannels);
            //条件查询
            List<ChannelBean> userChannels = dbUtils.getUserChannels();
            yuanshi.addAll(userChannels);
        }
        adapter = new Myadapter(getSupportFragmentManager(),yuanshi);
        viewpager.setAdapter(adapter);

    }

}
