package ywcai.ls.mobileutil.tools.Wifi.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.List;


import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.global.cfg.GlobalEventT;
import ywcai.ls.mobileutil.global.model.instance.CacheProcess;
import ywcai.ls.mobileutil.global.model.instance.MainApplication;
import ywcai.ls.mobileutil.global.util.statics.ConvertUtil;

import ywcai.ls.mobileutil.global.util.statics.MsgHelper;
import ywcai.ls.mobileutil.global.util.statics.MyTime;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.results.model.TaskTotal;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;
import ywcai.ls.mobileutil.tools.Wifi.presenter.inf.UpdateFragmentOneInf;

//大原则，主界面的交互事件在这个类里面响应，各子页面的交互事件在个子页面处理类中响应
public class WifiProcess implements Action1 {
    private Context context = MainApplication.getInstance().getApplicationContext();
    private CacheProcess cacheProcess = CacheProcess.getInstance();
    private WifiManager wifiMg = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    private WifiEntry connWifi = new WifiEntry();
    private WifiHardControl wifiControl;
    private List<WifiEntry> allList = new ArrayList<>();
    private int[] channelSum = new int[166];
    private WifiState wifiState;
    private WifiEntry lockEntry = null;
    private UpdateFragmentOneInf updateFragmentOne;
    private UpdateFragmentTwo updateFragmentTwo;
    private UpdateFragmentThree updateFragmentThree;
    private UpdateFragmentFour updateFragmentFour;

    public WifiProcess(WifiHardControl wifiControl) {
        this.wifiControl = wifiControl;
        connWifi.initConnWifi();
        wifiState = cacheProcess.getWifiState();
        updateFragmentOne = new UpdateFragmentOne(wifiState);
        updateFragmentTwo = new UpdateFragmentTwo(wifiState);
        updateFragmentThree = new UpdateFragmentThree(wifiState);
        updateFragmentFour = new UpdateFragmentFour(wifiState);
        recoveryAllData();
    }

    public void recoveryAllData() {
        synchronized (allList) {
            sendMsgTopBtnStatus();
            sendMsgTitleTip("共" + allList.size() + "个信号");
            updateFragmentOne.showSelectEntryInfo(lockEntry);
            updateFragmentOne.loadChannelTagStatus();
            updateFragmentOne.loadLockAndSaveVisible();
            updateFragmentOne.loadLockBtnStatus();
            updateFragmentOne.loadTaskBtnStatus();
            updateFragmentOne.loadSignalChangeData(allList, channelSum);
            updateFragmentTwo.refreshList(allList);
            updateFragmentTwo.refreshChart();

//            updateFragmentThree.refreshChannelLineRecord(channelSum);
            updateFragmentThree.refreshChannelBarChart(channelSum);
//            updateFragmentThree.refreshChannelPieChart(channelSum);


        }
    }

    //被WIFI核心处理方法调用，不能单独被外部类使用
    private void coreDraw() {
        sendMsgTitleTip("共" + allList.size() + "个信号");
        updateFragmentOne.loadSignalChangeData(allList, channelSum);
        updateFragmentOne.showSelectEntryInfo(lockEntry);

        updateFragmentTwo.refreshChart();
        updateFragmentTwo.refreshList(allList);

        updateFragmentThree.refreshFrequencyLevel(channelSum);
        updateFragmentThree.refreshChannelBarChart(channelSum);

        updateFragmentFour.refreshChannelLineRecord(channelSum);
        updateFragmentFour.switchLineChartSelect();

    }

    private void operatorDraw() {
        setTaskTotal();
        updateFragmentOne.loadTaskBtnStatus();
        updateFragmentTwo.refreshChart();
        updateFragmentTwo.refreshList(allList);
    }

    /*
    回调收到广播后的处理方法
     */
    @Override
    public void call(Object o) {
        Intent intent = (Intent) o;
        switch (intent.getAction()) {
            case WifiManager.RSSI_CHANGED_ACTION:
                //do nothing
                break;
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                synchronized (allList) {
                    processWifiResult();
                    coreDraw();
                }
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                processWifiEnableState();
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                processWifiConnState(intent);
                break;
        }
    }

