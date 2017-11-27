package ywcai.ls.mobileutil.global.model.instance;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.global.cfg.AppConfig;
import ywcai.ls.mobileutil.results.model.LogIndex;
import ywcai.ls.mobileutil.identity.model.User;
import ywcai.ls.mobileutil.results.model.ResultState;
import ywcai.ls.mobileutil.results.model.TaskTotal;
import ywcai.ls.mobileutil.tools.Ping.model.PingState;
import ywcai.ls.mobileutil.tools.Station.model.StationEntry;
import ywcai.ls.mobileutil.tools.Station.model.StationState;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiEntry;
import ywcai.ls.mobileutil.tools.Wifi.model.WifiState;

public class CacheProcess {

    private static Object lock = new Object();
    private static CacheProcess cacheProcess = null;
    private final String RESULT_STATE = "RESULT_STATE";
    private final String PING_STATE = "PING_STATE";
    private final String WIFI_STATE = "WIFI_STATE";
    private final String STATION_STATE = "STATION_STATE";
    //    private final String STATION_RECORD = "STATION_RECORD";
    private final String USER = "USER";
    private final String LOG_INDEX = "LOG_INDEX";
    private final String TASK_TOTAL = "TASK_TOTAL";
//    private final String WIFI_TEMP_DATA = "WIFI_TEMP_DATA";


    File file = MainApplication.getInstance().getFilesDir();


    private CacheProcess() {
    }

    public static CacheProcess getInstance() {
        synchronized (lock) {
            if (cacheProcess == null) {
                cacheProcess = new CacheProcess();
            }
        }
        return cacheProcess;
    }

    //登陆信息缓存
    public void setCacheUser(User user) {
        user.setSign();
        Gson gson = new Gson();
        String cache = gson.toJson(user, User.class);
        setCache(USER, cache);
    }

    public User getCacheUser() {
        String cache = getCache(USER);
        if (cache.equals("null")) {
            return null;
        }
        Gson gson = new Gson();
        User user = gson.fromJson(cache, User.class);
        if (user == null) {
            return null;
        }
        if (!user.isVal()) {
            return null;
        }
        return user;
    }

    public TaskTotal getCacheTaskTotal() {
        TaskTotal taskTotal = null;
        String cache = getCache(TASK_TOTAL);
        if (cache.equals("null")) {
            return new TaskTotal();
        }
        Gson gson = new Gson();
        try {
            taskTotal = gson.fromJson(cache, TaskTotal.class);
        } catch (Exception e) {

        }
        if (taskTotal == null) {
            return new TaskTotal();
        }
        return taskTotal;
    }

    public void setCacheTaskTotal(TaskTotal taskTotal) {
        Gson gson = new Gson();
        String cache = gson.toJson(taskTotal, TaskTotal.class);
        setCache(TASK_TOTAL, cache);
    }


