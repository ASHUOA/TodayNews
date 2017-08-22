package com.example.fanyishuo.zailai.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanyishuo.zailai.R;
import com.example.fanyishuo.zailai.Utils.MenuInfo;
import com.example.fanyishuo.zailai.dainjiListview.Xinwen;
import com.google.gson.Gson;
import com.limxing.xlistview.view.XListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by fanyishuo on 2017/8/3.
 */

public class ShishangFragment extends Fragment {

    private String text;
    private XListView xlistview;
    private boolean flag = false;
    private xlvadapter pter;
    private int index = 0;
    private PopupWindow popupWindow;
    private TextView deleteView;
    private ImageView closeView;
    private List<MenuInfo.ResultBean.DataBean> list;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View view1 = getView();
        //头条、新闻、财经、体育、娱乐、军事、教育、科技、NBA、股票、星座、女性、健康、育儿
        xlistview = (XListView) view1.findViewById(R.id.xlv);
        //换一次标头从新走一次
        TestPost();
        //一定别忘了调用

        //TestPost();
        xlistview.setPullLoadEnable(true);
        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                index++;
                TestPost();
                flag = true;
                xlistview.stopRefresh(true);
            }

            @Override
            public void onLoadMore() {
                index++;
                TestPost();
                flag = false;
                xlistview.stopLoadMore();
            }
        });


    }

    public void TestPost() {
        String path="http://v.juhe.cn/toutiao/index";
        RequestParams params = new RequestParams(path);
        params.addBodyParameter("key", "5b6258c74f4346147b12fe38490a12b2");
        params.addBodyParameter("type","shishang");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("*****************" + result);
                Gson gson = new Gson();
                MenuInfo menuInfo = gson.fromJson(result, MenuInfo.class);
                list = menuInfo.getResult().getData();
                if (pter == null) {
                    pter = new xlvadapter(list);
                    xlistview.setAdapter(pter);
                } else {
                    pter.moreloader(list, flag);
                    pter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    class xlvadapter extends BaseAdapter {
        private List<MenuInfo.ResultBean.DataBean> list;

        public xlvadapter(List<MenuInfo.ResultBean.DataBean> list) {
            this.list = list;
            intiPop();
        }

        ImageOptions options = new ImageOptions.Builder()
                .setSize(200, 200)
                .setUseMemCache(true)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .setCircular(true)
                .build();


        public xlvadapter(ImageOptions options) {
            this.options = options;
        }

        private void moreloader(List<MenuInfo.ResultBean.DataBean>  lists, boolean flag) {
            for (MenuInfo.ResultBean.DataBean bean : lists
                    ) {
                if (flag) {
                    list.add(0, bean);
                } else {
                    list.add(bean);
                }
            }
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            viewholder1 vh1 = null;
            if (convertView == null) {
                vh1 = new viewholder1();
                convertView = convertView.inflate(getActivity(), R.layout.item1, null);
                vh1.im1 = (ImageView) convertView.findViewById(R.id.tu1);
                vh1.tv1 = (TextView) convertView.findViewById(R.id.zi1);
                vh1.imageView = (ImageView) convertView.findViewById(R.id.more_iv);
                vh1.tt1 = (TextView) convertView.findViewById(R.id.tt1);
                vh1.tt2 = (TextView) convertView.findViewById(R.id.tt2);
                convertView.setTag(vh1);
            } else {
                vh1 = (viewholder1) convertView.getTag();
            }
            vh1.tv1.setText(list.get(position).getTitle());
            x.image().bind(vh1.im1, list.get(position).getThumbnail_pic_s());
            vh1.imageView.setOnClickListener(new PopAction(position));
            vh1.tt2.setText(list.get(position).getDate());
            vh1.tt1.setText(list.get(position).getAuthor_name());

            xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), Xinwen.class);
                    intent.putExtra("url", list.get(position - 1).getUrl());
                    startActivity(intent);
                }


            });

            return convertView;
        }

        class viewholder1 {
            ImageView im1;
            TextView tv1;
            ImageView imageView;
            TextView tt1;
            TextView tt2;
            ImageView re;
        }

        class PopAction implements View.OnClickListener {
            private int position;

            public PopAction(int position) {
                this.position = position;

            }

            @Override
            public void onClick(View v) {
                int[] arry = new int[2];
                v.getLocationOnScreen(arry);
                int x = arry[0];
                int y = arry[1];
                showPop(v, position, x, y);
            }
        }

        private void intiPop() {
            View inflate = View.inflate(getContext(), R.layout.popwindow_layout, null);
            popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
            popupWindow.setAnimationStyle(R.style.popWindowAnimation);

            //知道popwindow中间的控件 ,去做点击
            deleteView = (TextView) inflate.findViewById(R.id.delete_tv);
            closeView = (ImageView) inflate.findViewById(R.id.close_iv);
        }

        private void showPop(final View parent, final int position, int x, int y) {
            //根据view的位置显示popupwindow的位置
            popupWindow.showAtLocation(parent, 0, x, y);
            //设置popupwindow可以获取焦点,不获取焦点的话 popupwiondow点击无效
            popupWindow.setFocusable(true);

            //点击popupwindow的外部,popupwindow消失
            popupWindow.setOutsideTouchable(true);

            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "删除", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    pter.notifyDataSetChanged();
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });

            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        }
    }

}
