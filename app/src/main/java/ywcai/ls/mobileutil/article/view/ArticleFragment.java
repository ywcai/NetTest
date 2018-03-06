package ywcai.ls.mobileutil.article.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.global.cfg.AppConfig;


public class ArticleFragment extends Fragment {
    View view;
    ArrayList<Fragment> list ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_main_article, container, false);
        initPage();
        return view;
    }

    private void initPage() {
        list = new ArrayList<Fragment>();
        SlidingTabLayout tabTitle = (SlidingTabLayout) view.findViewById(R.id.article_sliding);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.article_pager);
        for (int i = 0; i < AppConfig.ARTICLE_TAG_NAME.length; i++) {
            ArticleListFragment fragment = new ArticleListFragment();
            fragment.setType(AppConfig.getArticleHashMap().get(AppConfig.ARTICLE_TAG_NAME[i]));
            fragment.setPageIndex(i);
            list.add(fragment);
        }
        tabTitle.setViewPager(viewPager,
                AppConfig.ARTICLE_TAG_NAME,
                this.getActivity(),
                list);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //如果是第一次 则请 fragment刷新数据
                ArticleListFragment selectFragment = (ArticleListFragment) list.get(position);
                if (!selectFragment.isExist) {
                    selectFragment.listView.setRefreshing(true);
                    selectFragment.pullLoad();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