    //缓存正在PING测试的状态
    public void setCachePingState(PingState pingState) {
        Gson gson = new Gson();
        String cache = gson.toJson(pingState, PingState.class);
        setCache(PING_STATE, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        if (!pingState.isRunning && !pingState.isProcessResult && pingState.send != 0) {
            taskTotal.state[AppConfig.INDEX_PING] = 100;
        } else {
            taskTotal.state[AppConfig.INDEX_PING] = pingState.send * 100 / pingState.packageCount;
        }
        taskTotal.autoCount();
        setCacheTaskTotal(taskTotal);
    }

    //缓存正在PING测试的状态
    public PingState getCachePingState() {
        String cache = getCache(PING_STATE);
        if (cache.equals("null")) {
            return null;
        }
        Gson gson = new Gson();
        PingState pingState = null;
        try {
            pingState = gson.fromJson(cache, PingState.class);
        } catch (Exception e) {

        }
        if (pingState == null) {
            return null;
        }
        return pingState;
    }


    //存储PING的临时测试数据
    public void setPingResult(String logFlag, List<Float> list) {
        //当null时，清除该缓存文件
        if (list == null) {
            deleteCache(logFlag);
            return;
        }
        String cache = "null";
        try {
            Gson gson = new Gson();
            cache = gson.toJson(list, new TypeToken<List<Float>>() {
            }.getType());
        } catch (Exception e) {
        }
        setCache(logFlag, cache);
    }

    public List<Float> getPingResult(String logFlag) {
        String cache = getCache(logFlag);
        Gson gson = new Gson();
        List<Float> list = null;
        if (!cache.equals("null")) {
            try {
                list = gson.fromJson(cache, new TypeToken<List<Float>>() {
                }.getType());
            } catch (Exception e) {

            }
        }
        return list;
    }

    public void deleteWifiTask(String logName) {
        deleteCache(logName);
    }

    //存储WifiTask的临时测试数据
    public void setWifiTaskResult(WifiEntry wifiEntry) {
        //当null时，清除该缓存文件
        List list = getWifiTaskResult(wifiEntry.logFlag);
        list.add(0, wifiEntry.dbm);
        String cache = "null";
        try {
            Gson gson = new Gson();
            cache = gson.toJson(list, new TypeToken<List<Integer>>() {
            }.getType());
        } catch (Exception e) {
        }
        setCache(wifiEntry.logFlag, cache);
    }

    public List<Integer> getWifiTaskResult(String logFlag) {
        String cache = getCache(logFlag);
        Gson gson = new Gson();
        List<Integer> list = new ArrayList<>();
        if (!cache.equals("null")) {
            try {
                list = gson.fromJson(cache, new TypeToken<List<Integer>>() {
                }.getType());
            } catch (Exception e) {

            }
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void addCacheLogIndex(LogIndex logIndex) {
        List<LogIndex> temp = getCacheLogIndex();
        if (temp == null) {
            temp = new ArrayList<LogIndex>();
        }
        temp.add(0, logIndex);
        Gson gson = new Gson();
        String cache = gson.toJson(temp);
        setCache(LOG_INDEX, cache);
        TaskTotal taskTotal = getCacheTaskTotal();
        taskTotal.savedCount = temp.size();
        setCacheTaskTotal(taskTotal);
    }

    public List<LogIndex> getCacheLogIndex() {
        String strLogIndex = getCache(LOG_INDEX);
        List<LogIndex> indexList = null;
        if (!strLogIndex.equals("null")) {
            Gson gson = new Gson();
            try {
                indexList = gson.fromJson(strLogIndex, new TypeToken<List<LogIndex>>() {
                }.getType());
            } catch (Exception e) {

            }
        } else {
            indexList = new ArrayList<LogIndex>();
        }
        return indexList;
    }

    //持久化的通用方法
    private void setCache(String fileName, String cache) {
        File f = new File(file, fileName);
        FileOutputStream fos = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = MainApplication.getInstance().getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(cache.getBytes("utf-8"));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCache(String fileName) {
        String cache = "null";
        File f = new File(file, fileName);
        if (f.exists()) {
            try {
                FileInputStream in = new FileInputStream(f);
                InputStreamReader inputStreamReader = new InputStreamReader(in, "utf-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                cache = "";
                String s = "";
                while ((s = reader.readLine()) != null) {
                    cache += s + "\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "null";
            }
        }
        return cache;
    }

    private void deleteCache(String fileName) {
        File f = new File(file, fileName);
        if (f.exists()) {
            try {
                f.delete();
            } catch (Exception e) {

            }
        }
    }

    public WifiState getWifiState() {
        String cache = getCache(WIFI_STATE);
        if (cache.equals("null")) {
            return new WifiState();
        }
        Gson gson = new Gson();
        WifiState wifiState = null;
        try {
            wifiState = gson.fromJson(cache, WifiState.class);
        } catch (Exception e) {

        }
        if (wifiState == null) {
            return new WifiState();
        }
        return wifiState;
    }

    public void setWifiState(WifiState wifiState) {
        String cacheWifiState = "null";
        if (wifiState == null) {
            setCache(WIFI_STATE, cacheWifiState);
        } else {
            Gson gson = new Gson();
            try {
                cacheWifiState = gson.toJson(wifiState);
            } catch (Exception e) {
            }

        }
        setCache(WIFI_STATE, cacheWifiState);

    }

    public ResultState getResultState() {
        String cache = getCache(RESULT_STATE);
        if (cache.equals("null")) {
            return new ResultState();
        }
        Gson gson = new Gson();
        ResultState resultState = null;
        try {
            resultState = gson.fromJson(cache, ResultState.class);
        } catch (Exception e) {

        }
        if (resultState == null) {
            return new ResultState();
        }
        return resultState;
    }

    public void setResultState(ResultState resultState) {
        String cache = "null";
        if (resultState == null) {
            setCache(RESULT_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(resultState);
            } catch (Exception e) {
            }
        }
        setCache(RESULT_STATE, cache);
    }

    public StationState getStationState() {
        String cache = getCache(STATION_STATE);
        if (cache.equals("null")) {
            return new StationState();
        }
        Gson gson = new Gson();
        StationState stationState = null;
        try {
            stationState = gson.fromJson(cache, StationState.class);
        } catch (Exception e) {

        }
        if (stationState == null) {
            return new StationState();
        }
        return stationState;
    }

    public void setStationState(StationState stationState) {
        String cache = "null";
        if (stationState == null) {
            setCache(STATION_STATE, cache);
        } else {
            Gson gson = new Gson();
            try {
                cache = gson.toJson(stationState);
            } catch (Exception e) {
            }
        }
        setCache(STATION_STATE, cache);
    }

    public List<StationEntry> getStationRecord(String logName) {
        String cache = getCache(logName);
        if (cache.equals("null")) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        List<StationEntry> stationStateList = null;
        try {
            stationStateList = gson.fromJson(cache, new TypeToken<List<StationEntry>>() {
            }.getType());
        } catch (Exception e) {

        }
        if (stationStateList == null) {
            return new ArrayList<>();
        }
        return stationStateList;
    }

    public void setStationRecord(String logName, StationEntry stationEntry) {
        String cache = "null";
        if (stationEntry == null) {
            deleteCache(logName);
            return;
        }
        List<StationEntry> list = getStationRecord(logName);
        list.add(0, stationEntry);
        Gson gson = new Gson();
        try {
            cache = gson.toJson(list, new TypeToken<List<StationEntry>>() {
            }.getType());
        } catch (Exception e) {

        }
        setCache(logName, cache);
    }
}

