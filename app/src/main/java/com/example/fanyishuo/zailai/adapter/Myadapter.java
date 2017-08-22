package com.example.fanyishuo.zailai.adapter;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;

        import com.andy.library.ChannelBean;
        import com.example.fanyishuo.zailai.Fragment.CaijingFragment;
        import com.example.fanyishuo.zailai.Fragment.GuoneiFragment;
        import com.example.fanyishuo.zailai.Fragment.JunshiFragment;
        import com.example.fanyishuo.zailai.Fragment.KejiFragment;
        import com.example.fanyishuo.zailai.Fragment.ShehuiFragment;
        import com.example.fanyishuo.zailai.Fragment.ShishangFragment;
        import com.example.fanyishuo.zailai.Fragment.TiyuFragment;
        import com.example.fanyishuo.zailai.Fragment.YuleFragment;

        import java.util.List;

/**
 * Created by fanyishuo on 2017/8/3.
 */
//头条、新闻、财经、体育、娱乐、军事、教育、科技、NBA、股票、星座、女性、健康、育儿
public class Myadapter extends FragmentPagerAdapter {
    //private String[] titles={"国内", "体育", "时尚", "社会","娱乐","科技","财经","军事"};

    //这个为开启事务
    private FragmentManager mFragmentManager;
    List<ChannelBean> mChannelBeanList;
    public Myadapter(FragmentManager fm, List<ChannelBean> yuanshi) {
        super(fm);
        mFragmentManager=fm;
        mChannelBeanList=yuanshi;

    }

    @Override
    public Fragment getItem(int position) {
        String name = mChannelBeanList.get(position).getName();
        switch (name){
            case "国内":
                return new GuoneiFragment();
            case "体育":
                return new TiyuFragment();
            case "时尚":
                return new ShishangFragment();
            case "社会":
                return new ShehuiFragment();
            case "娱乐":
                return new YuleFragment();
            case "科技":
                return new KejiFragment();
            case "财经":
                return new CaijingFragment();
            case "军事":
                return new JunshiFragment();

        }
        return null;


//        if (position == 1) {
//            return new GuoneiFragment();
//        } else if (position == 2) {
//            return new TiyuFragment();
//        } else if (position == 3) {
//            return new ShishangFragment();
//        } else if (position == 4) {
//            return new ShehuiFragment();
//        } else if (position == 5) {
//            return new YuleFragment();
//        } else if (position == 6) {
//            return new KejiFragment();
//        } else if (position == 7) {
//            return new CaijingFragment();
//        }
//
//        return new JunshiFragment();

    }

    //记得盘空
    @Override
    public int getCount() {
        return mChannelBeanList!=null?mChannelBeanList.size():0;
    }
    //设置tablayout的每个tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mChannelBeanList.get(position).getName();
    }
}
