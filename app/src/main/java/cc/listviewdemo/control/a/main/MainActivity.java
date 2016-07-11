package cc.listviewdemo.control.a.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import net.tsz.afinal.annotation.view.CodeNote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cc.listviewdemo.R;
import cc.listviewdemo.base.BaseActivity;
import cc.listviewdemo.view.CommonAdapter;
import cc.listviewdemo.view.CommonViewHolder;
import cc.listviewdemo.view.MeasureListView;
import cc.listviewdemo.view.NetworkImageHolderView;
import cc.listviewdemo.view.TotalScrollView;

public class MainActivity extends BaseActivity {
    @CodeNote(id= R.id.main)
    TotalScrollView main;
    @CodeNote(id=R.id.iv)
    RelativeLayout iv;
    @CodeNote(id=R.id.main_list)
    MeasureListView main_list;
    CommonAdapter<String> mAdapter;
    List<String> mList;
    @CodeNote(id=R.id.guanggao_cb)
    ConvenientBanner convenientBanner;
    List networkImages;
    private String[] images = {"http://avatar.csdn.net/6/A/B/1_leewenjin.jpg",
            "http://avatar.csdn.net/8/6/3/3_liu_dkalan.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    @Override
    public void initViews() {
        setContentView(R.layout.frag_main);
        mList = new ArrayList<>();
        iv.getBackground().setAlpha(0);
        initDatas();
    }

    @Override
    public void initEvents() {

//        mAdapter = new CommonAdapter<String>(this, mList, R.layout.item_main) {
//            @Override
//            public void convert(CommonViewHolder holder, String s, int position) {
//                holder.setText(R.id.tv, s);
//            }
//        };
        main.smoothScrollTo(0, 20);
        main_list.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(main_list);
        init();
    }

    /**
     * 加载ListView模拟数据
     */
    private void initDatas() {
        for (int i = 0; i < 20; i++) {
            mList.add(i + "");
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //初始化网络图片缓存库
    private void initImageLoader() {
        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private void init() {
        initImageLoader();
        //网络加载例子
        networkImages = Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages);
    }
}