    /*
    系统权限检测
     */
    private boolean checkGpsStatus() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void processWifiEnableState() {
        int state = wifiMg.getWifiState();
        if (state == WifiManager.WIFI_STATE_ENABLED) {
            //不需要处理
            sendMsgTitleTip("WIF模块已开启");
        }
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiClosed();//清除数据
            sendMsgTitleTip("WIF模块已关闭");
        }
        if (state == WifiManager.WIFI_STATE_ENABLING) {
            sendMsgTitleTip("正在开启WIFI...");
        }
        if (state == WifiManager.WIFI_STATE_DISABLING) {
            sendMsgTitleTip("正在关闭WIFI...");
        }
    }

    private void wifiClosed() {
        sendMsgTitleTip("Wifi模块被关闭");
    }

    private void processWifiConnState(Intent intent) {
        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != parcelableExtra) {
            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
            NetworkInfo.State state = networkInfo.getState();
            if (state == NetworkInfo.State.CONNECTED) {
                connectWifi();
            }
            if (state == NetworkInfo.State.CONNECTING) {
                sendMsgTitleTip("正在连接WIFI...");
            }
            if (state == NetworkInfo.State.DISCONNECTING) {
                sendMsgTitleTip("正在断开WIFI...");
            }
            if (state == NetworkInfo.State.DISCONNECTED) {
                disConnectWifi();
            }
        }
    }

    private void disConnectWifi() {
        connWifi.initConnWifi();
        sendMsgTitleTip("未连接WIFI");
    }

    private void connectWifi() {
        try {
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            String ip = ConvertUtil.ConvertIpToStr(wifiInfo.getIpAddress());
            connWifi.bssid = wifiInfo.getBSSID();
            connWifi.speed = wifiInfo.getLinkSpeed();
            connWifi.ip = ip;
            sendMsgTitleTip("已连接WIFI");
        } catch (Exception e) {
            disConnectWifi();
        }
    }

    private void clearLastData() {
        lockEntry = null;
        allList.clear();
        channelSum = new int[166];
    }

    /*
    处理信号的核心方法
     */
    private void processWifiResult() {
        clearLastData();
        if (!checkPermission()) {
            sendMsgTitleTip("没有赋予应用使用Wifi权限");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkGpsStatus()) {
                sendMsgTitleTip("打开GPS后方可扫描WIFI数据");

                return;
            }
        }
        List<ScanResult> results;
        try {
            results = wifiMg.getScanResults();
        } catch (Exception e) {
            results = null;
        }
        if (results == null) {

            return;
        }
        Observable.from(results).map(
                new Func1<ScanResult, WifiEntry>() {
                    @Override
                    public WifiEntry call(ScanResult result) {
                        WifiEntry wifiEntry = new WifiEntry();
                        wifiEntry.ssid = result.SSID;
                        wifiEntry.bssid = result.BSSID;
                        wifiEntry.channel = ConvertUtil.ConvertFrequencyToChannel(result.frequency);
                        wifiEntry.dbm = result.level;
                        wifiEntry.device = "";
                        wifiEntry.frequency = result.frequency;
                        wifiEntry.keyType = result.capabilities;
                        wifiEntry.is2G = wifiEntry.channel <= 13 ? true : false;
                        //过滤频率
                        if (connWifi.bssid.equals(result.BSSID)) {
                            wifiEntry.isConnWifi = true;
                            wifiEntry.device = connWifi.ip;
                            wifiEntry.speed = connWifi.speed;
                        }
                        //如果当前选择的有高亮显示的，直接更新起其获取到的内容，但如果当前没有获取到该信号，会怎么样？？？？？？
                        if (wifiState.selectEntry != null) {
                            //如果获取到了该信号的数据，则会更新，否则为NULL
                            if (wifiEntry.bssid.equals(wifiState.selectEntry.bssid)) {
                                lockEntry = wifiState.selectEntry;
                            }
                        }
                        channelSum[wifiEntry.channel]++;
                        return wifiEntry;
                    }
                }
        ).toList()
                .subscribe(new Action1<List<WifiEntry>>() {
                    @Override
                    public void call(List<WifiEntry> wifiEntries) {
                        allList.addAll(wifiEntries);
                        wifiControl.selfAdd++;
                    }
                });
    }


    //通知第三个页面的数据
    //频点信号热力图？使用柱状图
    private void processForFragmentThree() {

    }

    //通知第四个页面的数据
    //频点的散点气泡分布图，只看图，不显示名称，仅当前连接的信号显示名称
    private void processForFragmentFour() {

    }

    /*
    响应来自presenter的UI交互事件
     */
    //保存一个高亮的实体类，作为中间类辅助处理高亮事件????????
    public void saveHighlightHolder(Highlight[] hh) {
        //保存一个高亮类实体，在下一次打开时可反序列化创建。
        //这个和selectMac是同一事件，因此只处理事务，渲染UI在selectMac方法中处理
        if (wifiState.highlights == null) {
            wifiState.setHighlights(hh);
            cacheProcess.setWifiState(wifiState);
        }
    }

    public void setSelectEntry(WifiEntry wifiEntry) {
        //如果没有选择，则可能变为null
        synchronized (allList) {
            wifiState.selectEntry = wifiEntry;
            cacheProcess.setWifiState(wifiState);
            updateFragmentOne.showSelectEntryInfo(wifiEntry);//刷新页面文字提示信息
            updateFragmentOne.loadLockAndSaveVisible();
            updateFragmentOne.loadTaskBtnStatus();

        }
    }

    public void setChannelFilter(int index, boolean isSelect) {
        synchronized (allList) {
            wifiState.setChannelFilter(index, isSelect);
            cacheProcess.setWifiState(wifiState);
            updateFragmentOne.loadSignalChangeData(allList, channelSum);
        }
    }

    public void setAllTagSelectOrCancal(int[] index) {
        synchronized (allList) {
            wifiState.setAllChannelStatus(index);
            cacheProcess.setWifiState(wifiState);
            updateFragmentOne.loadSignalChangeData(allList, channelSum);
        }
    }

    public void set2d4G(boolean is2d4G) {
        wifiState.choose2d4G = is2d4G;
        cacheProcess.setWifiState(wifiState);
        sendMsgTopBtnStatus();
        updateFragmentOne.loadSignalChangeData(allList, channelSum);
        //Bar内容需要重新加载
        updateFragmentThree.refreshChannelBarChart(channelSum);
        //切换显示内容
        updateFragmentFour.switchLineChartSelect();
    }

    //点击锁定按钮
    public void lock() {
        wifiState.lockWifi = !wifiState.lockWifi;
        cacheProcess.setWifiState(wifiState);
        updateFragmentOne.loadLockBtnStatus();
    }

    //响应UI点击LIST的事件，显示操作界面
    public void popOperatorMenu() {
        updateFragmentTwo.popOperatorMenu();
    }

    public void setLineHide(int popTaskPos) {
        synchronized (allList) {
            wifiState.saveWifiList.get(popTaskPos).isShowInChart = !wifiState.saveWifiList.get(popTaskPos).isShowInChart;
            cacheProcess.setWifiState(wifiState);
            updateFragmentTwo.refreshChart();
            updateFragmentTwo.updateItemBtn(popTaskPos, wifiState.saveWifiList.get(popTaskPos).isShowInChart);
        }
    }

    //点击任务按钮
    public void task() {
        synchronized (allList) {
            if (wifiState.selectEntry != null) {
                for (int i = 0; i < wifiState.saveWifiList.size(); i++) {
                    if (wifiState.saveWifiList.get(i).bssid.equals(wifiState.selectEntry.bssid)) {
                        deleteLog(i);//删除日志
                        removeTask(i);//更新任务状态
                        operatorDraw();
                        return;
                    }
                }
                addTask();
                operatorDraw();
            }
        }
    }


    //单独加锁，用于外部调用.
    public void clearLog(int pos) {
        synchronized (allList) {
            deleteLog(pos);
            removeTask(pos);
            operatorDraw();
        }
    }


    public void saveLogForLocal(int pos) {
        synchronized (allList) {
            saveLogIndexOnLocal(pos);
            removeTask(pos);
            operatorDraw();
        }
    }

    //删除缓存LOG数据
    private void deleteLog(int pos) {
        String log = wifiState.saveWifiList.get(pos).logFlag;
        cacheProcess.deleteWifiTask(log);
    }

    //计算缓存的任务总量情况
    private void setTaskTotal() {
        TaskTotal taskTotal = cacheProcess.getCacheTaskTotal();
        taskTotal.state[AppConfig.INDEX_WIFI] = wifiState.saveWifiList.size();
        taskTotal.autoCount();
        cacheProcess.setCacheTaskTotal(taskTotal);
    }

    private void saveLogIndexOnLocal(final int pos) {
        final LogIndex logIndex = new LogIndex();
        logIndex.cacheTypeIndex = AppConfig.INDEX_WIFI;
        logIndex.logTime = wifiState.saveWifiList.get(pos).logFlag;
        logIndex.cacheFileName = wifiState.saveWifiList.get(pos).logFlag;
        logIndex.aliasFileName = wifiState.saveWifiList.get(pos).ssid;
        final List<Integer> list = cacheProcess.getWifiTaskResult(logIndex.cacheFileName);
        Observable.from(list)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer > -160;
                    }
                })
                .toSortedList()
                .subscribe(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> integers) {
                        if (integers.size() <= 0) {
                            logIndex.remarks =
                                    "MAC [" + wifiState.saveWifiList.get(pos).bssid + "]" +
                                            "  CH" + wifiState.saveWifiList.get(pos).channel +
                                            "\n扫描 [" + list.size() + "]次" +
                                            "  丢失 [" + (list.size() - integers.size()) + "]次" +
                                            "  丢失率 [100%]";
                        } else {
                            logIndex.remarks =
                                    "MAC [" + wifiState.saveWifiList.get(pos).bssid + "]" +
                                            "  CH" + wifiState.saveWifiList.get(pos).channel +
                                            "  最弱 [" + integers.get(0) + "db]" +
                                            "  最强 [" + integers.get(integers.size() - 1 <= 0 ? 0 : integers.size() - 1) + "db]" +
                                            "\n扫描 [" + list.size() + "]次" +
                                            "  丢失 [" + (list.size() - integers.size()) + "]次" +
                                            "  丢失率 [" + (list.size() - integers.size()) * 100 / list.size() + "%]";
                        }
                        cacheProcess.addCacheLogIndex(logIndex);
                    }
                });
    }

    //添加后台数据监听任务
    private void addTask() {
        //最多允许添加10个，点击时，肯定是select这个Mac的，起始不用传递参数？
        if (wifiState.saveWifiList.size() < 10) {
            wifiState.selectEntry.logFlag = MyTime.getDetailTime();
            wifiState.saveWifiList.add(wifiState.selectEntry);
            cacheProcess.setWifiState(wifiState);
            updateFragmentOne.addTaskEnd("当前共" + wifiState.saveWifiList.size() + "个任务", true);
        } else {
            updateFragmentOne.addTaskEnd("同时可添加到任务不能超过10个!", false);
        }
    }

    //移除后台数据监听任务， 本地保存、远端保存、清除数据、第一页的TASK()都会调用该方法，因此不在这里面加锁
    private void removeTask(int pos) {
        wifiState.saveWifiList.remove(pos);
        cacheProcess.setWifiState(wifiState);
    }

    /*
    EventBus 发起刷新UI的通知
     */
    //渲染activity标题
    private void sendMsgTitleTip(String tip) {
        MsgHelper.sendEvent(GlobalEventT.wifi_set_main_title_tip, tip, null);
    }

    //操作顶部标题的频率切换按钮
    private void sendMsgTopBtnStatus() {
        MsgHelper.sendEvent(GlobalEventT.wifi_set_channel_btn_status, "", wifiState.choose2d4G);
    }


    public void saveBitMap(LineChart wifiChannelRecord) {
        updateFragmentFour.saveBitMap(wifiChannelRecord);

    }
}
