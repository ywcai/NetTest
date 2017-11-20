package ywcai.ls.mobileutil.tools.Wifi.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.MainWifiActionInf;
import ywcai.ls.mobileutil.tools.Wifi.view.WifiFragmentOne;
import ywcai.ls.mobileutil.tools.Wifi.view.WifiFragmentThree;
import ywcai.ls.mobileutil.tools.Wifi.view.WifiFragmentTwo;


public class WifiPageAdapter extends FragmentStatePagerAdapter {
    public List<Fragment> pageList = new ArrayList<>();
    public WifiPageAdapter(FragmentManager fm, MainWifiActionInf mainWifiActionInf) {
        super(fm);
        Fragment fragment1 = new WifiFragmentOne();
        ((WifiFragmentOne)fragment1).setGlobalAction(mainWifiActionInf);
        Fragment fragment2 = new WifiFragmentTwo();
        ((WifiFragmentTwo)fragment2).setGlobalAction(mainWifiActionInf);
        Fragment fragment3 = new WifiFragmentThree();
        ((WifiFragmentThree)fragment3).setGlobalAction(mainWifiActionInf);
        Fragment fragment4 = new Fragment();
        pageList.add(fragment1);
        pageList.add(fragment2);
        pageList.add(fragment3);
        pageList.add(fragment4);
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
