package ywcai.ls.mobileutil.article.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.article.view.detail.ArticleCommentFragment;
import ywcai.ls.mobileutil.article.view.detail.ArticleContentFragment;


public class ArticleDetailPageAdapter extends FragmentStatePagerAdapter {
    ArticleIndex articleIndex;
    public List<Fragment> pageList = new ArrayList<>();

    public ArticleDetailPageAdapter(FragmentManager fm, ArticleIndex articleIndex) {
        super(fm);
        this.articleIndex = articleIndex;
        ArticleContentFragment fragment1 = new ArticleContentFragment();
        ArticleCommentFragment fragment2 = new ArticleCommentFragment();
        fragment1.setArticleIndex(articleIndex);
        fragment2.setArticleIndex(articleIndex);
        pageList.add(fragment1);
        pageList.add(fragment2);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return pageList.get(position);
    }
}
